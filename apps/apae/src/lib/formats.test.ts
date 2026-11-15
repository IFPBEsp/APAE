import { describe, expect, it } from "vitest";
import { formatBirthCertificate, formatIssuingBody } from "./formats";

const MATRICULA = "12345678901234567890123456789012";
const MATRICULA_FORMATADA = "123456 78 90 1234 5 67890 123 4567890 12";

describe("formatBirthCertificate (#1122)", () => {
  it("aplica a máscara da matrícula com 32 dígitos", () => {
    expect(formatBirthCertificate(MATRICULA)).toBe(MATRICULA_FORMATADA);
  });

  it("mantém o formato quando recebe um valor já formatado", () => {
    expect(formatBirthCertificate(MATRICULA_FORMATADA)).toBe(
      MATRICULA_FORMATADA,
    );
  });

  it("não aplica a máscara com menos de 32 dígitos", () => {
    expect(formatBirthCertificate("12345678901234567890")).toBe(
      "12345678901234567890",
    );
  });

  it("corta em 32 dígitos", () => {
    expect(formatBirthCertificate(`${MATRICULA}999`)).toBe(
      MATRICULA_FORMATADA,
    );
  });

  it("remove caracteres não numéricos", () => {
    expect(formatBirthCertificate("abc123-45")).toBe("12345");
  });

  it("retorna string vazia para entrada vazia", () => {
    expect(formatBirthCertificate("")).toBe("");
  });
});

describe("formatIssuingBody", () => {
  it("mantém sigla longa como DETRAN/RJ", () => {
    expect(formatIssuingBody("detran/rj")).toBe("DETRAN/RJ");
  });

  it("converte para maiúsculas e remove caracteres inválidos", () => {
    expect(formatIssuingBody("ssp1/pb")).toBe("SSP/PB");
  });

  it("permite até 13 caracteres (10 letras antes da barra)", () => {
    expect(formatIssuingBody("abcdefghij/rj")).toBe("ABCDEFGHIJ/RJ");
  });

  it("corta o valor em 13 caracteres", () => {
    expect(formatIssuingBody("abcdefghijk/rj")).toBe("ABCDEFGHIJK/R");
  });

  it("preserva a barra", () => {
    expect(formatIssuingBody("SSP/PB")).toBe("SSP/PB");
  });

  it("retorna string vazia para entrada vazia", () => {
    expect(formatIssuingBody("")).toBe("");
  });
});
