"use client";

import React, { useState, useEffect } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { DateTimePicker } from "@/components/ui/date-time-picker";
import { Combobox } from "@/components/ui/combobox";
import { Textarea } from "@/components/ui/textarea";
import { Checkbox } from "@/components/ui/checkbox";
import { Input } from "@/components/ui/input";
import { cn } from "@/lib/utils";

export interface Appointment {
  dataHora?: Date;
  paciente: string;
  areaDeAtendimento: string;
  descricao?: string;
  confirmada: boolean;
  justificativa?: string;
  realizada: boolean;
}

const pacientes = [
  { value: "joao-oliveira", label: "João Oliveira" },
  { value: "maria-silva", label: "Maria Silva" },
  { value: "pedro-almeida", label: "Pedro Almeida" },
  { value: "ana-castro", label: "Ana Castro" },
  { value: "luiza-rocha", label: "Luiza Rocha" },
];

const areasDeAtendimento = [
  { value: "nutricao", label: "Nutrição" },
  { value: "psicologia", label: "Psicologia" },
  { value: "fisioterapia", label: "Fisioterapia" },
  { value: "psiquiatria", label: "Psiquiatria" },
];

interface AppointmentFormProps {
  mode: "create" | "edit";
  appointment?: Appointment;
  onSave?: (appointment: Appointment) => void;
  onSuccess?: () => void;
}

export function AppointmentForm({
  mode,
  appointment,
  onSave,
  onSuccess,
}: AppointmentFormProps) {
  const [dataHora, setDataHora] = useState<Date | undefined>(
    appointment?.dataHora
  );
  const [paciente, setPaciente] = useState<string>(appointment?.paciente || "");
  const [area, setArea] = useState<string>(
    appointment?.areaDeAtendimento || ""
  );
  const [descricao, setDescricao] = useState<string>(
    appointment?.descricao || ""
  );
  const [confirmada, setConfirmada] = useState<boolean>(
    appointment?.confirmada || false
  );
  const [justificativa, setJustificativa] = useState<string>(
    appointment?.justificativa || ""
  );
  const [realizada, setRealizada] = useState<boolean>(
    appointment?.realizada || false
  );

  useEffect(() => {
    if (appointment) {
      setDataHora(appointment.dataHora);
      setPaciente(appointment.paciente || "");
      setArea(appointment.areaDeAtendimento || "");
      setDescricao(appointment.descricao || "");
      setConfirmada(appointment.confirmada || false);
      setJustificativa(appointment.justificativa || "");
      setRealizada(appointment.realizada || false);
    }
  }, [appointment]);

  const [validationErrors, setValidationErrors] = useState({
    dataHora: false,
    paciente: false,
    area: false,
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const errors = {
      dataHora: !dataHora,
      paciente: !paciente,
      area: !area,
      justificativa: false,
    };

    setValidationErrors(errors);

    if (Object.values(errors).some(Boolean)) return;

    const payload: Appointment = {
      dataHora,
      paciente,
      areaDeAtendimento: area,
      confirmada,
      descricao,
      justificativa,
      realizada,
    };

    console.log(
      mode === "create" ? "Novo agendamento:" : "Atualização do agendamento:",
      payload
    );

    if (onSave) {
      onSave(payload);
    }

    if (onSuccess) {
      onSuccess();
    }
  };

  return (
    <Card
      className="w-full max-w-full sm:max-w-sm md:max-w-md lg:max-w-lg xl:max-w-xl mx-auto border-none shadow-none p-4 sm:p-6
                 max-h-[70vh] overflow-auto"
    >
      <form onSubmit={handleSubmit}>
        <CardContent className="space-y-4">
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
            <Label htmlFor="paciente">
              Paciente <span className="text-red-500">*</span>
            </Label>

            {mode === "create" ? (
              <Combobox
                options={pacientes}
                value={paciente}
                onChange={setPaciente}
                placeholder="Pesquisar paciente"
                className={cn(
                  validationErrors.paciente && "border-red-500",
                  "font-normal text-gray-400"
                )}
              />
            ) : (
              <Input
                value={
                  pacientes.find((p) => p.value === paciente)?.label || paciente
                }
                disabled
              />
            )}

            {validationErrors.paciente && (
              <p className="text-sm text-red-500">Este campo é obrigatório.</p>
            )}
          </div>
          <div className="space-y-2">
            <Label htmlFor="area-atendimento">
              Área de Atendimento <span className="text-red-500">*</span>
            </Label>
            <Combobox
              options={areasDeAtendimento}
              value={area}
              onChange={setArea}
              placeholder="Pesquisar área de atendimento"
              className={cn(
                validationErrors.area && "border-red-500",
                "font-normal text-gray-400"
              )}
            />
            {validationErrors.area && (
              <p className="text-sm text-red-500">Este campo é obrigatório.</p>
            )}
          </div>
          {mode === "edit" && (
            <>
              <div className="space-y-2">
                <Label htmlFor="descricao">Descrição</Label>
                <Textarea
                  id="descricao"
                  placeholder="Descreva aqui"
                  value={descricao}
                  onChange={(e) => setDescricao(e.target.value)}
                />
              </div>

              <div className="flex items-center space-x-2">
                <Checkbox
                  id="confirmada"
                  checked={confirmada}
                  onCheckedChange={(checked) => setConfirmada(!!checked)}
                />
                <Label htmlFor="confirmada" className="font-normal">
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

          <div className="flex justify-end pt-6">
            <Button className="w-full bg-blue-800 text-white hover:bg-blue-900 text-xs sm:w-auto sm:text-sm">
              {mode === "create" ? "Cadastrar" : "Salvar Alterações"}
            </Button>
          </div>
        </CardContent>
      </form>
    </Card>
  );
}
