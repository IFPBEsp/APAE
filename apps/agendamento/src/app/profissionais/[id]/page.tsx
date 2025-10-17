"use client";

import { useGetByIdProfissional } from "@/hooks/profissional/use-get-by-id-profissional";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Pencil, Check, X, CircleCheck, CircleX } from "lucide-react";
import { useRouter } from "next/navigation";

interface ProfissionalExibicao {
  id: string;
  nome: string;
  areaDaSaude: string;
  docProfissional: string;
  email: string;
  telefone: string;
  rg: string;
  cpf: string;
  ativo: boolean;
  endereco: {
    estado: string;
    cidade: string;
    bairro: string;
    rua: string;
    numero: string;
    cep: string;
    complemento: string;
  };
  disponibilidade: {
    dia: string;
    manha: boolean;
    tarde: boolean;
  }[];
}

interface PageProps {
  params: {
    id: string;
  };
}

export default function VisualizarProfissional({ params }: PageProps) {
  const router = useRouter();
  const { profissional, loading, error } = useGetByIdProfissional(params.id);

  if (loading) return <p className="p-6">Carregando detalhes do profissional...</p>;
  if (error) return <p className="p-6 text-red-500">Erro ao carregar os dados.</p>;
  if (!profissional) return <p className="p-6">Profissional não encontrado.</p>;

  const dadosExibicao: ProfissionalExibicao = {
    ...profissional,
    cpf: profissional.cpf || "—",
    rg: profissional.rg || "—",
    ativo: profissional.ativo,
    docProfissional: profissional.docProfissional || "—",
    disponibilidade: profissional.disponibilidade || [],
    endereco: profissional.endereco || {
      rua: "—",
      numero: "—",
      bairro: "—",
      cidade: "—",
      estado: "—",
      cep: "—",
      complemento: "—",
    },
  };

  const handleEdit = () => {
    router.push(`/update-profissional/${params.id}`);
  };

  return (
    <div className="w-full max-w-5xl mx-auto p-4 sm:p-6 md:p-10">
      {/* Header */}
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

      {/* Avatar, nome e status */}
      <div className="flex flex-col items-center sm:flex-row sm:items-center gap-4 sm:gap-6 mb-10">
        <Avatar className="h-24 w-24 border-2 border-[#0D4F97]">
          <AvatarImage src={""} alt={dadosExibicao.nome} />
          <AvatarFallback className="text-xl font-bold bg-[#B2D7EC] text-[#0D4F97]">
            {dadosExibicao.nome?.charAt(0) || "P"}
          </AvatarFallback>
        </Avatar>
        <div className="text-center sm:text-left">
          <h2 className="text-xl font-bold text-[#0D4F97]">{dadosExibicao.nome}</h2>
          <p className="text-base text-gray-600">{dadosExibicao.areaDaSaude}</p>
        </div>
        <div className="flex items-center gap-2 mt-2 sm:mt-0">
          {dadosExibicao.ativo ? (
            <div className="flex items-center gap-2 text-[#008000]">
              <CircleCheck className="h-5 w-5" />
              <span className="font-medium">Ativo</span>
            </div>
          ) : (
            <div className="flex items-center gap-2 text-[#B30000]">
              <CircleX className="h-5 w-5" />
              <span className="font-medium">Inativo</span>
            </div>
          )}
        </div>
      </div>
      
      {/* Grid de cards responsivo */}
      <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
        {/* Informação de perfil */}
        <Card className="shadow-lg border-2 border-[#E0E7FF] text-[#0D4F97]">
          <CardHeader>
            <CardTitle className="text-lg font-semibold">Informação de perfil</CardTitle>
          </CardHeader>
          <CardContent className="space-y-2">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-2 sm:gap-4">
              <div className="col-span-1 sm:col-span-2">
                <p className="font-semibold text-sm">Documento profissional</p>
                <p className="text-base text-gray-700">{dadosExibicao.docProfissional}</p>
              </div>
              <div>
                <p className="font-semibold text-sm">Email</p>
                <p className="text-base text-gray-700">{dadosExibicao.email || "—"}</p>
              </div>
              <div>
                <p className="font-semibold text-sm">Telefone</p>
                <p className="text-base text-gray-700">{dadosExibicao.telefone || "—"}</p>
              </div>
              <div>
                <p className="font-semibold text-sm">CPF</p>
                <p className="text-base text-gray-700">{dadosExibicao.cpf}</p>
              </div>
              <div>
                <p className="font-semibold text-sm">RG</p>
                <p className="text-base text-gray-700">{dadosExibicao.rg}</p>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Informação de endereço */}
        <Card className="shadow-lg border-2 border-[#E0E7FF] text-[#0D4F97]">
          <CardHeader>
            <CardTitle className="text-lg font-semibold">Informação de endereço</CardTitle>
          </CardHeader>
          <CardContent className="space-y-2">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-2 sm:gap-4">
              <div className="col-span-1 sm:col-span-2">
                <p className="font-semibold text-sm">Endereço</p>
                <p className="text-base text-gray-700">
                  {`${dadosExibicao.endereco.rua}, ${dadosExibicao.endereco.numero} - ${dadosExibicao.endereco.bairro}`}
                </p>
              </div>
              <div className="col-span-1 sm:col-span-2">
                <p className="font-semibold text-sm">Complemento</p>
                <p className="text-base text-gray-700">{dadosExibicao.endereco.complemento || "—"}</p>
              </div>
              <div>
                <p className="font-semibold text-sm">Cidade</p>
                <p className="text-base text-gray-700">{dadosExibicao.endereco.cidade}</p>
              </div>
              <div>
                <p className="font-semibold text-sm">Estado</p>
                <p className="text-base text-gray-700">{dadosExibicao.endereco.estado}</p>
              </div>
              <div>
                <p className="font-semibold text-sm">CEP</p>
                <p className="text-base text-gray-700">{dadosExibicao.endereco.cep}</p>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Informação de disponibilidade */}
        <Card className="shadow-lg border-2 border-[#E0E7FF] md:col-span-2 text-[#0D4F97]">
          <CardHeader>
            <CardTitle className="text-lg font-semibold">Informação de disponibilidade</CardTitle>
          </CardHeader>
          <CardContent>
            {dadosExibicao.disponibilidade.length > 0 ? (
              <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200 text-sm">
                  <thead>
                    <tr>
                      <th className="px-2 sm:px-4 py-2 text-left font-medium text-gray-500 uppercase tracking-wider">
                        Período
                      </th>
                      {dadosExibicao.disponibilidade.map((d) => (
                        <th
                          key={d.dia}
                          className="px-2 sm:px-4 py-2 text-center font-medium text-gray-500 uppercase tracking-wider"
                        >
                          {d.dia}
                        </th>
                      ))}
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200">
                    <tr>
                      <td className="px-2 sm:px-4 py-2 font-medium text-gray-700">Manhã</td>
                      {dadosExibicao.disponibilidade.map((d, i) => (
                        <td key={i} className="px-2 sm:px-4 py-2 text-center">
                          {d.manha ? (
                            <Check className="h-4 w-4 text-green-600 inline-block" />
                          ) : (
                            <X className="h-4 w-4 text-red-600 inline-block" />
                          )}
                        </td>
                      ))}
                    </tr>
                    <tr>
                      <td className="px-2 sm:px-4 py-2 font-medium text-gray-700">Tarde</td>
                      {dadosExibicao.disponibilidade.map((d, i) => (
                        <td key={i} className="px-2 sm:px-4 py-2 text-center">
                          {d.tarde ? (
                            <Check className="h-4 w-4 text-green-600 inline-block" />
                          ) : (
                            <X className="h-4 w-4 text-red-600 inline-block" />
                          )}
                        </td>
                      ))}
                    </tr>
                  </tbody>
                </table>
              </div>
            ) : (
              <p className="text-gray-500 text-sm text-center py-4">Sem dados de disponibilidade</p>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
