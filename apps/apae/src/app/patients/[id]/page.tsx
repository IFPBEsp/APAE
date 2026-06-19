"use client";


import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DocumentCategoriesCard from "@/domains/documents/shared/document-categories-card";
import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState, useCallback } from "react";
import { toast } from "react-toastify";
import { Loader2, ArrowLeft, SquarePen, Plus } from "lucide-react";
import AnnualRegistryEditModal from "@/domains/patients/annual-registry/annual-registry-edit-modal";
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@/components/ui/tooltip";
import { PatientResponse } from "@/types/patient";

interface InfoRowProps {
  label: string;
  value?: string | number | null | boolean;
}

const InfoRow: React.FC<InfoRowProps> = ({ label, value }) => {
  let displayValue: string | number = "Não informado";

  if (value === null || value === undefined || value === "") {
    displayValue = "Não informado";
  } else if (label === "Recebe BPC?") {
    displayValue = (value === true || String(value).toLowerCase() === 'true') ? "Sim" : "Não";
  } else if (label === "Renda Familiar" && typeof value === 'number') {
    displayValue = value.toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
  } else if (typeof value === 'boolean') {
    displayValue = value ? "Sim" : "Não";
  } else {
    displayValue = value;
  }

  return (
    <div className="mb-2 overflow-hidden"> 
      <span className="text-sm font-semibold text-gray-500">{label}</span>
      <p className="text-base text-black break-all">{displayValue}</p>
    </div>
  );
};

export default function PersonDetailsPage() {
  const params = useParams();
  const router = useRouter();
  const id = params?.id as string;

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState<"create" | "edit">("edit");

  interface AnnualRegistry {
    id?: string;
    bpc: boolean;
    familyIncome: number;
    diseases: string;
    continuousMedication: string;
    disorders?: { id?: string; name: string }[];
    serviceTypes?: { id?: string; name: string }[];
  }

  const [person, setPerson] = useState<PatientResponse | null>(null);
  const [annualRegistry , setAnnualRegistry] = useState<AnnualRegistry | null>(null);
  
  const [existingYears, setExistingYears] = useState<string[]>([]);
  const [selectedYear, setSelectedYear] = useState<string>(new Date().getFullYear().toString());
  
  const [loadingPerson, setLoadingPerson] = useState(true);
  const [loadingRegistry, setLoadingRegistry] = useState(false);

  const fetchPerson = useCallback(async (silent = false) => {
    try {
      if (!silent) setLoadingPerson(true); 
      const response = await fetch(`/apae-geral/api/patients/${id}`);
      if (!response.ok) throw new Error("Falha ao buscar dados do paciente.");
      const data: PatientResponse = await response.json();
      setPerson(data);
    } catch (err: unknown) {
      const errorMessage = err instanceof Error ? err.message : "Erro ao buscar dados do paciente.";
      console.error(err);
      toast.error(errorMessage);
      router.push("/patients");
    } finally {
      if (!silent) setLoadingPerson(false);
    }
  }, [id, router]);

  const fetchYears = useCallback(async () => {
    try {
        const res = await fetch(`/apae-geral/api/patients/${id}/registro-anual/years-list`);
        let yearsData: number[] = [];
        
        if (res.ok) {
            yearsData = await res.json();
        }

        const currentYearStr = new Date().getFullYear().toString();

        if (yearsData.length > 0) {
            const sortedYears = yearsData.map(y => y.toString()).sort((a, b) => parseInt(b) - parseInt(a));
            setExistingYears(sortedYears);
            
            if (!sortedYears.includes(selectedYear)) {
                setSelectedYear(sortedYears[0]);
            }
        } else {
            setExistingYears([currentYearStr]);
            setSelectedYear(currentYearStr);
        }
    } catch (error) {
        console.error("Erro ao buscar anos:", error);
    }
  }, [id, selectedYear]);

  const fetchRecord = useCallback(async () => {
    try {
      setLoadingRegistry(true);
      setAnnualRegistry(null);
      const response = await fetch(`/apae-geral/api/patients/${id}/registro-anual/${selectedYear}`);
      if (response.ok) {
        const data = await response.json();
        setAnnualRegistry(data);
      }
    } catch (err) {
      console.error("Erro ao buscar registro anual:", err);
    } finally {
      setLoadingRegistry(false);
    }
  }, [id, selectedYear]);

  useEffect(() => {
    if (id) {
        fetchPerson();
        fetchYears();
    }
  }, [id, fetchPerson, fetchYears]);

  useEffect(() => {
    if (id && selectedYear) {
        fetchRecord();
    }
  }, [id, selectedYear, fetchRecord]);

  const handleEditClick = () => {
    setModalMode("edit");
    setIsModalOpen(true);
  };

  const handleCreateClick = () => {
    setModalMode("create");
    setIsModalOpen(true);
  };

  const handleModalClose = (savedYear?: string) => {
      setIsModalOpen(false);
      if (savedYear) {
          fetchYears().then(() => {
              setSelectedYear(savedYear);
              fetchPerson(true); 
          });
      } else {
          fetchYears();
          fetchRecord();
          fetchPerson(true);
      }
  };

  if (loadingPerson) {
    return (
      <div className="flex justify-center items-center h-screen">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    );
  }

  if (!person) {
    return (
      <div className="text-center mt-10"><p>Paciente não encontrado.</p><Button asChild variant="link"><Link href="/patients">Voltar</Link></Button></div>
    );
  }

  const hasRegistry = !!annualRegistry;

  return (
    <main className="container mx-auto p-4 md:p-6">
      <div className="mb-4">
        <Button variant="outline" onClick={() => router.push("/patients")} className="gap-2">
          <ArrowLeft className="h-4 w-4" /> Voltar para listagem
        </Button>
      </div>

      <div className="flex flex-col items-center gap-y-4 w-full mb-6">
        <Avatar className="h-40 w-40 border">
          <AvatarImage src={person?.photoUrl} alt={person?.fullName ?? "Foto do paciente"} />
          <AvatarFallback className="font-baloo font-bold text-[32px]">{person?.fullName?.charAt(0) ?? "P"}</AvatarFallback>
        </Avatar>
        <h3 className="font-baloo font-bold text-[#0D4F97] text-[24px]">{person?.fullName}</h3>
      </div>

      <DocumentCategoriesCard onClickCategory={(tipo: string) => { router.push(`/patients/${id}/documents/${tipo}`); }} />
      
      

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-6">
        {/* Card Dados Pessoais */}
        <Card className="w-full relative font-nunito">
          <CardHeader><CardTitle className="text-[#0D4F97]">Dados Pessoais</CardTitle></CardHeader>
          <CardContent className="grid grid-cols-1 md:grid-cols-2 gap-x-4">
            <InfoRow label="Nome Completo" value={person.fullName} />
            <InfoRow label="Data de Nasc." value={person.birthDate} />
            <InfoRow label="Naturalidade" value={person.birthplace} />
            <InfoRow label="Contato" value={person.contact} />
            <InfoRow label="Alergias" value={person.allergies} />
            <InfoRow label="Estudante?" value={person.isStudent} />
            <InfoRow label="Data de Cadastro" value={person.registrationDate} />
          </CardContent>
        </Card>

        {/* Card Documentação */}
        <Card className="w-full relative font-nunito">
          <CardHeader><CardTitle className="text-[#0D4F97]">Documentação</CardTitle></CardHeader>
          <CardContent className="grid grid-cols-1 md:grid-cols-2 gap-x-4">
            <InfoRow label="CPF" value={person.cpf} />
            <InfoRow label="RG" value={person.rg} />
            <InfoRow label="Orgão Emissor" value={person.issuingAgency} />
            <InfoRow label="Data de Emissão" value={person.issueDate} />
            <InfoRow label="CNS" value={person.cns} />
            <InfoRow label="NIS" value={person.nis} />
            <InfoRow label="Nº Cert. Nasc." value={person.birthCertificateNumber} />
            <InfoRow label="Cartório" value={person.registryOffice} />
            <InfoRow label="Livro" value={person.book} />
            <InfoRow label="Folha" value={person.fls} />
          </CardContent>
        </Card>

        {/* Card Endereço */}
        <Card className="w-full relative font-nunito">
          <CardHeader><CardTitle className="text-[#0D4F97]">Endereço Residencial</CardTitle></CardHeader>
          <CardContent className="grid grid-cols-1 md:grid-cols-2 gap-x-4">
            <InfoRow label="Rua" value={person.address?.street} />
            <InfoRow label="Número" value={person.address?.number} />
            <InfoRow label="Bairro" value={person.address?.neighborhood} />
            <InfoRow label="Cidade" value={person.address?.city} />
            <InfoRow label="Estado" value={person.address?.state} />
            <InfoRow label="CEP" value={person.address?.cep} />
            <InfoRow label="Complemento" value={person.address?.complement} />
          </CardContent>
        </Card>

        {/* Card Responsáveis */}
        <Card className="w-full relative font-nunito">
          <CardHeader><CardTitle className="text-[#0D4F97]">Responsáveis</CardTitle></CardHeader>
          <CardContent>
            {person.guardian && (
              <div className="mb-4 p-2 border rounded-md">
                <p className="font-bold text-base">Guardião Principal</p>
                <InfoRow label="Nome" value={person.guardian.name} />
                <InfoRow label="Parentesco" value={person.guardian.kinship} />
                <InfoRow label="Contato" value={person.guardian.contact} />
                <InfoRow label="Rua" value={person.guardian.address?.street} />
                <InfoRow label="Número" value={person.guardian.address?.number} />
                <InfoRow label="Bairro" value={person.guardian.address?.neighborhood} />
                <InfoRow label="Cidade" value={person.guardian.address?.city} />
                <InfoRow label="Estado" value={person.guardian.address?.state} />
                <InfoRow label="CEP" value={person.guardian.address?.cep} />
                <InfoRow label="Complemento" value={person.guardian.address?.complement} />
              </div>
            )}
            {person.parents?.map((parent) => (
              <div key={parent.id} className="mb-2 p-2 border-t border-gray-200">
                <p className="font-bold text-base">Parentes</p>
                <InfoRow label="Nome" value={parent.name} />
                <InfoRow label="CPF" value={parent.cpf} />
                <InfoRow label="Parentesco" value={parent.kinship}/>
                <InfoRow label="Profissão" value={parent.profession} />
                <InfoRow label="Vivo?" value={parent.isAlive} />
              </div>
            ))}
          </CardContent>
        </Card>

        {/* --- CARD DE SAÚDE DINÂMICO --- */}
        <Card className="w-full relative font-nunito">
          <CardHeader className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
            <CardTitle className="text-[#0D4F97]">Informações de Saúde</CardTitle>
            <div className="flex flex-wrap items-center gap-2">
              <TooltipProvider>
                <Tooltip>
                    <TooltipTrigger asChild>
                        <Button size="sm" onClick={handleCreateClick} className="gap-1 bg-green-600 hover:bg-green-700 text-white border-0 h-8">
                            <Plus className="h-4 w-4" /> Novo Ano
                        </Button>
                    </TooltipTrigger>
                    <TooltipContent><p>Adicionar registro para um novo ano</p></TooltipContent>
                </Tooltip>
              </TooltipProvider>
              <TooltipProvider>
                <Tooltip>
                    <TooltipTrigger asChild>
                         <span tabIndex={0}>
                            <Button
                                size="sm"
                                onClick={handleEditClick}
                                disabled={!hasRegistry || loadingRegistry}
                                className="gap-1 hover:!bg-gray-100 text-[#0D4F97] border-0 disabled:opacity-50 disabled:cursor-not-allowed h-8"
                                variant="outline"
                            >
                                <SquarePen className="h-4 w-4" /> {loadingRegistry ? "..." : "Editar"}
                            </Button>
                        </span>
                    </TooltipTrigger>
                    {!hasRegistry && !loadingRegistry && (<TooltipContent><p>Não existe registro para este ano.</p></TooltipContent>)}
                </Tooltip>
              </TooltipProvider>
              <div className="flex items-center gap-1 border border-gray-300 rounded-md px-2 py-1 h-8">
                 <label htmlFor="year-select" className="text-sm font-semibold text-gray-600">Ano:</label>
                <select
                    id="year-select"
                    value={selectedYear}
                    onChange={(e) => setSelectedYear(e.target.value)}
                    className="text-sm focus:outline-none bg-transparent"
                >
                    {existingYears.length > 0 ? (
                        existingYears.map((year) => <option key={year} value={year}>{year}</option>)
                    ) : (
                        <option value={new Date().getFullYear()}>{new Date().getFullYear()}</option>
                    )}
                </select>
              </div>
            </div>
          </CardHeader>
          
          <CardContent>
            <InfoRow label="Alergias" value={person.allergies} />
            <InfoRow label="Vacinas" value={person.vaccineNames?.map((v) => v.name).join(", ")} />
            
            <h3 className="font-bold text-base mt-4 pt-4 border-t text-[#0D4F97]">Registro Anual ({selectedYear})</h3>

            {loadingRegistry ? (
              <div className="py-4 flex justify-center"><Loader2 className="h-6 w-6 animate-spin text-[#0D4F97]" /></div>
            ) : annualRegistry  ? (
              <div className="mt-2 animate-in fade-in slide-in-from-bottom-2">
                <InfoRow label="Recebe BPC?" value={annualRegistry .bpc} />
                <InfoRow label="Renda Familiar" value={annualRegistry .familyIncome} />
                <InfoRow label="Tipo de Atendimento" value={annualRegistry .serviceTypes?.map((atendimento) => atendimento.name).join(", ")} />
                <InfoRow label="Doenças" value={annualRegistry .diseases} />
                <InfoRow label="Medicamentos Contínuos" value={annualRegistry .continuousMedication} />
                <InfoRow label="Transtornos" value={annualRegistry.disorders?.map((d) => d.name).join(", ")} />
              </div>
            ) : (
              <div className="text-center py-6 bg-slate-50 rounded-lg mt-2 border border-dashed border-slate-200">
                <p className="text-sm text-gray-500 mb-2">Nenhum registro encontrado para {selectedYear}.</p>
                <Button variant="link" onClick={handleCreateClick} className="text-[#0D4F97] h-auto p-0 text-sm">Clique para criar um registro</Button>
              </div>
            )}
          </CardContent>
        </Card>
        
        <AnnualRegistryEditModal 
          isOpen={isModalOpen} 
          onClose={handleModalClose}
          patientId={id} 
          currentYear={selectedYear}
          initialData={modalMode === "edit" ? annualRegistry  : null}
          mode={modalMode} 
        />
      </div>
    </main>
  );
}
