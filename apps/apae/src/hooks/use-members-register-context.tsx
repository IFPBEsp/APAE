"use client";

import { createContext, useCallback, useContext, useReducer } from "react";

interface PersonalData {
  name: string;
  cpf: string;
  phone: string;
  rg: {
    number: string;
    issuing: {
      body: string;
      date: Date;
    };
  };
  cns: string;
  nis: string;
  birth: {
    certificate: string;
    date: Date;
    place: string;
  };
}

interface KinshipData {
  type: string;
  rg: string;
  cpf: string;
  alive: boolean;
  name: string;
  occupation: string;
}

interface AddressData {
  cep: string;
  state: string;
  city: string;
  district: string;
  street: string;
}

interface AdditionalsData {
  id?: string;
  diseases: string;
  medications: string;
  vaccines: string[];
  allergies: string;
  disability: {
    types: string[];
    report: File | undefined;
  };
  care: {
    types: string[];
    referral: File | undefined;
  };
  bpc: boolean;
  householdIncome: string;
}

interface GuardianData {
  address: AddressData;
  name: string;
  kinship: string;
  contact: string;
}

interface ProfileData {
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

interface MembersRegisterState {
  personal: PersonalData;
  kinships: KinshipData[];
  address: AddressData;
  additionals: AdditionalsData;
  guardian: GuardianData;
  profile: ProfileData;
  step: MembersRegisterStep;
}

interface MembersRegisterContextData {
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
  register: (id?: string) => Promise<{ status: number; data: any }>;
}

type MembersRegisterAction =
  | { type: "SET_PERSONAL_DATA"; payload: Partial<PersonalData> }
  | { type: "SET_KINSHIPS_DATA"; payload: KinshipData[] }
  | { type: "SET_ADDRESS_DATA"; payload: Partial<AddressData> }
  | { type: "SET_ADDITIONALS_DATA"; payload: Partial<AdditionalsData> }
  | { type: "SET_GUARDIAN_DATA"; payload: Partial<GuardianData> }
  | { type: "SET_PROFILE_DATA"; payload: Partial<ProfileData> }
  | { type: "SET_STEP"; payload: MembersRegisterStep }
  | { type: "LOAD_ALL_DATA"; payload: MembersRegisterState };

function membersRegisterReducer(
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
              ...state.personal.rg.issuing,
              ...action.payload.rg?.issuing,
            },
          },
          birth: {
            ...state.personal.birth,
            ...action.payload.birth,
          },
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
          care: {
            ...state.additionals.care,
            ...action.payload.care,
          },
        },
      };
    case "SET_GUARDIAN_DATA":
      return {
        ...state,
        guardian: {
          ...state.guardian,
          ...action.payload,
          address: {
            ...state.guardian.address,
            ...action.payload.address,
          },
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

const initialState: MembersRegisterState = {
  personal: {
    name: "",
    cpf: "",
    phone: "",
    rg: { number: "", issuing: { body: "", date: new Date() } },
    cns: "",
    nis: "",
    birth: { certificate: "", date: new Date(), place: "" },
  },
  address: { cep: "", state: "", city: "", district: "", street: "" },
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
    address: { cep: "", state: "", city: "", district: "", street: "" },
    contact: "",
    kinship: "",
    name: "",
  },
  kinships: [],
  step: MembersRegisterStep.PERSONAL,
  profile: { role: "patient", photo: undefined },
};

const MembersRegisterContext = createContext<
  MembersRegisterContextData | undefined
>(undefined);

export function MembersRegisterProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [state, dispatch] = useReducer(membersRegisterReducer, initialState);

  const setters = {
    setPersonalData: useCallback(
      (data: Partial<PersonalData>) =>
        dispatch({ type: "SET_PERSONAL_DATA", payload: data }),
      [],
    ),
    setKinshipsData: useCallback(
      (data: KinshipData[]) =>
        dispatch({ type: "SET_KINSHIPS_DATA", payload: data }),
      [],
    ),
    setAddressData: useCallback(
      (data: Partial<AddressData>) =>
        dispatch({ type: "SET_ADDRESS_DATA", payload: data }),
      [],
    ),
    setAdditionalsData: useCallback(
      (data: Partial<AdditionalsData>) =>
        dispatch({ type: "SET_ADDITIONALS_DATA", payload: data }),
      [],
    ),
    setGuardianData: useCallback(
      (data: Partial<GuardianData>) =>
        dispatch({ type: "SET_GUARDIAN_DATA", payload: data }),
      [],
    ),
    setProfileData: useCallback(
      (data: Partial<ProfileData>) =>
        dispatch({ type: "SET_PROFILE_DATA", payload: data }),
      [],
    ),
    setStep: useCallback(
      (step: MembersRegisterStep) =>
        dispatch({ type: "SET_STEP", payload: step }),
      [],
    ),
    loadAllData: useCallback(
      (data: MembersRegisterState) =>
        dispatch({ type: "LOAD_ALL_DATA", payload: data }),
      [],
    ),
  };

  const register = useCallback(
    async (id?: string) => {
      const { personal, address, additionals, guardian, kinships, profile } =
        state;

      const formatDate = (date: any) => {
        if (!date) return null;
        const d = new Date(date);
        return isNaN(d.getTime()) ? null : d.toISOString().split("T")[0];
      };

      const parseIncome = (val: string) => {
        const clean = String(val).replace(/[^\d]/g, ""); 
        const num = parseFloat(clean) * 0.01; 
        return isNaN(num) ? 0.0 : num;
      };

      const patient: any = {
        fullName: personal.name || "Não informado",
        nationality: personal.birth.place || "Brasileiro",
        birthDate: formatDate(personal.birth.date),
        contact: personal.phone || "Não informado",
        birthCertificateNumber: personal.birth.certificate || "0",
        registryOffice: "Cartorio",
        fls: "0",
        book: "0",
        rg: personal.rg.number || "0",
        issueDate: formatDate(personal.rg.issuing.date),
        issuingAgency: personal.rg.issuing.body || "SSP/SP",
        cpf: personal.cpf,
        cns: personal.cns || "000 0000 0000 0000",
        nis: personal.nis || "0",
        registrationDate: formatDate(new Date()),
        allergies: additionals.allergies || "Nenhuma",
        isStudent: profile.role === "student",
        address: {
          city: address.city || "Não informado",
          cep: address.cep || "00000-000",
          state: address.state || "Não informado",
          neighborhood: address.district || "Não informado",
          street: address.street.split(",")[0].trim() || "Não informado",
          number: address.street.split(",")[1]?.trim() || "S/N",
          complement: "",
        },
        guardian: {
          name: guardian.name || "Não informado",
          contact: guardian.contact || "Não informado",
          kinship: guardian.kinship || "Não informado",
          address: {
            city: guardian.address.city || "Não informado",
            cep: guardian.address.cep || "00000-000",
            state: guardian.address.state || "Não informado",
            neighborhood: guardian.address.district || "Não informado",
            street: guardian.address.street.split(",")[0].trim() || "Não informado",
            number: guardian.address.street.split(",")[1]?.trim() || "S/N",
            complement: "",
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
      };

      if (id) {
        const responsePessoa = await fetch(`/api/pessoas/${id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(patient),
        });

        if (profile.photo instanceof File && responsePessoa.ok) {
          const photoFormData = new FormData();
          photoFormData.append("photo", profile.photo);
          await fetch(`/api/pessoas/${id}/photo`, {
            method: "PUT",
            body: photoFormData,
          });
        }

        let resData = {};
        const text = await responsePessoa.text();
        if (text) {
          try { resData = JSON.parse(text); } catch(e) {}
        }
        return { status: responsePessoa.status, data: resData };

      } else {
        patient.annualRegistry = {
          bpc: additionals.bpc,
          diseases: additionals.diseases,
          serviceArea: additionals.care.types.map((area: string) => ({ area })),
          familyIncome: parseIncome(additionals.householdIncome),
          year: new Date().getFullYear(),
          disorders: additionals.disability.types.map((name: string) => ({ name })),
        };

        const formData = new FormData();
        formData.append("patient", new Blob([JSON.stringify(patient)], { type: "application/json" }));
        if (profile.photo instanceof File) formData.append("photo", profile.photo);
        if (additionals.disability.report instanceof File) formData.append("reports", additionals.disability.report);
        if (additionals.care.referral instanceof File) formData.append("referrals", additionals.care.referral);

        const response = await fetch("/api/pessoas", { method: "POST", body: formData });
        const data = await response.json().catch(() => ({}));
        return { status: response.status, data };
      }
    },
    [state],
  );

  return (
    <MembersRegisterContext.Provider value={{ state, setters, register }}>
      {children}
    </MembersRegisterContext.Provider>
  );
}

export function useMembersRegisterContext() {
  const context = useContext(MembersRegisterContext);
  if (!context) throw new Error("useMembersRegisterContext must be used within a MembersRegisterProvider");
  return context;
}