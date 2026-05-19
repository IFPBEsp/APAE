"use client"

import React, { useState } from 'react';
import { Card, CardContent } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { DateTimePicker } from "@/components/ui/date-time-picker";
import { Input } from "@/components/ui/input";
import { Combobox } from "@/components/ui/combobox";
import { cn } from "@/lib/utils";

export interface Appointment {
  dataHora?: Date;
  paciente: string;
  area: string;
}

const pacientes = [
  { value: 'joao-oliveira', label: 'João Oliveira' },
  { value: 'maria-silva', label: 'Maria Silva' },
  { value: 'pedro-almeida', label: 'Pedro Almeida' },
];

const areasDeAtendimento = [
  { value: 'nutricao', label: 'Nutrição' },
  { value: 'psicologia', label: 'Psicologia' },
  { value: 'fisioterapia', label: 'Fisioterapia' },
];

export function EditAppointmentForm({
  appointment,
}: {
  appointment?: Appointment;
}) {
  const [dataHora, setDataHora] = useState<Date | undefined>(
    appointment?.dataHora
  );
  const [paciente, setPaciente] = useState<string>(appointment?.paciente || '');
  const [area, setArea] = useState<string>(appointment?.area || '');

  const [frequencyDays, setFrequencyDays] = useState<string | number>('');

  const [validationErrors, setValidationErrors] = useState({
    dataHora: false,
    paciente: false,
    area: false,
    frequencyDays: false,
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const errors = {
      dataHora: !dataHora,
      paciente: !paciente,
      area: !area,
      frequencyDays:
        !frequencyDays ||
        isNaN(Number(frequencyDays)) ||
        Number(frequencyDays) <= 0,
    };

    setValidationErrors(errors);

    if (Object.values(errors).some(Boolean)) {
      return;
    }
  };

  return (
    <Card className="w-full sm:max-w-[450px] mx-auto border-none shadow-none">
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
            <Combobox
              options={pacientes}
              value={paciente}
              onChange={setPaciente}
              placeholder="Pesquisar paciente"
              className={cn(
                validationErrors.paciente && 'border-red-500',
                'font-normal text-gray-400'
              )}
            />
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
                validationErrors.area && 'border-red-500',
                'font-normal text-gray-400'
              )}
            />

            {validationErrors.area && (
              <p className="text-sm text-red-500">Este campo é obrigatório.</p>
            )}
          </div>

          <div className="space-y-2">
            <Label htmlFor="frequencia">
              Frequência (dias) <span className="text-red-500">*</span>
            </Label>
            <Input
              id="frequencia"
              type="number"
              placeholder="Adicionar frequência"
              value={frequencyDays}
              onChange={e => setFrequencyDays(e.target.value)}
              className={cn(validationErrors.frequencyDays && 'border-red-500')}
            />
            {validationErrors.frequencyDays && (
              <p className="text-sm text-red-500">
                Frequência é obrigatória e deve ser maior que 0.
              </p>
            )}
          </div>

          <div className="flex justify-end pt-6">
            <Button className="w-full bg-[#0D4F97]  text-white hover:bg-blue-900 text-xs sm:w-auto sm:text-sm">
              Salvar Alterações
            </Button>
          </div>
        </CardContent>
      </form>
    </Card>
  );
}
