import { describe, expect, it } from "vitest";
import { initialState, membersRegisterReducer } from "./reducer";

describe("membersRegisterReducer birth certificate file", () => {
  it("merges the optional file into birth data without replacing other fields", () => {
    const file = new File(["cópia"], "certidao.pdf", {
      type: "application/pdf",
    });

    const nextState = membersRegisterReducer(initialState, {
      type: "SET_PERSONAL_DATA",
      payload: {
        birth: { ...initialState.personal.birth, file },
      },
    });

    expect(nextState.personal.birth.file).toBe(file);
    expect(nextState.personal.birth.certificate).toBe(
      initialState.personal.birth.certificate,
    );
  });

  it("merges the optional CPF copy file into personal data without replacing other fields", () => {
    const cpfFile = new File(["cópia"], "cpf.pdf", {
      type: "application/pdf",
    });

    const nextState = membersRegisterReducer(initialState, {
      type: "SET_PERSONAL_DATA",
      payload: { cpfFile },
    });

    expect(nextState.personal.cpfFile).toBe(cpfFile);
    expect(nextState.personal.cpf).toBe(initialState.personal.cpf);
  });
});
