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
        
        const formatStreet = (addr: any) => {
          if (!addr?.street) return "";
          const street = addr.street.trim();
          const number = addr.number ? addr.number.trim() : "";
          
          if (street.includes(",")) return street;
          return number ? `${street}, ${number}` : street;
        };

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
             district: data.address?.neighborhood || "", 
             street: formatStreet(data.address) 
           },
           additionals: { 
             diseases: data.annualRegistry?.diseases || "", 
             continuousMedication: data.annualRegistry?.continuousMedication || "", 
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
               district: data.guardian?.address?.neighborhood || "", 
               street: formatStreet(data.guardian?.address)  
             } 
           },
           kinships: data.parents?.map((p: any) => ({ 
             name: p.name || "", 
             cpf: p.cpf || "", 
             rg: p.rg || "", 
             occupation: p.profession || "", 
             alive: p.isAlive ?? true, 
             type: p.kinship || "" 
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
            {/* 🚀 NOVA ESTRUTURA VISUAL (Duas Colunas) */}
            <div className="h-[90vh] md:h-screen rounded-xl md:rounded-none md:mx-0 mx-4 my-4 md:my-0 relative grid grid-cols-1 md:grid-cols-[1fr_2fr] antialiased overflow-hidden shadow-2xl">
              
              {/* Fundo e degradê da coluna esquerda */}
              <div className="hidden md:block absolute inset-0 bg-cover bg-center bg-no-repeat grayscale-90 w-1/3" style={{ backgroundImage: `url(${Image.src})`, backgroundAttachment: "fixed" }} />
              <div className="hidden md:block absolute inset-0 w-1/3" style={{ background: "linear-gradient(180deg, rgba(13, 79, 151, 0.9) 0%, rgba(13, 79, 151, 0.95) 100%)" }} />

              {/* O MENU LATERAL */}
              <div className="hidden md:flex relative z-10 w-full h-full bg-[#0D4F97]/90 backdrop-blur-sm">
                <SidebarSteps />
              </div>

              {/* O FORMULÁRIO (Coluna direita) */}
              <div className="relative flex flex-col w-full h-full p-4 md:p-12 bg-slate-50 overflow-y-auto">
                <div className="max-w-3xl mx-auto w-full">
                  <h1 className="text-2xl md:text-3xl font-bold text-[#0D4F97] mb-6 border-b pb-4">
                    Edição de Paciente
                  </h1>
                  <div className="bg-white p-6 md:p-8 rounded-xl shadow-sm border border-slate-200">
                    {children}
                  </div>
                </div>
              </div>

            </div>
          </EditPatientDataLoader>
        </MembersRegisterProvider>
      </DisordersProvider>
    </VaccinesProvider>
  );
}