"use client";

import { useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { SearchFilters } from "@/components/search-filters";
import { Loader2, Plus } from "lucide-react";
import { DisorderListItem } from "../shared/disorder-list-item";
import { useDisordersList } from "./use-disorders-list";

export function DisordersList() {
  const [searchName, setSearchName] = useState("");
  const { disorders, loading, deleteDisorder } = useDisordersList();
  const router = useRouter();

  const filtered = disorders.filter((d) =>
    d.name.toLowerCase().includes(searchName.toLowerCase()),
  );

  return (
    <div className="!bg-slate-100 min-h-screen">
      <main className="container mx-auto p-4 md:p-6">
        <div className="bg-white rounded-xl shadow-md border-2 p-6 mb-4">
          <SearchFilters searchName={searchName} setSearchName={setSearchName} />
        </div>

        <section className="relative md:bg-white md:rounded-xl md:shadow-md md:border-2 md:p-6">
          <div className="hidden md:flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-[#003B93]">Transtornos Cadastrados</h2>
            <Button asChild className="!bg-[#0D4F97] !hover:bg-[#0b427d] text-white">
              <Link href="/disorders/new">Adicionar</Link>
            </Button>
          </div>

          <div className="mb-4 md:hidden">
            <h2 className="text-xl font-bold text-[#003B93]">Transtornos Cadastrados</h2>
          </div>

          {loading ? (
            <div className="flex justify-center items-center p-10">
              <Loader2 className="h-8 w-8 animate-spin text-gray-500" />
            </div>
          ) : filtered.length === 0 ? (
            <p className="text-center text-gray-500 p-10">
              {searchName ? `Nenhum transtorno encontrado para "${searchName}".` : "Nenhum transtorno cadastrado."}
            </p>
          ) : (
            <>
              <p className="text-sm text-gray-500 mb-4">{filtered.length} transtornos encontrados</p>
              <div className="space-y-2">
                {filtered.map((disorder) => (
                  <DisorderListItem
                    key={disorder.id}
                    disorder={disorder}
                    onEdit={() => router.push(`/disorders/${disorder.id}/edit`)}
                    onDelete={() => deleteDisorder(disorder.id)}
                  />
                ))}
              </div>
            </>
          )}
        </section>
      </main>

      <Button
        asChild
        className="fixed bottom-6 right-6 h-[53px] w-[53px] rounded-full shadow-lg md:hidden bg-[#0D4F97] hover:bg-[#0b427d]"
      >
        <Link href="/disorders/new">
          <Plus className="h-7 w-7" />
          <span className="sr-only">Adicionar Transtorno</span>
        </Link>
      </Button>
    </div>
  );
}
