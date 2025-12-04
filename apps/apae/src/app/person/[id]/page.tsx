"use client";

import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DocumentCategoriesCard from "@/components/shared/DocumentCategoriesCard";
import { SquarePen } from "lucide-react";
import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { Loader2, ArrowLeft } from "lucide-react";

interface InfoRowProps {
  label: string;
  value?: string | number | null | boolean;
}

const InfoRow: React.FC<InfoRowProps> = ({ label, value }) => {
  let displayValue: string | number = "Não informado";

  if (value === null || value === undefined || value === "") {
    displayValue = "Não informado";
  } else if (label === "Recebe BPC?") {
    displayValue = (value === true || String(value).toLowerCase() === 'true') ? "Sim" : "Não";
  } else if (label === "Renda Familiar" && typeof value === 'number') {
    displayValue = value.toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
  } else if (typeof value === 'boolean') {
    displayValue = value ? "Sim" : "Não";
  } else {
    displayValue = value;
  }

  return (
    <div className="mb-2">
      <span className="text-sm font-semibold text-gray-500">{label}</span>
      <p className="text-base text-black">{displayValue}</p>
    </div>
  );
};

export default function PersonDetailsPage() {
  const params = useParams();
  const router = useRouter();
  const id = params?.id as string;

  const [pessoa, setPessoa] = useState<any>(null);
  const [registroAnual, setRegistroAnual] = useState<any>(null);

  const [selectedYear, setSelectedYear] = useState<string>(new Date().getFullYear().toString());

  const [loadingPessoa, setLoadingPessoa] = useState(true);
  const [loadingRegistro, setLoadingRegistro] = useState(false);

  const currentYearInt = new Date().getFullYear();
  const years = Array.from({ length: 11 }, (_, i) => (currentYearInt - i).toString());

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
  }, [id, router]);

  useEffect(() => {
    if (!id) return;

    async function fetchRegistro() {
      try {
        setLoadingRegistro(true);
        setRegistroAnual(null);

        const response = await fetch(`/api/pessoas/${id}/registro-anual/${selectedYear}`);

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
        <Card className="w-full relative font-nunito">
          <CardHeader>
            <CardTitle className="text-[#0D4F97]">Dados Pessoais</CardTitle>
          </CardHeader>
          <CardContent className="grid grid-cols-1 md:grid-cols-2 gap-x-4">
            <InfoRow label="Nome Completo" value={pessoa.fullName} />
            <InfoRow label="Data de Nasc." value={pessoa.birthDate} />
            <InfoRow label="Naturalidade" value={pessoa.birthplace} />
            <InfoRow label="Contato" value={pessoa.contact} />
            <InfoRow label="Alergias" value={pessoa.allergies} />
            <InfoRow label="Estudante?" value={pessoa.isStudent} />
            <InfoRow label="Data de Cadastro" value={pessoa.registrationDate} />
          </CardContent>
        </Card>

        {/* Card: Documentação */}
        <Card className="w-full relative font-nunito">
          <CardHeader>
            <CardTitle className="text-[#0D4F97]">Documentação</CardTitle>
          </CardHeader>
          <CardContent className="grid grid-cols-1 md:grid-cols-2 gap-x-4">
            <InfoRow label="CPF" value={pessoa.cpf} />
            <InfoRow label="RG" value={pessoa.rg} />
            <InfoRow label="Orgão Emissor" value={pessoa.issuingAgency} />
            <InfoRow label="Data de Emissão" value={pessoa.issueDate} />
            <InfoRow label="CNS" value={pessoa.cns} />
            <InfoRow label="NIS" value={pessoa.nis} />
            <InfoRow label="Nº Cert. Nasc." value={pessoa.birthCertificateNumber} />
            <InfoRow label="Cartório" value={pessoa.registryOffice} />
            <InfoRow label="Livro" value={pessoa.book} />
            <InfoRow label="Folha" value={pessoa.fls} />
          </CardContent>
        </Card>

        {/* Card: Endereço */}
        <Card className="w-full relative font-nunito">
          <CardHeader>
            <CardTitle className="text-[#0D4F97]">Endereço Residencial</CardTitle>
          </CardHeader>
          <CardContent className="grid grid-cols-1 md:grid-cols-2 gap-x-4">
            <InfoRow label="Rua" value={pessoa.address?.street} />
            <InfoRow label="Número" value={pessoa.address?.number} />
            <InfoRow label="Bairro" value={pessoa.address?.neighborhood} />
            <InfoRow label="Cidade" value={pessoa.address?.city} />
            <InfoRow label="Estado" value={pessoa.address?.state} />
            <InfoRow label="CEP" value={pessoa.address?.cep} />
            <InfoRow label="Complemento" value={pessoa.address?.complement} />
          </CardContent>
        </Card>

        {/* Card: Responsáveis */}
        <Card className="w-full relative font-nunito">
          <CardHeader>
            <CardTitle className="text-[#0D4F97]">Responsáveis</CardTitle>
          </CardHeader>
          <CardContent>
            {pessoa.guardian && (
              <div className="mb-4 p-2 border rounded-md">
                <p className="font-bold text-base">Guardião Principal</p>
                <InfoRow label="Nome" value={pessoa.guardian.name} />
                <InfoRow label="Parentesco" value={pessoa.guardian.kinship} />
                <InfoRow label="Contato" value={pessoa.guardian.contact} />
                <InfoRow
                  label="Endereço"
                  value={`${pessoa.guardian.address?.street ?? ""}, ${
                    pessoa.guardian.address?.number ?? ""
                  }`}
                />
              </div>
            )}
            {pessoa.parents?.map((parent: any) => (
              <div key={parent.id} className="mb-2 p-2 border-t border-gray-200">
                <p className="font-bold text-base">
                  {parent.kinship === "PAI" ? "Pai" : "Mãe"}
                </p>
                <InfoRow label="Nome" value={parent.name} />
                <InfoRow label="CPF" value={parent.cpf} />
                <InfoRow label="Profissão" value={parent.profession} />
                <InfoRow label="Vivo?" value={parent.isAlive} />
              </div>
            ))}
          </CardContent>
        </Card>

        {/* --- CARD DE SAÚDE --- */}
        <Card className="w-full relative font-nunito">
          <CardHeader className="flex flex-row items-center justify-between">
            <CardTitle className="text-[#0D4F97]">Informações de Saúde</CardTitle>
            <div className="flex items-center gap-2">
              <label htmlFor="year-select" className="text-sm font-semibold text-gray-600">
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
              value={pessoa.vaccineNames
                ?.map((v: any) => v.name)
                .join(", ")}
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
                <InfoRow label="Renda Familiar" value={registroAnual.familyIncome} />
                <InfoRow label="Doenças" value={registroAnual.diseases} />
                <InfoRow label="Medicamentos Contínuos" value={registroAnual.continuousMedication} />
                <InfoRow
                  label="Transtornos"
                  value={registroAnual.disorders?.map((d: any) => d.name).join(", ")}
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