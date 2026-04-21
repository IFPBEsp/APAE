"use client";

import {
  MembersRegisterProvider,
  useMembersRegisterContext,
  MembersRegisterState,
  MembersRegisterStep,
} from "@/hooks/use-members-register-context";
import { VaccinesProvider } from "@/hooks/use-vaccines";
import { DisordersProvider } from "@/hooks/use-disorders";
import { useParams, usePathname } from "next/navigation";
import { useEffect, useState } from "react";
import { Loader2, CheckCircle2, Circle } from "lucide-react";
import Image from "@/assets/background_image.jpg";
import { SidebarSteps } from "@/components/shared/SidebarSteps";

interface AddressData {
  street?: string;
  number?: string;
}

interface VaccineName {
  name: string;
}

interface ParentData {
  name?: string;
  cpf?: string;
  rg?: string;
  profession?: string;
  isAlive?: boolean;
  kinship?: string;
}

function EditPatientDataLoader({ children }: { children: React.ReactNode }) {
  const { setters, state } = useMembersRegisterContext();
  const { id } = useParams();
  const pathname = usePathname();
  const [loaded, setLoaded] = useState(false);

  useEffect(() => {
    if (loaded || state.personal.name !== "") {
      setLoaded(true);
      return;
    }

    async function load() {
      try {
        const res = await fetch(`/api/pessoas/${id}`, { cache: "no-store" });
        if (!res.ok) throw new Error("Erro ao buscar dados do paciente");
        const data = await res.json();

        const formatStreet = (addr: AddressData | null | undefined) => {
          if (!addr?.street) return "";
          const street = addr.street.trim();
          const number = addr.number ? addr.number.trim() : "";

          if (street.includes(",")) return street;
          return number ? `${street}, ${number}` : street;
        };

        const mappedData: MembersRegisterState = {
          personal: {
            name: data.fullName || "",
            cpf: data.cpf || "",
            phone: data.contact || "",
            rg: {
              number: data.rg || "",
              issuing: {
                body: data.issuingAgency || "",
                date: data.issueDate ? new Date(data.issueDate) : new Date(),
              },
            },
            cns: data.cns || "",
            nis: data.nis || "",
            birth: {
              certificate: data.birthCertificateNumber || "",
              date: data.birthDate ? new Date(data.birthDate) : new Date(),
              place: data.birthplace || data.nationality || "",
            },
          },
          address: {
            cep: data.address?.cep || "",
            state: data.address?.state || "",
            city: data.address?.city || "",
            district: data.address?.neighborhood || "",
            street: formatStreet(data.address),
          },
          additionals: {
            diseases: data.annualRegistry?.diseases || "",
            medications: data.annualRegistry?.continuousMedication || "",
            vaccines: data.vaccineNames?.map((v: VaccineName) => v.name) || [],
            allergies: data.allergies || "",
            disability: { types: [], report: undefined },
            care: { types: [], referral: undefined },
            bpc: data.annualRegistry?.bpc ?? false,
            householdIncome: data.annualRegistry?.familyIncome
              ? (data.annualRegistry.familyIncome * 100).toString()
              : "0",
          },
          guardian: {
            name: data.guardian?.name || "",
            contact: data.guardian?.contact || "",
            kinship: data.guardian?.kinship || "",
            address: {
              cep: data.guardian?.address?.cep || "",
              state: data.guardian?.address?.state || "",
              city: data.guardian?.address?.city || "",
              district: data.guardian?.address?.neighborhood || "",
              street: formatStreet(data.guardian?.address),
            },
          },
          kinships:
            data.parents?.map((p: ParentData) => ({
              name: p.name || "",
              cpf: p.cpf || "",
              rg: p.rg || "",
              occupation: p.profession || "",
              alive: p.isAlive ?? true,
              type: p.kinship || "",
              isLegalGuardian: p.name === data.guardian?.name && p.kinship === data.guardian?.kinship
            })) || [],
          profile: {
            role: data.isStudent ? "student" : "patient",
            photo: undefined,
          },
          step: pathname.split("/").pop() as MembersRegisterStep,
        const mappedData: any = {
           personal: {
             name: data.fullName || "",
             cpf: data.cpf || "",
             phone: data.contact || "",
             rg: {
               number: data.rg || "",
               issuing: { body: data.issuingAgency || "", date: data.issueDate ? new Date(data.issueDate) : new Date() }
             },
             cns: data.cns || "",
             nis: data.nis || "",
             birth: { certificate: data.birthCertificateNumber || "", date: data.birthDate ? new Date(data.birthDate) : new Date(), place: data.birthplace || data.nationality || "" }
           },
           address: {
             cep: data.address?.cep || "",
             state: data.address?.state || "",
             city: data.address?.city || "",
             neighborhood: data.address?.neighborhood || "",
             street: data.address?.street || "",
              number: data.address?.number || "",
              noNumber: data.address?.number === "SN" || false,
              complement: data.address?.complement || ""
           },
           additionals: {
             diseases: data.annualRegistry?.diseases || "",
             medications: data.annualRegistry?.continuousMedication || "",
             vaccines: data.vaccineNames?.map((v: any) => v.name) || [],
             allergies: data.allergies || "",
             disability: { types: [], report: undefined },
             care: { types: [], referral: undefined },
             bpc: data.annualRegistry?.bpc ?? false,
             householdIncome: data.annualRegistry?.familyIncome ? (data.annualRegistry.familyIncome * 100).toString() : "0"
           },
           guardian: {
             name: data.guardian?.name || "",
             contact: data.guardian?.contact || "",
             kinship: data.guardian?.kinship || "",
             address: {
               cep: data.guardian?.address?.cep || "",
               state: data.guardian?.address?.state || "",
               city: data.guardian?.address?.city || "",
               neighborhood: data.guardian?.address?.neighborhood || "",
               street: data.guardian?.address?.street || "",
               number: data.guardian?.address?.number || "",
               noNumber: data.guardian?.address?.number === "SN" || false,
               complement: data.guardian?.address?.complement || ""
             }
           },
           kinships: data.parents?.map((p: any) => ({
             name: p.name || "",
             cpf: p.cpf || "",
             rg: p.rg || "",
             occupation: p.profession || "",
             alive: p.isAlive ?? true,
             type: p.kinship || "",
             isLegalGuardian: p.name === data.guardian?.name && p.kinship === data.guardian?.kinship
           })) || [],
           profile: {
             role: data.isStudent ? "student" : "patient",
             photo: undefined
           },
           step: pathname.split('/').pop() as any
        };

        setters.loadAllData(mappedData);
        setLoaded(true);
      } catch (e) {
        console.error("Erro no carregamento do Layout:", e);
      }
    }
    load();
  }, [id, setters, loaded, state.personal.name, pathname]);

  if (!loaded) {
    return (
      <div className="flex h-screen items-center justify-center flex-col gap-4">
        <Loader2 className="animate-spin text-[#0D4F97] h-10 w-10" />
        <p className="text-gray-500 font-medium">
          Carregando dados para edição...
        </p>
      </div>
    );
  }

  return <>{children}</>;
}

function MobileStepIndicator() {
  const { state: { step } } = useMembersRegisterContext();

  const stepsList = [
    { id: MembersRegisterStep.PERSONAL, label: "Dados Pessoais", shortLabel: "Pessoal" },
    { id: MembersRegisterStep.KINSHIPS, label: "Parentesco", shortLabel: "Parentes" },
    { id: MembersRegisterStep.ADDRESS, label: "Endereço", shortLabel: "Endereço" },
    { id: MembersRegisterStep.ADDITIONALS, label: "Saúde & Social", shortLabel: "Saúde" },
    { id: MembersRegisterStep.GUARDIAN, label: "Responsável", shortLabel: "Resp." },
    { id: MembersRegisterStep.PROFILE, label: "Perfil & Documentos", shortLabel: "Perfil" },
  ];

  const currentStepIndex = stepsList.findIndex((s) => s.id === step);

  return (
    <div className="xl:hidden bg-[#0D4F97] px-4 py-3 rounded-t-lg">
      <div className="flex items-center justify-between overflow-x-auto gap-2">
        {stepsList.map((s, index) => {
          const isCompleted = index < currentStepIndex;
          const isCurrent = index === currentStepIndex;

          return (
            <div key={s.id} className="flex flex-col items-center gap-1 flex-shrink-0">
              {isCompleted ? (
                <CheckCircle2 className="w-5 h-5 text-green-400" />
              ) : isCurrent ? (
                <div className="w-5 h-5 rounded-full bg-white flex items-center justify-center text-[#0D4F97] text-xs font-bold">{index + 1}</div>
              ) : (
                <Circle className="w-5 h-5 text-blue-300" />
              )}
              <span className={`text-[10px] whitespace-nowrap ${isCurrent ? "text-white font-semibold" : "text-blue-200"}`}>
                {s.shortLabel}
              </span>
            </div>
          );
        })}
      </div>
      <div className="mt-2 text-center">
        <p className="text-sm text-white font-semibold">
          {stepsList[currentStepIndex]?.label}
        </p>
      </div>
    </div>
  );
}

export default function EditPatientLayout({ children }: { children: React.ReactNode }) {
  return (
  <VaccinesProvider>
    {/* @ts-ignore */}
    <DisordersProvider>
      <MembersRegisterProvider>
        <EditPatientDataLoader>

          <div className="h-screen rounded-lg mx-2 sm:mx-4 md:mx-10 relative grid grid-cols-1 xl:grid-cols-[1fr_2fr] antialiased overflow-hidden">

            <div
              className="absolute inset-0 bg-cover bg-center bg-no-repeat grayscale-90"
              style={{
                backgroundImage: `url(${Image.src})`,
              }}
            />

            <div
              className="absolute inset-0"
              style={{
                background:
                  "linear-gradient(180deg, rgba(13, 79, 151, 0.2) 54.32%, rgba(255, 255, 255, 0.6) 110.28%)",
              }}
            />

            <div className="hidden xl:flex relative z-10 w-full h-full bg-[#0D4F97]/50">
              <SidebarSteps />
            </div>

            <div className="relative flex flex-col w-full h-full bg-muted overflow-y-auto">
              <MobileStepIndicator />
              <div className="flex-1 p-4 sm:p-6 md:p-8 overflow-y-auto">
                <h1 className="hidden xl:block text-2xl font-bold text-blue-900 mb-4">
                  Edição de paciente
                </h1>
                {children}
              </div>
            </div>

          </div>

        </EditPatientDataLoader>
      </MembersRegisterProvider>
    </DisordersProvider>
  </VaccinesProvider>
);
}