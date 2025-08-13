"use client"

import React, { useEffect, useState } from 'react';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { DateTimePicker } from "@/components/ui/date-time-picker";
import { Input } from "@/components/ui/input";
import { Combobox } from "@/components/ui/combobox";
import { cn } from "@/lib/utils";
import { mockPacientes, Paciente, saveAgendamento } from '@/app/services/agendamentoService';

type selectItem = {
  value: string;
  label: string;
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

export function AppointmentForm() {
  const [dataHora, setDataHora] = useState<Date | undefined>(undefined);
  const [paciente, setPaciente] = useState<string>("");
  const [area, setArea] = useState<string>("");
  const [periodo, setPeriodo] = useState<string>("");
  const [listaPacientes, setListaPacientes] = useState<selectItem[]>([]);

  useEffect(() => {
    const fetchMockPacientes = async () => {
      const mock = await mockPacientes();
      setListaPacientes(mock.map(p => ({ value: p.cidade, label: p.nome } as selectItem)))
    }
    fetchMockPacientes();
  }, []);

  const [validationErrors, setValidationErrors] = useState({
    dataHora: false,
    paciente: false,
    area: false,
    periodo: false,
  });

  const calculateNextAppointment = () => {
    if(dataHora && !isNaN(parseInt(periodo))){
      const horaDaConsultaEmMilissegundos = dataHora.getMilliseconds();
      const periodoEmMilissegundos = parseInt(periodo) * 86400000;
      return new Date(horaDaConsultaEmMilissegundos + periodoEmMilissegundos);
    }

    throw new Error("Erro ao calcular próxima consulta.");
  }

  const formatDate = (date: Date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    
    return [`${year}-${month}-${day}`, `${hours}:${minutes}:${seconds}`];
};

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    const errors = {
      dataHora: !dataHora,
      paciente: !paciente,
      area: !area,
      periodo: !periodo || isNaN(parseInt(periodo)),
    };
    
    setValidationErrors(errors);

    if (Object.values(errors).some(Boolean)) {
      return;
    }

    if(paciente && area && periodo && dataHora) {
      saveAgendamento({
        idPaciente: paciente,
        idProfissional: "d8c30ca9-5ea4-475e-a1b1-05caa378d6a6",
        frequenciaDias: parseInt(periodo),
        proximaConsulta: formatDate(calculateNextAppointment())[0],
        confirmado: false,
        horaProximaConsulta: formatDate(dataHora)[1]
      })
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
              options={listaPacientes}
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