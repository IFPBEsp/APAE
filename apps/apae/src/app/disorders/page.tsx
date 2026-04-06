"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { toast } from "react-toastify";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { TranstornoListItem } from "./TranstornosListItem";
import { Transtorno } from "@/schemas/transtornosSchema";
import { Loader2 } from "lucide-react";
import { SearchFilters } from "@/components/search-filters";

export default function TranstornosPage() {
  const router = useRouter();
  const [transtornos, setTranstornos] = useState<Transtorno[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  
  const [searchName, setSearchName] = useState<string>("");

  useEffect(() => {
    async function fetchTranstornos() {
      try {
        setIsLoading(true);
        setError(null);
        const response = await fetch("/api/transtornos");
        if (!response.ok) {
          throw new Error("Falha ao buscar transtornos.");
        }
        const data = await response.json();
        setTranstornos(data);
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : "Erro ao buscar transtornos";
        setError(msg);
        toast.error(msg);
      } finally {
        setIsLoading(false);
      }
    }
    fetchTranstornos();
  }, []);

  const handleDelete = async (id: string) => {
    
    try {
      const response = await fetch(`/api/transtornos/${id}`, {
        method: "DELETE",
      });
      
      if (!response.ok) {
        const errorMessage = await response.text();
        
        throw new Error(errorMessage || "Falha ao excluir o transtorno.");
      }
      
      setTranstornos((current) => current.filter((d) => d.id !== id));
      toast.success("Transtorno excluído com sucesso!");
    } catch (err: unknown) {
      toast.error(err instanceof Error ? err.message : "Erro ao excluir transtorno");
    }
  };
  const filteredTranstornos = transtornos.filter((transtorno) =>
    transtorno.name.toLowerCase().includes(searchName.toLowerCase())
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
    if (filteredTranstornos.length === 0) {
      return <p className="text-center text-gray-500">Nenhum transtorno encontrado.</p>;
    }
    return (
      <div className="space-y-2">
        {filteredTranstornos.map((transtorno) => (
          <TranstornoListItem
            key={transtorno.id}
            transtorno={transtorno}
            onEdit={() => router.push(`/disorders/${transtorno.id}/edit`)}
            onDelete={() => handleDelete(transtorno.id)}
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
              Transtornos Cadastrados
            </h2>
            <Button
              asChild
              className="!bg-[#0D4F97] !hover:bg-[#0b427d] text-white"
            >
              <Link href="/disorders/new">Adicionar</Link>
            </Button>
          </div>

          {/* para mobile */}
          <div className="mb-4 md:hidden">
            <h2 className="text-xl font-bold text-[#003B93]">
              Transtornos Cadastrados
            </h2>
          </div>
          
          <p className="text-sm text-gray-500 mb-4">
            {filteredTranstornos.length} transtornos encontrados
          </p>
          {renderContent()}
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