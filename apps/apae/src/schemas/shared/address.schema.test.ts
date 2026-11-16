import { describe, it, expect } from "vitest";
import { addressSchema } from "./address.schema";

const validAddress = {
  cep: "58135-000",
  state: "PB",
  city: "Esperança",
  neighborhood: "Centro",
  street: "Rua Principal",
  number: "123",
  complement: "Casa",
};

describe("addressSchema", () => {
  describe("rua (street)", () => {
    it.each([
      "Rua 7 de Setembro",
      "Av. Epitácio Pessoa",
      "Rua D'Ávila, Lote 2 - Centro",
    ])("aceita rua válida: %s", (street) => {
      const result = addressSchema.safeParse({ ...validAddress, street });
      expect(result.success).toBe(true);
    });

    it.each(["A", "A".repeat(81), "Rua @ Inválida"])(
      "rejeita rua inválida: %s",
      (street) => {
        const result = addressSchema.safeParse({ ...validAddress, street });
        expect(result.success).toBe(false);
      },
    );
  });

  describe("número (number)", () => {
    it.each(["SN", "12A", "100/B", "10-A", "123456"])(
      "aceita número válido: %s",
      (number) => {
        const result = addressSchema.safeParse({ ...validAddress, number });
        expect(result.success).toBe(true);
      },
    );

    it.each(["", "12345678901", "12 3", "12#"])(
      "rejeita número inválido: %s",
      (number) => {
        const result = addressSchema.safeParse({ ...validAddress, number });
        expect(result.success).toBe(false);
      },
    );
  });

  describe("complemento (complement)", () => {
    it.each([
      "Apto 101/B",
      "Casa (fundos)",
      "Bloco #2, 1º andar",
      "",
      undefined,
    ])("aceita complemento válido: %s", (complement) => {
      const result = addressSchema.safeParse({ ...validAddress, complement });
      expect(result.success).toBe(true);
    });

    it.each(["A".repeat(61), "Apto @ 101"])(
      "rejeita complemento inválido: %s",
      (complement) => {
        const result = addressSchema.safeParse({ ...validAddress, complement });
        expect(result.success).toBe(false);
      },
    );
  });

  describe("estado (state)", () => {
    it.each(["PB", "SP", "RJ", "RN", "PE"])(
      "aceita UF válida: %s",
      (state) => {
        const result = addressSchema.safeParse({ ...validAddress, state });
        expect(result.success).toBe(true);
      },
    );

    it.each(["Paraíba", "Paraiba", "pb", "XX", ""])(
      "rejeita estado fora da lista de UFs: %s",
      (state) => {
        const result = addressSchema.safeParse({ ...validAddress, state });
        expect(result.success).toBe(false);
      },
    );
  });

  describe("cidade (city)", () => {
    it.each(["Esperança", "Olho d'Água", "Pau dos Ferros", "São João do Rio do Peixe"])(
      "aceita cidade válida: %s",
      (city) => {
        const result = addressSchema.safeParse({ ...validAddress, city });
        expect(result.success).toBe(true);
      },
    );

    it.each(["A", "A".repeat(61), "Cidade 123"])(
      "rejeita cidade inválida: %s",
      (city) => {
        const result = addressSchema.safeParse({ ...validAddress, city });
        expect(result.success).toBe(false);
      },
    );
  });

  describe("bairro (neighborhood)", () => {
    it.each(["Centro", "Conjunto D'Ávila 2", "Setor Sul - Etapa 1"])(
      "aceita bairro válido: %s",
      (neighborhood) => {
        const result = addressSchema.safeParse({
          ...validAddress,
          neighborhood,
        });
        expect(result.success).toBe(true);
      },
    );

    it.each(["A", "A".repeat(61), "Bairro @ Centro"])(
      "rejeita bairro inválido: %s",
      (neighborhood) => {
        const result = addressSchema.safeParse({
          ...validAddress,
          neighborhood,
        });
        expect(result.success).toBe(false);
      },
    );
  });

  describe("CEP (cep)", () => {
    it("aceita CEP no formato 99999-999", () => {
      expect(
        addressSchema.safeParse({ ...validAddress, cep: "58135-000" }).success,
      ).toBe(true);
    });

    it.each(["58135000", "5813-5000", "abcde-fgh", ""])(
      "rejeita CEP inválido: %s",
      (cep) => {
        expect(
          addressSchema.safeParse({ ...validAddress, cep }).success,
        ).toBe(false);
      },
    );
  });
});
