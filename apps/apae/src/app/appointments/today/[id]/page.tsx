"use client";

import { format } from "date-fns";
import { useParams } from "next/navigation";
import { useEffect, useState, useRef } from "react";
import { AlertTriangle } from "lucide-react";

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@/components/ui/tooltip";

import { getTodayAppointmentById } from "@/app/services/appointmentService";
import { TodayAppointment } from "@/types/appointment";

export default function ViewTodayAppointment() {
  const { id } = useParams<{ id: string }>();
  const [appointment, setAppointment] = useState<TodayAppointment | null>(null);
  const [loading, setLoading] = useState(true);
  const initialized = useRef(false);

  const [alertPatientIds, setAlertPatientIds] = useState<Set<string>>(new Set());

  useEffect(() => {
    if (!id) return;
    if (initialized.current) return;

    async function loadData() {
      try {
        initialized.current = true;
        setLoading(true);

        // 1. Search for today's appointment
        const data = await getTodayAppointmentById(id as string);
        setAppointment(data);

        // 2. Search for patients with missed appointments via direct fetch.
        const response = await fetch("/apae-geral/api/patients/with-absences?minAbsences=3");

        if (response.ok) {
          const result = await response.json();
          const absencesList = result.content || [];
          const idsSet = new Set<string>(absencesList.map((item: any) => item.patient.id));
          setAlertPatientIds(idsSet);
        }
      } catch (error) {
        console.error("[ViewTodayAppointment]", error);
        setAppointment(null);
      } finally {
        setLoading(false);
      }
    }

    loadData();
  }, [id]);

  if (loading) {
    return <p className="mt-20 text-center">Carregando...</p>;
  }

  if (!appointment) {
    return <p className="mt-20 text-center">Agendamento não encontrado</p>;
  }

  const hasAbsenceAlert = alertPatientIds.has(appointment.patient?.id);

  return (
    <div className="mt-20 max-w-6xl mx-auto px-6 space-y-8">
      {/* HEADER */}
      <header className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Avatar className="w-16 h-16 bg-gray-100">
            <AvatarImage src="https://cdn-icons-png.flaticon.com/512/266/266033.png" />
            <AvatarFallback>{appointment.patient.fullName.charAt(0)}</AvatarFallback>
          </Avatar>

          <div className="flex items-center gap-2">
            <h1 className="text-xl font-bold text-[#0D4F97]">{appointment.patient.fullName}</h1>

            {hasAbsenceAlert && (
              <TooltipProvider>
                <Tooltip>
                  <TooltipTrigger asChild>
                    <AlertTriangle className="h-6 w-6 text-amber-500 cursor-help" />
                  </TooltipTrigger>
                  <TooltipContent>
                    <p>Paciente com 3+ faltas não justificadas</p>
                  </TooltipContent>
                </Tooltip>
              </TooltipProvider>
            )}
          </div>
        </div>

        <Badge
          className={`px-5 py-2 font-semibold border ${
            appointment.performed
              ? "border-green-600 bg-green-50 text-green-700"
              : "border-red-600 bg-red-50 text-red-700"
          }`}
        >
          {appointment.performed ? "Realizada" : "Pendente"}
        </Badge>
      </header>

      <Card className="border border-blue-100">
        <CardHeader>
          <CardTitle className="text-center text-[#0D4F97]">Agendamento</CardTitle>
        </CardHeader>

        <CardContent className="grid grid-cols-1 md:grid-cols-2 gap-6 text-sm text-[#0D4F97]">
          <div className="space-y-2">
            <p>
              <strong>Área de atendimento:</strong> {appointment.professional.healthSector || "—"}
            </p>
            <p>
              <strong>Status:</strong>{" "}
              <span
                className={
                  appointment.performed
                    ? "text-green-600 font-semibold"
                    : "text-red-600 font-semibold"
                }
              >
                {appointment.performed ? "Realizada" : "Pendente"}
              </span>
            </p>
          </div>

          <div className="space-y-2">
            <p>
              <strong>Data:</strong> {format(new Date(appointment.effectiveDateTime), "dd/MM/yyyy")}
            </p>
            <p>
              <strong>Horário:</strong> {format(new Date(appointment.effectiveDateTime), "HH:mm")}
            </p>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
