"use client";

import { isValid } from "date-fns";
import React from "react";

import type { Appointment } from '@/domains/appointments/types/appointments.types';
import { useAppointmentForm } from "../hooks/useAppointmentForm";
import { AppointmentDatePicker } from "./AppointmentDatePicker";
import { AppointmentTimeSelect } from "./AppointmentTimeSelect";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Combobox } from "@/components/ui/combobox";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { cn } from "@/lib/utils";

interface AppointmentFormProps {
  editAppointment?: Appointment;
}

export function AppointmentForm({ editAppointment }: AppointmentFormProps) {
  const {
    date, setDate, selectedTime, setSelectedTime,
    isCalendarOpen, setIsCalendarOpen,
    patient, setPatient, professional, setProfessional,
    listPatients, listaProfessional, frequencyDays, setFrequencyDays,
    validTimeSlots, validationErrors, submitError,
    isDayDisabled, handleSubmit,
  } = useAppointmentForm(editAppointment);

  return (
    <Card className="w-full mx-auto border-none shadow-none">
      <form onSubmit={handleSubmit}>
        <CardContent className="space-y-6">
          {!editAppointment && (
            <div className="space-y-2">
              <Label>Paciente <span className="text-red-500">*</span></Label>
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
              {validationErrors.patient && <p className="text-sm text-red-500">Obrigatório.</p>}
            </div>
          )}

          <div className="space-y-2">
            <Label>Profissional da Saúde <span className="text-red-500">*</span></Label>
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
            {validationErrors.professional && <p className="text-sm text-red-500">Obrigatório.</p>}
          </div>

          <div className="space-y-4">
            <Label className="font-bold">Data de Início e Horário <span className="text-red-500">*</span></Label>
            {!professional.value ? (
              <div className="p-4 border rounded-md bg-slate-50 text-sm text-muted-foreground">
                Selecione um profissional.
              </div>
            ) : (
              <div className="flex flex-col gap-4">
                <div className="space-y-2">
                  <Label className="text-xs">Data de Início</Label>
                  <AppointmentDatePicker
                    date={date}
                    isCalendarOpen={isCalendarOpen}
                    hasError={validationErrors.date}
                    isDayDisabled={isDayDisabled}
                    onDateChange={(d) => { if (d && isValid(d)) { setDate(d); setSelectedTime(""); } }}
                    onOpenChange={setIsCalendarOpen}
                  />
                </div>
                <div className="space-y-2">
                  <Label className="text-xs">Horário</Label>
                  <AppointmentTimeSelect
                    date={date}
                    selectedTime={selectedTime}
                    validTimeSlots={validTimeSlots}
                    hasError={validationErrors.time}
                    onTimeChange={setSelectedTime}
                  />
                </div>
              </div>
            )}
          </div>

          <div className="space-y-2">
            <Label>Frequência <span className="text-red-500">*</span></Label>
            <Select
              onValueChange={(value) => setFrequencyDays(Number(value))}
              value={frequencyDays > 0 ? String(frequencyDays) : undefined}
            >
              <SelectTrigger className={cn("w-full", validationErrors.frequencyDays && "border-red-500")}>
                <SelectValue placeholder="Selecione a frequência" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="7">Semanal (a cada 7 dias)</SelectItem>
                <SelectItem value="14">Quinzenal (a cada 14 dias)</SelectItem>
                <SelectItem value="30">Mensal (a cada mês)</SelectItem>
              </SelectContent>
            </Select>
            {validationErrors.frequencyDays && <p className="text-sm text-red-500">Selecione uma frequência válida.</p>}
          </div>

          {submitError && (
            <div className="p-3 mt-4 bg-red-50 text-red-600 border border-red-200 rounded-md text-sm font-medium text-center">
              {submitError}
            </div>
          )}

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