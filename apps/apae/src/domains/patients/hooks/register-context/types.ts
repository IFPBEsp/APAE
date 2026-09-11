export interface PersonalData {
  name: string;
  cpf: string;
  phone: string;
  rg: { number: string; issuing: { body: string; date: Date } };
  cns: string;
  nis: string;
  birth: { certificate: string; date: Date; place: string };
}

export interface KinshipData {
  type: string;
  rg: string;
  cpf: string;
  alive: boolean;
  name: string;
  occupation: string;
  isLegalGuardian?: boolean;
}

export interface AddressData {
  cep: string;
  state: string;
  city: string;
  neighborhood: string;
  street: string;
  noNumber?: boolean;
  number: string;
  complement?: string;
  district: string;
}

export interface AdditionalsData {
  id?: string;
  diseases: string;
  medications: string;
  vaccines: string[];
  allergies: string;
  disability: { types: string[]; report: File | string | undefined };
  care: { types: string[]; referral: File | string | undefined };
  bpc: boolean;
  householdIncome: string;
}

export interface GuardianData {
  address: AddressData;
  name: string;
  kinship: string;
  contact: string;
}

export interface ProfileData {
  photo: File | string | undefined;
  role: "student" | "patient";
}

export enum MembersRegisterStep {
  PERSONAL = "personal",
  KINSHIPS = "kinships",
  ADDRESS = "address",
  ADDITIONALS = "additionals",
  GUARDIAN = "guardian",
  PROFILE = "profile",
}

export interface MembersRegisterState {
  personal: PersonalData;
  kinships: KinshipData[];
  address: AddressData;
  additionals: AdditionalsData;
  guardian: GuardianData;
  profile: ProfileData;
  step: MembersRegisterStep;
}

export interface MembersRegisterContextData {
  state: MembersRegisterState;
  setters: {
    setPersonalData: (data: Partial<PersonalData>) => void;
    setKinshipsData: (data: KinshipData[]) => void;
    setAddressData: (data: Partial<AddressData>) => void;
    setAdditionalsData: (data: Partial<AdditionalsData>) => void;
    setGuardianData: (data: Partial<GuardianData>) => void;
    setProfileData: (data: Partial<ProfileData>) => void;
    setStep: (step: MembersRegisterStep) => void;
    loadAllData: (data: MembersRegisterState) => void;
  };
  register: (id?: string) => Promise<{ status: number; data: Record<string, unknown> }>;
}
