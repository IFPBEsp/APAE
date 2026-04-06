"use client";


import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DocumentCategoriesCard from "@/components/DocumentCategoriesCard";
import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState, useCallback } from "react";
import { toast } from "react-toastify";
import { Loader2, ArrowLeft, SquarePen, Plus } from "lucide-react";
import { Patient, AnnualRegistry, PatientParent, PatientVaccine, AnnualRegistryServiceArea, AnnualRegistryDisorder } from "@/types/patient"; 
import AnnualRegistryEditModal from "@/components/AnnualRegistryEditModal";
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@/components/ui/tooltip";

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

  const [pessoa, setPessoa] = useState<Patient | null>(null);
  const [registroAnual, setRegistroAnual] = useState<AnnualRegistry | null>(null);
  
  const [existingYears, setExistingYears] = useState<string[]>([]);
  const [selectedYear, setSelectedYear] = useState<string>(new Date().getFullYear().toString());
  
  const [loadingPessoa, setLoadingPessoa] = useState(true);
  const [loadingRegistro, setLoadingRegistro] = useState(false);

  const fetchPessoa = useCallback(async (silent = false) => {
    try {
      if (!silent) setLoadingPessoa(true); 
      const response = await fetch(`/api/pessoas/${id}`);
      if (!response.ok) throw new Error("Falha ao buscar dados do paciente.");
      const data = await response.json();
      setPessoa(data);
    } catch (err: unknown) {
      console.error(err);
      toast.error(err instanceof Error ? err.message : "Erro ao buscar paciente");
      router.push("/visualization-patients");
    } finally {
      if (!silent) setLoadingPessoa(false); 
    }
  }, [id, router]);

  const fetchYears = useCallback(async () => {
    try {
        const res = await fetch(`/api/pessoas/${id}/registro-anual/years-list`);
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

  const fetchRegistro = useCallback(async () => {
    try {
      setLoadingRegistro(true);
      setRegistroAnual(null);
      const response = await fetch(`/api/pessoas/${id}/registro-anual/${selectedYear}`);
      if (response.ok) {
        const data = await response.json();
        setRegistroAnual(data);
      }
    } catch (err) {
      console.error("Erro ao buscar registro anual:", err);
    } finally {
      setLoadingRegistro(false);
    }
  }, [id, selectedYear]);

  useEffect(() => {
    if (id) {
        fetchPessoa();
        fetchYears();
    }
  }, [id, fetchPessoa, fetchYears]);

  useEffect(() => {
    if (id && selectedYear) {
        fetchRegistro();
    }
  }, [id, selectedYear, fetchRegistro]);

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
              fetchPessoa(true); 
          });
      } else {
          fetchYears();
          fetchRegistro();
          fetchPessoa(true);
      }
  };

  if (loadingPessoa) {
    return (
      <div className="flex justify-center items-center h-screen">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    );
  }

  if (!pessoa) {
    return (
      <div className="text-center mt-10"><p>Paciente não encontrado.</p><Button asChild variant="link"><Link href="/visualization-patients">Voltar</Link></Button></div>
    );
  }

  const hasRegistro = !!registroAnual;

  return (
    <main className="container mx-auto p-4 md:p-6">
      <div className="mb-4">
        <Button variant="outline" onClick={() => router.push("/visualization-patients")} className="gap-2">
          <ArrowLeft className="h-4 w-4" /> Voltar para listagem
        </Button>
      </div>

      <div className="flex flex-col items-center gap-y-4 w-full mb-6">
        <Avatar className="h-40 w-40 border">
          <AvatarImage src={pessoa?.photoUrl || undefined} alt={pessoa?.fullName ?? "Foto do paciente"} />
          <AvatarFallback className="font-baloo font-bold text-[32px]">{pessoa?.fullName?.charAt(0) ?? "P"}</AvatarFallback>
        </Avatar>
        <h3 className="font-baloo font-bold text-[#0D4F97] text-[24px]">{pessoa?.fullName}</h3>
      </div>

      <DocumentCategoriesCard onClickCategoria={(tipo: string) => { router.push(`/person/${id}/documents/${tipo}`); }} />

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-6">
        {/* Card Dados Pessoais */}
        <Card className="w-full relative font-nunito">
          <CardHeader><CardTitle className="text-[#0D4F97]">Dados Pessoais</CardTitle></CardHeader>
          <CardContent className="grid grid-cols-1 md:grid-cols-2 gap-x-4">
            <InfoRow label="Nome Completo" value={pessoa.fullName} />
            <InfoRow label="Data de Nasc." value={pessoa.birthDate} />
            <InfoRow label="Naturalidade" value={pessoa.birthplace} />
            <InfoRow label="Contato" value={pessoa.contact} />
            <InfoRow label="Alergias" value={pessoa.allergies} />
            <InfoRow label="Estudante?" value={pessoa.isStudent} />
            <InfoRow label="Data de Cadastro" value={pessoa.registrationDate} />
          </CardContent>
        </Card>

        {/* Card Documentação */}
        <Card className="w-full relative font-nunito">
          <CardHeader><CardTitle className="text-[#0D4F97]">Documentação</CardTitle></CardHeader>
          <CardContent className="grid grid-cols-1 md:grid-cols-2 gap-x-4">
            <InfoRow label="CPF" value={pessoa.cpf} />
            <InfoRow label="RG" value={pessoa.rg} />
            <InfoRow label="Orgão Emissor" value={pessoa.issuingAgency} />
            <InfoRow label="Data de Emissão" value={pessoa.issueDate} />
            <InfoRow label="CNS" value={pessoa.cns} />
            <InfoRow label="NIS" value={pessoa.nis} />
            <InfoRow label="Nº Cert. Nasc." value={pessoa.birthCertificateNumber} />
            <InfoRow label="Cartório" value={pessoa.registryOffice} />
            <InfoRow label="Livro" value={pessoa.book} />
            <InfoRow label="Folha" value={pessoa.fls} />
          </CardContent>
        </Card>

        {/* Card Endereço */}
        <Card className="w-full relative font-nunito">
          <CardHeader><CardTitle className="text-[#0D4F97]">Endereço Residencial</CardTitle></CardHeader>
          <CardContent className="grid grid-cols-1 md:grid-cols-2 gap-x-4">
            <InfoRow label="Rua" value={pessoa.address?.street} />
            <InfoRow label="Número" value={pessoa.address?.number} />
            <InfoRow label="Bairro" value={pessoa.address?.neighborhood} />
            <InfoRow label="Cidade" value={pessoa.address?.city} />
            <InfoRow label="Estado" value={pessoa.address?.state} />
            <InfoRow label="CEP" value={pessoa.address?.cep} />
            <InfoRow label="Complemento" value={pessoa.address?.complement} />
          </CardContent>
        </Card>

        {/* Card Responsáveis */}
        <Card className="w-full relative font-nunito">
          <CardHeader><CardTitle className="text-[#0D4F97]">Responsáveis</CardTitle></CardHeader>
          <CardContent>
            {pessoa.guardian && (
              <div className="mb-4 p-2 border rounded-md">
                <p className="font-bold text-base">Guardião Principal</p>
                <InfoRow label="Nome" value={pessoa.guardian.name} />
                <InfoRow label="Parentesco" value={pessoa.guardian.kinship} />
                <InfoRow label="Contato" value={pessoa.guardian.contact} />
                <InfoRow label="Endereço" value={`${pessoa.guardian.address?.street ?? ""}, ${pessoa.guardian.address?.number ?? ""}`} />
              </div>
            )}
            {pessoa.parents?.map((parent: PatientParent) => (
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
          <CardHeader className="flex flex-row items-center justify-between">
            <CardTitle className="text-[#0D4F97]">Informações de Saúde</CardTitle>
            <div className="flex items-center gap-2">
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
                                disabled={!hasRegistro || loadingRegistro}
                                className="gap-1 hover:!bg-gray-100 text-[#0D4F97] border-0 disabled:opacity-50 disabled:cursor-not-allowed h-8"
                                variant="outline"
                            >
                                <SquarePen className="h-4 w-4" /> {loadingRegistro ? "..." : "Editar"}
                            </Button>
                        </span>
                    </TooltipTrigger>
                    {!hasRegistro && !loadingRegistro && (<TooltipContent><p>Não existe registro para este ano.</p></TooltipContent>)}
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
            <InfoRow label="Alergias" value={pessoa.allergies} />
            <InfoRow label="Vacinas" value={pessoa.vaccineNames?.map((v: PatientVaccine) => v.name).join(", ")} />
            
            <h3 className="font-bold text-base mt-4 pt-4 border-t text-[#0D4F97]">Registro Anual ({selectedYear})</h3>

            {loadingRegistro ? (
              <div className="py-4 flex justify-center"><Loader2 className="h-6 w-6 animate-spin text-[#0D4F97]" /></div>
            ) : registroAnual ? (
              <div className="mt-2 animate-in fade-in slide-in-from-bottom-2">
                <InfoRow label="Recebe BPC?" value={registroAnual.bpc} />
                <InfoRow label="Renda Familiar" value={registroAnual.familyIncome} />
                <InfoRow label="Tipo de Atendimento" value={registroAnual.serviceAreas?.map((atendimento: AnnualRegistryServiceArea) => atendimento.area).join(", ")} />
                <InfoRow label="Doenças" value={registroAnual.diseases} />
                <InfoRow label="Medicamentos Contínuos" value={registroAnual.continuousMedication} />
                <InfoRow label="Transtornos" value={registroAnual.disorders?.map((d: AnnualRegistryDisorder) => d.name).join(", ")} />
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
          initialData={modalMode === "edit" ? registroAnual : null}
          mode={modalMode} 
        />
      </div>
    </main>
  );
}