import { describe, expect, it } from "vitest";
import { isValidCpf } from "./cpf";

describe("isValidCpf", () => {
  it("deve retornar true para um CPF válido", () => {
    expect(isValidCpf("123.456.789-09")).toBe(true);
  });

  it("deve retornar false para um CPF com dígitos verificadores inválidos", () => {
    expect(isValidCpf("834.002.892-24")).toBe(false);
  });

  it("deve retornar false para CPF com todos os números iguais", () => {
    expect(isValidCpf("111.111.111-11")).toBe(false);
  });

  it("deve retornar false para CPF incompleto", () => {
    expect(isValidCpf("123.456")).toBe(false);
  });

  it("deve aceitar CPF sem máscara", () => {
    expect(isValidCpf("12345678909")).toBe(true);
  });
});