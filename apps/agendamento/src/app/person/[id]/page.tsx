"use client";
import DocumentCategoriesCard from "@/components/shared/DocumentCategoriesCard";
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { SquarePen } from "lucide-react";
import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function PersonDetails() {
  const params = useParams();
  const [pessoa, setPessoa] = useState<any>(null);
  const router = useRouter();
  const primeiroResponsavelVivo = pessoa?.responsaveis?.find(
    (r: any) => r.vivo === "Sim"
  );

  useEffect(() => {
    if (!params?.id) {
      return;
    }

    fetch(`/api/pessoas/${params.id}`)
      .then((response) => response.json())
      .then((data) => setPessoa(data))
      .catch((err) => console.error(err));
  }, [params?.id]);

  const handleCategoriaClick = (tipo: string) => {
    const rotas: Record<string, string> = {
      pessoais: `/person/${params.id}/documents/pessoal`,
      medicos: `/person/${params.id}/documents/medico`,
      escolares: `/person/${params.id}/documents/escolar`,
    };

    const rota = rotas[tipo];
    rota
      ? router.push(rota)
      : alert(`O paciente não possui documentos do tipo ${tipo}`);
  };

  return (
    <div className="flex flex-col items-center gap-y-4 w-full max-w-full mx-auto px-4 mt-6 mb-6">
      <Avatar className="h-40 w-40 border">
        <AvatarImage
          src={pessoa?.urlFoto ?? "https://via.placeholder.com/150"}
          alt={pessoa?.nomeCompleto ?? "Foto do paciente"}
        />
        <AvatarFallback className="font-baloo font-bold text-[32px]">
          {pessoa?.nomeCompleto?.charAt(0) ?? "P"}
        </AvatarFallback>
      </Avatar>
      <h3 className="font-baloo font-bold text-[#0D4F97] text-[24px]">
        {pessoa?.nomeCompleto}
      </h3>

      <DocumentCategoriesCard onClickCategoria={handleCategoriaClick} />

      <Card className="w-full relative font-nunito text-[#0D4F97]">
        <CardHeader>
          <CardTitle className="font-semibold text-[18px] text-center">
            Dados pessoais
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Button
            variant="ghost"
            size="icon"
            className="absolute top-2 right-2 !bg-transparent !hover:bg-muted"
          >
            <Link href="/">
              <SquarePen className="w-4 h-4 text-primary mb-5" />
            </Link>
          </Button>
          <p className="text-left">
            Contato: <span className="text-[#000000]">{pessoa?.telefone}</span>
          </p>
          <p className="text-left">
            Data de nascimento:{" "}
            <span className="text-[#000000]">{pessoa?.dataNascimento}</span>
          </p>
          <p className="text-left">
            Registro de nascimento:{" "}
            <span className="text-[#000000]">{pessoa?.numRegistroNasc}</span>
          </p>
          <p className="text-left">
            Fl.s: <span className="text-[#000000]">{pessoa?.fls}</span>
          </p>
          <p className="text-left">
            Livro: <span className="text-[#000000]">{pessoa?.livro}</span>
          </p>
          <p className="text-left">
            RG: <span className="text-[#000000]">{pessoa?.rg}</span>
          </p>
          <p className="text-left">
            Data de emissão:{" "}
            <span className="text-[#000000]">{pessoa?.dataEmissaoRg}</span>
          </p>
          <p className="text-left">
            Orgão emissor:{" "}
            <span className="text-[#000000]">{pessoa?.orgaoEmissorRg}</span>
          </p>
          <p className="text-left">
            CPF: <span className="text-[#000000]">{pessoa?.cpf}</span>
          </p>
          <p className="text-left">
            Naturalidade:{" "}
            <span className="text-[#000000]">{pessoa?.naturalidade}</span>
          </p>
          <p className="text-left">
            CNS: <span className="text-[#000000]">{pessoa?.cns}</span>
          </p>
          <p className="text-left">
            NIS: <span className="text-[#000000]">{pessoa?.nis}</span>
          </p>
          <p className="text-left">
            Possui BPC:{" "}
            <span className="text-[#000000]">
              {pessoa?.cadastroAnual?.possuiBpc}
            </span>
          </p>
          <p className="text-left">
            Renda familiar:{" "}
            <span className="text-[#000000]">
              R${pessoa?.cadastroAnual?.rendaFamiliar}
            </span>
          </p>
          <p className="text-left">
            Data de cadastro:{" "}
            <span className="text-[#000000]">{pessoa?.dataCadastramento}</span>
          </p>
        </CardContent>
      </Card>

      <Card className="w-full relative font-nunito text-[#0D4F97]">
        <CardHeader>
          <CardTitle className="font-semibold text-[18px] text-center">
            Dados residenciais
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Button
            variant="ghost"
            size="icon"
            className="absolute top-2 right-2 !bg-transparent !hover:bg-muted"
          >
            <Link href="/">
              <SquarePen className="w-4 h-4 text-primary mb-5" />
            </Link>
          </Button>
          <p className="text-left">
            Endereço: <span className="text-[#000000]">{pessoa?.endereco}</span>
          </p>
          <p className="text-left">
            Bairro: <span className="text-[#000000]">{pessoa?.bairro}</span>
          </p>
          <p className="text-left">
            Cidade: <span className="text-[#000000]">{pessoa?.cidade}</span>
          </p>
          <p className="text-left">
            Estado: <span className="text-[#000000]">{pessoa?.estado}</span>
          </p>
          <p className="text-left">
            CEP: <span className="text-[#000000]">{pessoa?.cep}</span>
          </p>
        </CardContent>
      </Card>

      <Card className="w-full relative font-nunito text-[#0D4F97]">
        <CardHeader>
          <CardTitle className="font-semibold text-[18px] text-center">
            Dados familiares
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Button
            variant="ghost"
            size="icon"
            className="absolute top-2 right-2 !bg-transparent !hover:bg-muted"
          >
            <Link href="/">
              <SquarePen className="w-4 h-4 text-primary mb-5" />
            </Link>
          </Button>
          {pessoa?.responsaveis?.length > 0 ? (
            pessoa.responsaveis.map((resp: any, index: number) => (
              <div key={index} className="mb-4 border-t border-gray-200 pt-2">
                <p className="font-semibold text-left">
                  {resp.tipoResponsavel
                    ? resp.tipoResponsavel
                    : `Responsável ${index + 1}`}
                </p>
                <p>
                  Nome: <span className="text-[#000000]">{resp.nome}</span>
                </p>
                <p>
                  Vivo: <span className="text-[#000000]">{resp.vivo}</span>
                </p>
                <p>
                  Profissão:{" "}
                  <span className="text-[#000000]">{resp.profissao}</span>
                </p>
                <p>
                  RG: <span className="text-[#000000]">{resp.rg}</span>
                </p>
                <p>
                  CPF: <span className="text-[#000000]">{resp.cpf}</span>
                </p>
                <p>
                  Onde procurar:{" "}
                  <span className="text-[#000000]">{resp.ondeProcurar}</span>
                </p>
                <p>
                  Número para contato:{" "}
                  <span className="text-[#000000]">{resp.emergencia}</span>
                </p>
              </div>
            ))
          ) : (
            <p>Nenhum responsável cadastrado.</p>
          )}
        </CardContent>
      </Card>

      <Card className="w-full relative font-nunito text-[#0D4F97]">
        <CardHeader>
          <CardTitle className="font-semibold text-[18px] text-center">
            Informações de saúde
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Button
            variant="ghost"
            size="icon"
            className="absolute top-2 right-2 !bg-transparent !hover:bg-muted"
          >
            <Link href="/">
              <SquarePen className="w-4 h-4 text-primary mb-5" />
            </Link>
          </Button>
          <p>
            Vacinas:{" "}
            <span className="text-[#000000]">
              {pessoa?.vacinas?.length
                ? pessoa.vacinas
                    .map((v: any) => `${v.nome} (${v.dataAplicacao})`)
                    .join(", ")
                : "Não informado"}
            </span>
          </p>
          <p>
            Doenças que já teve:{" "}
            <span className="text-[#000000]">
              {pessoa?.cadastroAnual?.doencas}
            </span>
          </p>
          <p>
            Alergias:{" "}
            <span className="text-[#000000]">
              {pessoa?.cadastroAnual?.alergias}
            </span>
          </p>
          <p>
            Tipo de medicação que toma:{" "}
            <span className="text-[#000000]">
              {pessoa?.cadastroAnual?.medicacao}
            </span>
          </p>
          <p>
            Tipos de deficiências:{" "}
            <span className="text-[#000000]">
              {pessoa?.deficiencias?.length
                ? pessoa.deficiencias.map((d: any) => d.descricao).join(", ")
                : "Não informado"}
            </span>
          </p>
          <p>
            Tipos de atendimento:{" "}
            <span className="text-[#000000]">
              {pessoa?.atendimentos?.length
                ? pessoa.atendimentos.map((a: any) => a.descricao).join(", ")
                : "Não informado"}
            </span>
          </p>
        </CardContent>
      </Card>

      <Card className="w-full relative font-nunito text-[#0D4F97]">
        <CardHeader>
          <CardTitle className="font-semibold text-[18px] text-center">
            Em caso de emergência a quem procurar e onde?
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Button
            variant="ghost"
            size="icon"
            className="absolute top-2 right-2 !bg-transparent !hover:bg-muted"
          >
            <Link href="/">
              <SquarePen className="w-4 h-4 text-primary mb-5" />
            </Link>
          </Button>
          {primeiroResponsavelVivo ? (
            <>
              <p>
                Quem procurar:{" "}
                <span className="text-[#000000]">
                  {primeiroResponsavelVivo?.nome}
                </span>
              </p>
              <p>
                Onde encontrar:{" "}
                <span className="text-[#000000]">
                  {primeiroResponsavelVivo?.ondeProcurar}
                </span>
              </p>
              <p>
                Contato de emergência:{" "}
                <span className="text-[#000000]">
                  {primeiroResponsavelVivo?.emergencia}
                </span>
              </p>
            </>
          ) : (
            <p>Nenhum responsável vivo cadastrado.</p>
          )}
        </CardContent>
      </Card>
    </div>
  );
}
