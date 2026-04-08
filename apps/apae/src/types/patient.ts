export interface Patient {
  id: string;
  fullName: string;
  cpf: string;
  rg: string;
  cns?: string;
  nis?: string;
  birthDate?: string;
  birthplace?: string;
  nationality?: string;
  contact?: string;
  phone?: string;
  allergies?: string;
  isStudent?: boolean;
  isDeleted?: boolean;
  photoUrl?: string;
  registrationDate?: string;
  issuingAgency?: string;
  issueDate?: string;
  birthCertificateNumber?: string;
  registryOffice?: string;
  book?: string;
  fls?: string;
  annualRegistry?: AnnualRegistry;
  vaccineNames?: Vaccine[];
  address?: Address;
  guardian?: Guardian;
  parents?: Parent[];
  createdAt?: string;
  updatedAt?: string;
  deleted?: boolean;
  age?: number;
}

export interface AnnualRegistry {
  id?: string;
  year?: number;
  bpc?: boolean;
  familyIncome?: number;
  diseases?: string;
  continuousMedication?: string;
  allergies?: string;
  disorders?: Disorder[];
  serviceAreas?: ServiceArea[];
  serviceArea?: ServiceArea[];
  serviceTypes?: ServiceArea[];
}

export interface Vaccine {
  id?: string;
  name: string;
}

export interface Disorder {
  id?: string;
  name: string;
}

export interface ServiceArea {
  id?: string;
  area?: string;
  name?: string;
}

export interface Address {
  id?: string;
  street?: string;
  number?: string;
  neighborhood?: string;
  city?: string;
  state?: string;
  cep?: string;
  complement?: string;
}

export interface Guardian {
  id?: string;
  name?: string;
  contact?: string;
  kinship?: string;
  address?: Address;
}

export interface Parent {
  id?: string;
  name?: string;
  cpf?: string;
  rg?: string;
  profession?: string;
  isAlive?: boolean;
  kinship?: string;
}

export interface ApiError {
  message: string;
  response?: {
    data?: {
      message?: string;
      fields?: Array<{ field?: string; message?: string }>;
    };
  };
}
