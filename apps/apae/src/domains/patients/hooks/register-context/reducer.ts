import {
  PersonalData,
  KinshipData,
  AddressData,
  AdditionalsData,
  GuardianData,
  ProfileData,
  MembersRegisterStep,
  MembersRegisterState,
} from "./types";

export type MembersRegisterAction =
  | { type: "SET_PERSONAL_DATA"; payload: Partial<PersonalData> }
  | { type: "SET_KINSHIPS_DATA"; payload: KinshipData[] }
  | { type: "SET_ADDRESS_DATA"; payload: Partial<AddressData> }
  | { type: "SET_ADDITIONALS_DATA"; payload: Partial<AdditionalsData> }
  | { type: "SET_GUARDIAN_DATA"; payload: Partial<GuardianData> }
  | { type: "SET_PROFILE_DATA"; payload: Partial<ProfileData> }
  | { type: "SET_STEP"; payload: MembersRegisterStep }
  | { type: "LOAD_ALL_DATA"; payload: MembersRegisterState };

export function membersRegisterReducer(
  state: MembersRegisterState,
  action: MembersRegisterAction,
): MembersRegisterState {
  switch (action.type) {
    case "SET_PERSONAL_DATA":
      return {
        ...state,
        personal: {
          ...state.personal,
          ...action.payload,
          rg: {
            ...state.personal.rg,
            ...action.payload.rg,
            issuing: {
              ...state.personal.rg?.issuing,
              ...action.payload.rg?.issuing,
            },
          },
          birth: { ...state.personal.birth, ...action.payload.birth },
        },
      };
    case "SET_KINSHIPS_DATA":
      return { ...state, kinships: action.payload };
    case "SET_ADDRESS_DATA":
      return { ...state, address: { ...state.address, ...action.payload } };
    case "SET_ADDITIONALS_DATA":
      return {
        ...state,
        additionals: {
          ...state.additionals,
          ...action.payload,
          disability: {
            ...state.additionals.disability,
            ...action.payload.disability,
          },
          care: { ...state.additionals.care, ...action.payload.care },
        },
      };
    case "SET_GUARDIAN_DATA":
      return {
        ...state,
        guardian: {
          ...state.guardian,
          ...action.payload,
          address: { ...state.guardian.address, ...action.payload.address },
        },
      };
    case "SET_PROFILE_DATA":
      return { ...state, profile: { ...state.profile, ...action.payload } };
    case "SET_STEP":
      return { ...state, step: action.payload };
    case "LOAD_ALL_DATA":
      return { ...action.payload };
    default:
      return state;
  }
}

export const initialState: MembersRegisterState = {
  personal: {
    name: "",
    cpf: "",
    phone: "",
    rg: { number: "", issuing: { body: "", date: new Date() } },
    cns: "",
    nis: "",
    birth: { certificate: "", date: new Date(), place: "" },
  },
  address: {
    cep: "",
    state: "",
    city: "",
    neighborhood: "",
    street: "",
    number: "",
    complement: "",
    noNumber: false,
    district: "",
  },
  additionals: {
    id: undefined,
    diseases: "",
    medications: "",
    vaccines: [],
    allergies: "",
    disability: { types: [], report: undefined },
    care: { types: [], referral: undefined },
    bpc: false,
    householdIncome: "",
  },
  guardian: {
    address: {
      cep: "",
      state: "",
      city: "",
      neighborhood: "",
      street: "",
      number: "",
      complement: "",
      noNumber: false,
      district: "",
    },
    contact: "",
    kinship: "",
    name: "",
  },
  kinships: [],
  step: MembersRegisterStep.PERSONAL,
  profile: { role: "patient", photo: undefined },
};
