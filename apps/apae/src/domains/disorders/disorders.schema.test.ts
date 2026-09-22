import { describe, it, expect } from "vitest";
import { createDisorderSchema, updateDisorderSchema } from "./disorders.schema";

describe("createDisorderSchema", () => {
  it("aceita nome preenchido", () => {
    const result = createDisorderSchema.safeParse({ name: "TEA" });
    expect(result.success).toBe(true);
  });

  it("rejeita nome vazio", () => {
    const result = createDisorderSchema.safeParse({ name: "" });
    expect(result.success).toBe(false);
    expect(result.error?.issues[0].message).toBe("O nome é obrigatório.");
  });
});

describe("updateDisorderSchema", () => {
  it("aceita nome preenchido", () => {
    const result = updateDisorderSchema.safeParse({ name: "TDAH" });
    expect(result.success).toBe(true);
  });

  it("rejeita nome vazio", () => {
    const result = updateDisorderSchema.safeParse({ name: "" });
    expect(result.success).toBe(false);
    expect(result.error?.issues[0].message).toBe("O nome é obrigatório.");
  });
});