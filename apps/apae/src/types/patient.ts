export interface PatientGuardianAddress {
  city?: string;
  cep?: string;
  state?: string;
  neighborhood?: string;
  street?: string;
  number?: string;
  complement?: string;
}

export interface PatientGuardian {
  name?: string;
  kinship?: string;
  contact?: string;
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

export interface PatientVaccine {
  name: string;
}

export interface PatientAddress {
  city: string | null;
  cep?: string;
  state?: string;
  neighborhood?: string;
  street?: string;
  number?: string;
  complement?: string;
}

export interface Patient {
  id?: string;
  fullName?: string;
  cpf?: string;
  contact?: string;
  birthDate?: string;
  birthplace?: string;
  photoUrl?: string | null;
  isStudent?: boolean;
  isDeleted?: boolean;
  allergies?: string;
  cns?: string;
  nis?: string;
  rg?: string;
  issuingAgency?: string;
  issueDate?: string;
  birthCertificateNumber?: string;
  registryOffice?: string;
  book?: string;
  fls?: string;
  registrationDate?: string;
  address?: PatientAddress | null;
  guardian?: PatientGuardian | null;
  parents?: PatientParent[];
  vaccineNames?: PatientVaccine[];
}

export interface AnnualRegistryServiceArea {
  id?: string;
  area?: string;
  name?: string;
}

export interface AnnualRegistryDisorder {
  id?: string;
  name?: string;
}

export interface AnnualRegistry {
  id?: string;
  year?: number;
  bpc?: boolean;
  familyIncome?: number;
  diseases?: string;
  continuousMedication?: string;
  serviceAreas?: AnnualRegistryServiceArea[];
  serviceArea?: AnnualRegistryServiceArea[];
  serviceTypes?: AnnualRegistryServiceArea[]; 
  disorders?: AnnualRegistryDisorder[];
}

export interface PatientWithRegistry {
  patient: Patient;
  annualRegistry?: AnnualRegistry;
}

export interface RegisterResponse {
  status: number;
  data: {
    message?: string;
    fields?: Array<{
      field: string;
      message: string;
    }>;
  };
}
