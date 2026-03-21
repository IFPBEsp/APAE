"use client";

import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import { 
  MembersRegisterProvider, 
  useMembersRegisterContext, 
  MembersRegisterStep 
} from "@/hooks/use-members-register-context";
import { Loader2, ArrowRight } from "lucide-react";
import { Button } from "@/components/ui/button";

import PersonalForm from "../../register/personal/page";
import KinshipsForm from "../../register/kinships/page";
import AddressForm from "../../register/address/page";
import AdditionalsForm from "../../register/additional/page";
import ResponsibleForm from "../../register/responsible/page";
import ProfileForm from "../../register/profile/page";

import { VaccinesProvider } from "@/hooks/use-vaccines";
import { DisordersProvider } from "@/hooks/use-disorders";

function EditPatientContainer({ id }: { id: string }) {
  const { state: { step }, setters } = useMembersRegisterContext();
  const [dataLoaded, setDataLoaded] = useState(false);

  useEffect(() => {
    if (dataLoaded) return;

    async function loadPatient() {
      try {
        const res = await fetch(`/api/pessoas/${id}`);
        if (!res.ok) throw new Error("Erro ao buscar dados");
        const data = await res.json();

        const mappedData: any = {
          personal: {
            name: data.fullName || "",
            cpf: data.cpf || "" ,
            phone: data.contact || "",
            rg: { 
              number: data.rg || "", 
              issuing: { 
                body: data.issuingAgency || "", 
                date: data.issueDate ? new Date(data.issueDate) : new Date() 
              } 
            },
            cns: data.cns || "",
            nis: data.nis || "",
            birth: { 
              certificate: data.birthCertificateNumber || "", 
              date: data.birthDate ? new Date(data.birthDate) : new Date(), 
              place: data.birthplace || data.nationality || ""
            }
          },
          address: {
            cep: data.address?.cep || "",
            state: data.address?.state || "",
            city: data.address?.city || "",
            district: data.address?.neighborhood || "",
            street: data.address?.street ? `${data.address.street}, ${data.address.number || ""}` : ""
          },
          additionals: {
            diseases: data.annualRegistry?.diseases || "",
            medications: data.annualRegistry?.continuousMedication || "", 
            vaccines: data.vaccineNames?.map((v: any) => v.name) || [], 
            allergies: data.allergies || "",
            disability: { 
              types: data.annualRegistry?.disorders?.map((d: any) => d.name) || [], 
              report: undefined 
            },
            care: { 
              types: data.annualRegistry?.serviceAreas?.map((s: any) => s.area) || [], 
              referral: undefined 
            },
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
               street: data.guardian?.address?.street || ""
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
            photo: null 
          },
          step: MembersRegisterStep.PERSONAL
        };

        setters.loadAllData(mappedData);
        setDataLoaded(true);
      } catch (error) {
        console.error("Erro ao carregar paciente:", error);
      }
    }
    
    if (id) loadPatient();
  }, [id, setters, dataLoaded]);

  if (!dataLoaded) {
    return (
      <div className="flex h-screen flex-col items-center justify-center gap-4">
        <Loader2 className="h-10 w-10 animate-spin text-[#0D4F97]" />
        <p className="text-gray-500 font-medium">Carregando dados para edição...</p>
      </div>
    );
  }

  return (
    <div className="container mx-auto py-6">
      <div className="mb-6 px-4 text-center md:text-left">
        <h1 className="text-3xl font-bold text-[#0D4F97]">Editar Paciente</h1>
        <p className="text-gray-500 mt-2">Atualize as informações cadastrais abaixo.</p>
      </div>

      <div className="bg-white rounded-xl shadow-sm border p-4 md:p-8 min-h-[400px]">
        {step === MembersRegisterStep.PERSONAL && <PersonalForm />}
        {step === MembersRegisterStep.KINSHIPS && <KinshipsForm />}
        {step === MembersRegisterStep.ADDRESS && <AddressForm />}
        
        
        {step === MembersRegisterStep.ADDITIONALS && (
          <div className="flex flex-col items-center justify-center py-12 text-center space-y-6">
            <div className="bg-blue-50 p-6 rounded-full">
              <Loader2 className="h-12 w-12 text-[#0D4F97] animate-pulse" />
            </div>
            <div className="max-w-md">
              <h2 className="text-xl font-bold text-gray-800">Informações de Saúde</h2>
              <p className="text-gray-500 mt-2 leading-relaxed">
                Os dados de saúde e registros anuais devem ser editados diretamente na seção de <strong>"Informações de Saúde"</strong> do paciente.
              </p>
            </div>
            <Button 
              onClick={() => setters.setStep(MembersRegisterStep.GUARDIAN)}
              className="bg-[#0D4F97] hover:bg-[#0a3d75] gap-2 px-8 h-12"
            >
              Continuar para Responsável <ArrowRight size={18} />
            </Button>
          </div>
        )}

        {step === MembersRegisterStep.GUARDIAN && <ResponsibleForm />}
        {step === MembersRegisterStep.PROFILE && <ProfileForm />}
      </div>
    </div>
  );
}

export default function EditPatientPage() {
  const params = useParams();
  const id = params?.id as string;

  return (
    <VaccinesProvider>
      <DisordersProvider>
        <MembersRegisterProvider>
          <EditPatientContainer id={id} />
        </MembersRegisterProvider>
      </DisordersProvider>
    </VaccinesProvider>
  );
}