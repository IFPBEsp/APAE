"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { parseAsInteger, parseAsString, useQueryStates } from "nuqs";
import { Button } from "@/components/ui/button";
import { SearchFilters } from "@/components/search-filters";
import { Pagination } from "@/components/shared/pagination";
import { useDebounce } from "@/hooks/use-debounce";
import { ArrowLeft, Loader2, Plus } from "lucide-react";
import { VaccineListItem } from "../shared/vaccine-list-item";
import { useVaccinesList } from "./use-vaccines-list";

export function VaccinesList() {
  const [query, setQuery] = useQueryStates({
    name: parseAsString.withDefault(""),
    page: parseAsInteger.withDefault(0),
    size: parseAsInteger.withDefault(10),
  });

  const debouncedSearchName = useDebounce(query.name, 500);

  const {
    vaccines,
    loading,
    totalPages,
    totalElements,
    deleteVaccine,
  } = useVaccinesList({
    name: debouncedSearchName,
    page: query.page,
    size: query.size,
  });

  const router = useRouter();

  const updateQuery = (nextValues: Partial<typeof query>) => {
    setQuery(nextValues);
  };

  return (
    <div className="!bg-slate-100 min-h-screen">
      <main className="container mx-auto p-4 md:p-6">
        <Button
          variant="ghost"
          onClick={() => router.push("/patients")}
          className="mb-4 text-sm text-[#003B93] hover:bg-blue-50" 
        >
          <ArrowLeft className="h-4 w-4 mr-2"/>
            Voltar
        </Button>
        <div className="bg-white rounded-xl shadow-md border-2 p-6 mb-4">
          <SearchFilters
            searchName={query.name}
            setSearchName={(value) => {
              updateQuery({
                name: value || "",
                page: 0,
              });
            }}
          />
        </div>

        <section className="relative md:bg-white md:rounded-xl md:shadow-md md:border-2 md:p-6">
          <div className="hidden md:flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-[#003B93]">Vacinas Cadastradas</h2>
            <Button asChild className="!bg-[#0D4F97] !hover:bg-[#0b427d] text-white">
              <Link href="/vaccines/new">Adicionar</Link>
            </Button>
          </div>

          <div className="mb-4 md:hidden">
            <h2 className="text-xl font-bold text-[#003B93]">Vacinas Cadastradas</h2>
          </div>

          {loading ? (
            <div className="flex justify-center items-center p-10">
              <Loader2 className="h-8 w-8 animate-spin text-gray-500" />
            </div>
          ) : vaccines.length === 0 ? (
            <p className="text-center text-gray-500 p-10">
              {debouncedSearchName ? `Nenhuma vacina encontrada para "${debouncedSearchName}".` : "Nenhuma vacina cadastrada."}
            </p>
          ) : (
            <>
              <p className="text-sm text-gray-500 mb-4">{totalElements} vacinas encontradas</p>
              <div className="space-y-2">
                {vaccines.map((vaccine) => (
                  <VaccineListItem
                    key={vaccine.id}
                    vaccine={vaccine}
                    onEdit={() => router.push(`/vaccines/${vaccine.id}/edit`)}
                    onDelete={() => deleteVaccine(vaccine.id)}
                  />
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
          )}
        </section>
      </main>

      <Button
        asChild
        className="fixed bottom-6 right-6 h-[53px] w-[53px] rounded-full shadow-lg md:hidden bg-[#0D4F97] hover:bg-[#0b427d]"
      >
        <Link href="/vaccines/new">
          <Plus className="h-7 w-7" />
          <span className="sr-only">Adicionar Vacina</span>
        </Link>
      </Button>
    </div>
  );
}
