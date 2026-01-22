"use client";

import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardAction,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import DocumentCategoriesCard from "@/components/DocumentCategoriesCard";
import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useMemo, useState } from "react";
import { toast } from "react-toastify";
import { Loader2, ArrowLeft, SquarePen } from "lucide-react";

import { DialogType } from "./(dialogs)/dialog-types";
import { EditPersonalDialog } from "./(dialogs)/edit-personal-dialog";
import { EditDocumentationDialog } from "./(dialogs)/edit-documentation-dialog";
import { EditAddressDialog } from "./(dialogs)/edit-address-dialog";
import { EditGuardiansDialog } from "./(dialogs)/edit-guardians-dialog";

type InfoRowProps = Readonly<{
  label: string;
  value?: string | number | null | boolean;
}>;

function InfoRow({ label, value }: InfoRowProps) {
  const displayValue = useMemo(() => {
    if (value === null || value === undefined || value === "") {
      return "Não informado";
    }

    if (label === "Recebe BPC?") {
      return value === true || String(value).toLowerCase() === "true"
        ? "Sim"
        : "Não";
    }

    if (label === "Renda Familiar" && typeof value === "number") {
      return value.toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL",
      });
    }

    if (typeof value === "boolean") {
      return value ? "Sim" : "Não";
    }

    return value;
  }, [label, value]);

  return (
    <div className="mb-2">
      <span className="text-sm font-semibold text-gray-500">{label}</span>
      <p className="text-base text-black">{displayValue}</p>
    </div>
  );
}

type FieldsCardProps = Readonly<{
  title: string;
  rows: InfoRowProps[];
  onEdit?: () => void;
}>;

function FieldsCard({ title, rows, onEdit }: FieldsCardProps) {
  return (
    <Card className="w-full relative font-nunito">
      <CardHeader>
        <CardTitle className="text-[#0D4F97]">{title}</CardTitle>
        <CardAction>
          {onEdit && (
            <Button
              className="cursor-pointer"
              variant="ghost"
              size="icon"
              onClick={() => onEdit()}
            >
              <SquarePen color="#0D4F97" />
            </Button>
          )}
        </CardAction>
      </CardHeader>
      <CardContent className="grid grid-cols-1 md:grid-cols-2 gap-x-4">
        {rows.map(
          (row) =>
            row.label && (
              <InfoRow
                key={`${title.toLowerCase()}-${row.label.toLowerCase()}`}
                {...row}
              />
            ),
        )}
      </CardContent>
    </Card>
  );
}

export default function PersonDetailsPage() {
  const params = useParams();
  const router = useRouter();
  const id = params?.id as string;

  const [pessoa, setPessoa] = useState<any>(null);
  const [registroAnual, setRegistroAnual] = useState<any>(null);
  const [dialog, setDialog] = useState<DialogType | null>(null);

  const [selectedYear, setSelectedYear] = useState<string>(
    new Date().getFullYear().toString(),
  );

  const [loadingPessoa, setLoadingPessoa] = useState(true);
  const [loadingRegistro, setLoadingRegistro] = useState(false);

  const currentYearInt = new Date().getFullYear();
  const years = Array.from({ length: 11 }, (_, i) =>
    (currentYearInt - i).toString(),
  );

  useEffect(() => {
    if (!id) return;

    async function fetchPessoa() {
      try {
        setLoadingPessoa(true);
        const response = await fetch(`/api/pessoas/${id}`);

        if (!response.ok) throw new Error("Falha ao buscar dados do paciente.");

        const data = await response.json();
        setPessoa(data);
      } catch (err: any) {
        console.error(err);
        toast.error(err.message);
        router.push("/home");
      } finally {
        setLoadingPessoa(false);
      }
    }

    fetchPessoa();
  }, [id, router, dialog]);

  useEffect(() => {
    if (!id) return;

    async function fetchRegistro() {
      try {
        setLoadingRegistro(true);
        setRegistroAnual(null);

        const response = await fetch(
          `/api/pessoas/${id}/registro-anual/${selectedYear}`,
        );

        if (response.ok) {
          const data = await response.json();
          setRegistroAnual(data);
        } else {
          setRegistroAnual(null);
        }
      } catch (err) {
        console.error("Erro ao buscar registro anual:", err);
      } finally {
        setLoadingRegistro(false);
      }
    }

    fetchRegistro();
  }, [id, selectedYear]);

  if (loadingPessoa) {
    return (
      <div className="flex justify-center items-center h-screen">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    );
  }

  if (!pessoa) {
    return (
      <div className="text-center mt-10">
        <p>Paciente não encontrado.</p>
        <Button asChild variant="link">
          <Link href="/home">Voltar para a lista</Link>
        </Button>
      </div>
    );
  }

  return (
    <main className="container mx-auto p-4 md:p-6">
      <EditPersonalDialog
        open={dialog === DialogType.PERSONAL}
        member={pessoa}
        onOpenChange={(open) => setDialog(open ? DialogType.PERSONAL : null)}
      />
      <EditDocumentationDialog
        open={dialog === DialogType.DOCUMENTATION}
        member={pessoa}
        onOpenChange={(open) =>
          setDialog(open ? DialogType.DOCUMENTATION : null)
        }
      />
      <EditAddressDialog
        open={dialog === DialogType.ADDRESS}
        member={pessoa}
        onOpenChange={(open) => setDialog(open ? DialogType.ADDRESS : null)}
      />
      <EditGuardiansDialog
        open={dialog === DialogType.GUARDIANS}
        member={pessoa}
        onOpenChange={(open) => setDialog(open ? DialogType.GUARDIANS : null)}
      />

      <div className="mb-4">
        <Button
          variant="outline"
          onClick={() => router.push("/home")}
          className="gap-2"
        >
          <ArrowLeft className="h-4 w-4" />
          Voltar para listagem
        </Button>
      </div>

      <div className="flex flex-col items-center gap-y-4 w-full mb-6">
        <Avatar className="h-40 w-40 border">
          <AvatarImage
            src={pessoa?.urlFoto ?? "https://via.placeholder.com/150"}
            alt={pessoa?.fullName ?? "Foto do paciente"}
          />
          <AvatarFallback className="font-baloo font-bold text-[32px]">
            {pessoa?.fullName?.charAt(0) ?? "P"}
          </AvatarFallback>
        </Avatar>
        <h3 className="font-baloo font-bold text-[#0D4F97] text-[24px]">
          {pessoa?.fullName}
        </h3>
      </div>

      <DocumentCategoriesCard
        onClickCategoria={(tipo: string) => {
          router.push(`/pessoa/${id}/documentos/${tipo}`);
        }}
      />

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-6">
        {/* Card: Dados Pessoais */}
        <FieldsCard
          title="Dados Pessoais"
          onEdit={() => setDialog(DialogType.PERSONAL)}
          rows={[
            { label: "Nome Completo", value: pessoa.fullName },
            { label: "Data de Nasc.", value: pessoa.birthDate },
            { label: "Naturalidade", value: pessoa.birthplace },
            { label: "Contato", value: pessoa.contact },
            { label: "Alergias", value: pessoa.allergies },
            { label: "Estudante?", value: pessoa.isStudent },
            { label: "Data de Cadastro", value: pessoa.registrationDate },
          ]}
        />

        {/* Card: Documentação */}
        <FieldsCard
          title="Documentação"
          onEdit={() => setDialog(DialogType.DOCUMENTATION)}
          rows={[
            { label: "CPF", value: pessoa.cpf },
            { label: "RG", value: pessoa.rg },
            { label: "Orgão Emissor", value: pessoa.issuingAgency },
            { label: "Data de Emissão", value: pessoa.issueDate },
            { label: "CNS", value: pessoa.cns },
            { label: "NIS", value: pessoa.nis },
            {
              label: "Nº Cert. Nasc.",
              value: pessoa.birthCertificateNumber,
            },
            { label: "Cartório", value: pessoa.registryOffice },
            { label: "Livro", value: pessoa.book },
            { label: "Folha", value: pessoa.fls },
          ]}
        />

        {/* Card: Endereço */}
        <FieldsCard
          title="Endereço Residencial"
          onEdit={() => setDialog(DialogType.ADDRESS)}
          rows={[
            { label: "Rua", value: pessoa.address?.street },
            { label: "Número", value: pessoa.address?.number },
            { label: "Bairro", value: pessoa.address?.neighborhood },
            { label: "Cidade", value: pessoa.address?.city },
            { label: "Estado", value: pessoa.address?.state },
            { label: "CEP", value: pessoa.address?.cep },
            { label: "Complemento", value: pessoa.address?.complement },
          ]}
        />

        {/* Card: Responsáveis */}
        <FieldsCard
          title="Responsáveis"
          onEdit={() => setDialog(DialogType.GUARDIANS)}
          rows={[
            ...(pessoa.guardian
              ? [
                  {
                    label: "Nome do Responsável",
                    value: pessoa.guardian.name,
                  },
                  {
                    label: "Parentesco do Responsável",
                    value: pessoa.guardian.kinship,
                  },
                  {
                    label: "Contato do Responsável",
                    value: pessoa.guardian.contact,
                  },
                  {
                    label: "Endereço do Responsável",
                    value: `${pessoa.guardian.address?.street ?? ""}, ${
                      pessoa.guardian.address?.number ?? ""
                    }`,
                  },
                ]
              : []),
            ...pessoa.parents.flatMap((parent: any) => {
              return [
                {
                  label: `Nome do parente (${parent.kinship})`,
                  value: parent.name,
                },
                {
                  label: `CPF do parente (${parent.kinship})`,
                  value: parent.cpf,
                },
                {
                  label: `Profissão do parente (${parent.kinship})`,
                  value: parent.profession,
                },
                {
                  label: `Parente vivo? (${parent.kinship})`,
                  value: parent.isAlive,
                },
              ];
            }),
          ]}
        />

        {/* --- CARD DE SAÚDE --- */}
        <Card className="w-full relative font-nunito">
          <CardHeader className="flex flex-row items-center justify-between">
            <CardTitle className="text-[#0D4F97]">
              Informações de Saúde
            </CardTitle>
            <div className="flex items-center gap-2">
              <label
                htmlFor="year-select"
                className="text-sm font-semibold text-gray-600"
              >
                Ano:
              </label>
              <select
                id="year-select"
                value={selectedYear}
                onChange={(e) => setSelectedYear(e.target.value)}
                className="border border-gray-300 rounded-md px-2 py-1 text-sm focus:outline-none focus:ring-2 focus:ring-[#0D4F97]"
              >
                {years.map((year) => (
                  <option key={year} value={year}>
                    {year}
                  </option>
                ))}
              </select>
            </div>
          </CardHeader>

          <CardContent>
            <InfoRow label="Alergias" value={pessoa.allergies} />
            <InfoRow
              label="Vacinas"
              value={pessoa.vaccineNames?.map((v: any) => v.name).join(", ")}
            />

            <h3 className="font-bold text-base mt-4 pt-4 border-t text-[#0D4F97]">
              Registro Anual ({selectedYear})
            </h3>

            {loadingRegistro ? (
              <div className="py-4 flex justify-center">
                <Loader2 className="h-6 w-6 animate-spin text-[#0D4F97]" />
              </div>
            ) : registroAnual ? (
              <div className="mt-2 animate-in fade-in slide-in-from-bottom-2">
                <InfoRow label="Recebe BPC?" value={registroAnual.bpc} />
                <InfoRow
                  label="Renda Familiar"
                  value={registroAnual.familyIncome}
                />
                <InfoRow label="Doenças" value={registroAnual.diseases} />
                <InfoRow
                  label="Medicamentos Contínuos"
                  value={registroAnual.continuousMedication}
                />
                <InfoRow
                  label="Transtornos"
                  value={registroAnual.disorders
                    ?.map((d: any) => d.name)
                    .join(", ")}
                />
              </div>
            ) : (
              <p className="text-sm text-gray-500 mt-2 italic">
                Nenhum registro anual encontrado para o ano de {selectedYear}.
              </p>
            )}
          </CardContent>
        </Card>
      </div>
    </main>
  );
}
