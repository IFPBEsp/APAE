"use client";

import { useState, useEffect } from "react";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { PatientCard } from "@/components/patient-card";
import { Patient } from "@/schemas/authSchema";
import { SearchFilters } from "@/components/search-filters";

export default function PatientsAndStudentsScreen() {
  const [patients, setPatients] = useState<Patient[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [activeFilter, setActiveFilter] = useState<string>("paciente");
  const [activeStatus, setActiveStatus] = useState<string>("Todos");
  const [searchName, setSearchName] = useState<string>("");

  useEffect(() => {
    const loadData = async () => {
      try {
        const response = await fetch("/api/pessoas");
        if (!response.ok) throw new Error("Erro ao buscar dados");
        const data = await response.json();
        setPatients(data);
      } catch (err) {
        console.error("Erro ao buscar dados:", err);
        setError("Não foi possível carregar os dados.");
      } finally {
        setIsLoading(false);
      }
    };
    loadData();
  }, []);

  const renderContent = () => {
    if (isLoading) {
      return <p className="text-center text-gray-500">Carregando...</p>;
    }
    if (error) {
      return <p className="text-center text-red-500">{error}</p>;
    }

    const filteredPatients = patients.filter((patient) => {
      const isCorrectType =
        (activeFilter === "paciente" && patient.status === "Ativo") ||
        (activeFilter === "aluno" && patient.status !== "Ativo");

      const isCorrectStatus =
        activeStatus === "Todos" || patient.status === activeStatus;

      const matchesSearch =
        searchName.trim() === "" ||
        patient.nome?.toLowerCase().includes(searchName.toLowerCase());

      return isCorrectType && isCorrectStatus && matchesSearch;
    });

    if (filteredPatients.length === 0) {
      return (
        <p className="text-center text-gray-500">
          Nenhum resultado encontrado.
        </p>
      );
    }

    return (
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        {filteredPatients.map((patient) => (
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
            activeFilter={activeFilter}
            setActiveFilter={setActiveFilter}
            activeStatus={activeStatus}
            setActiveStatus={setActiveStatus}
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
        className="fixed bottom-6 right-6 h-[53px] w-[53px] rounded-full shadow-lg md:hidden bg-[#0D4F97] hover:bg-[#0b427d]"
      >
        <Link href="/pessoa/cadastro">
          <Plus className="h-7 w-7" />
          <span className="sr-only">Adicionar Pessoa</span>
        </Link>
      </Button>
    </div>
  );
}
