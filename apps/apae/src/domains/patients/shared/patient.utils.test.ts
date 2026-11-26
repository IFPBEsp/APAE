import { describe, expect, it } from "vitest";
import { initialState } from "@/domains/patients/hooks/register-context/reducer";
import type { MembersRegisterState } from "@/domains/patients/hooks/register-context/types";
import {
  buildPatientPayload,
  draftNeedsFileWarning,
  getStepForBackendField,
  hasRequiredRegistrationFiles,
} from "./patient.utils";
import { MembersRegisterStep } from "../hooks/register-context/types";

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

describe("getStepForBackendField", () => {
  it("mapeia reports para Informações Adicionais", () => {
    expect(getStepForBackendField("reports")).toBe(
      MembersRegisterStep.ADDITIONALS,
    );
  });

  it("exige o laudo médico antes do cadastro", () => {
    expect(
      hasRequiredRegistrationFiles({
        disability: { report: new File(["laudo"], "laudo.pdf"), types: [] },
      }),
    ).toBe(true);

    expect(
      hasRequiredRegistrationFiles({
        disability: { report: undefined, types: [] },
      }),
    ).toBe(false);
  });

  it("avisa quando um rascunho recuperado já passou de Informações Adicionais", () => {
    expect(draftNeedsFileWarning(MembersRegisterStep.PROFILE)).toBe(true);
    expect(draftNeedsFileWarning(MembersRegisterStep.ADDITIONALS)).toBe(false);
    expect(draftNeedsFileWarning(MembersRegisterStep.GUARDIAN)).toBe(false);
  });
});

describe("buildPatientPayload - doenças opcional (#1151)", () => {
  const buildWithDiseases = (diseases?: string): MembersRegisterState => ({
    ...initialState,
    additionals: {
      ...initialState.additionals,
      diseases,
    },
  });

  it("envia null (e não 'Nenhuma') quando doenças está vazio, só espaços ou indefinido", () => {
    expect(buildPatientPayload(buildWithDiseases("")).annualRegistry.diseases).toBeNull();
    expect(buildPatientPayload(buildWithDiseases("   ")).annualRegistry.diseases).toBeNull();
    expect(buildPatientPayload(buildWithDiseases(undefined)).annualRegistry.diseases).toBeNull();
  });

  it("envia o valor informado quando doenças é preenchido", () => {
    const payload = buildPatientPayload(buildWithDiseases("Diabetes, Catapora"));
    expect(payload.annualRegistry.diseases).toBe("Diabetes, Catapora");
  });
});
