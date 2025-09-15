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
  bpc: boolean;
}

interface GuardianData {
  rg: string;
  cpf: string;
  alive: boolean;
  name: string;
  occupation: string;
  emergencyContact: string;
  whereToFind: string;
}

interface GuardiansData {
  father: GuardianData;
  mother: GuardianData;
  others?: string;
  householdIncome: string;
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
    cns: "",
    nis: "",
    birth: {
      certificate: "",
      date: new Date(),
      place: "",
    },
  },
  address: {
    cep: "",
    state: "",
    city: "",
    district: "",
    street: "",
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
    bpc: false,
  },
  guardians: {
    father: {
      rg: "",
      cpf: "",
      alive: false,
      name: "",
      occupation: "",
      emergencyContact: "",
      whereToFind: "",
    },
    mother: {
      rg: "",
      cpf: "",
      alive: false,
      name: "",
      occupation: "",
      emergencyContact: "",
      whereToFind: "",
    },
    others: "",
    householdIncome: "",
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

    const parseBirthCertificate = (certificate: string) => {
      const [
        cartorio,
        acervo,
        servicoRegistroCivil,
        ano,
        tipo,
        livro,
        folha,
        termo,
        digitoVerificador,
      ] = certificate.split(" ");

      return {
        cartorio,
        acervo,
        servicoRegistroCivil,
        ano,
        tipo,
        livro,
        folha,
        termo,
        digitoVerificador,
      };
    };

    const {
      cartorio,
      livro,
      folha: fls,
    } = parseBirthCertificate(personal.birth.certificate);

    const data = {
      nomeCompleto: personal.name,
      dataNascimento: personal.birth.date.toISOString().split("T")[0],
      numRegistroNasc: personal.birth.certificate,
      fls,
      livro,
      cartorio,
      cpf: personal.cpf,
      rg: personal.rg.number,
      dataEmissaoRg: personal.rg.issuing.date.toISOString().split("T")[0],
      orgaoEmissorRg: personal.rg.issuing.body,
      cns: personal.cns,
      nis: personal.nis,
      dataCadastramento: new Date().toISOString().split("T")[0],
      contatoRequest: [
        {
          enderecoAtivo: "S",
          comprovanteResidencia: undefined,
          endereco: address.street,
          bairro: address.district,
          cidade: address.city,
          estado: address.state,
          cep: address.cep,
          naturalidade: personal.birth.place,
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
          beneficioDePrestacaoContinuada: additionals.bpc,
          historicosAlergias: additionals.allergies,
          medicacoesContinuas: additionals.medications,
          historicoDoencas: additionals.diseases,
          rendaFamiliar: Number(guardians.householdIncome) * 0.01,
        },
      ],
      responsaveisRequests: [
        {
          nome: guardians.mother.name,
          ondeProcurar: guardians.mother.whereToFind,
          vivo: guardians.mother.alive,
          profissao: guardians.mother.occupation,
          rg: guardians.mother.rg,
          cpf: guardians.mother.cpf,
          emergencia: guardians.mother.emergencyContact,
          tipoResponsavel: "MAE",
        },
        {
          nome: guardians.father.name,
          ondeProcurar: guardians.father.whereToFind,
          vivo: guardians.father.alive,
          profissao: guardians.father.occupation,
          rg: guardians.father.rg,
          cpf: guardians.father.cpf,
          emergencia: guardians.father.emergencyContact,
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
