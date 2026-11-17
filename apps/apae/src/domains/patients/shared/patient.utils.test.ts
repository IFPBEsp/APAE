import { describe, expect, it } from "vitest";
import { initialState } from "@/domains/patients/hooks/register-context/reducer";
import type { MembersRegisterState } from "@/domains/patients/hooks/register-context/types";
import { buildPatientPayload } from "./patient.utils";

const buildState = (
  birth: Partial<MembersRegisterState["personal"]["birth"]>,
): MembersRegisterState => ({
  ...initialState,
  personal: {
    ...initialState.personal,
    birth: { ...initialState.personal.birth, ...birth },
  },
});

describe("buildPatientPayload - certidão (#1132)", () => {
  it("envia cartório, fls e livro digitados", () => {
    const payload = buildPatientPayload(
      buildState({
        certificate: "12345",
        registryOffice: "Cartório X",
        fls: "10",
        book: "A-1",
      }),
    );

    expect(payload.registryOffice).toBe("Cartório X");
    expect(payload.fls).toBe("10");
    expect(payload.book).toBe("A-1");
  });

  it("envia null quando cartório, fls e livro estão vazios", () => {
    const payload = buildPatientPayload(
      buildState({
        certificate: "1".repeat(32),
        registryOffice: "",
        fls: "",
        book: "",
      }),
    );

    expect(payload.registryOffice).toBeNull();
    expect(payload.fls).toBeNull();
    expect(payload.book).toBeNull();
  });

  it("envia null quando os campos não foram definidos ou só têm espaços", () => {
    const payload = buildPatientPayload(
      buildState({ certificate: "12345", registryOffice: "  ", fls: undefined }),
    );

    expect(payload.registryOffice).toBeNull();
    expect(payload.fls).toBeNull();
  });

  it("não substitui certidão vazia por '0'", () => {
    const payload = buildPatientPayload(buildState({ certificate: "" }));
    expect(payload.birthCertificateNumber).toBe("");
  });

  it("mantém o número da certidão informado", () => {
    const payload = buildPatientPayload(buildState({ certificate: "12345" }));
    expect(payload.birthCertificateNumber).toBe("12345");
  });
});
