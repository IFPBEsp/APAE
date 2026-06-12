"use client";

import { FormEvent, useState } from "react";
import { useRouter } from "next/navigation";
import { Loader2, Plus } from "lucide-react";

import { SearchFilters } from "@/components/search-filters";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

import { useServiceTypes } from "../hooks/use-service-types";
import { ServiceTypeListItem } from "../components/service-type-list-item";
import type { ServiceType } from "../service-types.types";

export function ServiceTypesPage() {
  const router = useRouter();
  const [newArea, setNewArea] = useState("");
  const {
    createServiceType,
    deleteServiceType,
    error,
    filteredServiceTypes,
    isLoading,
    isSaving,
    searchName,
    setSearchName,
  } = useServiceTypes();

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const normalizedArea = newArea.trim();
    if (!normalizedArea) {
      return;
    }

    await createServiceType(normalizedArea);
    setNewArea("");
  }

  async function handleDelete(service: ServiceType) {
    const shouldDelete = window.confirm(`Deseja remover o tipo de atendimento \"${service.area}\"?`);

    if (!shouldDelete) {
      return;
    }

    await deleteServiceType(service.id);
  }

  function renderContent() {
    if (isLoading) {
      return (
        <div className="flex justify-center items-center p-10">
          <Loader2 className="h-8 w-8 animate-spin text-gray-500" />
        </div>
      );
    }

    if (error) {
      return <p className="text-center text-red-500">{error}</p>;
    }

    if (filteredServiceTypes.length === 0) {
      return <p className="text-center text-gray-500">Nenhum tipo de atendimento encontrado.</p>;
    }

    return (
      <div className="space-y-2">
        {filteredServiceTypes.map((service) => (
          <ServiceTypeListItem
            key={service.id}
            service={service}
            onEdit={() => router.push(`/service-types/${service.id}/edit`)}
            onDelete={handleDelete}
          />
        ))}
      </div>
    );
  }

  return (
    <div className="!bg-slate-100 min-h-screen">
      <main className="container mx-auto p-4 md:p-6">
        <div className="bg-white rounded-xl shadow-md border-2 p-6 mb-4 space-y-4">
          <SearchFilters searchName={searchName} setSearchName={setSearchName} />
          <form onSubmit={handleSubmit} className="flex flex-col gap-3 md:flex-row">
            <Input
              value={newArea}
              onChange={(event) => setNewArea(event.target.value)}
              placeholder="Novo tipo de atendimento"
            />
            <Button type="submit" disabled={isSaving} className="bg-[#0D4F97] hover:bg-[#0b427d] text-white">
              {isSaving ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : <Plus className="mr-2 h-4 w-4" />}
              Cadastrar
            </Button>
          </form>
        </div>

        <section className="relative md:bg-white md:rounded-xl md:shadow-md md:border-2 md:p-6">
          <div className="hidden md:flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-[#003B93]">Tipos de atendimento cadastrados</h2>
          </div>

          <div className="mb-4 md:hidden">
            <h2 className="text-xl font-bold text-[#003B93]">Tipos de atendimento cadastrados</h2>
          </div>

          <p className="text-sm text-gray-500 mb-4">
            {filteredServiceTypes.length} tipos de atendimento encontrados
          </p>
          {renderContent()}
        </section>
      </main>
    </div>
  );
}
