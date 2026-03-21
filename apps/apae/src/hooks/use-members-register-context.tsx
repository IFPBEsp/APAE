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
  photo: File | undefined;
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

      const parseBirthCertificate = (certificate: string) => {
        const p = certificate.trim().split(/\s+/);
        return {
          cartorio: p[0] || "",
          livro: p[5] || "",
          folha: p[6] || "",
        };
      };

      const { cartorio, livro, folha } = parseBirthCertificate(
        personal.birth.certificate,
      );

      const formatDate = (date: any) => {
        if (!date) return null;
        const d = new Date(date);
        return isNaN(d.getTime()) ? null : d.toISOString().split("T")[0];
      };

      const patientPayload: any = {
        fullName: personal.name,
        nationality: personal.birth.place,
        birthDate: formatDate(personal.birth.date),
        contact: personal.phone,
        birthCertificateNumber: personal.birth.certificate,
        registryOffice: cartorio || "Não informado",
        fls: folha || "0",
        book: livro || "0",
        rg: personal.rg.number,
        issueDate: formatDate(personal.rg.issuing.date),
        issuingAgency: personal.rg.issuing.body,
        cpf: personal.cpf,
        cns: personal.cns,
        nis: personal.nis,
        registrationDate: formatDate(new Date()),
        allergies: additionals.allergies || "Nenhuma",
        isStudent: profile.role === "student",
        address: {
          city: address.city,
          cep: address.cep,
          state: address.state,
          neighborhood: address.district,
          street: address.street.split(",")[0].trim(),
          number: address.street.split(",")[1]?.trim() || "S/N",
          complement: "",
        },
        guardian: {
          name: guardian.name,
          contact: guardian.contact,
          kinship: guardian.kinship,
          address: {
            city: guardian.address.city,
            cep: guardian.address.cep,
            state: guardian.address.state,
            neighborhood: guardian.address.district,
            street: guardian.address.street.split(",")[0].trim(),
            number: guardian.address.street.split(",")[1]?.trim() || "S/N",
            complement: "",
          },
        },
        parents: kinships.map((k) => ({
          name: k.name,
          rg: k.rg,
          cpf: k.cpf,
          profession: k.occupation,
          isAlive: k.alive,
          kinship: k.type,
        })),
        vaccineNames:
          additionals.vaccines.length > 0
            ? additionals.vaccines.map((v) => ({ name: v }))
            : [{ name: "Nenhuma" }],
      };

      if (id) {
        const response = await fetch(`/api/pessoas/${id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(patientPayload),
        });

        let responseData = {};
        const text = await response.text();
        if (text) {
          try {
            responseData = JSON.parse(text);
          } catch (e) {
            responseData = { message: text };
          }
        }

        return { status: response.status, data: responseData };
      } else {
        patientPayload.annualRegistry = {
          bpc: additionals.bpc,
          diseases: additionals.diseases,
          serviceArea: additionals.care.types.map((area: string) => ({ area })),
          familyIncome:
            Number(additionals.householdIncome.replace(/\D/g, "")) * 0.01,
          year: new Date().getFullYear(),
          disorders: additionals.disability.types.map((name: string) => ({
            name,
          })),
        };

        const formData = new FormData();
        formData.append(
          "patient",
          new Blob([JSON.stringify(patientPayload)], {
            type: "application/json",
          }),
        );

        if (profile.photo instanceof File) formData.append("photo", profile.photo);
        if (additionals.disability.report instanceof File) formData.append("reports", additionals.disability.report);
        if (additionals.care.referral instanceof File) formData.append("referrals", additionals.care.referral);

        const response = await fetch("/api/pessoas", {
          method: "POST",
          body: formData,
        });

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
  if (!context) throw new Error("Context error");
  return context;
}