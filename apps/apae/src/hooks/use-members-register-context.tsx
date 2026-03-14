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

enum MembersRegisterStep {
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
  };
  register: () => Promise<Response>;
}

type MembersRegisterAction =
  | { type: "SET_PERSONAL_DATA"; payload: Partial<PersonalData> }
  | { type: "SET_KINSHIPS_DATA"; payload: KinshipData[] }
  | { type: "SET_ADDRESS_DATA"; payload: Partial<AddressData> }
  | { type: "SET_ADDITIONALS_DATA"; payload: Partial<AdditionalsData> }
  | { type: "SET_GUARDIAN_DATA"; payload: Partial<GuardianData> }
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

    case "SET_KINSHIPS_DATA":
      return {
        ...state,
        kinships: action.payload,
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
    vaccines: [],
    allergies: "",
    disability: {
      types: [],
      report: undefined,
    },
    care: {
      types: [],
      referral: undefined,
    },
    bpc: false,
    householdIncome: "",
  },
  guardian: {
    address: {
      cep: "",
      state: "",
      city: "",
      district: "",
      street: "",
    },
    contact: "",
    kinship: "",
    name: "",
  },
  kinships: [],
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
    setKinshipsData: useCallback((data: KinshipData[]) => {
      dispatch({ type: "SET_KINSHIPS_DATA", payload: data });
    }, []),
    setAddressData: useCallback((data: Partial<AddressData>) => {
      dispatch({ type: "SET_ADDRESS_DATA", payload: data });
    }, []),
    setAdditionalsData: useCallback((data: Partial<AdditionalsData>) => {
      dispatch({ type: "SET_ADDITIONALS_DATA", payload: data });
    }, []),
    setGuardianData: useCallback((data: Partial<GuardianData>) => {
      dispatch({ type: "SET_GUARDIAN_DATA", payload: data });
    }, []),
    setProfileData: useCallback((data: Partial<ProfileData>) => {
      dispatch({ type: "SET_PROFILE_DATA", payload: data });
    }, []),
    setStep: useCallback((step: MembersRegisterStep) => {
      dispatch({ type: "SET_STEP", payload: step });
    }, []),
  };

  const register = useCallback(async () => {
    const { personal, address, additionals, guardian, kinships, profile } =
      state;

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

    const { cartorio, livro, folha } = parseBirthCertificate(
      personal.birth.certificate,
    );

    const data = new Date();

    const ano = data.getFullYear();
    const mes = String(data.getMonth() + 1).padStart(2, "0");
    const dia = String(data.getDate()).padStart(2, "0");

    const hoje = `${ano}-${mes}-${dia}`;

    const patient = {
      fullName: personal.name,
      nationality: personal.birth.place,
      birthDate: personal.birth.date.toISOString().split("T")[0],
      contact: personal.phone,
      birthCertificateNumber: personal.birth.certificate,
      registryOffice: cartorio,
      fls: folha,
      book: livro,
      rg: personal.rg.number,
      issueDate: personal.rg.issuing.date.toISOString().split("T")[0],
      issuingAgency: personal.rg.issuing.body,
      cpf: personal.cpf,
      cns: personal.cns,
      nis: personal.nis,
      registrationDate: hoje,
      allergies: additionals.allergies,
      isStudent: profile.role === "student",
      address: {
        city: address.city,
        cep: address.cep,
        state: address.state,
        neighborhood: address.district,
        street: address.street.replaceAll(/, \d+/g, ""),
        number: address.street.replaceAll(/\D/g, ""),
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
          street: guardian.address.street,
          number: guardian.address.street.replaceAll(/\D/g, ""),
          complement: "",
        },
      },
      parents: kinships.map((kinship) => ({
        name: kinship.name,
        rg: kinship.rg,
        cpf: kinship.cpf,
        profession: kinship.occupation,
        isAlive: kinship.alive,
        kinship: kinship.type,
      })),
      vaccineNames: additionals.vaccines.map((vac) => ({ name: vac })),
      annualRegistry: {
        bpc: additionals.bpc,
        diseases: additionals.diseases,
        serviceArea: additionals.care.types.map((types) => ({ area: types })),
        familyIncome:
          Number(additionals.householdIncome.replaceAll(/\D/g, "")) * 0.01,
        year: new Date().getFullYear(),
        disorders: additionals.disability.types.map((dis) => ({ name: dis })),
      },
    };

    console.log(additionals.vaccines);

    const formData = new FormData();
    formData.append(
      "patient",
      new Blob([JSON.stringify(patient)], {
        type: "application/json",
      }),
    );

    formData.append("photo", profile.photo!);
    formData.append("reports", additionals.disability.report!);
    formData.append("referrals", additionals.care.referral!);

    return fetch("/api/pessoas", {
      method: "POST",
      body: formData,
    });
  }, [state]);

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
