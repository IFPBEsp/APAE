import { describe, expect, it } from "vitest";
import { draftNeedsFileWarning, getStepForBackendField, hasRequiredRegistrationFiles } from "./patient.utils";
import { MembersRegisterStep } from "../hooks/register-context/types";

describe("getStepForBackendField", () => {
  it.each(["reports", "referrals"])("maps %s to additional information", (field) => {
    expect(getStepForBackendField(field)).toBe(MembersRegisterStep.ADDITIONALS);
  });

  it("requires both medical documents before registration", () => {
    const additionals = {
      disability: { report: new File(["laudo"], "laudo.pdf"), types: [] },
      care: { referral: new File(["encaminhamento"], "encaminhamento.pdf"), types: [] },
    };
    expect(hasRequiredRegistrationFiles(additionals)).toBe(true);
    expect(hasRequiredRegistrationFiles({
      disability: { report: undefined, types: [] },
      care: { referral: undefined, types: [] },
    })).toBe(false);
  });

  it("warns when a restored draft has passed additional information", () => {
    expect(draftNeedsFileWarning(MembersRegisterStep.PROFILE)).toBe(true);
    expect(draftNeedsFileWarning(MembersRegisterStep.ADDITIONALS)).toBe(false);
    expect(draftNeedsFileWarning(MembersRegisterStep.GUARDIAN)).toBe(false);
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
