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
  address?: {
    city: string;
    street?: string;
    neighborhood?: string;
    state?: string;
    cep?: string;
    number?: string;
    complement?: string;
  };
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
  address?: {
    city: string;
    street: string;
    neighborhood: string;
    state: string;
    cep: string;
    number: string;
    complement: string;
  };
  guardian?: {
    name: string;
    contact: string;
    kinship: string;
    address?: {
      city: string;
      street: string;
      neighborhood: string;
      state: string;
      cep: string;
      number: string;
      complement: string;
    };
  };
  parents?: {
    id: string;
    name: string;
    rg: string;
    cpf: string;
    profession: string;
    isAlive: boolean;
    kinship: string;
  }[];
  vaccineNames?: { name: string }[];
}

export interface Parent {
  id: string;
  name: string;
  rg: string;
  cpf: string;
  profession: string;
  isAlive: boolean;
  kinship: string;
}
