"use client";

import { createContext, useCallback, useContext, useReducer, useEffect, useMemo } from "react";
import { useParams } from "next/navigation";

interface PersonalData {
  name: string;
  cpf: string;
  phone: string;
  rg: { number: string; issuing: { body: string; date: Date } };
  cns: string;
  nis: string;
  birth: { certificate: string; date: Date; place: string };
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
  disability: { types: string[]; report: File | string | undefined };
  care: { types: string[]; referral: File | string | undefined };
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

function membersRegisterReducer(state: MembersRegisterState, action: MembersRegisterAction): MembersRegisterState {
  switch (action.type) {
    case "SET_PERSONAL_DATA":
      return { ...state, personal: { ...state.personal, ...action.payload, rg: { ...state.personal.rg, ...action.payload.rg, issuing: { ...state.personal.rg.issuing, ...action.payload.rg?.issuing } }, birth: { ...state.personal.birth, ...action.payload.birth } } };
    case "SET_KINSHIPS_DATA": return { ...state, kinships: action.payload };
    case "SET_ADDRESS_DATA": return { ...state, address: { ...state.address, ...action.payload } };
    case "SET_ADDITIONALS_DATA": return { ...state, additionals: { ...state.additionals, ...action.payload, disability: { ...state.additionals.disability, ...action.payload.disability }, care: { ...state.additionals.care, ...action.payload.care } } };
    case "SET_GUARDIAN_DATA": return { ...state, guardian: { ...state.guardian, ...action.payload, address: { ...state.guardian.address, ...action.payload.address } } };
    case "SET_PROFILE_DATA": return { ...state, profile: { ...state.profile, ...action.payload } };
    case "SET_STEP": return { ...state, step: action.payload };
    case "LOAD_ALL_DATA": return { ...action.payload };
    default: return state;
  }
}

const initialState: MembersRegisterState = {
  personal: { name: "", cpf: "", phone: "", rg: { number: "", issuing: { body: "", date: new Date() } }, cns: "", nis: "", birth: { certificate: "", date: new Date(), place: "" } },
  address: { cep: "", state: "", city: "", district: "", street: "" },
  additionals: { id: undefined, diseases: "", medications: "", vaccines: [], allergies: "", disability: { types: [], report: undefined }, care: { types: [], referral: undefined }, bpc: false, householdIncome: "" },
  guardian: { address: { cep: "", state: "", city: "", district: "", street: "" }, contact: "", kinship: "", name: "" },
  kinships: [],
  step: MembersRegisterStep.PERSONAL,
  profile: { role: "patient", photo: undefined },
};
const fileToBase64 = (file: File): Promise<string> => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result as string);
    reader.onerror = (error) => reject(error);
  });
};
const base64ToFile = (base64: string, filename: string, mimeType: string): File => {
  const arr = base64.split(",");
  const bstr = atob(arr[1]);
  let n = bstr.length;
  const u8arr = new Uint8Array(n);
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }
  return new File([u8arr], filename, { type: mimeType });
};

const MembersRegisterContext = createContext<MembersRegisterContextData | undefined>(undefined);

export function MembersRegisterProvider({ children }: { children: React.ReactNode }) {
  const [state, dispatch] = useReducer(membersRegisterReducer, initialState);
  const params = useParams();
  
  const STORAGE_KEY = useMemo(() => {
    const patientId = params?.id as string;
    return patientId ? `apae_edit_cache_${patientId}` : "apae_register_cache";
  }, [params?.id]);

  const setters = {
    setPersonalData: useCallback((data: Partial<PersonalData>) => dispatch({ type: "SET_PERSONAL_DATA", payload: data }), []),
    setKinshipsData: useCallback((data: KinshipData[]) => dispatch({ type: "SET_KINSHIPS_DATA", payload: data }), []),
    setAddressData: useCallback((data: Partial<AddressData>) => dispatch({ type: "SET_ADDRESS_DATA", payload: data }), []),
    setAdditionalsData: useCallback((data: Partial<AdditionalsData>) => dispatch({ type: "SET_ADDITIONALS_DATA", payload: data }), []),
    setGuardianData: useCallback((data: Partial<GuardianData>) => dispatch({ type: "SET_GUARDIAN_DATA", payload: data }), []),
    setProfileData: useCallback((data: Partial<ProfileData>) => dispatch({ type: "SET_PROFILE_DATA", payload: data }), []),
    setStep: useCallback((step: MembersRegisterStep) => dispatch({ type: "SET_STEP", payload: step }), []),
    loadAllData: useCallback((apiData: MembersRegisterState) => {
      if (typeof window !== "undefined") {
        const saved = localStorage.getItem(STORAGE_KEY);
        if (saved) {
          try {
            const parsed = JSON.parse(saved);

            if (parsed.additionals?.disability?.report?.base64) {
              parsed.additionals.disability.report = base64ToFile(
                parsed.additionals.disability.report.base64,
                parsed.additionals.disability.report.name,
                parsed.additionals.disability.report.type
              );
            }
            if (parsed.additionals?.care?.referral?.base64) {
              parsed.additionals.care.referral = base64ToFile(
                parsed.additionals.care.referral.base64,
                parsed.additionals.care.referral.name,
                parsed.additionals.care.referral.type
              );
            }
            if (parsed.profile?.photo?.base64) {
              parsed.profile.photo = base64ToFile(
                parsed.profile.photo.base64,
                parsed.profile.photo.name,
                parsed.profile.photo.type
              );
            }

            dispatch({ 
              type: "LOAD_ALL_DATA", 
              payload: { ...apiData, ...parsed } 
            });
            return; 
          } catch (e) {
            console.error("Erro ao carregar rascunho na edição", e);
          }
        }
      }
      dispatch({ type: "LOAD_ALL_DATA", payload: apiData });
    }, [STORAGE_KEY]),
  };

  useEffect(() => {
  if (typeof window !== "undefined") {
    const saved = localStorage.getItem(STORAGE_KEY);
    if (saved) {
      try {
        const parsed = JSON.parse(saved);
        if (!params?.id && state.personal.name === "") {
           const draftWithFiles = reconstructFiles(parsed);
           dispatch({ type: "LOAD_ALL_DATA", payload: { ...state, ...draftWithFiles } });
        }
      } catch (e) { console.error("Erro no rascunho", e); }
    }
  }
}, [STORAGE_KEY, params?.id]);  

  const reconstructFiles = (obj: any) => {
    if (obj.additionals?.disability?.report?.base64) {
      obj.additionals.disability.report = base64ToFile(
        obj.additionals.disability.report.base64,
        obj.additionals.disability.report.name,
        obj.additionals.disability.report.type
      );
    }
    if (obj.additionals?.care?.referral?.base64) {
      obj.additionals.care.referral = base64ToFile(
        obj.additionals.care.referral.base64,
        obj.additionals.care.referral.name,
        obj.additionals.care.referral.type
      );
    }
    if (obj.profile?.photo?.base64) {
      obj.profile.photo = base64ToFile(
        obj.profile.photo.base64,
        obj.profile.photo.name,
        obj.profile.photo.type
      );
    }
    return obj;
  };

  useEffect(() => {
    if (typeof window === "undefined") return;

    const saveDraft = async () => {
      if (params?.id && state.personal.name === "") {
        return;
      }

      try {
        const draft = JSON.parse(JSON.stringify(state, (key, value) => value));

        if (state.additionals.disability.report instanceof File) {
          draft.additionals.disability.report = {
            base64: await fileToBase64(state.additionals.disability.report),
            name: state.additionals.disability.report.name,
            type: state.additionals.disability.report.type
          };
        }
        
        if (state.additionals.care.referral instanceof File) {
          draft.additionals.care.referral = {
            base64: await fileToBase64(state.additionals.care.referral),
            name: state.additionals.care.referral.name,
            type: state.additionals.care.referral.type
          };
        }
        
        if (state.profile.photo instanceof File) {
          draft.profile.photo = {
            base64: await fileToBase64(state.profile.photo),
            name: state.profile.photo.name,
            type: state.profile.photo.type
          };
        }

        localStorage.setItem(STORAGE_KEY, JSON.stringify(draft));
      } catch (error: any) {
        if (error.name === 'QuotaExceededError') {
          console.warn("Aviso: Limite do LocalStorage excedido.");
        }
      }
    };

    saveDraft();
  }, [state, STORAGE_KEY, params?.id]);
  
  const register = useCallback(
    async (id?: string) => {
      const { personal, address, additionals, guardian, kinships, profile } = state;
      const formatDate = (date: any) => (date && !isNaN(new Date(date).getTime())) ? new Date(date).toISOString().split("T")[0] : null;
      const parseIncome = (val: string) => { const clean = String(val).replace(/[^\d]/g, ""); return isNaN(parseFloat(clean)) ? 0 : parseFloat(clean) * 0.01; };

      const patient: any = {
        fullName: personal.name || "Não informado",
        nationality: personal.birth.place || "Brasileiro",
        birthDate: formatDate(personal.birth.date),
        contact: personal.phone || "Não informado",
        birthCertificateNumber: personal.birth.certificate || "0",
        registryOffice: "Cartorio", fls: "0", book: "0",
        rg: personal.rg.number || "0",
        issueDate: formatDate(personal.rg.issuing.date),
        issuingAgency: personal.rg.issuing.body || "SSP/SP",
        cpf: personal.cpf, cns: personal.cns || "000 0000 0000 0000", nis: personal.nis || "0",
        registrationDate: formatDate(new Date()),
        allergies: additionals.allergies || "Nenhuma",
        isStudent: profile.role === "student",
        address: { city: address.city || "Não informado", cep: address.cep || "00000-000", state: address.state || "Não informado", neighborhood: address.district || "Não informado", street: address.street.split(",")[0].trim() || "Não informado", number: address.street.split(",")[1]?.trim() || "S/N", complement: "" },
        guardian: { name: guardian.name || "Não informado", contact: guardian.contact || "Não informado", kinship: guardian.kinship || "Não informado", address: { city: guardian.address.city || "Não informado", cep: guardian.address.cep || "00000-000", state: guardian.address.state || "Não informado", neighborhood: guardian.address.district || "Não informado", street: guardian.address.street.split(",")[0].trim() || "Não informado", number: guardian.address.street.split(",")[1]?.trim() || "S/N", complement: "" } },
        parents: kinships.map((k) => ({ name: k.name || "Não informado", rg: k.rg || "0", cpf: k.cpf || "000.000.000-00", profession: k.occupation || "Não informado", isAlive: k.alive, kinship: k.type || "Pai/Mãe" })),
        vaccineNames: additionals.vaccines.map((v) => ({ name: v })),
      };

      if (id) {
        const res = await fetch(`/api/pessoas/${id}`, { method: "PUT", headers: { "Content-Type": "application/json" }, body: JSON.stringify(patient) });
        if (res.ok) { localStorage.removeItem(STORAGE_KEY); }

        if (profile.photo instanceof File && res.ok) {
          const photoFormData = new FormData(); photoFormData.append("photo", profile.photo);
          await fetch(`/api/pessoas/${id}/photo`, { method: "PUT", body: photoFormData });
        }
        const data = await res.json().catch(() => ({}));
        return { status: res.status, data };
      } else {
        patient.annualRegistry = { bpc: additionals.bpc, diseases: additionals.diseases, serviceArea: additionals.care.types.map((area: string) => ({ area })), familyIncome: parseIncome(additionals.householdIncome), year: new Date().getFullYear(), disorders: additionals.disability.types.map((name: string) => ({ name })) };
        const formData = new FormData();
        formData.append("patient", new Blob([JSON.stringify(patient)], { type: "application/json" }));
        if (profile.photo instanceof File) formData.append("photo", profile.photo);
        if (additionals.disability.report instanceof File) formData.append("reports", additionals.disability.report);
        if (additionals.care.referral instanceof File) formData.append("referrals", additionals.care.referral);

        const res = await fetch("/api/pessoas", { method: "POST", body: formData });
        if (res.ok) { localStorage.removeItem(STORAGE_KEY); }
        const data = await res.json().catch(() => ({}));
        return { status: res.status, data };
      }
    },
    [state, STORAGE_KEY]
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