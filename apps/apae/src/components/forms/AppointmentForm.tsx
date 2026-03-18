"use client";

import { format, getDay, isBefore, startOfDay, isValid } from "date-fns";
import { ptBR } from "date-fns/locale";
import { CalendarIcon } from "lucide-react";
import React, { useEffect, useMemo, useRef, useState } from "react";

import {
  Appointment,
  getPacientes,
  getProfissionaisDaSaude,
  saveAppointment,
  updateAppointmentRule,
} from "@/app/services/appointmentService";
import { Button } from "@/components/ui/button";
import { Calendar } from "@/components/ui/calendar";
import { Card, CardContent } from "@/components/ui/card";
import { Combobox } from "@/components/ui/combobox";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { cn } from "@/lib/utils";

interface Option {
  value: string;
  label: string;
}

interface AppointmentFormProps {
  editAppointment?: Appointment;
}

const mapDayOfWeek: Record<number, string> = {
  1: "SEGUNDA",
  2: "TERCA",
  3: "QUARTA",
  4: "QUINTA",
  5: "SEXTA",
};

const morningSlots = [
  "08:00",
  "08:30",
  "09:00",
  "09:30",
  "10:00",
  "10:30",
  "11:00",
  "11:30",
];
const afternoonSlots = [
  "13:00",
  "13:30",
  "14:00",
  "14:30",
  "15:00",
  "15:30",
  "16:00",
  "16:30",
  "17:00",
];

export function AppointmentForm({ editAppointment }: AppointmentFormProps) {
  const isInitialMount = useRef(true);
  const prevProfessionalId = useRef(editAppointment?.professional?.id || "");

  const [date, setDate] = useState<Date | undefined>(() => {
    if (editAppointment?.initialDate) {
      const parts = editAppointment.initialDate.split("-").map(Number);
      const d = new Date(parts[0], parts[1] - 1, parts[2]);

      return isValid(d) ? d : undefined;
    }

    return undefined;
  });

  const [selectedTime, setSelectedTime] = useState<string>(() => {
    if (editAppointment?.hour) {
      return editAppointment.hour.slice(0, 5);
    }

    return "";
  });

  const [isCalendarOpen, setIsCalendarOpen] = useState(false);
  const [patient, setPatient] = useState<Option>(() => {
    const p = editAppointment?.annualRegistration.patient;
    return p ? { value: p.id, label: p.fullName } : { value: "", label: "" };
  });
  const [professional, setProfessional] = useState<Option>(() => {
    const prof = editAppointment?.professional;

    return prof
      ? { value: prof.id, label: prof.name }
      : { value: "", label: "" };
  });

  const [listPatients, setListPatients] = useState<Option[]>([]);
  const [listaProfessional, setListaProfessionals] = useState<Option[]>([]);
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
      patient: !patient.value,
      professional: !professional.value,
      frequencyDays:
        !frequencyDays ||
        isNaN(frequencyDays) ||
        frequencyDays <= 0,
    };

    setValidationErrors(errors);

    if (Object.values(errors).some(Boolean)) {
      return;
    }

    if (patient.value && professional.value && dateHour && frequencyDays > 0) {
      const initialDate = format(dateHour, "yyyy-MM-dd");   
      const hour = format(dateHour, "HH:mm:ss");  
      
      if (editAppointment?.id) {
        await updateAppointmentRule(editAppointment.id, {
          newFrequency: frequencyDays,
          newTime: hour,
        });
      } else {
        await saveAppointment({
          patientId: patient.value,
          professionalId: professional.value,
          serviceId: "ea4c3a4d-c3f4-4a83-ab29-ff24c50e844c",
          initialDate,
          hour,
          frequencyDays,
        });
      }
    }
    window.location.reload();
  };

  return (
    <Card className="w-full mx-auto border-none shadow-none">
      <form onSubmit={handleSubmit}>
        <CardContent className="space-y-6">
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