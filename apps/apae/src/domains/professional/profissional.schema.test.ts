import { describe, it, expect } from "vitest";
import {
  registerSchema,
  updateProfessionalSchema,
} from "./profissional.schema";

const makePdfFile = (name = "doc.pdf") =>
  new File([new Uint8Array(1024)], name, { type: "application/pdf" });

const makePhotoFile = (name = "foto.png") =>
  new File([new Uint8Array(1024)], name, { type: "image/png" });

const baseValidData = {
  fullName: "Maria da Silva",
  email: "maria@apae.org.br",
  cpf: "529.982.247-25",
  serviceArea: "PSICOLOGIA",
  rg: "1234567",
  state: "PB",
  city: "Esperança",
  neighborhood: "Centro",
  street: "Rua Principal",
  number: "100",
  complement: "Sala 1",
  cep: "58135-000",
  phone: "(83) 99999-9999",
  availability: [],
};

const validRegisterData = {
  ...baseValidData,
  photo: makePhotoFile(),
  volunteerAgreement: makePdfFile("termo.pdf"),
  curriculum: makePdfFile("curriculo.pdf"),
};

describe("profissional.schema - professionalDocument", () => {
  describe("campo vazio ou ausente (opcional)", () => {
    it.each([
      ["string vazia", ""],
      ["apenas espaços", "   "],
      ["undefined", undefined],
      ["null", null],
    ])(
      "aceita documento profissional como %s no cadastro e na edição",
      (_, professionalDocument) => {
        const registerResult = registerSchema.safeParse({
          ...validRegisterData,
          professionalDocument,
        });
        const updateResult = updateProfessionalSchema.safeParse({
          ...baseValidData,
          professionalDocument,
        });

        expect(registerResult.success).toBe(true);
        expect(updateResult.success).toBe(true);
      },
    );
  });

  describe("valor válido (entre 3 e 50 caracteres)", () => {
    it.each([
      ["3 caracteres (limite mínimo)", "CRP"],
      ["formato comum", "CRP/PB 123456"],
      ["50 caracteres (limite máximo)", "A".repeat(50)],
    ])(
      "aceita documento profissional com %s no cadastro e na edição",
      (_, professionalDocument) => {
        const registerResult = registerSchema.safeParse({
          ...validRegisterData,
          professionalDocument,
        });
        const updateResult = updateProfessionalSchema.safeParse({
          ...baseValidData,
          professionalDocument,
        });

        expect(registerResult.success).toBe(true);
        expect(updateResult.success).toBe(true);
      },
    );
  });

  describe("valor curto demais (menos de 3 caracteres)", () => {
    it.each(["A", "AB", " AB "])(
      "rejeita documento profissional curto demais ('%s') no cadastro e na edição",
      (professionalDocument) => {
        const registerResult = registerSchema.safeParse({
          ...validRegisterData,
          professionalDocument,
        });
        const updateResult = updateProfessionalSchema.safeParse({
          ...baseValidData,
          professionalDocument,
        });

        expect(registerResult.success).toBe(false);
        expect(updateResult.success).toBe(false);

        if (!registerResult.success) {
          expect(registerResult.error.issues[0].path).toEqual([
            "professionalDocument",
          ]);
          expect(registerResult.error.issues[0].message).toBe(
            "O documento profissional deve ter no mínimo 3 caracteres",
          );
        }

        if (!updateResult.success) {
          expect(updateResult.error.issues[0].path).toEqual([
            "professionalDocument",
          ]);
          expect(updateResult.error.issues[0].message).toBe(
            "O documento profissional deve ter no mínimo 3 caracteres",
          );
        }
      },
    );
  });

  describe("valor longo demais (mais de 50 caracteres)", () => {
    it("rejeita documento profissional com 51 caracteres no cadastro e na edição", () => {
      const professionalDocument = "A".repeat(51);

      const registerResult = registerSchema.safeParse({
        ...validRegisterData,
        professionalDocument,
      });
      const updateResult = updateProfessionalSchema.safeParse({
        ...baseValidData,
        professionalDocument,
      });

      expect(registerResult.success).toBe(false);
      expect(updateResult.success).toBe(false);

      if (!registerResult.success) {
        expect(registerResult.error.issues[0].path).toEqual([
          "professionalDocument",
        ]);
        expect(registerResult.error.issues[0].message).toBe(
          "O documento profissional deve ter no máximo 50 caracteres",
        );
      }

      if (!updateResult.success) {
        expect(updateResult.error.issues[0].path).toEqual([
          "professionalDocument",
        ]);
        expect(updateResult.error.issues[0].message).toBe(
          "O documento profissional deve ter no máximo 50 caracteres",
        );
      }
    });
  });
});

const makeFile = (name: string, type: string, size = 1024) =>
  new File([new Uint8Array(size)], name, { type });

const makeValues = (rg: unknown) => ({
  fullName: "Maria Silva",
  email: "maria@apae.org",
  cpf: "529.982.247-25",
  professionalDocument: "CRP-123",
  serviceArea: "PSICOLOGIA",
  rg,
  state: "PB",
  city: "Esperança",
  neighborhood: "Centro",
  street: "Rua A",
  number: "10",
  noNumber: false,
  complement: "Casa",
  cep: "58135-000",
  phone: "(83) 99999-9999",
  availability: [],
});

const makeRegisterValues = (rg: unknown) => ({
  ...makeValues(rg),
  photo: makeFile("foto.png", "image/png"),
  volunteerAgreement: makeFile("doc.pdf", "application/pdf"),
  curriculum: makeFile("doc.pdf", "application/pdf"),
});

type Issue = { message: string; path: PropertyKey[] };
type Result = { success: boolean; error?: { issues: Issue[] } };

const rgMessages = (result: Result) =>
  result.error?.issues.filter((i) => i.path[0] === "rg").map((i) => i.message) ?? [];

describe("Critério: RG do profissional é opcional, mas validado quando preenchido", () => {
  it("aceita RG vazio", () => {
    const result = updateProfessionalSchema.safeParse(makeValues(""));
    expect(rgMessages(result)).toEqual([]);
    expect(result.success).toBe(true);
  });

  it("aceita RG válido de 9 dígitos", () => {
    const result = updateProfessionalSchema.safeParse(makeValues("12.345.678-9"));
    expect(rgMessages(result)).toEqual([]);
    expect(result.success).toBe(true);
  });

  it("rejeita RG inválido com a mensagem 'RG inválido'", () => {
    const result = updateProfessionalSchema.safeParse(makeValues("12a"));
    expect(result.success).toBe(false);
    expect(rgMessages(result)).toEqual(["RG inválido"]);
  });
});

describe("Critério: registerSchema aceita RG vazio e valida RG preenchido", () => {
  it("aceita RG vazio", () => {
    const result = registerSchema.safeParse(makeRegisterValues(""));
    expect(rgMessages(result)).toEqual([]);
    expect(result.success).toBe(true);
  });

  it("aceita RG válido de 9 dígitos", () => {
    const result = registerSchema.safeParse(makeRegisterValues("12.345.678-9"));
    expect(rgMessages(result)).toEqual([]);
    expect(result.success).toBe(true);
  });

  it("rejeita RG inválido com a mensagem 'RG inválido'", () => {
    const result = registerSchema.safeParse(makeRegisterValues("12a"));
    expect(result.success).toBe(false);
    expect(rgMessages(result)).toEqual(["RG inválido"]);
  });
});