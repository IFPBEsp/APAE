"use client";

import { useState, useEffect } from "react";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { PatientCard } from "@/components/patient-card";
import { PatientCardData } from "@/schemas/patientSchema"; 
import { SearchFilters } from "@/components/search-filters";
import { toast } from "react-toastify";
import { useDebounce } from "@/hooks/use-debounce"; 

import { createClientApi } from '@/lib/api-client';

export default function PatientsAndStudentsScreen() {
  const [patients, setPatients] = useState<PatientCardData[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [searchName, setSearchName] = useState<string>("");
  const [transtorno, setTranstorno] = useState<string>("");
  const [ano, setAno] = useState<string>("");
  const [cidade, setCidade] = useState<string>("");
  
  const debouncedSearchName = useDebounce(searchName, 500);

  const [transtornoOptions, setTranstornoOptions] = useState<string[]>([]);
  const [anoOptions, setAnoOptions] = useState<string[]>([]);
  const [cidadeOptions, setCidadeOptions] = useState<string[]>([]);

  useEffect(() => {
    const fetchFilterOptions = async () => {
      try {
        const api = createClientApi(); 

        const transtornosPromise = api.get('/patients/filtros/transtornos');
        const anosPromise = api.get('/patients/filtros/anos');
        const cidadesPromise = api.get('/patients/filtros/cidades');

        const [
          transtornosResponse,
          anosResponse,
          cidadesResponse
        ] = await Promise.all([
          transtornosPromise,
          anosPromise,
          cidadesPromise
        ]);
        
        setTranstornoOptions(transtornosResponse.data);
        setAnoOptions(anosResponse.data);
        setCidadeOptions(cidadesResponse.data);

      } catch (err) {
        console.error("Erro ao buscar opções de filtro:", err);
        toast.error("Não foi possível carregar os filtros.");
      }
    };
    
    fetchFilterOptions();
  }, []); 

  useEffect(() => {
    const loadData = async () => {
      setIsLoading(true); 
      try {
        const params = {
          Nome: debouncedSearchName || undefined,
          transtorno: transtorno || undefined,
          ano: ano || undefined,
          cidade: cidade || undefined,
        };
        
        const api = createClientApi(); 
        
        console.log("TESTE: Buscando pacientes com params:", params);
        
        const response = await api.get('/patients', { params }); 
        
        console.log("Resposta recebida (pacientes):", response.data);
        
        setPatients(response.data);
        setError(null); 
      } catch (err) {
        console.error("Erro ao buscar dados (pacientes):", err);
        const errorMsg = "Não foi possível carregar os dados.";
        setError(errorMsg);
        toast.error(errorMsg);
      } finally {
        setIsLoading(false);
      }
    };
    
    loadData();
  }, [debouncedSearchName, transtorno, ano, cidade]);

  
  const renderContent = () => {
    if (isLoading) {
      return <p className="text-center text-gray-500">Carregando...</p>;
    }
    if (error) {
      return <p className="text-center text-red-500">{error}</p>;
    }
    if (patients.length === 0) {
      return (
        <p className="text-center text-gray-500">
          Nenhum resultado encontrado.
        </p>
      );
    }
    return (
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        {patients.map((patient) => (
          <PatientCard key={patient.id} patient={patient} />
        ))}
      </div>
    );
  };

  return (
    <div className="!bg-slate-100 min-h-screen">
      <main className="container mx-auto p-4 md:p-6">
        <div className="bg-white rounded-xl shadow-md border-2 p-6 mb-4">
          <SearchFilters
            searchName={searchName}
            setSearchName={setSearchName}
            transtorno={transtorno}
            setTranstorno={setTranstorno}
            ano={ano}
            setAno={setAno}
            cidade={cidade}
            setCidade={setCidade}
            transtornoOptions={transtornoOptions}
            anoOptions={anoOptions}
            cidadeOptions={cidadeOptions}
          />
        </div>
        <section className="relative md:bg-white md:rounded-xl md:shadow-md md:border-2 md:p-6">
          <div className="hidden md:flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-[#003B93]">
              Pacientes e Alunos
            </h2>
            <Button
              asChild
              className="!bg-[#0D4F97] !hover:bg-[#0b427d] text-white"
            >
              <Link href="/pessoa/cadastro">Adicionar</Link>
            </Button>
          </div>
          {renderContent()}
        </section>
      </main>
      <Button
        asChild
        className="fixed bottom-6 right-6 h-[53px] w-[53px] rounded-full shadow-lg md:hidden bg-[#0D4F97] !hover:bg-[#0b427d]"
      >
        <Link href="/pessoa/cadastro">
          <Plus className="h-7 w-7" />
          <span className="sr-only">Adicionar Pessoa</span>
        </Link>
      </Button>
    </div>
  );
}
