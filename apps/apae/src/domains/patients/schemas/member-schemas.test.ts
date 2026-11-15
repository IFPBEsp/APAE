import { describe, expect, it } from "vitest";
import { Personal, Guardian, Kinship, birthCertificateSchema } from "./member-schemas";

const MATRICULA = "12345678901234567890123456789012";
const MATRICULA_FORMATADA = "123456 78 90 1234 5 67890 123 4567890 12";
const MENSAGEM_OBRIGATORIA = "Informe o número da certidão de nascimento";

function firstError(value: string) {
  const result = birthCertificateSchema.safeParse(value);
  return result.success ? null : result.error.issues[0].message;
}

describe("birthCertificateSchema (#1122)", () => {
  it("aceita matrícula de 32 dígitos com máscara", () => {
    expect(birthCertificateSchema.safeParse(MATRICULA_FORMATADA).success).toBe(
      true,
    );
  });

  it("aceita matrícula de 32 dígitos sem máscara", () => {
    expect(birthCertificateSchema.safeParse(MATRICULA).success).toBe(true);
  });

  it("aceita certidão antiga com menos de 32 dígitos", () => {
    expect(birthCertificateSchema.safeParse("12345").success).toBe(true);
    expect(birthCertificateSchema.safeParse("1").success).toBe(true);
  });

  it("recusa campo vazio", () => {
    expect(firstError("")).toBe(MENSAGEM_OBRIGATORIA);
  });

  it("recusa texto sem nenhum dígito", () => {
    expect(firstError("   ")).toBe(MENSAGEM_OBRIGATORIA);
    expect(firstError("abc")).toBe(MENSAGEM_OBRIGATORIA);
  });

  it("recusa mais de 32 dígitos", () => {
    expect(birthCertificateSchema.safeParse(`${MATRICULA}1`).success).toBe(
      false,
    );
  });

  it("não cita 32 dígitos nas mensagens de erro", () => {
    expect(firstError("")).not.toMatch(/32/);
    expect(firstError(`${MATRICULA}1`)).not.toMatch(/32/);
  });
});

describe("Órgão emissor", () => {
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

  const withIssuingBody = (body: string) =>
    Personal.safeParse({
      ...validPersonalBase,
      rg: {
        ...validPersonalBase.rg,
        issuing: { ...validPersonalBase.rg.issuing, body },
      },
    });

  it.each(["SSP/PB", "PC/RJ", "DETRAN/RJ", "MARINHA/RJ"])(
    "aceita órgão emissor válido: %s",
    (body) => {
      expect(withIssuingBody(body).success).toBe(true);
    },
  );

  it.each(["D/SP", "DETRAN/RJX", "DETRAN-RJ", "DETRAN/R", "DETRANRJ"])(
    "recusa órgão emissor inválido: %s",
    (body) => {
      expect(withIssuingBody(body).success).toBe(false);
    },
  );

  it("aceita órgão emissor com 13 caracteres", () => {
    expect(withIssuingBody("ABCDEFGHIJ/RJ").success).toBe(true);
  });

  it("recusa órgão emissor com mais de 13 caracteres", () => {
    expect(withIssuingBody("ABCDEFGHIJK/RJ").success).toBe(false);
  });
});

describe("Nomes com símbolos nos membros", () => {
  const nomes = [
    "Ana Clara D'Ávila",
    "Maria Sant’Ana",
    "Ana-Clara Souza",
    "José Silva Jr.",
  ];

  const validAddress = {
    cep: "58135-000",
    state: "PB",
    city: "Esperança",
    neighborhood: "Centro",
    street: "Rua A",
    number: "100",
    complement: "Casa",
  };

  it.each(nomes)("aceita nome no responsável: %s", (name) => {
    const result = Guardian.safeParse({
      name,
      contact: "(83) 99999-9999",
      kinship: "Mãe",
      address: validAddress,
    });

    expect(result.success).toBe(true);
  });

  it.each(nomes)("aceita nome no parente: %s", (name) => {
    const result = Kinship.safeParse({
      rg: "",
      cpf: "529.982.247-25",
      alive: true,
      name,
      occupation: "Professora",
      type: "Mãe",
    });

    expect(result.success).toBe(true);
  });
});
