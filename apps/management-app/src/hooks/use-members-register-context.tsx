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
  birth: {
    certificate: string;
    date: Date;
  };
}

interface AddressData {
  cep: string;
  district: string;
  street: string;
  state: string;
}

interface AdditionalsData {
  diseases: string;
  medications: string;
  vaccines: string;
  allergies: string;
  disability: {
    type: string;
    report: File | undefined;
  };
  care: {
    type: string;
    referral: File | undefined;
  };
}

interface GuardianData {
  rg: string;
  cpf: string;
  bpc: boolean;
  alive: boolean;
  name: string;
  occupation: string;
}

interface GuardiansData {
  father: GuardianData;
  mother: GuardianData;
  others?: string;
  householdIncome: string;
  emergencyContact: string;
}

interface ProfileData {
  photo: File | undefined;
  role: "student" | "patient";
}

enum MembersRegisterStep {
  PERSONAL = "personal",
  ADDRESS = "address",
  ADDITIONALS = "additionals",
  GUARDIANS = "guardians",
  PROFILE = "profile",
}

interface MembersRegisterState {
  personal: PersonalData;
  address: AddressData;
  additionals: AdditionalsData;
  guardians: GuardiansData;
  profile: ProfileData;

  step: MembersRegisterStep;
}

interface MembersRegisterContextData {
  state: MembersRegisterState;
  setters: {
    setPersonalData: (data: Partial<PersonalData>) => void;
    setAddressData: (data: Partial<AddressData>) => void;
    setAdditionalsData: (data: Partial<AdditionalsData>) => void;
    setGuardiansData: (data: Partial<GuardiansData>) => void;
    setProfileData: (data: Partial<ProfileData>) => void;
    setStep: (step: MembersRegisterStep) => void;
  };
  register: () => Promise<void>;
}

type MembersRegisterAction =
  | { type: "SET_PERSONAL_DATA"; payload: Partial<PersonalData> }
  | { type: "SET_ADDRESS_DATA"; payload: Partial<AddressData> }
  | { type: "SET_ADDITIONALS_DATA"; payload: Partial<AdditionalsData> }
  | { type: "SET_GUARDIANS_DATA"; payload: Partial<GuardiansData> }
  | { type: "SET_PROFILE_DATA"; payload: Partial<ProfileData> }
  | { type: "SET_STEP"; payload: MembersRegisterStep };

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

    case "SET_ADDRESS_DATA":
      return {
        ...state,
        address: { ...state.address, ...action.payload },
      };

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

    case "SET_GUARDIANS_DATA":
      return {
        ...state,
        guardians: { ...state.guardians, ...action.payload },
      };

    case "SET_PROFILE_DATA":
      return {
        ...state,
        profile: { ...state.profile, ...action.payload },
      };

    case "SET_STEP":
      return {
        ...state,
        step: action.payload,
      };

    default:
      return state;
  }
}

const initialState: MembersRegisterState = {
  personal: {
    name: "",
    cpf: "",
    phone: "",
    rg: {
      number: "",
      issuing: {
        body: "",
        date: new Date(),
      },
    },
    birth: {
      certificate: "",
      date: new Date(),
    },
  },
  address: {
    cep: "",
    district: "",
    street: "",
    state: "",
  },
  additionals: {
    diseases: "",
    medications: "",
    vaccines: "",
    allergies: "",
    disability: {
      type: "",
      report: undefined,
    },
    care: {
      type: "",
      referral: undefined,
    },
  },
  guardians: {
    father: {
      rg: "",
      cpf: "",
      bpc: false,
      alive: false,
      name: "",
      occupation: "",
    },
    mother: {
      rg: "",
      cpf: "",
      bpc: false,
      alive: false,
      name: "",
      occupation: "",
    },
    others: "",
    householdIncome: "",
    emergencyContact: "",
  },
  step: MembersRegisterStep.PERSONAL,
  profile: {
    role: "patient",
    photo: undefined,
  },
};

const MembersRegisterContext = createContext<
  MembersRegisterContextData | undefined
>(undefined);

function MembersRegisterProvider({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const [state, dispatch] = useReducer(membersRegisterReducer, initialState);

  const setters = {
    setPersonalData: useCallback((data: Partial<PersonalData>) => {
      dispatch({ type: "SET_PERSONAL_DATA", payload: data });
    }, []),
    setAddressData: useCallback((data: Partial<AddressData>) => {
      dispatch({ type: "SET_ADDRESS_DATA", payload: data });
    }, []),
    setAdditionalsData: useCallback((data: Partial<AdditionalsData>) => {
      dispatch({ type: "SET_ADDITIONALS_DATA", payload: data });
    }, []),
    setGuardiansData: useCallback((data: Partial<GuardiansData>) => {
      dispatch({ type: "SET_GUARDIANS_DATA", payload: data });
    }, []),
    setProfileData: useCallback((data: Partial<ProfileData>) => {
      dispatch({ type: "SET_PROFILE_DATA", payload: data });
    }, []),
    setStep: useCallback((step: MembersRegisterStep) => {
      dispatch({ type: "SET_STEP", payload: step });
    }, []),
  };

  const register = useCallback(async () => {
    const { personal, address, additionals, guardians } = state;

    const data = {
      nomeCompleto: personal.name,
      dataNascimento: personal.birth.date.toISOString().split("T")[0],
      numRegistroNasc: personal.birth.certificate,
      fls: "",
      livro: "",
      cartorio: "",
      cpf: personal.cpf,
      rg: personal.rg.number,
      dataEmissaoRg: personal.rg.issuing.date.toISOString().split("T")[0],
      orgaoEmissorRg: personal.rg.issuing.body,
      cns: "",
      nis: "",
      dataCadastramento: new Date().toISOString().split("T")[0],
      contatoRequest: [
        {
          enderecoAtivo: "S",
          comprovanteResidencia: "",
          endereco: address.street,
          bairro: address.district,
          cidade: "",
          estado: address.state,
          cep: address.cep,
          naturalidade: "",
          telefone: personal.phone,
        },
      ],
      vacinacoesRequests: additionals.vaccines.split(",").map((vaccine) => ({
        nome: vaccine,
      })),

      deficienciasRequests: [
        {
          descricao: additionals.disability.type,
        },
      ],
      atendimentosRequests: [
        {
          descricao: additionals.care.type,
        },
      ],
      cadastrosAnuaisRequests: [
        {
          beneficioDePrestacaoContinuada:
            guardians.father.bpc || guardians.mother.bpc,
          historicosAlergias: additionals.allergies,
          medicacoesContinuas: additionals.medications,
          historicoDoencas: additionals.diseases,
          rendaFamiliar: Number(guardians.householdIncome) * 0.01,
        },
      ],
      responsaveisRequests: [
        {
          nome: guardians.mother.name,
          ondeProcurar: "",
          vivo: guardians.mother.alive,
          profissao: guardians.mother.occupation,
          rg: guardians.mother.rg,
          cpf: guardians.mother.cpf,
          emergencia: guardians.emergencyContact,
          tipoResponsavel: "MAE",
        },
        {
          nome: guardians.father.name,
          ondeProcurar: "",
          vivo: guardians.father.alive,
          profissao: guardians.father.occupation,
          rg: guardians.father.rg,
          cpf: guardians.father.cpf,
          emergencia: guardians.emergencyContact,
          tipoResponsavel: "PAI",
        },
      ],
    };

    await fetch("/api/pessoas", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });
  }, []);

  return (
    <MembersRegisterContext.Provider
      value={{
        state,
        setters,
        register,
      }}
    >
      {children}
    </MembersRegisterContext.Provider>
  );
}

function useMembersRegisterContext() {
  const context = useContext(MembersRegisterContext);
  if (!context) {
    throw new Error(
      "useMembersRegisterContext must be used within a MembersRegisterProvider",
    );
  }
  return context;
}

export {
  MembersRegisterStep,
  MembersRegisterProvider,
  useMembersRegisterContext,
};
