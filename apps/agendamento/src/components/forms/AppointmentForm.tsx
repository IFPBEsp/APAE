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
  getPacientes,
  getProfissionaisDaSaude,
  getProfissionalDaSaude,
  Patient,
  Professional,
  saveAppointment,
} from "@/app/services/AppointmentService";
import { Textarea } from "../ui/textarea";
import { Checkbox } from "../ui/checkbox";

type selectItem = {
  value: string;
  label: string;
};

interface PageProps {
  editAppointment?: Appointment;
}

export function AppointmentForm({ editAppointment }: PageProps) {
  const separaETransformaEmNumero = (valor: unknown, separador: string) => {
    if (typeof valor == "string" && valor.length) {
      return (valor as string).split(separador).map((n) => parseInt(n));
    }
    return [NaN, NaN, NaN];
  };
// Prox consulta -> data de inicio
  const [year, month, day] = separaETransformaEmNumero(
    editAppointment?.initialDate,
    "-"
  );

  // hora prox consulta -> hora da consulta
  const [hour, minute, second] = separaETransformaEmNumero(
    editAppointment?.hour,
    ":"
  );
  const existingAppointmentDate =
    !isNaN(year) &&
    !isNaN(month) &&
    !isNaN(day) &&
    !isNaN(hour) &&
    !isNaN(minute) &&
    !isNaN(second)
      ? new Date(year, month, day, hour, minute, second)
      : undefined;

  const [dateHour, setDateHour] = useState<Date | undefined>(
    existingAppointmentDate
  );

  //editar
  const [paciente, setPaciente] = useState<string>(
    editAppointment?.paciente || ""
  );


  const [professional, setProfessional] = useState<string>(
    editAppointment?.professionalId || ""
  );
  const [endDate, setEndDate] = useState<string>(
    editAppointment?.endDate || ""
  );
  const [creationDate, setCreationDate] = useState<string>(
    editAppointment?.creationDate || ""
  );
  const [isActive, setIsActive] = useState<boolean>(
    editAppointment?.isActive || false
  );
  const [listPatients, setListPatients] = useState<selectItem[]>([]);
  const [listaProfessional, setListaProfessionals] = useState<selectItem[]>(
    []
  );

  useEffect(() => {
    const fetchPatientsAndProfessionals = async () => {
      const registeredPatients: Patient[] = await getPacientes();
      const registeredProfessionals: Professional[] =
        await getProfissionaisDaSaude();

      setListPatients(
        registeredPatients.map(
          (p) => ({ value: p.id, label: p.name } as selectItem)
        )
      );
      setListaProfessionals(
        registeredProfessionals.map(
          (p) => ({ value: p.id, label: p.name } as selectItem)
        )
      );
    };
    fetchPatientsAndProfessionals();
  }, []);

  // Apenas necessário devido à existência de duplicatas nos dados mockados
  useEffect(() => {
    const redefineProfissional = async () => {
      if (editAppointment) {
        const nameProfessional = (await getProfissionalDaSaude(professional)).name;
        const idProfessional = listaProfessional.find(p => p.label === nameProfessional)?.value;
        setProfessional(idProfessional || professional);
        console.log(
          professional,
          nameProfessional,
          idProfessional
        );
      }
    }
    redefineProfissional();
  }, [listaProfessional]);

  const [validationErrors, setValidationErrors] = useState({
    dateHour: false,
    // paciente: false,           EDITAR
    professional: false,
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
    const [year, month, day] = data.split("-");
    const [hour, minute, second] = horario.split(":");
    const emDate = new Date(
      parseInt(year),
      parseInt(month),
      parseInt(day),
      parseInt(hour),
      parseInt(minute),
      parseInt(second)
    );
    const agora = new Date();

    return agora > emDate;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const errors = {
      dateHour: !dateHour,
      // pati: !paciente,       EDITAR  
      professional: !professional,
    };

    setValidationErrors(errors);

    if (Object.values(errors).some(Boolean)) {
      return;
    }

    if (paciente && professional && dateHour) {


        
        // editar
      await saveAgendamento(
        {
          idPaciente: paciente,
          idProfissional: professional,
          frequenciadays: 15,
          proximaConsulta: formatDate(dateHour)[0],
          confirmado: isActive,
          horaProximaConsulta: formatDate(dateHour)[1],
          // justificativa: justificativa,
          // descricao: descricao,
        },
        editAppointment?.id
      );
      window.location.reload();
    }

    console.log("Novo agendamento:", {
      dateHour,
      // paciente,          EDITAR  
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
            <DateTimePicker value={dateHour} onChange={setDateHour} />
            {validationErrors.dateHour && (
              <p className="text-sm text-red-500">Este campo é obrigatório.</p>
            )}
          </div>

          {!editAppointment && (
            <div className="space-y-2">
              <Label htmlFor="paciente">
                Paciente <span className="text-red-500">*</span>
              </Label>
              <Combobox
                options={listPatients}
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
              options={listaProfessional}
              value={professional}
              onChange={setProfessional}
              placeholder="Pesquisar área de atendimento"
              className={cn(
                validationErrors.professional && "border-red-500",
                "font-normal",
                "text-gray-400"
              )}
            />
            {validationErrors.professional && (
              <p className="text-sm text-red-500">Este campo é obrigatório.</p>
            )}
          </div>
{/* EDITAR   */}
          <div className="space-y-2">
            <Label htmlFor="descricao">Descrição</Label>
            <Textarea
              id="descricao"
              placeholder="Descreva aqui"
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
            />
          </div>

          {editAppointment && (
            <>
              <div className="flex items-center space-x-2">
                <Checkbox
                  id="confirmado"
                  checked={isActive}
                  onCheckedChange={(checked) => setIsActive(!!checked)}
                />
                <Label htmlFor="confirmado" className="font-normal">
                  Consulta Confirmada
                </Label>
              </div>

              {dataPassou(

                // editar
                editAppointment.proximaConsulta,
                editAppointment.horaProximaConsulta
              ) && (
                // EDITAR
                <div className="space-y-2">
                  <Label htmlFor="justificativa">Justificativa de Falta</Label>
                  <Textarea
                    id="justificativa"
                    placeholder="Informe sua justificativa"
                    value={justificativa}
                    onChange={(e) => setJustificativa(e.target.value)}
                  />
                </div>
              )}
            </>
          )}

          <div className="flex justify-end">
            <Button className="w-full bg-[#0D4F97]  text-white hover:bg-blue-900 text-xs sm:w-auto sm:text-sm">
              {editAppointment ? "Atualizar" : "Cadastrar"}
            </Button>
          </div>
        </CardContent>
      </form>
    </Card>
  );
}
