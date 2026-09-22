import { describe, it, expect } from "vitest";
import { cn, separateAndTransformIntoNumber, formatDatePTBR } from "./utils";

describe("cn", () => {
  it("junta classes simples", () => {
    expect(cn("a", "b")).toBe("a b");
  });

  it("ignora valores falsy (condicionais)", () => {
    expect(cn("a", false && "b", undefined, null, "c")).toBe("a c");
  });

  it("resolve conflito do tailwind-merge (última classe vence)", () => {
    expect(cn("p-2", "p-4")).toBe("p-4");
  });
});

describe("separateAndTransformIntoNumber", () => {
  it("separa e converte uma string com o separador informado", () => {
    expect(separateAndTransformIntoNumber("1-2-3", "-")).toEqual([1, 2, 3]);
  });

  it("funciona com separador diferente, tipo vírgula", () => {
    expect(separateAndTransformIntoNumber("10,20,30", ",")).toEqual([10, 20, 30]);
  });

  it("retorna [NaN, NaN, NaN] se o valor não for string", () => {
    expect(separateAndTransformIntoNumber(123, "-")).toEqual([NaN, NaN, NaN]);
  });

  it("retorna [NaN, NaN, NaN] se a string for vazia", () => {
    expect(separateAndTransformIntoNumber("", "-")).toEqual([NaN, NaN, NaN]);
  });

  it("retorna [NaN, NaN, NaN] se o valor for undefined", () => {
    expect(separateAndTransformIntoNumber(undefined, "-")).toEqual([NaN, NaN, NaN]);
  });
});

describe("formatDatePTBR", () => {
  it("formata a data no padrão 'dd de MMMM de yyyy' em português", () => {
    expect(formatDatePTBR("2024-03-15")).toBe("15 de março de 2024");
  });

  it("formata corretamente datas em diferentes meses", () => {
    expect(formatDatePTBR("2025-01-01")).toBe("01 de janeiro de 2025");
  });
});