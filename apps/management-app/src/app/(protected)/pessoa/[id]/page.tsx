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
import { useCallback, useEffect, useMemo, useState } from "react";
import { toast } from "react-toastify";
import { Loader2, ArrowLeft, SquarePen, Plus } from "lucide-react";

import AnnualRegistryEditModal from "@/components/AnnualRegistryEditModal";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";

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

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState<"create" | "edit">("edit");

  const [pessoa, setPessoa] = useState<any>(null);
  const [registroAnual, setRegistroAnual] = useState<any>(null);
  const [dialog, setDialog] = useState<DialogType | null>(null);

  const [existingYears, setExistingYears] = useState<string[]>([]);
  const [selectedYear, setSelectedYear] = useState<string>(
    new Date().getFullYear().toString(),
  );

  const [loadingPessoa, setLoadingPessoa] = useState(true);
  const [loadingRegistro, setLoadingRegistro] = useState(false);

  const fetchPessoa = useCallback(async () => {
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
  }, [id, router]);

  const fetchYears = useCallback(async () => {
    try {
      const res = await fetch(`/api/pessoas/${id}/registro-anual/years-list`);
      let yearsData: number[] = [];

      if (res.ok) {
        yearsData = await res.json();
      }

      const currentYearStr = new Date().getFullYear().toString();

      if (yearsData.length > 0) {
        const sortedYears = yearsData
          .map((y) => y.toString())
          .sort((a, b) => parseInt(b) - parseInt(a));
        setExistingYears(sortedYears);

        if (!sortedYears.includes(selectedYear)) {
          setSelectedYear(sortedYears[0]);
        }
      } else {
        setExistingYears([currentYearStr]);
        setSelectedYear(currentYearStr);
      }
    } catch (error) {
      console.error("Erro ao buscar anos:", error);
    }
  }, [id, selectedYear]);

  const fetchRegistro = useCallback(async () => {
    try {
      setLoadingRegistro(true);
      setRegistroAnual(null);
      const response = await fetch(
        `/api/pessoas/${id}/registro-anual/${selectedYear}`,
      );
      if (response.ok) {
        const data = await response.json();
        setRegistroAnual(data);
      }
    } catch (err) {
      console.error("Erro ao buscar registro anual:", err);
    } finally {
      setLoadingRegistro(false);
    }
  }, [id, selectedYear]);

  useEffect(() => {
    if (id) {
      fetchPessoa();
      fetchYears();
    }
  }, [id, fetchPessoa, fetchYears]);

  useEffect(() => {
    if (id && selectedYear) {
      fetchRegistro();
    }
  }, [id, selectedYear, fetchRegistro]);

  const handleEditClick = () => {
    setModalMode("edit");
    setIsModalOpen(true);
  };

  const handleCreateClick = () => {
    setModalMode("create");
    setIsModalOpen(true);
  };

  const handleModalClose = (savedYear?: string) => {
    setIsModalOpen(false);
    if (savedYear) {
      fetchYears().then(() => setSelectedYear(savedYear));
    } else {
      fetchYears();
      fetchRegistro();
      fetchPessoa();
    }
  };

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
          <Link href="/home">Voltar</Link>
        </Button>
      </div>
    );
  }

  const hasRegistro = !!registroAnual;

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
          <ArrowLeft className="h-4 w-4" /> Voltar para listagem
        </Button>
      </div>

      <div className="flex flex-col items-center gap-y-4 w-full mb-6">
        <Avatar className="h-40 w-40 border">
          <AvatarImage
            src={pessoa?.photoUrl}
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

        {/* --- CARD DE SAÚDE DINÂMICO --- */}
        <Card className="w-full relative font-nunito">
          <CardHeader className="flex flex-row items-center justify-between">
            <CardTitle className="text-[#0D4F97]">
              Informações de Saúde
            </CardTitle>
            <div className="flex items-center gap-2">
              <TooltipProvider>
                <Tooltip>
                  <TooltipTrigger asChild>
                    <Button
                      size="sm"
                      onClick={handleCreateClick}
                      className="gap-1 bg-green-600 hover:bg-green-700 text-white border-0 h-8"
                    >
                      <Plus className="h-4 w-4" /> Novo Ano
                    </Button>
                  </TooltipTrigger>
                  <TooltipContent>
                    <p>Adicionar registro para um novo ano</p>
                  </TooltipContent>
                </Tooltip>
              </TooltipProvider>
              <TooltipProvider>
                <Tooltip>
                  <TooltipTrigger asChild>
                    <span tabIndex={0}>
                      <Button
                        size="sm"
                        onClick={handleEditClick}
                        disabled={!hasRegistro || loadingRegistro}
                        className="gap-1 hover:!bg-gray-100 text-[#0D4F97] border-0 disabled:opacity-50 disabled:cursor-not-allowed h-8"
                        variant="outline"
                      >
                        <SquarePen className="h-4 w-4" />{" "}
                        {loadingRegistro ? "..." : "Editar"}
                      </Button>
                    </span>
                  </TooltipTrigger>
                  {!hasRegistro && !loadingRegistro && (
                    <TooltipContent>
                      <p>Não existe registro para este ano.</p>
                    </TooltipContent>
                  )}
                </Tooltip>
              </TooltipProvider>
              <div className="flex items-center gap-1 border border-gray-300 rounded-md px-2 py-1 h-8">
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
                  className="text-sm focus:outline-none bg-transparent"
                >
                  {existingYears.length > 0 ? (
                    existingYears.map((year) => (
                      <option key={year} value={year}>
                        {year}
                      </option>
                    ))
                  ) : (
                    <option value={new Date().getFullYear()}>
                      {new Date().getFullYear()}
                    </option>
                  )}
                </select>
              </div>
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
                <InfoRow
                  label="Tipo de Atendimento"
                  value={registroAnual.serviceAreas
                    ?.map((atendimento: any) => atendimento.area)
                    .join(", ")}
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
              <div className="text-center py-6 bg-slate-50 rounded-lg mt-2 border border-dashed border-slate-200">
                <p className="text-sm text-gray-500 mb-2">
                  Nenhum registro encontrado para {selectedYear}.
                </p>
                <Button
                  variant="link"
                  onClick={handleCreateClick}
                  className="text-[#0D4F97] h-auto p-0 text-sm"
                >
                  Clique para criar um registro
                </Button>
              </div>
            )}
          </CardContent>
        </Card>

        <AnnualRegistryEditModal
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          patientId={id}
          currentYear={selectedYear}
          initialData={modalMode === "edit" ? registroAnual : null}
          mode={modalMode}
        />
      </div>
    </main>
  );
}
