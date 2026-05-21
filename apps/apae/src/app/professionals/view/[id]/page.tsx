"use client";

import { useMemo } from "react";
import { useParams, useRouter } from "next/navigation";

import { useGetByIdProfessional } from "@/hooks/profissional/use-get-by-id-profissional";
import { useProfessionalDocuments } from "@/hooks/profissional/use-professional-documents";

import { Checkbox } from "@/components/ui/checkbox";
import { generateAvailabilityMatrix } from "@/domains/professional/shared/disponibilidade.utils";

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Pencil } from "lucide-react";

export default function VisualizarProfessional() {
  const router = useRouter();
  const params = useParams<{ id: string }>();
  const id = params?.id;

  const { professional, loading, error } = useGetByIdProfessional();

  const { documents, loading: loadingDocs, error: errorDocs } =
    useProfessionalDocuments(id);

  const disponibilidadeMatrix = useMemo(() => {
    const avs = professional?.availabilities ?? [];

    return generateAvailabilityMatrix(
      avs.map((a) => ({
        dia: a.day?.toLowerCase(),
        turno: a.shift?.toLowerCase(),
        checked: true,
      })),
    );
  }, [professional?.availabilities]);

  if (!id) return <p className="p-6">ID inválido.</p>;
  if (loading)
    return <p className="p-6">Carregando detalhes do profissional...</p>;
  if (error)
    return <p className="p-6 text-red-500">Erro ao carregar os dados.</p>;
  if (!professional) return <p className="p-6">Profissional não encontrado.</p>;

  const dados = {
    ...professional,
    address: professional.address || {
      street: "—",
      number: "—",
      neighborhood: "—",
      city: "—",
      state: "—",
      cep: "—",
      complement: "—",
    },
  };

  const handleEdit = () => router.push(`/professionals/edit/${id}`);

  const DAYS = ["segunda", "terca", "quarta", "quinta", "sexta"] as const;
  const SHIFTS = ["manha", "tarde"] as const;

  const DAY_LABEL: Record<(typeof DAYS)[number], string> = {
    segunda: "Segunda",
    terca: "Terça",
    quarta: "Quarta",
    quinta: "Quinta",
    sexta: "Sexta",
  };

  const SHIFT_LABEL: Record<(typeof SHIFTS)[number], string> = {
    manha: "Manhã",
    tarde: "Tarde",
  };

  return (
    <div className="w-full max-w-5xl mx-auto p-4 sm:p-6 md:p-10">
      <header className="flex flex-col sm:flex-row items-center sm:justify-between mb-8 gap-4">
        <h1 className="text-2xl font-semibold text-foreground">
          Detalhes do profissional
        </h1>
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
          <p className="text-base text-gray-600">{dados.serviceArea?.area}</p>
        </div>
      </div>

      <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
        <Card className="shadow-lg border-2 border-[#E0E7FF] text-[#0D4F97]">
          <CardHeader>
            <CardTitle className="text-lg font-semibold">
              Informação de perfil
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-2">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-2 sm:gap-4">
              <div className="col-span-1 sm:col-span-2">
                <p className="font-semibold text-sm">Documento profissional</p>
                <p className="text-base text-gray-700">
                  {dados.professionalDocument || "—"}
                </p>
              </div>
              <div>
                <p className="font-semibold text-sm">Email</p>
                <p className="text-base text-gray-700">{dados.email || "—"}</p>
              </div>
              <div>
                <p className="font-semibold text-sm">Telefone</p>
                <p className="text-base text-gray-700">
                  {dados.phoneNumber || "—"}
                </p>
              </div>
              <div>
                <p className="font-semibold text-sm">RG</p>
                <p className="text-base text-gray-700">
                  {dados.identityDocument || "—"}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card className="shadow-lg border-2 border-[#E0E7FF] text-[#0D4F97]">
          <CardHeader>
            <CardTitle className="text-lg font-semibold">
              Informação de endereço
            </CardTitle>
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
                <p className="text-base text-gray-700">
                  {dados.address.complement || "—"}
                </p>
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

        <Card className="shadow-lg border-2 border-[#E0E7FF] text-[#0D4F97] md:col-span-2">
          <CardHeader>
            <CardTitle className="text-lg font-semibold">
              Disponibilidade
            </CardTitle>
          </CardHeader>

          <CardContent className="space-y-3">
            {!disponibilidadeMatrix || disponibilidadeMatrix.length === 0 ? (
              <p className="text-gray-700">—</p>
            ) : (
              <div className="overflow-x-auto rounded-md border">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="bg-slate-50">
                      <th className="p-3 text-left font-semibold text-[#0D4F97]">
                        Turno
                      </th>
                      {DAYS.map((day) => (
                        <th
                          key={day}
                          className="p-3 text-center font-semibold text-[#0D4F97]"
                        >
                          {DAY_LABEL[day]}
                        </th>
                      ))}
                    </tr>
                  </thead>

                  <tbody>
                    {SHIFTS.map((shift) => (
                      <tr key={shift} className="border-t">
                        <td className="p-3 font-medium text-gray-700">
                          {SHIFT_LABEL[shift]}
                        </td>

                        {DAYS.map((day) => {
                          const cell = disponibilidadeMatrix.find(
                            (d) => d?.dia === day && d?.turno === shift,
                          );

                          return (
                            <td
                              key={`${day}-${shift}`}
                              className="p-3 text-center"
                            >
                              <Checkbox checked={Boolean(cell?.checked)} disabled />
                            </td>
                          );
                        })}
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardContent>
        </Card>
        <Card className="shadow-lg border-2 border-[#E0E7FF] text-[#0D4F97] md:col-span-2">
          <CardHeader>
            <CardTitle className="text-lg font-semibold">
              Documentos anexados
            </CardTitle>
          </CardHeader>

          <CardContent className="space-y-3">
            {loadingDocs ? (
              <p className="text-gray-700">Carregando documentos...</p>
            ) : errorDocs ? (
              <p className="text-red-500">{errorDocs}</p>
            ) : !documents || documents.length === 0 ? (
              <p className="text-gray-700">Nenhum documento anexado.</p>
            ) : (
              <ul className="space-y-2">
                {documents.map((doc) => (
                  <li
                    key={doc.id}
                    className="flex items-center justify-between rounded-md border p-3"
                  >
                    <div>
                      <p className="font-medium text-sm">{doc.name}</p>
                      <p className="text-xs text-gray-500">
                        {doc.type} • {doc.year}
                      </p>
                    </div>

                    <a
                      href={doc.url}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-sm font-medium text-[#0D4F97] hover:underline"
                    >
                      Visualizar
                    </a>
                  </li>
                ))}
              </ul>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
