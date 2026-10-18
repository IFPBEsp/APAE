import { serializeCivilDate } from "@/lib/date";
import {
  MembersRegisterStep,
  type MembersRegisterState,
} from "@/domains/patients/hooks/register-context/types";
import type {
  PatientListQueryValues,
  PatientPayload,
} from "@/domains/patients/types/patient";

export function buildPatientListQuery(
  values: PatientListQueryValues = {},
): URLSearchParams {
  const params = new URLSearchParams();

  const name = values.name?.trim();
  const disorder = values.disorder?.trim();
  const year = values.year?.trim();
  const city = values.city?.trim();
  const treatmentType = values.treatmentType?.trim();

  if (name) params.append("name", name);
  if (disorder) params.append("disorder", disorder);
  if (year) params.append("year", year);
  if (city) params.append("city", city);
  if (treatmentType) params.append("treatmentType", treatmentType);

  params.append("page", String(values.page ?? 0));
  params.append("size", String(values.size ?? 10));

  return params;
}

function parseIncome(value: string) {
  const clean = String(value).replace(/[^\d]/g, "");
  const num = parseFloat(clean) * 0.01;
  return Number.isNaN(num) ? 0.0 : num;
}

export function buildPatientPayload(
  state: MembersRegisterState,
): PatientPayload {
  const { personal, address, additionals, guardian, kinships, profile } = state;

  const annualRegistryData = {
    bpc: additionals.bpc,
    diseases: additionals.diseases || "Nenhuma",
    continuousMedication: additionals.medications || "Nenhum",
    serviceArea: additionals.care.types.map((area: string) => ({ area })),
    familyIncome: parseIncome(additionals.householdIncome),
    year: new Date().getFullYear(),
    disorders: additionals.disability.types.map((name: string) => ({ name })),
  };

  return {
    fullName: personal.name || "Não informado",
    nationality: personal.birth.place || "Brasileiro",
    birthDate: serializeCivilDate(personal.birth.date),
    contact: personal.phone || "Não informado",
    birthCertificateNumber: personal.birth.certificate || "0",
    registryOffice: "Cartorio",
    fls: "0",
    book: "0",
    rg: personal.rg.number || "0",
    issueDate: serializeCivilDate(personal.rg.issuing.date),
    issuingAgency: personal.rg.issuing.body || "SSP/SP",
    cpf: personal.cpf,
    cns: personal.cns || "000 0000 0000 0000",
    nis: personal.nis || "0",
    registrationDate: serializeCivilDate(personal.rg.issuing.date),
    allergies: additionals.allergies || "Nenhuma",
    continuousMedication: additionals.medications || "Nenhum",
    isStudent: profile.role === "student",
    address: {
      city: address.city || "Não informado",
      cep: address.cep || "00000-000",
      state: address.state || "Não informado",
      neighborhood: address.neighborhood || "Não informado",
      street: address.street || "Não informado",
      number: address.number || "S/N",
      complement: address.complement || "",
    },
    guardian: {
      name: guardian.name || "Não informado",
      contact: guardian.contact || "Não informado",
      kinship: guardian.kinship || "Não informado",
      address: {
        city: guardian.address.city || "Não informado",
        cep: guardian.address.cep || "00000-000",
        state: guardian.address.state || "Não informado",
        neighborhood: guardian.address.neighborhood || "Não informado",
        street: guardian.address.street || "Não informado",
        number: guardian.address.number || "SN",
        complement: guardian.address.complement || "",
      },
    },
    parents: kinships.map((k) => ({
      name: k.name || "Não informado",
      rg: k.rg || "0",
      cpf: k.cpf || "000.000.000-00",
      profession: k.occupation || "Não informado",
      isAlive: k.alive,
      kinship: k.type || "Pai/Mãe",
    })),
    vaccineNames: additionals.vaccines.map((v) => ({ name: v })),
    annualRegistry: annualRegistryData,
  };
}

export interface ConflictError {
  field: string;
  message: string;
}

const PERSONAL_FIELDS = [
  "fullName",
  "cpf",
  "rg",
  "contact",
  "birth",
  "nationality",
  "cns",
  "nis",
  "phone",
  "name",
];

const ADDITIONALS_FIELDS = [
  "annualRegistry",
  "vaccine",
  "allergies",
  "diseases",
  "familyIncome",
  "householdIncome",
];

export function isSuccessStatus(status: number): boolean {
  return status === 201 || status === 200 || status === 204;
}

export function getConflictError(backendMessage: string): ConflictError {
  const msgLower = backendMessage.toLowerCase();

  if (msgLower.includes("rg") || msgLower.includes("identidade")) {
    return {
      field: "rg.number",
      message: "Este RG já está cadastrado no sistema.",
    };
  }
  if (msgLower.includes("cns")) {
    return { field: "cns", message: "Este CNS já está cadastrado no sistema." };
  }
  if (msgLower.includes("cpf")) {
    return { field: "cpf", message: "Este CPF já está cadastrado no sistema." };
  }
  return {
    field: "cpf",
    message: "CPF ou documento já cadastrado no sistema.",
  };
}

function includesAny(fieldLower: string, fields: string[]): boolean {
  return fields.some((f) => fieldLower.includes(f.toLowerCase()));
}

export function getStepForBackendField(
  backendField: string,
): MembersRegisterStep | undefined {
  const fieldLower = backendField.toLowerCase();

  if (includesAny(fieldLower, PERSONAL_FIELDS)) {
    return MembersRegisterStep.PERSONAL;
  }
  if (fieldLower.includes("parents") || fieldLower.includes("kinships")) {
    return MembersRegisterStep.KINSHIPS;
  }
  if (fieldLower.includes("address") && !fieldLower.includes("guardian")) {
    return MembersRegisterStep.ADDRESS;
  }
  if (includesAny(fieldLower, ADDITIONALS_FIELDS)) {
    return MembersRegisterStep.ADDITIONALS;
  }
  if (fieldLower.includes("guardian")) {
    return MembersRegisterStep.GUARDIAN;
  }
  return undefined;
}
