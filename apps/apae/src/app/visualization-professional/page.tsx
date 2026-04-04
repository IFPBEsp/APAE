"use client";

import { useEffect, useMemo, useState } from "react";
import Link from "next/link";
import { parseAsInteger, parseAsString, useQueryStates } from "nuqs";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { PatientCard } from "@/components/shared/patient-card";
import { SearchFilters } from "@/components/search-filters";
import { Pagination } from "@/components/shared/pagination";
import { toast } from "react-toastify";
import { useDebounce } from "@/hooks/use-debounce";
import { usePatientFilters } from "@/hooks/use-patients-filters";
import type { PatientCardData } from "@/schemas/patientSchema";
import type { Page } from "@/types/pagination";

export default function PatientsAndStudentsScreen() {
  const [patients, setPatients] = useState<PatientCardData[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const {
    transtornoOptions,
    anoOptions,
    cidadeOptions,
    tipoAtendimentoOptions,
  } = usePatientFilters();

  const [query, setQuery] = useQueryStates(
    {
      name: parseAsString.withDefault(""),
      disorder: parseAsString.withDefault(""),
      year: parseAsString.withDefault(""),
      city: parseAsString.withDefault(""),
      treatmentType: parseAsString.withDefault(""),
      page: parseAsInteger.withDefault(0),
      size: parseAsInteger.withDefault(10),
    },
    {
      history: "push",
      clearOnDefault: true,
      shallow: false,
    },
  );

  const debouncedSearchName = useDebounce(query.name, 500);

  const requestParams = useMemo(() => {
    const params = new URLSearchParams();

    if (debouncedSearchName) params.set("name", debouncedSearchName);
    if (query.disorder) params.set("disorder", query.disorder);
    if (query.year) params.set("year", query.year);
    if (query.city) params.set("city", query.city);
    if (query.treatmentType) params.set("treatmentType", query.treatmentType);

    params.set("page", String(query.page));
    params.set("size", String(query.size));

    return params.toString();
  }, [
    debouncedSearchName,
    query.disorder,
    query.year,
    query.city,
    query.treatmentType,
    query.page,
    query.size,
  ]);

  useEffect(() => {
    const loadData = async () => {
      setIsLoading(true);

      try {
        const response = await fetch(`/api/patients?${requestParams}`);

        if (!response.ok) {
          const errorData = await response.json();
          console.error(
            "[ERRO API PATIENTS]:",
            errorData.response?.data || errorData.message,
          );
          throw new Error(errorData.message || "Erro ao buscar dados");
        }

        const data: Page<PatientCardData> = await response.json();

        setPatients(data.content);
        setTotalPages(data.totalPages);
        setTotalElements(data.totalElements);
        setError(null);

        if (data.totalPages > 0 && query.page > data.totalPages - 1) {
          await setQuery({ page: Math.max(data.totalPages - 1, 0) });
        }
      } catch (err) {
        console.error("Erro ao buscar dados (pacientes):", err);
        const errorMsg =
          err instanceof Error
            ? err.message
            : "Não foi possível carregar os dados.";
        setError(errorMsg);
        toast.error(errorMsg);
      } finally {
        setIsLoading(false);
      }
    };

    loadData();
  }, [requestParams, query.page, setQuery]);

  const updateFilter = async (
    field: "name" | "disorder" | "year" | "city" | "treatmentType",
    value: string,
  ) => {
    await setQuery({
      [field]: value,
      page: 0,
    });
  };

  const handlePageChange = async (page: number) => {
    if (page < 0 || page >= totalPages) return;
    await setQuery({ page });
  };

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
      <>
        <div className="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          {patients.map((patient) => (
            <PatientCard key={patient.id} patient={patient} />
          ))}
        </div>

        <Pagination
          currentPage={query.page}
          totalPages={totalPages}
          totalElements={totalElements}
          onPageChange={handlePageChange}
        />
      </>
    );
  };

  return (
    <div className="!bg-slate-100 min-h-screen">
      <main className="container mx-auto p-4 md:p-6">
        <div className="bg-white rounded-xl shadow-md border-2 p-6 mb-4">
          <SearchFilters
            searchName={query.name}
            setSearchName={(value) => updateFilter("name", value)}
            transtorno={query.disorder}
            setTranstorno={(value) => updateFilter("disorder", value)}
            ano={query.year}
            setAno={(value) => updateFilter("year", value)}
            cidade={query.city}
            setCidade={(value) => updateFilter("city", value)}
            tipoAtendimento={query.treatmentType}
            setTipoAtendimento={(value) => updateFilter("treatmentType", value)}
            transtornoOptions={transtornoOptions}
            anoOptions={anoOptions}
            cidadeOptions={cidadeOptions}
            tipoAtendimentoOptions={tipoAtendimentoOptions}
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
              <Link href="/person/register">Adicionar</Link>
            </Button>
          </div>
          {renderContent()}
        </section>
      </main>

      <Button
        asChild
        className="fixed bottom-6 right-6 h-[53px] w-[53px] rounded-full shadow-lg md:hidden bg-[#0D4F97] !hover:bg-[#0b427d]"
      >
        <Link href="/person/register">
          <Plus className="h-7 w-7" />
          <span className="sr-only">Adicionar Pessoa</span>
        </Link>
      </Button>
    </div>
  );
}
