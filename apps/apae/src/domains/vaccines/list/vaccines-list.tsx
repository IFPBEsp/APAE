"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

import { Button } from "@/components/ui/button";
import { SearchFilters } from "@/components/search-filters";

import { ArrowLeft, Loader2, Plus } from "lucide-react";

import { VaccineListItem } from "../shared/vaccine-list-item";
import { useVaccinesList } from "./use-vaccines-list";

export function VaccinesList() {
  const [searchName, setSearchName] = useState("");

  const { vaccines, loading, deleteVaccine } = useVaccinesList();

  const router = useRouter();

  const filtered = vaccines.filter((v) =>
    v.name.toLowerCase().includes(searchName.toLowerCase()),
  );

  const handleCreate = () => {
    router.push("/vaccines/new");
  };

  const handleEdit = (id: string) => {
    router.push(`/vaccines/${id}/edit`);
  };

  return (
    <div className="!bg-slate-100 min-h-screen">
      <main className="container mx-auto p-4 md:p-6">
        <Button
          variant="ghost"
          onClick={() => router.push("/patients")}
          className="mb-4 text-sm text-[#003B93] hover:bg-blue-50"
        >
          <ArrowLeft className="h-4 w-4 mr-2" />
          Voltar
        </Button>

        <div className="bg-white rounded-xl shadow-md border-2 p-6 mb-4">
          <SearchFilters
            searchName={searchName}
            setSearchName={setSearchName}
          />
        </div>

        <section className="relative md:bg-white md:rounded-xl md:shadow-md md:border-2 md:p-6">
          <div className="hidden md:flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-[#003B93]">
              Vacinas Cadastradas
            </h2>

            <Button
              onClick={handleCreate}
              className="!bg-[#0D4F97] !hover:bg-[#0b427d] text-white"
            >
              <Plus className="h-4 w-4 mr-2" />
              Adicionar Vacina
            </Button>
          </div>

          <div className="mb-4 md:hidden">
            <h2 className="text-xl font-bold text-[#003B93]">
              Vacinas Cadastradas
            </h2>
          </div>

          {loading ? (
            <div className="flex justify-center items-center p-10">
              <Loader2 className="h-8 w-8 animate-spin text-gray-500" />
            </div>
          ) : filtered.length === 0 ? (
            <p className="text-center text-gray-500 p-10">
              {searchName
                ? `Nenhuma vacina encontrada para "${searchName}".`
                : "Nenhuma vacina cadastrada."}
            </p>
          ) : (
            <>
              <p className="text-sm text-gray-500 mb-4">
                {filtered.length} vacinas encontradas
              </p>

              <div className="space-y-2">
                {filtered.map((vaccine) => (
                  <VaccineListItem
                    key={vaccine.id}
                    vaccine={vaccine}
                    onEdit={() => handleEdit(vaccine.id)}
                    onDelete={() => deleteVaccine(vaccine.id)}
                  />
                ))}
              </div>
            </>
          )}

          <Button
            onClick={handleCreate}
            size="icon"
            className="fixed bottom-6 right-6 h-14 w-14 rounded-full shadow-lg md:hidden !bg-[#0D4F97] text-white"
            aria-label="Adicionar vacina"
          >
            <Plus className="h-6 w-6" />
          </Button>
        </section>
      </main>
    </div>
  );
}