'use client';

import { useEffect, useState } from "react";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import {
  CalendarDays,
  Users,
  MessageCircleWarning,
  CalendarX,
} from "lucide-react";
import { Page } from '@/types/pagination';
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Calendar } from "@/components/ui/calendar";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { Badge } from "@/components/ui/badge";

import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
  DialogDescription,
} from "@/components/ui/dialog";
import { TodayAppointment } from '@/types/appointment';
import { getAppointments, listTodayAppointment, markAsPerformed, UUID, type AppointmentResponseDTO } from "./services/appointmentService";

import { AppointmentForm } from "@/components/forms/AppointmentForm";
import { InfoCard } from "@/components/shared/InfoCard";
import Link from "next/link";

import { Checkbox } from "@/components/ui/checkbox";

export default function DashboardPage() {
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());
  const [todayAppointments, setTodayAppointments] = useState<TodayAppointment[]>([]);

  const [allAppointments, setAllAppointments] = useState<AppointmentResponseDTO[]>([]);

  const fetchTodayAppointments = async () => {
    const todayAppointmentsPage: Page<TodayAppointment> = await listTodayAppointment();
    setTodayAppointments(todayAppointmentsPage.content);
  };

  const fetchAllAppointments = async () => {
    const allAppointmentsPage: Page<AppointmentResponseDTO> = await getAppointments();
    setAllAppointments(allAppointmentsPage.content);
  };

  useEffect(() => {
    fetchTodayAppointments();
    fetchAllAppointments();
  }, [selectedDate]);

  const markAsPerformedHandle = async (id: UUID) => {
    await markAsPerformed(id);
    window.location.reload();
  }

  return (
    <div className="min-h-screen w-full text-sm overflow-x-hidden">
      <main className="flex-1 p-3 sm:p-6 max-w-[100vw] mx-auto">
        <div className="mb-4 flex flex-col justify-between gap-3 sm:mb-6 sm:flex-row sm:items-center">
          <h1 className="text-lg font-bold sm:text-2xl text-[#0D4F97]">
            Agendamentos de Hoje
          </h1>
          <div className="flex flex-col gap-2 sm:flex-row sm:items-center">
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className="w-full justify-start bg-white border-[#0D4F97] text-left text-[#0D4F97] font-normal text-xs sm:w-[220px] sm:text-sm"
                >
                  <CalendarDays className="mr-2 h-4 w-4" />
                  {selectedDate ? (
                    format(selectedDate, "dd 'de' MMMM 'de' yyyy", {
                      locale: ptBR,
                    })
                  ) : (
                    <span>Escolha uma data</span>
                  )}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0 bg-white">
                <Calendar
                  mode="single"
                  selected={selectedDate}
                  onSelect={setSelectedDate}
                  initialFocus
                  locale={ptBR}
                  required
                />
              </PopoverContent>
            </Popover>

            <Dialog>
              <DialogTrigger asChild>
                <Button className="w-full bg-[#0D4F97] text-white hover:bg-blue-900 text-xs sm:w-auto sm:text-sm">
                  Novo agendamento
                </Button>
              </DialogTrigger>
              <DialogContent className="w-full sm:max-w-[425px]">
                <DialogHeader>
                  <DialogTitle className="text-[#0D4F97]">
                    Cadastrar Novo Agendamento
                  </DialogTitle>
                  <DialogDescription className="text-[#0D4F97] opacity-50">
                    Preencha os detalhes abaixo para agendar uma consulta.
                  </DialogDescription>
                </DialogHeader>
                <AppointmentForm />
              </DialogContent>
            </Dialog>
          </div>
        </div>

        <div className="mb-4 grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-4">
          <InfoCard
            title="Agendados pra hoje"
            icon={Users}
            value={todayAppointments.length}
            subtitle={`${
              todayAppointments.length
            } confirmados, ${
              todayAppointments.length
            } pendentes`}
            titleClassName="text-[#0D4F97]"
            valueClassName="text-[#0D4F97]"
          />
          <InfoCard
            title="Todos os agendamentos"
            icon={Users}
            value={allAppointments.length}
            titleClassName="text-[#0D4F97]"
            valueClassName="text-[#0D4F97]"
          />
        </div>

        <Card>
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead className="px-3 py-2 text-xs text-[#0D4F97] sm:px-4 sm:py-3 sm:text-sm">
                    Paciente
                  </TableHead>
                  <TableHead className="px-3 py-2 text-xs text-[#0D4F97] sm:px-4 sm:py-3 sm:text-sm">
                    Confirmou Presença
                  </TableHead>
                  <TableHead className="px-3 py-2 text-xs text-[#0D4F97] sm:px-4 sm:py-3 sm:text-sm">
                    Profissional
                  </TableHead>
                  <TableHead className="px-3 py-2 text-xs text-[#0D4F97] sm:px-4 sm:py-3 sm:text-sm">
                    Ações
                  </TableHead>
                  <TableHead className="px-3 py-2 text-xs text-[#0D4F97] sm:px-4 sm:py-3 sm:text-sm">
                    Realizada
                  </TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {todayAppointments.map((item, index) => (
                  <TableRow key={index}>
                    <TableCell className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">
                      {item.patient.fullName}
                    </TableCell>
                    <TableCell className="px-3 py-2">
                      <Badge
                        variant="outline"
                        className={`text-xs ${
                          true ? "text-green-400" : "text-red-400"
                        } sm:text-sm`}
                      >
                        {true ? "Sim" : "Não"}
                      </Badge>
                    </TableCell>
                    <TableCell className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">
                      {item.professional.name}
                    </TableCell>
                    <TableCell className="px-3 py-2">
                      <Link
                        href={`/agendamentos/${item.id}`}
                        className="cursor-pointer text-xs text-blue-800 underline hover:underline sm:text-sm"
                      >
                        Detalhes
                      </Link>
                    </TableCell>
                    <TableCell className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">
                      <Checkbox
                          checked={item.performed}
                          onCheckedChange={() => markAsPerformedHandle(item.id)}
                      />
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </main>
    </div>
  );
}
