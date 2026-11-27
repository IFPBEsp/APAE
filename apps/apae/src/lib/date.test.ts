import { describe, expect, it } from "vitest";
import { calculateAge } from "./date";

describe("calculateAge (#1162)", () => {
  describe("véspera e dia do aniversário", () => {
    it("retorna 24 anos na véspera (02/11/2026) e 25 anos no dia do aniversário (03/11/2026) para nascido em 03/11/2001", () => {
      expect(calculateAge("2001-11-03", "2026-11-02")).toBe("24 anos");
      expect(calculateAge("2001-11-03", "2026-11-03")).toBe("25 anos");
      expect(calculateAge("03/11/2001", "02/11/2026")).toBe("24 anos");
      expect(calculateAge("03/11/2001", "03/11/2026")).toBe("25 anos");
    });

    it("considera mês anterior e mês posterior ao aniversário", () => {
      expect(calculateAge("2001-11-03", "2026-10-31")).toBe("24 anos");
      expect(calculateAge("2001-11-03", "2026-12-01")).toBe("25 anos");
    });
  });

  describe("singular ('1 ano') e menores de 1 ano ('0 anos')", () => {
    it("retorna '0 anos' para menores de 1 ano (inclusive na véspera de completar 1 ano)", () => {
      expect(calculateAge("2025-11-03", "2025-11-03")).toBe("0 anos");
      expect(calculateAge("2025-11-03", "2026-05-10")).toBe("0 anos");
      expect(calculateAge("2025-11-03", "2026-11-02")).toBe("0 anos");
    });

    it("retorna '1 ano' no singular exatamente ao completar 1 ano e até a véspera de 2 anos", () => {
      expect(calculateAge("2025-11-03", "2026-11-03")).toBe("1 ano");
      expect(calculateAge("2025-11-03", "2027-11-02")).toBe("1 ano");
      expect(calculateAge("2025-11-03", "2027-11-03")).toBe("2 anos");
    });
  });

  describe("nascimento em 29/02 (ano bissexto)", () => {
    it("em ano não bissexto, mantém a idade anterior em 28/02 e incrementa em 01/03", () => {
      expect(calculateAge("2004-02-29", "2025-02-28")).toBe("20 anos");
      expect(calculateAge("2004-02-29", "2025-03-01")).toBe("21 anos");
    });

    it("em ano bissexto, mantém a idade anterior em 28/02 e incrementa em 29/02", () => {
      expect(calculateAge("2004-02-29", "2024-02-28")).toBe("19 anos");
      expect(calculateAge("2004-02-29", "2024-02-29")).toBe("20 anos");
    });
  });

  describe("data de nascimento ausente ou inválida", () => {
    it("retorna null quando a data de nascimento é vazia, null, undefined ou inválida", () => {
      expect(calculateAge(null)).toBeNull();
      expect(calculateAge(undefined)).toBeNull();
      expect(calculateAge("")).toBeNull();
      expect(calculateAge("data-invalida")).toBeNull();
    });
  });
});
