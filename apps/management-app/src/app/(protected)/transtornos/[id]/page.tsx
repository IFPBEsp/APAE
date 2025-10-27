import { createBaseApi } from "@/lib/axios";
import { Transtorno } from "@/schemas/transtornosSchema";
import { notFound, redirect } from "next/navigation";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { ArrowLeft } from "lucide-react";

async function getTranstorno(id: string): Promise<Transtorno | null> {
  try {
    const api = await createBaseApi(); 
    const { data } = await api.get(`/transtornos/${id}`);
    return data;
  } catch (error: any) {
    if (error.response?.status === 404) {
      notFound(); 
    }
    console.error("Falha ao buscar transtorno:", error.message);
    return null;
  }
}

export default async function ViewTranstornoPage({
  params,
}: {
  params: { id: string };
}) {
  const transtorno = await getTranstorno(params.id);

  if (!transtorno) {
    redirect("/transtornos");
  }

  return (
    <div className="!bg-slate-100 min-h-screen">
      <main className="container mx-auto p-4 md:p-6">
        <div className="bg-white rounded-xl shadow-md border-2 p-6 mb-4">
          <Button 
            asChild 
            variant="ghost" 
            className="mb-4 text-sm text-gray-700 hover:bg-gray-100"
          >
            <Link href="/transtornos">
              <ArrowLeft className="h-4 w-4 mr-2" />
              Voltar para a lista
            </Link>
          </Button>
          
          <div className="space-y-4">
            <div>
              <h2 className="text-sm font-medium text-gray-500">Nome</h2>
              <p className="text-2xl font-bold text-gray-900">{transtorno.name}</p>
            </div>
            
            <div>
              <h2 className="text-sm font-medium text-gray-500">ID</h2>
              <p className="text-base text-gray-700">{transtorno.id}</p>
            </div>
            
            <div className="pt-4">
              <Button asChild>
                <Link href={`/transtornos/${transtorno.id}/edit`}>
                  Editar
                </Link>
              </Button>
            </div>
          </div>
          
        </div>
      </main>
    </div>
  );
}