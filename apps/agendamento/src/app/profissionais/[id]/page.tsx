"use client";

import { use } from "react";
import { useGetByIdProfissional } from "@/hooks/profissional/use-get-by-id-profissional";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Pencil } from "lucide-react";
import { useRouter } from "next/navigation";

interface PageProps {
  params: Promise<{ id: string }>;
}

export default function VisualizarProfissional({ params }: PageProps) {
  const router = useRouter();

  const { id } = use(params);

  const { profissional, loading, error } = useGetByIdProfissional();

  if (loading) return <p className="p-6">Carregando detalhes do profissional...</p>;
  if (error) return <p className="p-6 text-red-500">Erro ao carregar os dados.</p>;
  if (!profissional) return <p className="p-6">Profissional não encontrado.</p>;

  const dados = {
    ...profissional,
    address: profissional.address || {
      street: "—",
      number: "—",
      neighborhood: "—",
      city: "—",
      state: "—",
      cep: "—",
      complement: "—",
    },
  };

  const handleEdit = () => {
    router.push(`/update-profissional/${id}`);
  };

  return (
    <div className="w-full max-w-5xl mx-auto p-4 sm:p-6 md:p-10">
      <header className="flex flex-col sm:flex-row items-center sm:justify-between mb-8 gap-4">
        <h1 className="text-2xl font-semibold text-foreground">Detalhes do profissional</h1>
        <Button
          variant="outline"
          className="text-[#0D4F97] border-[#0D4F97] hover:bg-slate-100"
          onClick={handleEdit}
        >
          <Pencil className="mr-2 h-4 w-4" />
          Editar
        </Button>
      </header>

      <div className="flex flex-col items-center sm:flex-row sm:items-center gap-4 sm:gap-6 mb-10">
        <Avatar className="h-24 w-24 border-2 border-[#0D4F97]">
          <AvatarImage src="" alt={dados.name} />
          <AvatarFallback className="text-xl font-bold bg-[#B2D7EC] text-[#0D4F97]">
            {dados.name?.charAt(0) || "P"}
          </AvatarFallback>
        </Avatar>
        <div className="text-center sm:text-left">
          <h2 className="text-xl font-bold text-[#0D4F97]">{dados.name}</h2>
          <p className="text-base text-gray-600">{dados.serviceArea.area}</p>
        </div>
      </div>

      <div className="grid grid-cols-1 gap-6 md:grid-cols-2">

        <Card className="shadow-lg border-2 border-[#E0E7FF] text-[#0D4F97]">
          <CardHeader>
            <CardTitle className="text-lg font-semibold">Informação de perfil</CardTitle>
          </CardHeader>
          <CardContent className="space-y-2">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-2 sm:gap-4">
              <div className="col-span-1 sm:col-span-2">
                <p className="font-semibold text-sm">Documento profissional</p>
                <p className="text-base text-gray-700">{dados.professionalDocument || "—"}</p>
              </div>
              <div>
                <p className="font-semibold text-sm">Email</p>
                <p className="text-base text-gray-700">{dados.email || "—"}</p>
              </div>
              <div>
                <p className="font-semibold text-sm">Telefone</p>
                <p className="text-base text-gray-700">{dados.phoneNumber || "—"}</p>
              </div>
              <div>
                <p className="font-semibold text-sm">Identidade</p>
                <p className="text-base text-gray-700">{dados.identityDocument || "—"}</p>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card className="shadow-lg border-2 border-[#E0E7FF] text-[#0D4F97]">
          <CardHeader>
            <CardTitle className="text-lg font-semibold">Informação de endereço</CardTitle>
          </CardHeader>
          <CardContent className="space-y-2">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-2 sm:gap-4">
              <div className="col-span-1 sm:col-span-2">
                <p className="font-semibold text-sm">Endereço</p>
                <p className="text-base text-gray-700">
                  {`${dados.address.street}, ${dados.address.number} - ${dados.address.neighborhood}`}
                </p>
              </div>

              <div className="col-span-1 sm:col-span-2">
                <p className="font-semibold text-sm">Complemento</p>
                <p className="text-base text-gray-700">{dados.address.complement || "—"}</p>
              </div>

              <div>
                <p className="font-semibold text-sm">Cidade</p>
                <p className="text-base text-gray-700">{dados.address.city}</p>
              </div>

              <div>
                <p className="font-semibold text-sm">Estado</p>
                <p className="text-base text-gray-700">{dados.address.state}</p>
              </div>

              <div>
                <p className="font-semibold text-sm">CEP</p>
                <p className="text-base text-gray-700">{dados.address.cep}</p>
              </div>
            </div>
          </CardContent>
        </Card>

      </div>
    </div>
  );
}
