"use client";

import React, { useContext, useEffect, useState } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { DateTimePicker } from "@/components/ui/date-time-picker";
import { Combobox } from "@/components/ui/combobox";
import { cn } from "@/lib/utils";
import {
  Appointment,
  getPatients,
  getHealthProfessionals,
  getHealthProfessional,
  Patient,
  HealthProfessional,
  saveAppointment,
} from "@/app/services/agendamentoService";
import { Textarea } from "../ui/textarea";
import { Checkbox } from "../ui/checkbox";

type selectItem = {
  value: string;
  label: string;
};

interface PageProps {
  agendamentoAEditar?: Appointment;
}

export function AppointmentForm({ agendamentoAEditar }: PageProps) {
  const separaETransformaEmNumero = (valor: unknown, separador: string) => {
    if (typeof valor == "string" && valor.length) {
      return (valor as string).split(separador).map((n) => parseInt(n));
    }
    return [NaN, NaN, NaN];
  };

  const [ano, mes, dia] = separaETransformaEmNumero(
    agendamentoAEditar?.nextAppointment,
    "-"
  );
  const [hora, minuto, segundo] = separaETransformaEmNumero(
    agendamentoAEditar?.nextAppointmentTime,
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
  const [patient, setPatient] = useState<string>(
    agendamentoAEditar?.patient.id || ""
  );
  const [professional, setProfessional] = useState<string>(
    agendamentoAEditar?.professional.id || ""
  );
  const [description, setDescription] = useState<string>(
    agendamentoAEditar?.description || ""
  );
  const [justification, setJustification] = useState<string>(
    agendamentoAEditar?.justification || ""
  );
  const [confirmed, setConfirmed] = useState<boolean>(
    agendamentoAEditar?.confirmed || false
  );
  const [patientList, setPatientList] = useState<selectItem[]>([]);
  const [professionalList, setProfessionalList] = useState<selectItem[]>([]);

  useEffect(() => {
    const fetchPacientesEProfissionais = async () => {
      const registedPatients: Patient[] = await getPatients();
      const registedProfessionals: HealthProfessional[] =
        await getHealthProfessionals();

      setPatientList(
        registedPatients.map(
          (p) => ({ value: p.id, label: p.name } as selectItem)
        )
      );
      setProfessionalList(
        registedProfessionals.map(
          (p) => ({ value: p.id, label: p.name } as selectItem)
        )
      );
    };
    fetchPacientesEProfissionais();
  }, []);

  // Apenas necessário devido à existência de duplicatas nos dados mockados
  useEffect(() => {
    const redefineProfissional = async () => {
      if (agendamentoAEditar) {
        const professionalName = (await getHealthProfessional(professional))
          .name;
        const professionalId = professionalList.find(
          (p) => p.label === professionalName
        )?.value;
        setProfessional(professionalId || professional);
        console.log(professional, professionalName, professionalId);
      }
    };
    redefineProfissional();
  }, [professionalList]);

  const [validationErrors, setValidationErrors] = useState({
    dataHora: false,
    paciente: false,
    profissional: false,
  });

  const formatDate = (date: Date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth()).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");

    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");
    const seconds = String(date.getSeconds()).padStart(2, "0");

    return [`${year}-${month}-${day}`, `${hours}:${minutes}:${seconds}`];
  };

  const dataPassou = (data: string, horario: string) => {
    const [ano, mes, dia] = data.split("-");
    const [hora, minuto, segundo] = horario.split(":");
    const emDate = new Date(
      parseInt(ano),
      parseInt(mes),
      parseInt(dia),
      parseInt(hora),
      parseInt(minuto),
      parseInt(segundo)
    );
    const agora = new Date();

    return agora > emDate;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const errors = {
      dataHora: !dataHora,
      paciente: !patient,
      profissional: !professional,
    };

    setValidationErrors(errors);

    if (Object.values(errors).some(Boolean)) {
      return;
    }

    if (patient && professional && dataHora) {
      await saveAppointment(
        {
          patientId: patient,
          professionalId: professional,
          frequencyDays: 15,
          nextAppointment: formatDate(dataHora)[0],
          confirmed: confirmed,
          nextAppointmentTime: formatDate(dataHora)[1],
          justification: justification,
          description: description,
        },
        agendamentoAEditar?.id
      );
      window.location.reload();
    }

    console.log("Novo agendamento:", {
      dataHora,
      patient,
      professional,
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

          {!agendamentoAEditar && (
            <div className="space-y-2">
              <Label htmlFor="paciente">
                Paciente <span className="text-red-500">*</span>
              </Label>
              <Combobox
                options={patientList}
                value={patient}
                onChange={setPatient}
                placeholder="Pesquisar paciente"
                className={cn(
                  validationErrors.paciente && "border-red-500",
                  "font-normal",
                  "text-gray-400"
                )}
              />
              {validationErrors.paciente && (
                <p className="text-sm text-red-500">
                  Este campo é obrigatório.
                </p>
              )}
            </div>
          )}

          <div className="space-y-2">
            <Label htmlFor="profissional">
              Profissional da Saúde <span className="text-red-500">*</span>
            </Label>
            <Combobox
              options={professionalList}
              value={professional}
              onChange={setProfessional}
              placeholder="Pesquisar área de atendimento"
              className={cn(
                validationErrors.profissional && "border-red-500",
                "font-normal",
                "text-gray-400"
              )}
            />
            {validationErrors.profissional && (
              <p className="text-sm text-red-500">Este campo é obrigatório.</p>
            )}
          </div>

          <div className="space-y-2">
            <Label htmlFor="descricao">Descrição</Label>
            <Textarea
              id="descricao"
              placeholder="Descreva aqui"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>

          {agendamentoAEditar && (
            <>
              <div className="flex items-center space-x-2">
                <Checkbox
                  id="confirmado"
                  checked={confirmed}
                  onCheckedChange={(checked) => setConfirmed(!!checked)}
                />
                <Label htmlFor="confirmado" className="font-normal">
                  Consulta Confirmada
                </Label>
              </div>

              {dataPassou(
                agendamentoAEditar.nextAppointment,
                agendamentoAEditar.nextAppointmentTime
              ) && (
                <div className="space-y-2">
                  <Label htmlFor="justificativa">Justificativa de Falta</Label>
                  <Textarea
                    id="justificativa"
                    placeholder="Informe sua justificativa"
                    value={justification}
                    onChange={(e) => setJustification(e.target.value)}
                  />
                </div>
              )}
            </>
          )}

          <div className="flex justify-end">
            <Button className="w-full bg-[#0D4F97]  text-white hover:bg-blue-900 text-xs sm:w-auto sm:text-sm">
              {agendamentoAEditar ? "Atualizar" : "Cadastrar"}
            </Button>
          </div>
        </CardContent>
      </form>
    </Card>
  );
}
