"use client";

import { MembersRegisterProvider, useMembersRegisterContext, MembersRegisterStep } from "@/hooks/use-members-register-context";
import { VaccinesProvider } from "@/hooks/use-vaccines";
import { DisordersProvider } from "@/hooks/use-disorders";
import { useParams, useRouter, usePathname } from "next/navigation";
import { useEffect, useState } from "react";
import { Loader2 } from "lucide-react";
import { Address, Guardian, KinshipData, Parent } from "@/types/patient";

interface ApiAddress {
  street?: string;
  number?: string;
  neighborhood?: string;
  city?: string;
  state?: string;
  cep?: string;
}

interface ApiGuardian {
  name?: string;
  contact?: string;
  kinship?: string;
  address?: ApiAddress;
}

interface ApiParent {
  name?: string;
  cpf?: string;
  rg?: string;
  profession?: string;
  isAlive?: boolean;
  kinship?: string;
}

interface ApiVaccine {
  name?: string;
}

interface ApiAnnualRegistry {
  diseases?: string;
  continuousMedication?: string;
  bpc?: boolean;
  familyIncome?: number;
}

interface ApiPatientData {
  fullName?: string;
  cpf?: string;
  contact?: string;
  rg?: string;
  issuingAgency?: string;
  issueDate?: string;
  cns?: string;
  nis?: string;
  birthCertificateNumber?: string;
  birthDate?: string;
  birthplace?: string;
  nationality?: string;
  isStudent?: boolean;
  allergies?: string;
  address?: ApiAddress;
  guardian?: ApiGuardian;
  parents?: ApiParent[];
  vaccineNames?: ApiVaccine[];
  annualRegistry?: ApiAnnualRegistry;
}

function formatStreet(addr?: ApiAddress): string {
  if (!addr?.street) return "";
  const street = addr.street.trim();
  const number = addr.number ? addr.number.trim() : "";

  if (street.includes(",")) return street;
  return number ? `${street}, ${number}` : street;
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
        const res = await fetch(`/api/pessoas/${id}`, { cache: 'no-store' });
        if (!res.ok) throw new Error("Erro ao buscar dados do paciente");
        const data: ApiPatientData = await res.json();

        const mappedData = {
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
             district: data.address?.neighborhood || "",
             street: formatStreet(data.address)
           },
           additionals: {
             diseases: data.annualRegistry?.diseases || "",
             medications: data.annualRegistry?.continuousMedication || "",
             vaccines: data.vaccineNames?.map((v: ApiVaccine) => v.name || "") || [],
             allergies: data.allergies || "",
             disability: { types: [] as string[], report: undefined as File | undefined },
             care: { types: [] as string[], referral: undefined as File | undefined },
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
               district: data.guardian?.address?.neighborhood || "",
               street: formatStreet(data.guardian?.address)
             }
           },
           kinships: data.parents?.map((p: ApiParent) => ({
             name: p.name || "",
             cpf: p.cpf || "",
             rg: p.rg || "",
             occupation: p.profession || "",
             alive: p.isAlive ?? true,
             type: p.kinship || ""
           })) || [],
           profile: {
             role: data.isStudent ? "student" : "patient" as "student" | "patient",
             photo: undefined as File | undefined
           },
           step: pathname.split('/').pop() as MembersRegisterStep
        };

        setters.loadAllData(mappedData);
        setLoaded(true);
      } catch (e) { 
        console.error("Erro no carregamento do Layout:", e); 
      }
    }
    load();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id, setters, loaded, state.personal.name, pathname]);

  if (!loaded) {
    return (
      <div className="flex h-screen items-center justify-center flex-col gap-4">
        <Loader2 className="animate-spin text-[#0D4F97] h-10 w-10" />
        <p className="text-gray-500 font-medium">Carregando dados para edição...</p>
      </div>
    );
  }

  return <>{children}</>;
}

export default function EditPatientLayout({ children }: { children: React.ReactNode }) {
  return (
    <VaccinesProvider>
      {/* @ts-ignore */}
      <DisordersProvider>
        <MembersRegisterProvider>
          <EditPatientDataLoader>{children}</EditPatientDataLoader>
        </MembersRegisterProvider>
      </DisordersProvider>
    </VaccinesProvider>
  );
}