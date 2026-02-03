"use client";

import {
  Appointment,
  getPacientes,
  getProfissionaisDaSaude,
  Patient,
  Professional,
  updateAppointmentRule
} from "@/app/services/appointmentService";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Combobox } from "@/components/ui/combobox";
import { DateTimePicker } from "@/components/ui/date-time-picker";
import { Label } from "@/components/ui/label";
import { cn, separaETransformaEmNumero } from '@/lib/utils';
import { format } from "date-fns";
import React, { useEffect, useState } from "react";
import { Input } from '../ui/input';


type selectItem = {
  value: string;
  label: string;
};

interface PageProps {
  editAppointment?: Appointment;
}

export function AppointmentForm({ editAppointment }: PageProps) {
  const [year, month, day] = separaETransformaEmNumero(
    editAppointment?.initialDate,
    '-'
  );
  const [hour, minute] = separaETransformaEmNumero(
    editAppointment?.hour,
    ':'
  );
  const existingAppointmentDate =
    !isNaN(year) &&
    !isNaN(month) &&
    !isNaN(day) &&
    !isNaN(hour) &&
    !isNaN(minute)
      ? new Date(year, month, day, hour, minute)
      : undefined;

  const [dateHour, setDateHour] = useState<Date | undefined>(existingAppointmentDate);

  const [patient, setPatient] = useState<selectItem>(() => {
      const patient = editAppointment?.annualRegistration.patient;
      return patient ? { value: patient.id,  label: patient.fullName } : { value: "", label: "" }
    }
  );

  const [professional, setProfessional] = useState<selectItem>(() => {
      const professional = editAppointment?.professional;
      return professional ? {value: professional.id, label: professional.name } : { value: "", label: "" }
    }
  );

  const [listPatients, setListPatients] = useState<selectItem[]>([]);
  const [listaProfessional, setListaProfessionals] = useState<selectItem[]>([]);

  const [frequencyDays, setFrequencyDays] = useState<number>(
    editAppointment?.frequencyDays || 0
  );

  useEffect(() => {
    const fetchPatientsAndProfessionals = async () => {
      const registeredPatients: Patient[] = await getPacientes();
      const registeredProfessionals: Professional[] = await getProfissionaisDaSaude();
      setListPatients(
        registeredPatients.map(
          p => ({ value: p.id, label: p.fullName } as selectItem)
        )
      );
      setListaProfessionals(
        registeredProfessionals.map(
          p => ({ value: p.id, label: p.name } as selectItem)
        )
      );
    };
    fetchPatientsAndProfessionals();
  }, []);

  // Apenas necessário devido à existência de duplicatas nos dados mockados
  //useEffect(() => {
    //const redefineProfissional = async () => {
      //if (editAppointment) {
        //const nameProfessional = (await getProfissionalDaSaude(professional.value))
          //.name;
        //const idProfessional = listaProfessional.find(
          //p => p.label === nameProfessional
        //)?.value;
        //setProfessional(idProfessional || professional.value);
      //}
    //};
    //redefineProfissional();
  //}, [listaProfessional]);

  const [validationErrors, setValidationErrors] = useState({
    dateHour: false,
    patient: false,
    professional: false,
    frequencyDays: false,
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const errors = {
      dateHour: !dateHour,
      patient: !patient,
      professional: !professional,
      frequencyDays:
        !frequencyDays ||
        isNaN(frequencyDays) ||
        frequencyDays <= 0,
    };

    setValidationErrors(errors);

    if (Object.values(errors).some(Boolean)) {
      return;
    }

    if (patient && professional && dateHour && frequencyDays > 0) {
      const initialDate = format(dateHour, "yyyy-MM-dd");   
      const hour = format(dateHour, "HH:mm:ss");  
      if(!editAppointment?.id) {
        throw new Error("ID do agendamento não encontrado");
      }
      
      await updateAppointmentRule(editAppointment?.id, {
        newFrequency: frequencyDays,
        newTime: hour,
      });

    }
  };

  return (
    <Card className="w-full mx-auto border-none shadow-none">
      <form onSubmit={handleSubmit}>
        <CardContent className="space-y-6">
          <div className="space-y-2">
            <Label htmlFor="data-hora">
              Data de Início e Horário <span className="text-red-500">*</span>
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
                value={patient.value}
                onChange={(value) => {
                  const selected = listPatients.find((p) => p.value === value);
                  if (selected) setPatient(selected);
                }}
                placeholder="Pesquisar paciente"
                className={cn(
                  validationErrors.patient && 'border-red-500',
                  'font-normal',
                  'text-gray-400'
                )}
              />
              {validationErrors.patient && (
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
              value={professional.value}
              onChange={(value) => {
                const selected = listaProfessional.find((p) => p.value == value);
                if (selected) setProfessional(selected);
              }}
              placeholder="Pesquisar área de atendimento"
              className={cn(
                validationErrors.professional && 'border-red-500',
                'font-normal',
                'text-gray-400'
              )}
            />
            {validationErrors.professional && (
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
              min="1"
              placeholder="Adicionar frequência"
              value={frequencyDays < 1 ? "" : frequencyDays}
              onChange={e => setFrequencyDays(Number(e.target.value))}
              className={cn(validationErrors.frequencyDays && 'border-red-500')}
            />
            {validationErrors.frequencyDays && (
              <p className="text-sm text-red-500">
                Frequência é obrigatória e deve ser maior que 0.
              </p>
            )}
          </div>
          <div className="flex justify-end">
            <Button className="w-full bg-[#0D4F97]  text-white hover:bg-blue-900 text-xs sm:w-auto sm:text-sm">
              {editAppointment ? 'Atualizar' : 'Cadastrar'}
            </Button>
          </div>
        </CardContent>
      </form>
    </Card>
  );
}
