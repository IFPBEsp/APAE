"use client";

import {
  createContext,
  useCallback,
  useContext,
  useReducer,
  useEffect,
  useMemo,
  useRef,
} from "react";
import { useParams } from "next/navigation";
import { parseCivilDate } from "@/lib/date";

import {
  MembersRegisterStep,
  MembersRegisterState,
  MembersRegisterContextData,
  PersonalData,
  KinshipData,
  AddressData,
  AdditionalsData,
  GuardianData,
  ProfileData,
} from "./register-context/types";

import { membersRegisterReducer, initialState } from "./register-context/reducer";
import { useRegisterSubmission } from "./register-context/useRegisterSubmission";

export { MembersRegisterStep };
export type { MembersRegisterState };

// --- UTILS: FILE CONVERSION ---
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

type DraftAttachment = {
  base64: string;
  name: string;
  type: string;
};

type DraftCache = {
  additionals?: {
    disability?: { report?: DraftAttachment | File };
    care?: { referral?: DraftAttachment | File };
  };
  profile?: { photo?: DraftAttachment | File };
  personal?: {
    rg?: { issuing?: { date?: string | Date } };
    birth?: { date?: string | Date };
  };
};

function isDraftAttachment(value: unknown): value is DraftAttachment {
  return (
    typeof value === "object" &&
    value !== null &&
    "base64" in value &&
    typeof (value as { base64?: unknown }).base64 === "string" &&
    "name" in value &&
    typeof (value as { name?: unknown }).name === "string" &&
    "type" in value &&
    typeof (value as { type?: unknown }).type === "string"
  );
}

const MembersRegisterContext = createContext<MembersRegisterContextData | undefined>(undefined);

export function MembersRegisterProvider({ children }: { children: React.ReactNode }) {
  const [state, dispatch] = useReducer(membersRegisterReducer, initialState);
  const params = useParams();
  const hasHydrated = useRef(false);

  const STORAGE_KEY = useMemo(() => {
    const patientId = params?.id as string;
    return patientId ? `apae_edit_cache_${patientId}` : "apae_register_cache";
  }, [params?.id]);

  const register = useRegisterSubmission(state, STORAGE_KEY);

  const reconstructFiles = useCallback((obj: DraftCache) => {
    if (isDraftAttachment(obj.additionals?.disability?.report)) {
      obj.additionals.disability.report = base64ToFile(
        obj.additionals.disability.report.base64,
        obj.additionals.disability.report.name,
        obj.additionals.disability.report.type,
      );
    }
    if (isDraftAttachment(obj.additionals?.care?.referral)) {
      obj.additionals.care.referral = base64ToFile(
        obj.additionals.care.referral.base64,
        obj.additionals.care.referral.name,
        obj.additionals.care.referral.type,
      );
    }
    if (isDraftAttachment(obj.profile?.photo)) {
      obj.profile.photo = base64ToFile(
        obj.profile.photo.base64,
        obj.profile.photo.name,
        obj.profile.photo.type,
      );
    }
    if (obj.personal?.rg?.issuing?.date) {
      obj.personal.rg.issuing.date = parseCivilDate(obj.personal.rg.issuing.date) || new Date();
    }
    if (obj.personal?.birth?.date) {
      obj.personal.birth.date = parseCivilDate(obj.personal.birth.date) || new Date();
    }
    return obj;
  }, []);

  const setters = {
    setPersonalData: useCallback(
      (data: Partial<PersonalData>) => dispatch({ type: "SET_PERSONAL_DATA", payload: data }),
      [],
    ),
    setKinshipsData: useCallback(
      (data: KinshipData[]) => dispatch({ type: "SET_KINSHIPS_DATA", payload: data }),
      [],
    ),
    setAddressData: useCallback(
      (data: Partial<AddressData>) => dispatch({ type: "SET_ADDRESS_DATA", payload: data }),
      [],
    ),
    setAdditionalsData: useCallback(
      (data: Partial<AdditionalsData>) => dispatch({ type: "SET_ADDITIONALS_DATA", payload: data }),
      [],
    ),
    setGuardianData: useCallback(
      (data: Partial<GuardianData>) => dispatch({ type: "SET_GUARDIAN_DATA", payload: data }),
      [],
    ),
    setProfileData: useCallback(
      (data: Partial<ProfileData>) => dispatch({ type: "SET_PROFILE_DATA", payload: data }),
      [],
    ),
    setStep: useCallback(
      (step: MembersRegisterStep) => dispatch({ type: "SET_STEP", payload: step }),
      [],
    ),
    loadAllData: useCallback((apiData: MembersRegisterState) => {
      dispatch({ type: "LOAD_ALL_DATA", payload: apiData });
    }, []),
  };

  useEffect(() => {
    if (hasHydrated.current) return;
    if (typeof window === "undefined") return;

    const saved = localStorage.getItem(STORAGE_KEY);
    if (saved) {
      try {
        const parsed = JSON.parse(saved);
        const draftWithFiles = reconstructFiles(parsed as DraftCache);
        dispatch({
          type: "LOAD_ALL_DATA",
          payload: draftWithFiles as MembersRegisterState,
        });
        hasHydrated.current = true;
        return;
      } catch (e) {
        console.error("Erro ao carregar rascunho:", e);
      }
    }

    hasHydrated.current = true;
  }, [STORAGE_KEY, reconstructFiles]);

  useEffect(() => {
    if (typeof window === "undefined") return;
    if (!hasHydrated.current) return;

    const saveDraft = async () => {
      if (params?.id && state.personal.name === "") return;

      try {
        const draft = JSON.parse(JSON.stringify(state));

        if (state.additionals.disability.report instanceof File) {
          draft.additionals.disability.report = {
            base64: await fileToBase64(state.additionals.disability.report),
            name: state.additionals.disability.report.name,
            type: state.additionals.disability.report.type,
          };
        }
        if (state.additionals.care.referral instanceof File) {
          draft.additionals.care.referral = {
            base64: await fileToBase64(state.additionals.care.referral),
            name: state.additionals.care.referral.name,
            type: state.additionals.care.referral.type,
          };
        }
        if (state.profile.photo instanceof File) {
          draft.profile.photo = {
            base64: await fileToBase64(state.profile.photo),
            name: state.profile.photo.name,
            type: state.profile.photo.type,
          };
        }

        localStorage.setItem(STORAGE_KEY, JSON.stringify(draft));
      } catch (error: unknown) {
        if (error instanceof DOMException && error.name === "QuotaExceededError") {
          console.warn("Aviso: Limite do LocalStorage excedido.");
        } else {
          console.error("Erro ao salvar rascunho:", error);
        }
      }
    };

    const timer = setTimeout(saveDraft, 1000);
    return () => clearTimeout(timer);
  }, [state, STORAGE_KEY, params?.id]);

  return (
    <MembersRegisterContext.Provider value={{ state, setters, register }}>
      {children}
    </MembersRegisterContext.Provider>
  );
}

export function useMembersRegisterContext() {
  const context = useContext(MembersRegisterContext);
  if (!context)
    throw new Error("useMembersRegisterContext must be used within a MembersRegisterProvider");
  return context;
}
