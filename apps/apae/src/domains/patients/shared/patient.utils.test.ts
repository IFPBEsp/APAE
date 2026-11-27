import { describe, expect, it } from "vitest";
import { initialState } from "@/domains/patients/hooks/register-context/reducer";
import type { MembersRegisterState } from "@/domains/patients/hooks/register-context/types";
import {
  buildPatientPayload,
  draftNeedsFileWarning,
  getStepForBackendField,
  hasRequiredRegistrationFiles,
  mapParentProfessionToForm,
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

describe("buildPatientPayload - CPF opcional do parente (#1156)", () => {
  it("envia null quando o CPF do parente está vazio", () => {
    const state: MembersRegisterState = {
      ...initialState,
      kinships: [
        {
          name: "João da Silva",
          cpf: "",
          rg: "",
          occupation: "Professor",
          alive: true,
          type: "Pai",
        },
      ],
    };

    expect(buildPatientPayload(state).parents[0].cpf).toBeNull();
  });
});

describe("buildPatientPayload - vacinas opcional (#1154)", () => {
  const buildWithVaccines = (vaccines: string[]): MembersRegisterState => ({
    ...initialState,
    additionals: {
      ...initialState.additionals,
      vaccines,
    },
  });

  it("envia array vazio (nunca null) quando nenhuma vacina é selecionada", () => {
    const payload = buildPatientPayload(buildWithVaccines([]));
    expect(payload.vaccineNames).toEqual([]);
    expect(payload.vaccineNames).not.toBeNull();
  });

  it("ignora itens vazios ou 'Não informado' e preserva vacinas válidas", () => {
    const payload = buildPatientPayload(
      buildWithVaccines(["", "   ", "Não informado", "BCG", "Poliomielite"]),
    );
    expect(payload.vaccineNames).toEqual([{ name: "BCG" }, { name: "Poliomielite" }]);
  });
});

describe("buildPatientPayload - contato de emergência do responsável opcional (#1158)", () => {
  const buildWithGuardianContact = (contact?: string): MembersRegisterState => ({
    ...initialState,
    guardian: {
      ...initialState.guardian,
      contact: contact as string,
    },
  });

  it("envia null (e não 'Não informado') quando o contato do responsável está vazio, só espaços ou indefinido", () => {
    expect(buildPatientPayload(buildWithGuardianContact("")).guardian.contact).toBeNull();
    expect(buildPatientPayload(buildWithGuardianContact("   ")).guardian.contact).toBeNull();
    expect(buildPatientPayload(buildWithGuardianContact(undefined)).guardian.contact).toBeNull();
  });

  it("envia o telefone formatado quando o contato do responsável é informado", () => {
    const payload = buildPatientPayload(buildWithGuardianContact("(83) 99999-9999"));
    expect(payload.guardian.contact).toBe("(83) 99999-9999");
  });
});

describe("buildPatientPayload e edição - profissão do parente opcional (#1157)", () => {
  const buildWithOccupation = (occupation?: string): MembersRegisterState => ({
    ...initialState,
    kinships: [
      {
        name: "Maria da Silva",
        rg: "",
        cpf: "529.982.247-25",
        alive: true,
        occupation,
        type: "Mãe",
      },
    ],
  });

  it("envia null (e não 'Não informado') quando occupation está vazio, só espaços ou indefinido", () => {
    expect(buildPatientPayload(buildWithOccupation("")).parents[0].profession).toBeNull();
    expect(buildPatientPayload(buildWithOccupation("   ")).parents[0].profession).toBeNull();
    expect(buildPatientPayload(buildWithOccupation(undefined)).parents[0].profession).toBeNull();
  });

  it("envia a profissão com trim quando preenchida", () => {
    expect(buildPatientPayload(buildWithOccupation("Agricultora")).parents[0].profession).toBe(
      "Agricultora",
    );
    expect(buildPatientPayload(buildWithOccupation("  Professor  ")).parents[0].profession).toBe(
      "Professor",
    );
  });

  it("mapParentProfessionToForm abre vazio quando profession é null, vazio ou 'Não informado'", () => {
    expect(mapParentProfessionToForm(null)).toBe("");
    expect(mapParentProfessionToForm(undefined)).toBe("");
    expect(mapParentProfessionToForm("")).toBe("");
    expect(mapParentProfessionToForm("   ")).toBe("");
    expect(mapParentProfessionToForm("Não informado")).toBe("");
    expect(mapParentProfessionToForm("Agricultora")).toBe("Agricultora");
  });
});
