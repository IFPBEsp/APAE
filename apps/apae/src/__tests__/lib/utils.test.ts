import { describe, it, expect } from "vitest";
import { separaETransformaEmNumero, formatDatePTBR } from "@/lib/utils";

describe("separaETransformaEmNumero", () => {
  it("splits a string by separator and converts to numbers", () => {
    const result = separaETransformaEmNumero("1/2/3", "/");
    expect(result).toEqual([1, 2, 3]);
  });

  it("returns [NaN, NaN, NaN] for an empty string", () => {
    const result = separaETransformaEmNumero("", "/");
    expect(result).toEqual([NaN, NaN, NaN]);
  });

  it("returns [NaN, NaN, NaN] for a non-string value (number)", () => {
    const result = separaETransformaEmNumero(42, "/");
    expect(result).toEqual([NaN, NaN, NaN]);
  });

  it("returns [NaN, NaN, NaN] for null", () => {
    const result = separaETransformaEmNumero(null, "/");
    expect(result).toEqual([NaN, NaN, NaN]);
  });

  it("returns [NaN, NaN, NaN] for undefined", () => {
    const result = separaETransformaEmNumero(undefined, "/");
    expect(result).toEqual([NaN, NaN, NaN]);
  });

  it("handles hyphen separator", () => {
    const result = separaETransformaEmNumero("2024-01-15", "-");
    expect(result).toEqual([2024, 1, 15]);
  });

  it("produces NaN for non-numeric parts", () => {
    const result = separaETransformaEmNumero("a/b/c", "/");
    result.forEach((n) => expect(Number.isNaN(n)).toBe(true));
  });

  it("handles a string with a single segment (no separator match)", () => {
    const result = separaETransformaEmNumero("2024", "/");
    expect(result).toEqual([2024]);
  });

  it("handles numeric strings with leading spaces (parseInt trims whitespace)", () => {
    // parseInt trims leading whitespace, so " 20" becomes 20
    const result = separaETransformaEmNumero("10/ 20/ 30", "/");
    expect(result).toEqual([10, 20, 30]);
  });
});

describe("formatDatePTBR", () => {
  it("formats an ISO date string to Portuguese BR format", () => {
    // 2024-01-15 should be '15 de janeiro de 2024'
    const result = formatDatePTBR("2024-01-15");
    expect(result).toBe("15 de janeiro de 2024");
  });

  it("formats another month correctly", () => {
    const result = formatDatePTBR("2023-06-01");
    expect(result).toBe("01 de junho de 2023");
  });

  it("formats december correctly", () => {
    const result = formatDatePTBR("2022-12-31");
    expect(result).toBe("31 de dezembro de 2022");
  });

  it("formats march correctly", () => {
    const result = formatDatePTBR("2025-03-08");
    expect(result).toBe("08 de março de 2025");
  });

  it("handles a Date object .toString() passed as string", () => {
    // new Date("2024-07-04") in UTC zone
    const result = formatDatePTBR("2024-07-04");
    expect(result).toBe("04 de julho de 2024");
  });
});