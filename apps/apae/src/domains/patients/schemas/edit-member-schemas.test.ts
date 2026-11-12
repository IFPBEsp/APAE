import { describe, it, expect } from "vitest";
import { Personal, Profile } from "./member-schemas";
import { EditProfile } from "./edit-member-schemas";
import { photoFileSchema } from "@/lib/photo.schema";
import { MAX_FILE_SIZE_BYTES } from "@/lib/constants";
import { buildPatientPayload } from "@/domains/patients/shared/patient.utils";
import { initialState } from "@/domains/patients/hooks/register-context/reducer";

const makeFile = (name: string, type: string, size = 1024) =>
  new File([new Uint8Array(size)], name, { type });

const validPhoto = () => makeFile("foto.png", "image/png");
const invalidType = () => makeFile("doc.pdf", "application/pdf");
const tooBig = () => makeFile("grande.png", "image/png", MAX_FILE_SIZE_BYTES + 1);

type Issue = { message: string; path: PropertyKey[] };
type Result = { success: boolean; error?: { issues: Issue[] } };

const photoMessages = (result: Result) =>
  result.error?.issues.filter((i) => i.path[0] === "photo").map((i) => i.message) ?? [];

const sharedMessages = (value: unknown) =>
  photoFileSchema.safeParse(value).error?.issues.map((i) => i.message) ?? [];

describe("Critério 1: cadastro de paciente valida tipo e tamanho da foto no submit", () => {
  it("aceita foto válida", () => {
    expect(Profile.safeParse({ role: "patient", photo: validPhoto() }).success).toBe(true);
  });

  it("rejeita foto ausente", () => {
    expect(Profile.safeParse({ role: "patient" }).success).toBe(false);
  });

  it("rejeita tipo inválido", () => {
    expect(Profile.safeParse({ role: "patient", photo: invalidType() }).success).toBe(false);
  });

  it("rejeita foto acima do limite", () => {
    expect(Profile.safeParse({ role: "patient", photo: tooBig() }).success).toBe(false);
  });

  it("rejeita arquivo vazio", () => {
    const empty = makeFile("vazio.png", "image/png", 0);
    expect(Profile.safeParse({ role: "patient", photo: empty }).success).toBe(false);
  });
});

describe("Critério 2: edição de paciente valida tipo e tamanho, aceitando a URL atual", () => {
  it("aceita a URL da foto já cadastrada", () => {
    expect(
      EditProfile.safeParse({ role: "patient", photo: "https://exemplo.com/foto.png" }).success,
    ).toBe(true);
  });

  it("aceita edição sem alterar a foto", () => {
    expect(EditProfile.safeParse({ role: "patient" }).success).toBe(true);
  });

  it("aceita nova foto válida", () => {
    expect(EditProfile.safeParse({ role: "patient", photo: validPhoto() }).success).toBe(true);
  });

  it("rejeita tipo inválido", () => {
    expect(EditProfile.safeParse({ role: "patient", photo: invalidType() }).success).toBe(false);
  });

  it("rejeita foto acima do limite", () => {
    expect(EditProfile.safeParse({ role: "patient", photo: tooBig() }).success).toBe(false);
  });

  it("rejeita valor que não é File nem string", () => {
    expect(EditProfile.safeParse({ role: "patient", photo: 123 }).success).toBe(false);
  });
});

describe("Critério 4: mensagens de erro consistentes com o schema compartilhado (profissional)", () => {
  const cases: [string, () => unknown][] = [
    ["tipo inválido", invalidType],
    ["foto acima do limite", tooBig],
    ["arquivo vazio", () => makeFile("vazio.png", "image/png", 0)],
  ];

  it.each(cases)("cadastro: %s usa as mesmas mensagens do photoFileSchema", (_, build) => {
    const photo = build();
    const result = Profile.safeParse({ role: "patient", photo });
    expect(photoMessages(result)).toEqual(sharedMessages(photo));
  });

  it.each(cases)("edição: %s usa as mesmas mensagens do photoFileSchema", (_, build) => {
    const photo = build();
    const result = EditProfile.safeParse({ role: "patient", photo });
    expect(photoMessages(result)).toEqual(sharedMessages(photo));
  });

  it("cadastro: foto ausente usa a mesma mensagem do photoFileSchema", () => {
    const result = Profile.safeParse({ role: "patient" });
    expect(photoMessages(result)).toEqual(sharedMessages(undefined));
  });
});

describe("Validação e payload do campo NIS opcional (#1121)", () => {
  const validPersonalBase = {
    name: "João da Silva",
    cpf: "529.982.247-25",
    phone: "(83) 99999-9999",
    rg: {
      number: "1234567",
      issuing: {
        body: "SSP/PB",
        date: new Date(2020, 0, 1),
      },
    },
    cns: "123 4567 8901 2345",
    birth: {
      certificate: "123456 01 55 2010 1 12345 123 1234567 12",
      date: new Date(2010, 0, 1),
      place: "Campina Grande",
    },
  };

  it("aceita NIS vazio ('') ou não informado", () => {
    expect(Personal.safeParse({ ...validPersonalBase, nis: "" }).success).toBe(true);
    expect(Personal.safeParse({ ...validPersonalBase, nis: undefined }).success).toBe(true);
  });

  it("aceita NIS válido com 11 números", () => {
    expect(Personal.safeParse({ ...validPersonalBase, nis: "12345678901" }).success).toBe(true);
  });

  it("rejeita NIS preenchido com menos de 11 dígitos ou com letras", () => {
    expect(Personal.safeParse({ ...validPersonalBase, nis: "12345" }).success).toBe(false);
    expect(Personal.safeParse({ ...validPersonalBase, nis: "1234567890a" }).success).toBe(false);
  });

  it("buildPatientPayload envia string vazia e não '0' quando NIS está vazio", () => {
    const payload = buildPatientPayload(initialState);
    expect(payload.nis).toBe("");
    expect(payload.nis).not.toBe("0");
  });
});
