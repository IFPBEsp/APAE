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
    editAppointment?.frequencyDays || 0,
  );
  const [availabilities, setAvailabilities] = useState<
    { day: string; shift: string }[]
  >([]);

  const [validationErrors, setValidationErrors] = useState({
    date: false,
    time: false,
    patient: false,
    professional: false,
    frequencyDays: false,
  });

  useEffect(() => {
    const fetchData = async () => {
      const [patients, professionals] = await Promise.all([
        getPacientes(),
        getProfissionaisDaSaude(),
      ]);

      setListPatients(
        patients.map((p) => ({ value: p.id, label: p.fullName })),
      );

      setListaProfessionals(
        professionals.map((p) => ({ value: p.id, label: p.name })),
      );
    };
    fetchData();
  }, []);

  useEffect(() => {
    const loadProfessionalData = async () => {
      if (professional.value) {
        try {
          const res = await fetch(`/api/professionals/${professional.value}`);
          const data = await res.json();
          setAvailabilities(data.availabilities || []);
        } catch {
          setAvailabilities([]);
        }
      } else {
        setAvailabilities([]);
      }
    };

    loadProfessionalData();

    if (!isInitialMount.current) {
      if (professional.value !== prevProfessionalId.current) {
        setSelectedTime("");
      }
    }

    prevProfessionalId.current = professional.value;
    isInitialMount.current = false;
  }, [professional.value]);

  const [availableTimeSlots, setAvailableTimeSlots] = useState<string[]>([]);

  useEffect(() => {
  const fetchAvailableTimes = async () => {
    if (!date || !professional.value) return;

    try {
      const formattedDate = format(date, "yyyy-MM-dd");

      const res = await fetch(
        `/api/professionals/${professional.value}/available-times?date=${formattedDate}`
      );

      const data = await res.json();

      setAvailableTimeSlots(data || []);
    } catch (err) {
      setAvailableTimeSlots([]);
    }
  };

  fetchAvailableTimes();
}, [date, professional.value]);

  const isDayDisabled = (day: Date) => {
    const today = startOfDay(new Date());
    const dayOfWeek = getDay(day);

    if (dayOfWeek === 0 || dayOfWeek === 6) return true;

    const dayName = mapDayOfWeek[dayOfWeek];
    const isPast = isBefore(day, today);
    const professionalWorksThisDay = availabilities.some(
      (a) => a.day === dayName,
    );

    if (editAppointment?.initialDate) {
      const [y, m, d] = editAppointment.initialDate.split("-").map(Number);
      const editDate = new Date(y, m - 1, d);

      if (format(day, "yyyy-MM-dd") === format(editDate, "yyyy-MM-dd")) {
        return false;
      }
    }

    return isPast || !professionalWorksThisDay;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const errors = {
      date: !date || !isValid(date),
      time: !selectedTime,
      patient: !patient.value,
      professional: !professional.value,
      frequencyDays:
        !frequencyDays || isNaN(frequencyDays) || frequencyDays <= 0,
    };

    setValidationErrors(errors);
    if (Object.values(errors).some(Boolean)) return;

    const initialDate = format(date!, "yyyy-MM-dd");
    const hourStr = `${selectedTime}:00`;

    if (editAppointment?.id) {
      await updateAppointmentRule(editAppointment.id, {
        newFrequency: frequencyDays,
        newTime: hourStr,
      });
    } else {
      await saveAppointment({
        patientId: patient.value,
        professionalId: professional.value,
        serviceId: "ea4c3a4d-c3f4-4a83-ab29-ff24c50e844c",
        initialDate,
        hour: hourStr,
        frequencyDays,
      });
    }
    window.location.reload();
  };

  return (
    <Card className="w-full mx-auto border-none shadow-none">
      <form onSubmit={handleSubmit}>
        <CardContent className="space-y-6">
          {!editAppointment && (
            <div className="space-y-2">
              <Label>
                Paciente <span className="text-red-500">*</span>
              </Label>

              <Combobox
                options={listPatients}
                value={patient.value}
                onChange={(val) => {
                  const selected = listPatients.find((p) => p.value === val);
                  if (selected) setPatient(selected);
                }}
                placeholder="Pesquisar paciente"
                className="w-full"
              />

              {validationErrors.patient && (
                <p className="text-sm text-red-500">Obrigatório.</p>
              )}
            </div>
          )}

          <div className="space-y-2">
            <Label>
              Profissional da Saúde <span className="text-red-500">*</span>
            </Label>

            <Combobox
              options={listaProfessional}
              value={professional.value}
              onChange={(val) => {
                const selected = listaProfessional.find((p) => p.value === val);
                if (selected) setProfessional(selected);
              }}
              placeholder="Pesquisar profissional"
              className="w-full"
            />

            {validationErrors.professional && (
              <p className="text-sm text-red-500">Obrigatório.</p>
            )}
          </div>

          <div className="space-y-4">
            <Label className="font-bold">
              Data de Início e Horário <span className="text-red-500">*</span>
            </Label>

            {!professional.value ? (
              <div className="p-4 border rounded-md bg-slate-50 text-sm text-muted-foreground">
                Selecione um profissional.
              </div>
            ) : (
              <div className="flex flex-col gap-4">
                <div className="flex flex-col space-y-2">
                  <Label className="text-xs">Data de Início</Label>

                  <Popover
                    open={isCalendarOpen}
                    onOpenChange={setIsCalendarOpen}
                  >
                    <PopoverTrigger asChild>
                      <Button
                        variant={"outline"}
                        className={cn(
                          "w-full justify-start text-left font-normal",
                          !date && "text-muted-foreground",
                          validationErrors.date && "border-red-500",
                        )}
                      >
                        <CalendarIcon className="mr-2 h-4 w-4" />

                        {date && isValid(date) ? (
                          format(date, "PPP", { locale: ptBR })
                        ) : (
                          <span>Selecione uma data</span>
                        )}
                      </Button>
                    </PopoverTrigger>

                    <PopoverContent className="w-auto p-0" align="start">
                      <Calendar
                        mode="single"
                        selected={date}
                        onSelect={(d) => {
                          if (d && isValid(d)) {
                            setDate(d);
                            setSelectedTime("");
                          }
                          setIsCalendarOpen(false);
                        }}
                        disabled={isDayDisabled}
                        initialFocus
                      />
                    </PopoverContent>
                  </Popover>
                </div>

                <div className="flex flex-col space-y-2">
                  <Label className="text-xs">Horário</Label>

                  <Select
                    disabled={!date}
                    onValueChange={setSelectedTime}
                    value={selectedTime}
                  >
                    <SelectTrigger
                      className={cn(
                        "w-full",
                        validationErrors.time && "border-red-500",
                      )}
                    >
                      <SelectValue
                        placeholder={
                          date
                            ? "Escolha o horário"
                            : "Selecione a data primeiro"
                        }
                      />
                      </SelectTrigger>

                      <SelectContent>
                      {availableTimeSlots.length === 0 ? (
                        <div className="p-2 text-sm text-muted-foreground text-red-500">
                          Nenhum horário disponível
                        </div>
                      ) : (
                        availableTimeSlots.map((slot) => (
                          <SelectItem key={slot} value={slot}>
                            {slot}
                          </SelectItem>
                        ))
                      )}
                    </SelectContent>
                  </Select>
                </div>
              </div>
            )}
          </div>

          <div className="space-y-2">
            <Label>
              Frequência (dias) <span className="text-red-500">*</span>
            </Label>

            <Input
              type="number"
              min="1"
              value={frequencyDays < 1 ? "" : frequencyDays}
              onChange={(e) => setFrequencyDays(Number(e.target.value))}
              placeholder="Ex: 7"
              className="w-full"
            />
            {validationErrors.frequencyDays && (
              <p className="text-sm text-red-500">Maior que 0.</p>
            )}
          </div>

          <div className="flex justify-end pt-4">
            <Button className="w-full bg-[#0D4F97] text-white hover:bg-blue-900 sm:w-auto">
              {editAppointment ? "Atualizar" : "Cadastrar"}
            </Button>
          </div>
        </CardContent>
      </form>
    </Card>
  );
}
