"use client"

import React, { useState } from 'react';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { DateTimePicker } from "@/components/ui/date-time-picker";
import { Input } from "@/components/ui/input";
import { Combobox } from "@/components/ui/combobox";
import { cn } from "@/lib/utils";

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

export function AppointmentForm() {
  const [dataHora, setDataHora] = useState<Date | undefined>(undefined);
  const [paciente, setPaciente] = useState<string>("");
  const [area, setArea] = useState<string>("");
  const [periodo, setPeriodo] = useState<string>("");

  const [validationErrors, setValidationErrors] = useState({
    dataHora: false,
    paciente: false,
    area: false,
    periodo: false,
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    const errors = {
      dataHora: !dataHora,
      paciente: !paciente,
      area: !area,
      periodo: !periodo,
    };
    
    setValidationErrors(errors);

    if (Object.values(errors).some(Boolean)) {
      return;
    }

    console.log("Novo agendamento:", { dataHora, paciente, area, periodo });
  };

  return (
    <Card className="w-full mx-auto border-none shadow-none">
      <form onSubmit={handleSubmit}>
        <CardContent className="space-y-6">
          
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
            <Label htmlFor="periodo-atendimento">
              Período de Atendimento <span className="text-red-500">*</span>
            </Label>
            <Input
              id="periodo-atendimento"
              value={periodo}
              onChange={(e) => setPeriodo(e.target.value)}
              placeholder="Informe a frequência da consulta"
              className={cn(validationErrors.periodo && "border-red-500", "font-normal", "text-gray-400")}

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
              options={pacientes}
              value={paciente}
              onChange={setPaciente}
              placeholder="Pesquisar paciente"
              className={cn(validationErrors.paciente && "border-red-500", "font-normal","text-gray-400")}
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
              className={cn(validationErrors.area && "border-red-500", "font-normal","text-gray-400")}
            />
            {validationErrors.area && (
              <p className="text-sm text-red-500">Este campo é obrigatório.</p>
            )}
          </div>
          <div className="flex justify-end">
              <Button className="w-full bg-blue-800 text-white hover:bg-blue-900 text-xs sm:w-auto sm:text-sm">Cadastrar</Button>

          </div>

        </CardContent>
      </form>
    </Card>
  );
}