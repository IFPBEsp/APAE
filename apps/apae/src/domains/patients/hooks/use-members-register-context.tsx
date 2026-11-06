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
import { getSessionId, readSessionId } from "@/domains/auth/session-id";
import {
  saveDraft,
  loadDraft,
  purgeForeignDrafts,
  registerDraftKey,
  editDraftKey,
} from "@/domains/patients/utils/draft-storage";

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

type DraftCache = {
  personal?: {
    rg?: { issuing?: { date?: string | Date } };
    birth?: { date?: string | Date };
  };
};

function stripFiles(state: MembersRegisterState): MembersRegisterState {
  const draft = JSON.parse(JSON.stringify(state)) as MembersRegisterState;

  draft.additionals.disability.report =
    initialState.additionals.disability.report;
  draft.additionals.care.referral = initialState.additionals.care.referral;
  draft.profile.photo = initialState.profile.photo;

  return draft;
}

function reviveDates(obj: DraftCache) {
  if (obj.personal?.rg?.issuing?.date) {
    obj.personal.rg.issuing.date =
      parseCivilDate(obj.personal.rg.issuing.date) || new Date();
  }
  if (obj.personal?.birth?.date) {
    obj.personal.birth.date =
      parseCivilDate(obj.personal.birth.date) || new Date();
  }
  return obj;
}

const MembersRegisterContext = createContext<
  MembersRegisterContextData | undefined
>(undefined);

export function MembersRegisterProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [state, dispatch] = useReducer(membersRegisterReducer, initialState);
  const params = useParams();
  const hasHydrated = useRef(false);

  const STORAGE_KEY = useMemo(() => {
    const patientId = params?.id as string;
    return patientId ? editDraftKey(patientId) : registerDraftKey();
  }, [params?.id]);

  const register = useRegisterSubmission(state, STORAGE_KEY);

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
    loadAllData: useCallback((apiData: MembersRegisterState) => {
      dispatch({ type: "LOAD_ALL_DATA", payload: apiData });
    }, []),
  };

  useEffect(() => {
    if (hasHydrated.current) return;
    if (typeof window === "undefined") return;

    const ownerId = getSessionId();

    purgeForeignDrafts(ownerId);

    const draft = loadDraft<DraftCache>(STORAGE_KEY, ownerId);
    if (draft) {
      dispatch({
        type: "LOAD_ALL_DATA",
        payload: reviveDates(draft) as MembersRegisterState,
      });
    }

    hasHydrated.current = true;
  }, [STORAGE_KEY]);

  useEffect(() => {
    if (typeof window === "undefined") return;
    if (!hasHydrated.current) return;
    if (params?.id && state.personal.name === "") return;

    const timer = setTimeout(() => {
      const ownerId = readSessionId();
      if (!ownerId) return;
      saveDraft(STORAGE_KEY, ownerId, stripFiles(state));
    }, 1000);

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
    throw new Error(
      "useMembersRegisterContext must be used within a MembersRegisterProvider",
    );
  return context;
}
