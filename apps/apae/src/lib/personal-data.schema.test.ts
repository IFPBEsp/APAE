import { describe, expect, it } from "vitest";
import {
  cpfSchema,
  nomeCompletoSchema,
  rgSchema,
  telefoneSchema,
} from "./personal-data.schema";

const isValid = (
  schema: { safeParse: (v: unknown) => { success: boolean } },
  value: string
) => schema.safeParse(value).success;

describe("nomeCompletoSchema", () => {
  it("aceita nome e sobrenome simples", () => {
    expect(isValid(nomeCompletoSchema, "Maria Silva")).toBe(true);
  });

  it("aceita nome completo com acentos e múltiplas palavras", () => {
    expect(isValid(nomeCompletoSchema, "José da Conceição Júnior")).toBe(true);
  });

  it("remove espaços nas pontas (trim)", () => {
    expect(nomeCompletoSchema.parse("  Maria Silva  ")).toBe("Maria Silva");
  });

  it("aceita nome com exatamente 100 caracteres", () => {
    // 49 'a' + 1 espaço + 50 'b' = 100 caracteres
    const nome100 = `${"a".repeat(49)} ${"b".repeat(50)}`;
    expect(isValid(nomeCompletoSchema, nome100)).toBe(true);
  });

  it("recusa nome com mais de 100 caracteres", () => {
    // 50 'a' + 1 espaço + 50 'b' = 101 caracteres
    const nome101 = `${"a".repeat(50)} ${"b".repeat(50)}`;
    expect(isValid(nomeCompletoSchema, nome101)).toBe(false);
  });

  it.each([
    ["uma palavra só", "Maria"],
    ["vazio", ""],
    ["apenas espaços", "   "],
    ["com números", "Maria Silva 2"],
    ["com hífen", "Ana-Maria Silva"],
    ["com apóstrofo", "Joana D'Arc"],
    ["espaço duplo entre palavras", "Maria  Silva"],
  ])("recusa %s (%s)", (_desc, valor) => {
    expect(isValid(nomeCompletoSchema, valor)).toBe(false);
  });
});

describe("cpfSchema", () => {
  it("aceita CPF válido com máscara", () => {
    expect(isValid(cpfSchema, "123.456.789-09")).toBe(true);
  });

  it.each([
    ["dígitos verificadores inválidos", "834.002.892-24"],
    ["todos os números iguais", "111.111.111-11"],
    ["sem máscara", "12345678909"],
    ["incompleto", "123.456.789-0"],
    ["vazio", ""],
  ])("recusa CPF com %s (%s)", (_desc, cpf) => {
    expect(isValid(cpfSchema, cpf)).toBe(false);
  });
});

describe("rgSchema", () => {
  it.each(["56454545", "1.234.567", "12.345.678-9", "12.345.678-X"])(
    "aceita formato válido: %s",
    (rg) => {
      expect(isValid(rgSchema, rg)).toBe(true);
    }
  );

  it("remove espaços nas pontas (trim)", () => {
    expect(rgSchema.parse("  12.345.678-X  ")).toBe("12.345.678-X");
  });

  it.each(["1..2", "12.34a", "12.345.678-x", "", "   "])(
    "recusa formato inválido: %s",
    (rg) => {
      expect(isValid(rgSchema, rg)).toBe(false);
    }
  );
});

describe("telefoneSchema", () => {
  it.each(["(83) 99999-9999", "(83) 3333-3333"])(
    "aceita formato válido: %s",
    (tel) => {
      expect(isValid(telefoneSchema, tel)).toBe(true);
    }
  );

  it("remove espaços nas pontas (trim)", () => {
    expect(telefoneSchema.parse("  (83) 99999-9999  ")).toBe(
      "(83) 99999-9999"
    );
  });

  it.each([
    ["sem máscara", "83999999999"],
    ["incompleto", "(83) 9999-99"],
    ["celular sem começar com 9", "(83) 88888-8888"],
    ["DDD incompleto", "(8) 99999-9999"],
    ["vazio", ""],
  ])("recusa %s (%s)", (_desc, tel) => {
    expect(isValid(telefoneSchema, tel)).toBe(false);
  });
});
