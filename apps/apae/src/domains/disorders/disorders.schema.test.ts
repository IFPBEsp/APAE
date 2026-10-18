import { describe, it, expect } from "vitest";
import { disorderFormSchema  } from "./disorders.schema";

describe("disorderFormSchema", () => {
  it("aceita nome preenchido", () => {
    const result = disorderFormSchema.safeParse({ name: "TEA" });
    expect(result.success).toBe(true);
  });

  it("rejeita nome vazio", () => {
    const result = disorderFormSchema.safeParse({ name: "" });
    expect(result.success).toBe(false);
    expect(result.error?.issues[0].message).toBe("O nome é obrigatório.");
  });
});
