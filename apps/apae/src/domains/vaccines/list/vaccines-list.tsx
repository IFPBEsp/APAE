"use client";

import { useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { SearchFilters } from "@/components/search-filters";
import { ArrowLeft, Loader2, Plus } from "lucide-react";
import { VaccineListItem } from "@/domains/vaccines/shared/vaccine-list-item";
import { useVaccinesList } from "@/domains/vaccines/list/use-vaccines-list";

export function VaccinesList() {
  const [searchName, setSearchName] = useState("");
  const { vaccines, loading, deleteVaccine } = useVaccinesList();
  const router = useRouter();

  const filtered = vaccines.filter((v) =>
    v.name.toLowerCase().includes(searchName.toLowerCase())
  );

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
          <SearchFilters searchName={searchName} setSearchName={setSearchName} />
        </div>

        <section className="relative md:bg-white md:rounded-xl md:shadow-md md:border-2 md:p-6">
          <div className="hidden md:flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-[#003B93]">Vacinas Cadastradas</h2>
            <Button asChild className="!bg-[#0D4F97] !hover:bg-[#0b427d] text-white">
              <Link href="/vaccines/new">Adicionar</Link>
            </Button>
          </div>

          {loading ? (
            <div className="flex justify-center items-center py-10">
              <Loader2 className="h-8 w-8 animate-spin text-[#003B93]" />
            </div>
          ) : (
            <div className="space-y-4">
              {filtered.map((vaccine) => (
                <VaccineListItem
                  key={vaccine.id}
                  vaccine={vaccine}
                  onEdit={() => router.push(`/vaccines/${vaccine.id}/edit`)}
                  onDelete={() => deleteVaccine(vaccine.id)}
                />
              ))}
            </div>
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