import { describe, it, expect } from "vitest";
import { registerSchema, updateProfessionalSchema } from "./profissional.schema";

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


