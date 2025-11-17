'use client';

import React, { useContext, useEffect, useState } from 'react';
import { Card, CardContent } from '@/components/ui/card';
import { Label } from '@/components/ui/label';
import { Button } from '@/components/ui/button';
import { DateTimePicker } from '@/components/ui/date-time-picker';
import { Combobox } from '@/components/ui/combobox';
import { cn } from '@/lib/utils';
import {
  Appointment,
  getPacientes,
  getProfissionalDaSaude,
  Patient,
  Professional,
  saveAppointment,
} from '@/app/services/appointmentService';
import { Input } from '../ui/input';

type selectItem = {
  value: string;
  label: string;
};

interface PageProps {
  editAppointment?: Appointment;
}

export function AppointmentForm({ editAppointment }: PageProps) {
  const separaETransformaEmNumero = (valor: unknown, separador: string) => {
    if (typeof valor == 'string' && valor.length) {
      return (valor as string).split(separador).map(n => parseInt(n));
    }
    return [NaN, NaN, NaN];
  };

  const [year, month, day] = separaETransformaEmNumero(
    editAppointment?.initialDate,
    '-'
  );
  const [hour, minute, second] = separaETransformaEmNumero(
    editAppointment?.hour,
    ':'
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

  const [patient, setPatient] = useState<string>(
    editAppointment?.annualRegistration.patient.fullName || ''
  );
  const [professional, setProfessional] = useState<string>(
    editAppointment?.professionalId || ''
  );
  const [endDate, setEndDate] = useState<string>(
    editAppointment?.endDate || ''
  );
  const [creationDate, setCreationDate] = useState<string>(
    editAppointment?.creationDate || ''
  );
  const [isActive, setIsActive] = useState<boolean>(
    editAppointment?.isActive || false
  );
  const [listPatients, setListPatients] = useState<selectItem[]>([]);
  const [listaProfessional, setListaProfessionals] = useState<selectItem[]>([]);

  const [frequencyDays, setFrequencyDays] = useState<string | number>(
    editAppointment?.frequencyDays || ''
  );

  useEffect(() => {
    const fetchPatientsAndProfessionals = async () => {
      const registeredPatients: Patient[] = await getPacientes();
      const registeredProfessionals: Professional[] =
        await getProfissionaisDaSaude();

      setListPatients(
        registeredPatients.map(
          p => ({ value: p.id, label: p.name } as selectItem)
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
  useEffect(() => {
    const redefineProfissional = async () => {
      if (editAppointment) {
        const nameProfessional = (await getProfissionalDaSaude(professional))
          .name;
        const idProfessional = listaProfessional.find(
          p => p.label === nameProfessional
        )?.value;
        setProfessional(idProfessional || professional);
        console.log(professional, nameProfessional, idProfessional);
      }
    };
    redefineProfissional();
  }, [listaProfessional]);

  const [validationErrors, setValidationErrors] = useState({
    dateHour: false,
    patient: false,
    professional: false,
    frequencyDays: false,
  });

  const formatDate = (date: Date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth()).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return [`${year}-${month}-${day}`, `${hours}:${minutes}:${seconds}`];
  };

  const dataPassou = (data: string, horario: string) => {
    const [year, month, day] = data.split('-');
    const [hour, minute, second] = horario.split(':');
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
      patient: !patient,
      professional: !professional,
      frequencyDays:
        !frequencyDays ||
        isNaN(Number(frequencyDays)) ||
        Number(frequencyDays) <= 0,
    };

    setValidationErrors(errors);

    if (Object.values(errors).some(Boolean)) {
      return;
    }

    const frequency = Number(frequencyDays);

    if (patient && professional && dateHour && frequency > 0) {
      await saveAppointment({
        annualRegistrationId: patient,
        serviceId: 'service-001',
        professionalId: professional,
        frequencyDays: 15,
        initialDate: formatDate(dateHour)[0],
        hour: formatDate(dateHour)[1],
      });
      window.location.reload();
    }

    console.log('Novo agendamento: ', {
      dateHour,
      patient,
      professional,
      frequencyDays,
    });
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
                value={patient}
                onChange={setPatient}
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
              value={professional}
              onChange={setProfessional}
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
