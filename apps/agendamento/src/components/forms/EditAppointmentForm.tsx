"use client"

import React, { useState } from 'react';
import { Card, CardContent } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { DateTimePicker } from "@/components/ui/date-time-picker";
import { Input } from "@/components/ui/input";
import { Combobox } from "@/components/ui/combobox";
import { Textarea } from "@/components/ui/textarea";
import { Checkbox } from "@/components/ui/checkbox";
import { cn } from "@/lib/utils";

const pacientes = [
  { value: "joao-oliveira", label: "João Oliveira" },
  { value: "maria-silva", label: "Maria Silva" },
  { value: "pedro-almeida", label: "Pedro Almeida" },
];

const areasDeAtendimento = [
  { value: "nutricao", label: "Nutrição" },
  { value: "psicologia", label: "Psicologia" },
  { value: "fisioterapia", label: "Fisioterapia" },
];

export function EditAppointmentForm() {
  const [dataHora, setDataHora] = useState<Date | undefined>(undefined);
  const [paciente, setPaciente] = useState<string>("");
  const [area, setArea] = useState<string>("");
  const [periodo, setPeriodo] = useState<string>("");
  const [descricao, setDescricao] = useState<string>("");
  const [confirmada, setConfirmada] = useState<boolean>(false);
  const [justificativa, setJustificativa] = useState<string>("");
  const [realizada, setRealizada] = useState<boolean>(false);

  const [validationErrors, setValidationErrors] = useState({
    dataHora: false,
    paciente: false,
    area: false,
    periodo: false,
    justificativa: false,
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const errors = {
      dataHora: !dataHora,
      paciente: !paciente,
      area: !area,
      periodo: !periodo,
      justificativa: !confirmada && justificativa.trim() === "",
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
            <DateTimePicker
              value={dataHora}
              onChange={setDataHora}
            />
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
              className={cn(validationErrors.paciente && "border-red-500", "font-normal text-gray-400")}
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
              className={cn(validationErrors.area && "border-red-500", "font-normal text-gray-400")}
            />

            {validationErrors.area && (
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
              className={cn(validationErrors.justificativa && "border-red-500")}
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

          <div className="flex justify-end pt-6">
            <Button className="w-full bg-blue-800 text-white hover:bg-blue-900 text-xs sm:w-auto sm:text-sm">Salvar Alterações</Button>
          </div>

        </CardContent>
      </form>
    </Card>
  );
}