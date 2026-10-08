import { MembersRegisterStep } from "./hooks/register-context/types";

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
