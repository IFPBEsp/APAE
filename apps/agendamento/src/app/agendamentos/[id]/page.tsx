import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import {
  Card,
  CardAction,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { SquareArrowOutUpRight } from "lucide-react";
import { Button } from "@/components/ui/button";

import {
  getAgendamentoById,
  Agendamento,
} from "@/app/services/agendamentoService";

interface PageProps {
  params: {
    id: string;
  };
}

function getStatusStyle(status: boolean | undefined, data: Date | null) {
  if (!data) {
    return { class: "bg-gray-400", text: "Data Inválida" };
  }
  const dataAtual = new Date();
  if (status) {
    return { class: "bg-[#0D9767]", text: "Consulta Realizada" };
  } else if (data > dataAtual) {
    return { class: "bg-[#0D4F97]", text: "Consulta Pendente" };
  } else {
    return { class: "bg-[#970D0D]", text: "Consulta Não Realizada" };
  }
}

export default async function VisualizarAgendamento({ params }: PageProps) {
  const { id } = params;

  const agendamento: Agendamento = await getAgendamentoById(id);

  // proximaConsulta é LocalDate no backend, aqui converta para Date com cuidado:
  const dataHoraDate = agendamento.proximaConsulta
    ? new Date(agendamento.proximaConsulta)
    : null;

  const statusInfo = getStatusStyle(agendamento.confirmado, dataHoraDate);

  return (
    <div className="mt-20 w-full mr-17 ml-10">
      <header className="flex flex-row items-center justify-between">
        <div className="flex items-center">
          <div className="bg-gray-200 rounded-full w-17 h-17 md:w-23 md:h-23">
            <Avatar className="p-5 w-17 h-17 md:w-23 md:h-23">
              <AvatarImage
                src="https://cdn-icons-png.flaticon.com/512/266/266033.png"
                alt="avatar"
              />
              {/* UUID precisa ser convertido em string para pegar primeira letra */}
              <AvatarFallback>
                {agendamento.idPaciente?.toString().charAt(0) || "?"}
              </AvatarFallback>
            </Avatar>
          </div>
          <h1 className="ml-10 text-[#0D4F97] text-xl md:text-2xl font-bold mr-5">
            {agendamento.idPaciente}
          </h1>
        </div>
        <Badge
          className={`${statusInfo.class} text-white py-1 px-2 rounded-md text-xs cursor-default font-medium text-center`}
        >
          {statusInfo.text}
        </Badge>
      </header>

      <main className="mt-7 mb-15">
        {/* Card Agendamento */}
        <Card className="text-[#0D4F97] mb-7">
          <CardHeader>
            <CardTitle className="font-bold text-center text-lg md:text-xl">
              Agendamento
            </CardTitle>
            <CardAction>
              <Button className="bg-transparent cursor-pointer text-[#0D4F97] hover:text-[#0d4f55] active:text-[#0d4ffe] hover:bg-transparent transition-colors">
                <SquareArrowOutUpRight />
              </Button>
            </CardAction>
          </CardHeader>
          <CardContent>
            <div className="flex justify-between mb-2">
              <div className="flex">
                <p className="font-medium mr-2">Data: </p>
                <p>
                  {dataHoraDate
                    ? new Intl.DateTimeFormat("pt-BR").format(dataHoraDate)
                    : "—"}
                </p>
              </div>
              <div className="flex">
                <p className="font-medium mr-2">Período: </p>
                <p>
                  {agendamento.frequenciaDias !== undefined
                    ? `${agendamento.frequenciaDias} dias`
                    : "—"}
                </p>
              </div>
            </div>
            <div className="flex mb-2">
              <p className="font-medium mr-2">Horário: </p>
              <p>
                {dataHoraDate
                  ? dataHoraDate.toLocaleTimeString("pt-BR", {
                      hour: "2-digit",
                      minute: "2-digit",
                    })
                  : "—"}
              </p>
            </div>
            <div className="flex mb-2">
              <p className="font-medium mr-2">Área de atendimento: </p>
              <p>{agendamento.idProfissional || "—"}</p>
            </div>
            <div className="flex mb-2">
              <p className="font-medium mr-2">Confirmada: </p>
              <p>{agendamento.confirmado ? "Sim" : "Não"}</p>
            </div>
            <div className="mb-2">
              <p className="font-medium mb-1">Descrição: </p>
              <p className="break-words whitespace-pre-wrap">{"—"}</p>
            </div>
            <div className="mb-3">
              <p className="font-medium mb-1">Justificativa: </p>
              <p className="break-words whitespace-pre-wrap">{"—"}</p>
            </div>
          </CardContent>
        </Card>

        {/* Card Profissional da Saúde */}
        <Card className="text-[#0D4F97] mb-7">
          <CardHeader>
            <CardTitle className="font-bold text-center text-lg md:text-xl">
              Profissional da Saúde
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex mb-2">
              <p className="font-medium mr-2">Nome: </p>
              <p>{agendamento.profissionalDaSaude?.nome || "—"}</p>
            </div>
            <div className="flex mb-2">
              <p className="font-medium mr-2">Email: </p>
              <p>{agendamento.profissionalDaSaude?.email || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">Telefone: </p>
              <p>{agendamento.profissionalDaSaude?.telefone || "—"}</p>
            </div>
          </CardContent>
        </Card>

        {/* Card Dados Pessoais do Paciente */}
        <Card className="text-[#0D4F97] mb-7">
          <CardHeader>
            <CardTitle className="font-bold text-center text-lg md:text-xl">
              Dados Pessoais do Paciente
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex mb-2">
              <p className="font-medium mr-2">Contato: </p>
              <p>{agendamento.dadosPaciente?.contato || "—"}</p>
            </div>
            <div className="flex mb-2">
              <p className="font-medium mr-2">Data de Nascimento: </p>
              <p>{agendamento.dadosPaciente?.dataDeNascimento || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">CPF: </p>
              <p>{agendamento.dadosPaciente?.cpf || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">RG: </p>
              <p>{agendamento.dadosPaciente?.rg || "—"}</p>
            </div>

            <CardTitle className="font-bold text-center text-lg md:text-xl">
              Dados Residenciais do Paciente
            </CardTitle>

            <div className="flex mb-3">
              <p className="font-medium mr-2">Endereço: </p>
              <p>{agendamento.dadosResidenciais?.endereco || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">Bairro: </p>
              <p>{agendamento.dadosResidenciais?.bairro || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">Cidade: </p>
              <p>{agendamento.dadosResidenciais?.cidade || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">Estado: </p>
              <p>{agendamento.dadosResidenciais?.estado || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">CEP: </p>
              <p>{agendamento.dadosResidenciais?.cep || "—"}</p>
            </div>
          </CardContent>
        </Card>

        {/* Card Informações de Saúde do Paciente */}
        <Card className="text-[#0D4F97] mb-7">
          <CardHeader>
            <CardTitle className="font-bold text-center text-lg md:text-xl">
              Informações de Saúde do Paciente
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex flex-col mb-2">
              <p className="font-medium mb-1">Vacinas que tomou: </p>
              <div className="flex flex-wrap gap-2 justify-evenly">
                {(agendamento.informacoesSaudePaciente?.vacinas || []).map(
                  (vacina: string, i: number) => (
                    <p key={i}>{vacina}</p>
                  )
                )}
              </div>
            </div>
            <div className="flex flex-col mb-2">
              <p className="font-medium mr-2">Doenças que já teve: </p>
              <div className="flex flex-wrap gap-2 justify-start">
                {(agendamento.informacoesSaudePaciente?.doencas || []).map(
                  (doenca: string, index: number, array: string[]) => (
                    <p key={index}>
                      {doenca}
                      {index < array.length - 1 ? "," : "."}
                    </p>
                  )
                )}
              </div>
            </div>
            <div className="flex flex-col mb-2">
              <p className="font-medium mr-2">Alergias: </p>
              <div className="flex flex-wrap gap-2 justify-start">
                {(agendamento.informacoesSaudePaciente?.alergias || []).map(
                  (alergia: string, index: number, array: string[]) => (
                    <p key={index}>
                      {alergia}
                      {index < array.length - 1 ? "," : "."}
                    </p>
                  )
                )}
              </div>
            </div>
            <div className="flex flex-col mb-2">
              <p className="font-medium mr-2">Tipo de medicação que toma: </p>
              <div className="flex flex-wrap gap-2 justify-start">
                {(agendamento.informacoesSaudePaciente?.medicacoes || []).map(
                  (medicacao: string, index: number, array: string[]) => (
                    <p key={index}>
                      {medicacao}
                      {index < array.length - 1 ? "," : "."}
                    </p>
                  )
                )}
              </div>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">Tipo de deficiência: </p>
              <p>{agendamento.informacoesSaudePaciente?.deficiencia || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">Tipo de atendimento: </p>
              <p>{agendamento.informacoesSaudePaciente?.atendimento || "—"}</p>
            </div>
          </CardContent>
        </Card>
      </main>
    </div>
  );
}
