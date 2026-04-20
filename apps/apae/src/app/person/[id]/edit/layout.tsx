"use client";

import { MembersRegisterProvider, useMembersRegisterContext, MembersRegisterStep } from "@/hooks/use-members-register-context";
import { VaccinesProvider } from "@/hooks/use-vaccines";
import { DisordersProvider } from "@/hooks/use-disorders";
import { useParams, useRouter, usePathname } from "next/navigation";
import { useEffect, useState } from "react";
import { Loader2 } from "lucide-react";
import Image from "@/assets/background_image.jpg";
import { SidebarSteps } from "@/components/shared/SidebarSteps";

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
        const data = await res.json();

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
        <EditPatientDataLoader>

          <div className="h-screen rounded-lg mx-10 relative grid grid-cols-1 md:grid-cols-[1fr_2fr] antialiased overflow-hidden">
            
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

            <div className="hidden md:flex relative z-10 w-full h-full bg-[#0D4F97]/50">
              <SidebarSteps />
            </div>

            <div className="relative flex flex-col w-full h-full p-8 bg-muted overflow-y-auto">
              <h1 className="text-2xl font-bold text-blue-900 mb-4">
                Edição de paciente
              </h1>
              {children}
            </div>

          </div>

        </EditPatientDataLoader>
      </MembersRegisterProvider>
    </DisordersProvider>
  </VaccinesProvider>
);
}