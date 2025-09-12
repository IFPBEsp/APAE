"use client";

import React, { useEffect, useState } from "react";
import {
  Card,
  CardContent,
} from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { DateTimePicker } from "@/components/ui/date-time-picker";
import { Input } from "@/components/ui/input";
import { Combobox } from "@/components/ui/combobox";
import { cn } from "@/lib/utils";
import {
  Agendamento,
  getPacientes,
  getAreasDaSaude,
  saveAgendamento,
} from "@/app/services/agendamentoService";
import { Textarea } from "../ui/textarea";
import { Checkbox } from "../ui/checkbox";

type selectItem = {
  value: string;
  label: string;
};

interface PageProps {
  agendamentoAEditar?: Agendamento;
}

export function AppointmentForm({ agendamentoAEditar }: PageProps) {
  const separaETransformaEmNumero = (valor: unknown, separador: string) => {
    if (typeof valor == "string" && valor.length) {
      return (valor as string).split(separador).map((n) => parseInt(n));
    }
    return [NaN, NaN, NaN];
  };

  const [ano, mes, dia] = separaETransformaEmNumero(
    agendamentoAEditar?.proximaConsulta,
    "-"
  );
  const [hora, minuto, segundo] = separaETransformaEmNumero(
    agendamentoAEditar?.horaProximaConsulta,
    ":"
  );
  const dateAgendamentoExistente =
    !isNaN(ano) &&
    !isNaN(mes) &&
    !isNaN(dia) &&
    !isNaN(hora) &&
    !isNaN(minuto) &&
    !isNaN(segundo)
      ? new Date(ano, mes, dia, hora, minuto, segundo)
      : undefined;

  const [dataHora, setDataHora] = useState<Date | undefined>(
    dateAgendamentoExistente
  );
  const [paciente, setPaciente] = useState<string>(
    agendamentoAEditar?.paciente.id || ""
  );
  const [areaDaSaude, setAreaDaSaude] = useState<string>(
    agendamentoAEditar?.areaDaSaude.id || ""
  );
  const [periodo, setPeriodo] = useState<string>(
    agendamentoAEditar?.frequenciaDias.toString() || ""
  );
  const [descricao, setDescricao] = useState<string>(
    agendamentoAEditar?.descricao || ""
  );
  const [justificativa, setJustificativa] = useState<string>(
    agendamentoAEditar?.justificativa || ""
  );
  const [confirmado, setConfirmado] = useState<boolean>(
    agendamentoAEditar?.confirmado || false
  );
  const [realizada, setRealizada] = useState<boolean>(false);
  const [listaPacientes, setListaPacientes] = useState<selectItem[]>([]);
  const [listaAreasDaSaude, setAreasDaSaude] = useState<selectItem[]>([]);

  useEffect(() => {
    const fetchPacientesEProfissionais = async () => {
      const pacientesCadastrados = await getPacientes();
      setListaPacientes(
        pacientesCadastrados.map(
          (p) => ({ value: p.id, label: p.nome } as selectItem)
        )
      );
      const areasDaSaudeCadastrados = await getAreasDaSaude();
      setAreasDaSaude(
        areasDaSaudeCadastrados.map(
          (area) => ({ value: area.id, label: area.name} as selectItem)
        )
      );
    };
    fetchPacientesEProfissionais();
  }, []);

  const [validationErrors, setValidationErrors] = useState({
    dataHora: false,
    paciente: false,
    areaDaSaude: false,
    periodo: false,
    justificativa: false,
  });

  const calculateNextAppointment = () => {
    if (dataHora && !isNaN(parseInt(periodo))) {
      const horaDaConsultaEmMilissegundos = dataHora.getTime();
      const periodoEmMilissegundos = parseInt(periodo) * 86400000;
      return new Date(horaDaConsultaEmMilissegundos + periodoEmMilissegundos);
    }

    throw new Error("Erro ao calcular próxima consulta.");
  };

  const formatDate = (date: Date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");

    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");
    const seconds = String(date.getSeconds()).padStart(2, "0");

    return [`${year}-${month}-${day}`, `${hours}:${minutes}:${seconds}`];
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const errors = {
      dataHora: !dataHora,
      paciente: !paciente,
      areaDaSaude: !areaDaSaude,
      periodo: !periodo || isNaN(parseInt(periodo)),
      justificativa: !!agendamentoAEditar && !confirmado && justificativa.trim() === "",
    };

    setValidationErrors(errors);

    if (Object.values(errors).some(Boolean)) {
      return;
    }

    if (paciente && areaDaSaude && periodo && dataHora) {
      await saveAgendamento(
        {
          idPaciente: paciente,
          idAreaDaSaude: areaDaSaude,
          frequenciaDias: parseInt(periodo),
          proximaConsulta: formatDate(dataHora)[0],
          confirmado: confirmado,
          horaProximaConsulta: formatDate(dataHora)[1],
          justificativa: justificativa,
          descricao: descricao
        },
        agendamentoAEditar?.id
      );
      window.location.reload();
    }

    console.log("Novo agendamento:", {
      dataHora,
      paciente,
      areaDaSaude,
      periodo,
    });
  };

  return (
    <Card className="w-full mx-auto border-none shadow-none">
      <form onSubmit={handleSubmit}>
        <CardContent className="space-y-6">
          <div className="space-y-2">
            <Label htmlFor="data-hora">
              Escolher Data e Horário <span className="text-red-500">*</span>
            </Label>
            <DateTimePicker value={dataHora} onChange={setDataHora} />
            {validationErrors.dataHora && (
              <p className="text-sm text-red-500">Este campo é obrigatório.</p>
            )}
          </div>

          <div className="space-y-2">
            <Label htmlFor="periodo-atendimento">
              Período de Atendimento <span className="text-red-500">*</span>
            </Label>
            <Input
              id="periodo-atendimento"
              value={periodo}
              onChange={(e) => {
                const value = e.target.value;
                if (!isNaN(parseInt(value))) {
                  setPeriodo(value);
                }
              }}
              placeholder="Informe a frequência da consulta"
              className={cn(
                validationErrors.periodo && "border-red-500",
                "font-normal",
                "text-gray-400"
              )}
            />
            {validationErrors.periodo && (
              <p className="text-sm text-red-500">Este campo é obrigatório.</p>
            )}
          </div>
          <div className="space-y-2">
            <Label htmlFor="paciente">
              Paciente <span className="text-red-500">*</span>
            </Label>
            <Combobox
              options={listaPacientes}
              value={paciente}
              onChange={setPaciente}
              placeholder="Pesquisar paciente"
              className={cn(
                validationErrors.paciente && "border-red-500",
                "font-normal",
                "text-gray-400"
              )}
            />
            {validationErrors.paciente && (
              <p className="text-sm text-red-500">Este campo é obrigatório.</p>
            )}
          </div>

          <div className="space-y-2">
            <Label htmlFor="profissional">
              Área da Saúde <span className="text-red-500">*</span>
            </Label>
            <Combobox
              options={listaAreasDaSaude}
              value={areaDaSaude}
              onChange={setAreaDaSaude}
              placeholder="Pesquisar área da saúde"
              className={cn(
                validationErrors.areaDaSaude && "border-red-500",
                "font-normal",
                "text-gray-400"
              )}
            />
            {validationErrors.areaDaSaude && (
              <p className="text-sm text-red-500">Este campo é obrigatório.</p>
            )}
          </div>

          <div className="space-y-2">
            <Label htmlFor="descricao">Descrição</Label>
            <Textarea
              id="descricao"
              placeholder="Descreva aqui"
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
            />
          </div>

          {agendamentoAEditar && (
            <>
              <div className="flex items-center space-x-2">
                <Checkbox
                  id="confirmado"
                  checked={confirmado}
                  onCheckedChange={(checked) => setConfirmado(!!checked)}
                />
                <Label htmlFor="confirmado" className="font-normal">
                  Consulta Confirmada
                </Label>
              </div>

              <div className="space-y-2">
                <Label htmlFor="justificativa">Justificativa de Falta</Label>
                <Textarea
                  id="justificativa"
                  placeholder="Informe sua justificativa"
                  value={justificativa}
                  onChange={(e) => setJustificativa(e.target.value)}
                  className={cn(
                    validationErrors.justificativa && "border-red-500"
                  )}
                />
              </div>

              <div className="flex items-center space-x-2">
                <Checkbox
                  id="realizada"
                  checked={realizada}
                  onCheckedChange={(checked) => setRealizada(!!checked)}
                />
                <Label htmlFor="realizada" className="font-normal">
                  Consulta Realizada
                </Label>
              </div>
            </>
          )}

          <div className="flex justify-end">
            <Button className="w-full bg-blue-800 text-white hover:bg-blue-900 text-xs sm:w-auto sm:text-sm">
              {agendamentoAEditar ? "Atualizar" : "Cadastrar"}
            </Button>
          </div>
        </CardContent>
      </form>
    </Card>
  );
}
