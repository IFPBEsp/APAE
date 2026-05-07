"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import { ServiceTypeListItemItem } from "./service-type-item";
import { ServiceType } from "@/schemas/service-type-schemas";
import { Loader2 } from "lucide-react";
import { SearchFilters } from "@/components/search-filters";

export default function ServiceTypesPage() {
  const router = useRouter();
  const [serviceTypes, setServiceTypes] = useState<ServiceType[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  
  const [searchName, setSearchName] = useState<string>("");

  useEffect(() => {
    async function fetchserviceTypes() {
      try {
        setIsLoading(true);
        setError(null);
        const response = await fetch("/api/service-types");
        if (!response.ok) {
          throw new Error("Falha ao buscar os tipos de atendimentos.");
        }
        const data = await response.json();
        setServiceTypes(data);
      } catch (err: any) {
        setError(err.message);
        toast.error(err.message);
      } finally {
        setIsLoading(false);
      }
    }
    fetchserviceTypes();
  }, []);

  const filteredserviceTypes = serviceTypes.filter((serviceType) =>
    serviceType.area.toLowerCase().includes(searchName.toLowerCase())
  );

  const renderContent = () => {
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
    if (filteredserviceTypes.length === 0) {
      return <p className="text-center text-gray-500">Nenhum tipo de atendimento encontrado.</p>;
    }
    return (
      <div className="space-y-2">
        {filteredserviceTypes.map((service) => (
          <ServiceTypeListItemItem
            key={service.id}
            service={service}
            onEdit={() => router.push(`/tipo-atendimento/${service.id}/edit`)}
          />
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
          />
        </div>

        <section className="relative md:bg-white md:rounded-xl md:shadow-md md:border-2 md:p-6">  
          <div className="hidden md:flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-[#003B93]">
              Tipos de atendimentos cadastrados
            </h2>
          </div>

          <div className="mb-4 md:hidden">
            <h2 className="text-xl font-bold text-[#003B93]">
              Tipos de atendimentos cadastrados
            </h2>
          </div>
          
          <p className="text-sm text-gray-500 mb-4">
            {filteredserviceTypes.length} tipos de atendimentos encontrados
          </p>
          {renderContent()}
        </section>

      </main>
    </div>
  );
}