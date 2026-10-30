import { describe, it, expect } from "vitest";
import { Profile } from "../domains/patients/schemas/member-schemas";
import { EditProfile } from "./edit-member-schemas";
import { photoFileSchema } from "@/schemas/shared/photo.schema";
import { MAX_FILE_SIZE_BYTES } from "@/lib/constants";

const makeFile = (name: string, type: string, size = 1024) =>
  new File([new Uint8Array(size)], name, { type });

const validPhoto = () => makeFile("foto.png", "image/png");
const invalidType = () => makeFile("doc.pdf", "application/pdf");
const tooBig = () => makeFile("grande.png", "image/png", MAX_FILE_SIZE_BYTES + 1);

type Issue = { message: string; path: PropertyKey[] };
type Result = { success: boolean; error?: { issues: Issue[] } };

// Mensagens de erro do campo "photo" em um schema de paciente
const photoMessages = (result: Result) =>
  result.error?.issues.filter((i) => i.path[0] === "photo").map((i) => i.message) ?? [];

// Mensagens do schema compartilhado (fonte da verdade, usada também no profissional)
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
