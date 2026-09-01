"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { SearchFilters } from "@/components/search-filters";
import { ArrowLeft, Loader2, Plus } from "lucide-react";
import { VaccineListItem } from "../shared/vaccine-list-item";
import { useVaccinesList } from "./use-vaccines-list";
import type { Vaccine } from "../vaccines.types";

export function VaccinesList() {
  const [searchName, setSearchName] = useState("");
  const [selectedVaccineToDelete, setSelectedVaccineToDelete] = useState<Vaccine | null>(null);
  const { vaccines, loading, handleDelete } = useVaccinesList();
  const router = useRouter();

  const filtered = vaccines.filter((v) =>
    v.name.toLowerCase().includes(searchName.toLowerCase()),
  );

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
          <SearchFilters searchName={searchName} setSearchName={setSearchName} />
        </div>

        <section className="relative md:bg-white md:rounded-xl md:shadow-md md:border-2 md:p-6">
          <div className="hidden md:flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-[#003B93]">Vacinas Cadastradas</h2>
            <Button
              onClick={() => router.push("/vaccines/new")}
              className="bg-[#003B93] hover:bg-blue-800 text-white font-medium"
            >
              <Plus className="h-4 w-4 mr-2" />
              Cadastrar Vacina
            </Button>
          </div>

          <div className="mb-4 md:hidden flex justify-between items-center">
            <h2 className="text-xl font-bold text-[#003B93]">Vacinas Cadastradas</h2>
            <Button
              onClick={() => router.push("/vaccines/new")}
              size="sm"
              className="bg-[#003B93] hover:bg-blue-800 text-white text-xs font-medium"
            >
              <Plus className="h-3 w-3 mr-1" />
              Cadastrar
            </Button>
          </div>

          {loading ? (
            <div className="flex justify-center items-center p-10">
              <Loader2 className="h-8 w-8 animate-spin text-gray-500" />
            </div>
          ) : filtered.length === 0 ? (
            <p className="text-center text-gray-500 p-10">
              {searchName ? `Nenhuma vacina encontrada para "${searchName}".` : "Nenhuma vacina cadastrada."}
            </p>
          ) : (
            <>
              <p className="text-sm text-gray-500 mb-4">{filtered.length} vacinas encontradas</p>
              <div className="space-y-2">
                {filtered.map((vaccine) => (
                  <VaccineListItem
                    key={vaccine.id}
                    vaccine={vaccine}
                    onDelete={() => setSelectedVaccineToDelete(vaccine)}
                  />
                ))}
              </div>
            </>
          )}
        </section>
      </main>

      {/* Modal de Confirmação de Exclusão (idêntico ao vídeo) */}
      {selectedVaccineToDelete && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
          <div className="bg-white rounded-xl shadow-lg max-w-md w-full p-6 space-y-4 animate-in fade-in zoom-in duration-200">
            <h3 className="text-lg font-bold text-gray-900">Tem certeza?</h3>
            <p className="text-sm text-gray-600">
              Essa ação não pode ser desfeita. Isso irá excluir permanentemente a vacina{" "}
              <span className="font-semibold text-gray-900">{selectedVaccineToDelete.name}</span>.
            </p>

            <div className="flex justify-end gap-3 pt-2">
              <button
                type="button"
                onClick={() => setSelectedVaccineToDelete(null)}
                className="px-4 py-2 border rounded-lg text-sm font-medium text-gray-700 hover:bg-gray-100 transition-colors"
              >
                Cancelar
              </button>

              <button
                type="button"
                onClick={async () => {
                  const vaccine = selectedVaccineToDelete;
                  setSelectedVaccineToDelete(null);
                  await handleDelete(vaccine.id);
                }}
                className="px-4 py-2 bg-[#003B93] hover:bg-blue-800 text-white text-sm font-medium rounded-lg transition-colors"
              >
                Confirmar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}