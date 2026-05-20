"use client";

import { useEffect, useRef, useState } from "react";
import Link from "next/link";
import { parseAsInteger, parseAsString, useQueryStates } from "nuqs";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { PatientCard } from "@/components/shared/patient-card";
import { PatientCardData } from "@/schemas/patientSchema";
import { SearchFilters } from "@/components/search-filters";
import { Pagination } from "@/components/shared/pagination";
import { toast } from "react-toastify";
import { useDebounce } from "@/hooks/use-debounce";
import { usePatientFilters } from "@/hooks/use-patients-filters";
import type { Page } from "@/types/pagination";

import { Suspense } from "react";

function PatientsAndStudentsScreenContent() {
  const [patients, setPatients] = useState<PatientCardData[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const latestRequestKeyRef = useRef("");

  const [query, setQuery] = useQueryStates({
    name: parseAsString.withDefault(""),
    disorder: parseAsString.withDefault(""),
    year: parseAsString.withDefault(""),
    city: parseAsString.withDefault(""),
    treatmentType: parseAsString.withDefault(""),
    page: parseAsInteger.withDefault(0),
    size: parseAsInteger.withDefault(10),
  });

  const debouncedSearchName = useDebounce(query.name, 500);

  const {
    disorderOptions,
    anoOptions,
    cityOptions,
    tipoAtendimentoOptions,
  } = usePatientFilters();

  useEffect(() => {
    const controller = new AbortController();

    const loadData = async () => {
      setIsLoading(true);

      try {
        const params = new URLSearchParams();

        if (debouncedSearchName) params.append("name", debouncedSearchName);
        if (query.disorder) params.append("disorder", query.disorder);
        if (query.year) params.append("year", query.year);
        if (query.city) params.append("city", query.city);
        if (query.treatmentType) {
          params.append("treatmentType", query.treatmentType);
        }

        params.append("page", String(query.page));
        params.append("size", String(query.size));

        const requestKey = params.toString();
        latestRequestKeyRef.current = requestKey;

        const response = await fetch(`/apae-geral/api/patients?${requestKey}`, {
          signal: controller.signal,
        });

        if (!response.ok) {
          const errorData = await response.json();
          console.error(
            "[ERRO API PATIENTS]:",
            errorData.response?.data || errorData.message,
          );
          throw new Error(errorData.message || "Erro ao buscar dados");
        }

        const data: Page<PatientCardData> = await response.json();

        if (latestRequestKeyRef.current !== requestKey) {
          return;
        }

        setPatients(data.content);
        setTotalPages(data.totalPages);
        setTotalElements(data.totalElements);
        setError(null);
      } catch (err) {
        if (err instanceof DOMException && err.name === "AbortError") {
          return;
        }

        console.error("Erro ao buscar dados (pacientes):", err);

        const errorMsg =
          err instanceof Error
            ? err.message
            : "Não foi possível carregar os dados.";

        setError(errorMsg);
        toast.error(errorMsg);
      } finally {
        if (!controller.signal.aborted) {
          setIsLoading(false);
        }
      }
    };

    void loadData();

    return () => {
      controller.abort();
    };
  }, [
    debouncedSearchName,
    query.disorder,
    query.year,
    query.city,
    query.treatmentType,
    query.page,
    query.size,
  ]);

  const updateQuery = (nextValues: Partial<typeof query>) => {
    void setQuery(nextValues);
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
          pageSize={query.size}
          onPageChange={(nextPage) => {
            updateQuery({ page: nextPage });
          }}
          onPageSizeChange={(nextSize) => {
            updateQuery({ size: nextSize, page: 0 });
          }}
        />
      </>
    );
  };

  return (
    <div className="!bg-slate-100 min-h-screen">
      <main className="container mx-auto p-4 md:p-6">
        <div className="mb-4 rounded-xl border-2 bg-white p-6 shadow-md">
          <SearchFilters
            searchName={query.name}
            setSearchName={(value) => {
              updateQuery({
                name: value || "",
                page: 0,
              });
            }}
            disorder={query.disorder}
            setDisorder={(value) => {
              updateQuery({
                disorder: value || "",
                page: 0,
              });
            }}
            ano={query.year}
            setAno={(value) => {
              updateQuery({
                year: value || "",
                page: 0,
              });
            }}
            city={query.city}
            setCity={(value) => {
              updateQuery({
                city: value || "",
                page: 0,
              });
            }}
            tipoAtendimento={query.treatmentType}
            setTipoAtendimento={(value) => {
              updateQuery({
                treatmentType: value || "",
                page: 0,
              });
            }}
            disorderOptions={disorderOptions}
            anoOptions={anoOptions}
            cityOptions={cityOptions}
            tipoAtendimentoOptions={tipoAtendimentoOptions}
          />
        </div>

        <section className="relative md:rounded-xl md:border-2 md:bg-white md:p-6 md:shadow-md">
          <div className="mb-4 hidden items-center justify-between md:flex">
            <h2 className="text-xl font-bold text-[#003B93]">
              Pacientes e Alunos
            </h2>

            <Button
              asChild
              className="!bg-[#0D4F97] text-white !hover:bg-[#0b427d]"
            >
              <Link href="/patients/create">Adicionar</Link>
            </Button>
          </div>

          {renderContent()}
        </section>
      </main>

      <Button
        asChild
        className="fixed bottom-6 right-6 h-[53px] w-[53px] rounded-full bg-[#0D4F97] shadow-lg !hover:bg-[#0b427d] md:hidden"
      >
        <Link href="/patients/create">
          <Plus className="h-7 w-7" />
          <span className="sr-only">Adicionar Pessoa</span>
        </Link>
      </Button>
    </div>
  );
}

export default function PatientsAndStudentsScreen() {
  return (
    <Suspense fallback={<div className="min-h-screen bg-slate-100 flex items-center justify-center">Carregando...</div>}>
      <PatientsAndStudentsScreenContent />
    </Suspense>
  );
}
