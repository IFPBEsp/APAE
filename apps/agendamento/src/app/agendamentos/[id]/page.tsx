import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import {
  Card,
  CardAction,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
  DialogDescription,
} from "@/components/ui/dialog";
import { Pencil } from "lucide-react";
import { Button } from "@/components/ui/button";

import {
  getAgendamentoById,
  Agendamento
} from "@/app/services/agendamentoService";
import { AppointmentForm } from "@/components/forms/AppointmentForm";
import TrashButton from "@/components/buttons/trashButton";
import ConfirmButton from "@/components/buttons/confirmButton";
import { separaETransformaEmNumero } from "@/lib/utils";

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
  if (status && data < dataAtual) {
    return { class: "bg-[#0D9767]", text: "Consulta Realizada" };
  } else if (status) {
    return { class: "bg-[#0D4F97]", text: "Consulta Confirmada" };
  } else if (data > dataAtual) {
    return { class: "bg-[#f0bc1f]", text: "Consulta Pendente" };
  } else {
    return { class: "bg-[#970D0D]", text: "Consulta Não Realizada" };
  }
}

export default async function VisualizarAgendamento({ params }: PageProps) {
  const { id } = await params;
  const agendamento: Agendamento = await getAgendamentoById(id);
  const [ano, mes, dia] = separaETransformaEmNumero(
    agendamento.proximaConsulta,
    "-"
  );
  const [hora, minuto, segundo] = separaETransformaEmNumero(
    agendamento.horaProximaConsulta,
    ":"
  );
  const dataHoraDate =
    !isNaN(ano) &&
    !isNaN(mes) &&
    !isNaN(dia) &&
    !isNaN(hora) &&
    !isNaN(minuto) &&
    !isNaN(segundo)
      ? new Date(ano, mes, dia, hora, minuto, segundo)
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
              <AvatarFallback>
                {agendamento.paciente.nome.charAt(0) || "?"}
              </AvatarFallback>
            </Avatar>
          </div>
          <h1 className="ml-10 text-[#0D4F97] text-xl md:text-2xl font-bold mr-5">
            {agendamento.paciente.nome}
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
              <Dialog>
                <DialogTrigger asChild>
                  <Button className="bg-transparent cursor-pointer text-[#0D4F97] hover:text-[#0d4f55] active:text-[#0d4ffe] hover:bg-[rgba(0,0,0,0.1)] transition-colors">
                    <Pencil />
                  </Button>
                </DialogTrigger>
                <DialogContent className="w-full sm:max-w-[425px]">
                  <DialogHeader>
                    <DialogTitle>Editar Agendamento</DialogTitle>
                    <DialogDescription>
                      Edite os detalhes abaixo para agendar uma consulta.
                    </DialogDescription>
                  </DialogHeader>
                  <AppointmentForm agendamentoAEditar={agendamento} />
                </DialogContent>
              </Dialog>
              <TrashButton id={id} />
              <ConfirmButton id={id} />
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
              <p>{agendamento.profissional.areaDaSaude || "—"}</p>
            </div>
            <div className="flex mb-2">
              <p className="font-medium mr-2">Confirmada: </p>
              <p>{agendamento.confirmado ? "Sim" : "Não"}</p>
            </div>
            <div className="mb-2">
              <p className="font-medium mb-1">Descrição: </p>
              <p className="break-words whitespace-pre-wrap">{agendamento.descricao || "—"}</p>
            </div>
            <div className="mb-3">
              <p className="font-medium mb-1">Justificativa: </p>
              <p className="break-words whitespace-pre-wrap">{agendamento.justificativa || "—"}</p>
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
              <p>{agendamento.profissional.nome || "—"}</p>
            </div>
            <div className="flex mb-2">
              <p className="font-medium mr-2">Email: </p>
              <p>{agendamento.profissional.email || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">Telefone: </p>
              <p>{agendamento.profissional.telefone || "—"}</p>
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
              <p>{agendamento.paciente.telefone || "—"}</p>
            </div>
            <div className="flex mb-2">
              <p className="font-medium mr-2">Data de Nascimento: </p>
              <p>
                {separaETransformaEmNumero(
                  agendamento.paciente.dateNascimento,
                  "-"
                )
                  .map((n, i) => (i == 0 ? n : n.toString().padStart(2, "0")))
                  .reverse()
                  .join("/") || "—"}
              </p>
              <p>
                {separaETransformaEmNumero(
                  agendamento.paciente.dateNascimento,
                  "-"
                )
                  .map((n, i) => (i == 0 ? n : n.toString().padStart(2, "0")))
                  .reverse()
                  .join("/") || "—"}
              </p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">CPF: </p>
              <p>{agendamento.paciente.cpf || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">RG: </p>
              <p>{agendamento.paciente.rg || "—"}</p>
            </div>

            <CardTitle className="font-bold text-center text-lg md:text-xl">
              Dados Residenciais do Paciente
            </CardTitle>

            <div className="flex mb-3">
              <p className="font-medium mr-2">Endereço: </p>
              <p>{agendamento.paciente.endereco || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">Bairro: </p>
              <p>{agendamento.paciente.bairro || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">Cidade: </p>
              <p>{agendamento.paciente.cidade || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">Estado: </p>
              <p>{agendamento.paciente.estado || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">CEP: </p>
              <p>{agendamento.paciente.cep || "—"}</p>
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
                {(agendamento.paciente.vacinacoes || []).map(
                  (vacina: string, i: number) => (
                    <p key={i}>{vacina}</p>
                  )
                )}
              </div>
            </div>
            <div className="flex flex-col mb-2">
              <p className="font-medium mr-2">Doenças que já teve: </p>
              <div className="flex flex-wrap gap-2 justify-start">
                {(agendamento.paciente?.doencas || []).map(
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
                {(agendamento.paciente?.alergias || []).map(
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
                {(agendamento.paciente?.medicacoes || []).map(
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
              <p>{agendamento.paciente?.deficiencias || "—"}</p>
            </div>
            <div className="flex mb-3">
              <p className="font-medium mr-2">Tipo de atendimento: </p>
              <p>{agendamento.paciente?.tiposAtendimentos || "—"}</p>
            </div>
          </CardContent>
        </Card>
      </main>
    </div>
  );
}
