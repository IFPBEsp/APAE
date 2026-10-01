export interface PatientAddress {
  city: string;
  cep: string;
  state: string;
  neighborhood: string;
  street: string;
  number: string;
  complement: string;
}

/** Endereço reduzido devolvido pela listagem: só a cidade é garantida. */
export type PatientAddressSummary = Pick<PatientAddress, "city"> &
  Partial<Omit<PatientAddress, "city">>;

export type PatientGuardianAddress = PatientAddress;

export interface PatientGuardian {
  name: string;
  contact: string;
  kinship: string;
  address?: PatientGuardianAddress;
}

export interface PatientParent {
  id?: string;
  name: string;
  rg: string;
  cpf: string;
  profession: string;
  isAlive: boolean;
  kinship: string;
}

export interface PatientAnnualRegistry {
  bpc: boolean;
  diseases: string;
  continuousMedication: string;
  serviceArea: { area: string }[];
  familyIncome: number;
  year: number;
  disorders: { name: string }[];
}

export interface PatientPayload {
  fullName: string;
  nationality: string;
  birthDate: string | null;
  contact: string;
  birthCertificateNumber: string;
  registryOffice: string;
  fls: string;
  book: string;
  rg: string;
  issueDate: string | null;
  issuingAgency: string;
  cpf: string;
  cns: string;
  nis: string;
  registrationDate: Date | string | null;
  allergies: string;
  continuousMedication: string;
  isStudent: boolean;
  address: PatientAddress;
  guardian: {
    name: string;
    contact: string;
    kinship: string;
    address: PatientGuardianAddress;
  };
  parents: PatientParent[];
  vaccineNames: { name: string }[];
  annualRegistry: PatientAnnualRegistry;
}

export interface PatientListQueryValues {
  name?: string;
  disorder?: string;
  year?: string;
  city?: string;
  treatmentType?: string;
  page?: number;
  size?: number;
}

export interface Patient {
  id: string;
  fullName: string;
  cpf: string;
  rg: string;
  cns: string;
  nis: string;
  birthDate: string;
  contact: string;
  photoUrl?: string;
  isStudent: boolean;
  isDeleted: boolean;
  address?: PatientAddressSummary;
}

export interface PatientResponse {
  id: string;
  fullName: string;
  nationality: string;
  birthDate: string;
  birthplace?: string;
  contact: string;
  birthCertificateNumber: string;
  registryOffice: string;
  fls: string;
  book: string;
  rg: string;
  issueDate: string;
  issuingAgency: string;
  cpf: string;
  cns: string;
  nis: string;
  registrationDate: string;
  allergies: string;
  isStudent: boolean;
  photoUrl?: string;
  isDeleted: boolean;
  address?: PatientAddress;
  guardian?: PatientGuardian;
  parents?: PatientParent[];
  vaccineNames?: { name: string }[];
}

export interface PatientCardData {
  isDeleted?: boolean;
  isStudent?: boolean;
  id: string;
  photoUrl: string | null;
  fullName: string | null;
  cpf: string | null;
  contact: string | null;
  address: {
    city: string | null;
  } | null;
}
