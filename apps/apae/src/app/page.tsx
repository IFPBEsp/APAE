'use client';

import { Button } from '@/components/ui/button';
import { Calendar } from '@/components/ui/calendar';
import { Card, CardContent } from '@/components/ui/card';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { Page } from '@/types/pagination';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import {
  CalendarDays,
  Users,
  UserRoundCheck,
  UserRoundX
} from 'lucide-react';
import { useEffect, useState, useRef } from 'react';

import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import { TodayAppointment } from '@/types/appointment';
import {
  getAppointments,
  listTodayAppointment,
  markAsPerformed,
  UUID,
  type AppointmentResponseDTO,
} from './services/appointmentService';

import { AppointmentForm } from '@/components/forms/AppointmentForm';
import { InfoCard } from '@/components/shared/InfoCard';
import Link from 'next/link';

import { RegistrarFaltaButton } from '@/components/buttons/RegistrarFaltaButton';

export default function DashboardPage() {
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());
  const [todayAppointments, setTodayAppointments] = useState<TodayAppointment[]>([]);
  const [allAppointments, setAllAppointments] = useState<AppointmentResponseDTO[]>([]);
  const [activeAppointments, setActiveAppointments] = useState<TodayAppointment[]>([]);
  const [inactiveAppointments, setInactiveAppointments] = useState<TodayAppointment[]>([]);
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const lastFetchedDate = useRef<string | null>(null);


  const fetchTodayAppointments = async () => {
    const formattedDate = format(selectedDate, 'yyyy-MM-dd');  
    const todayAppointmentsPage: Page<TodayAppointment> =
      await listTodayAppointment(formattedDate); 
      
    setTodayAppointments(todayAppointmentsPage.content || []);
  };

  const fetchAllAppointments = async () => {
    const allAppointmentsPage: Page<AppointmentResponseDTO> =
      await getAppointments();
    setAllAppointments(allAppointmentsPage.content || []);
  };

  const fetchTodayAppointmentsByStatus = async () => {
    if (!todayAppointments.length || !allAppointments.length) return;

    const appointmentMap = new Map(
      allAppointments.map(a => [a.id, a])
    );

    const active: TodayAppointment[] = [];
    const inactive: TodayAppointment[] = [];

    for (const today of todayAppointments) {
      const related = appointmentMap.get(today.ruleId);

      if (!related) continue;

      if (related.isActive) {
        active.push(today);
      } else {
        inactive.push(today);
      }
    }

    setActiveAppointments(active);
    setInactiveAppointments(inactive);
  };

  useEffect(() => {

    const dateKey = format(selectedDate, 'yyyy-MM-dd');

    if (lastFetchedDate.current === dateKey) return;

    lastFetchedDate.current = dateKey;

    fetchTodayAppointments();
    fetchAllAppointments();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedDate]);

  useEffect(() => {
    fetchTodayAppointmentsByStatus();
  }, [todayAppointments, allAppointments]);

  const markAsPerformedHandle = async (id: UUID) => {
    await markAsPerformed(id);
    window.location.reload();
  };

  return (
    <div className="min-h-screen w-full text-sm overflow-x-hidden">
      <main className="flex-1 p-3 sm:p-6 max-w-[100vw] mx-auto">
        <div className="mb-4 flex flex-col justify-between gap-3 sm:mb-6 sm:flex-row sm:items-center">
          <h1 className="text-lg font-bold sm:text-2xl text-[#0D4F97]">
            Agendamentos do Dia
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
                  onSelect={(date) => date && setSelectedDate(date)}
                  initialFocus
                  locale={ptBR}
                  required
                />
              </PopoverContent>
            </Popover>

            <Dialog open={isCreateOpen} onOpenChange = {setIsCreateOpen}>
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
                {isCreateOpen && <AppointmentForm />}
              </DialogContent>
            </Dialog>
          </div>
        </div>

        <div className="mb-4 grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-4">
          <InfoCard
            title="Agendados pro dia"
            icon={Users}
            value={todayAppointments.length}
            subtitle={`${todayAppointments.filter(a => a.performed).length} realizados`}
            titleClassName="text-[#0D4F97]"
            valueClassName="text-[#0D4F97]"
          />
          <InfoCard
            title="Ativos"
            icon={UserRoundCheck}
            value={activeAppointments.length}
            titleClassName="text-[#0D4F97]"
            valueClassName="text-[#0D4F97]"
          />
          <InfoCard
            title="Inativos"
            icon={UserRoundX}
            value={inactiveAppointments.length}
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
                    Horário
                  </TableHead>
                  <TableHead className="px-3 py-2 text-xs text-[#0D4F97] sm:px-4 sm:py-3 sm:text-sm">
                    Paciente
                  </TableHead>
                  <TableHead className="px-3 py-2 text-xs text-[#0D4F97] sm:px-4 sm:py-3 sm:text-sm">
                    Profissional
                  </TableHead>
                  <TableHead className="px-3 py-2 text-xs text-[#0D4F97] sm:px-4 sm:py-3 sm:text-sm">
                    Ações
                  </TableHead>
                  <TableHead className="px-3 py-2 text-xs text-[#0D4F97] sm:px-4 sm:py-3 sm:text-sm">
                    Faltou
                  </TableHead>
                  <TableHead className="px-3 py-2 text-xs text-[#0D4F97] sm:px-4 sm:py-3 sm:text-sm">
                    Registrar Falta
                  </TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {todayAppointments.map((item, index) => (
                  <TableRow key={index}>
                    <TableCell className="px-3 py-2 font-bold text-[#0D4F97] text-xs sm:px-4 sm:py-3 sm:text-sm">
                      {item.effectiveDateTime ? format(new Date(item.effectiveDateTime), 'HH:mm') : '—'}
                    </TableCell>
                    <TableCell className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">
                      {item.patient.fullName}
                    </TableCell>
                    <TableCell className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">
                      {item.professional.name}
                    </TableCell>
                    <TableCell className="px-3 py-2">
                      <Link
                        href={`/appointments/today/${item.id}`}
                        className="cursor-pointer text-xs text-blue-800 underline hover:underline sm:text-sm"
                      >
                        Detalhes
                      </Link>
                    </TableCell>
                    <TableCell className="px-3 py-2">
                      {item.hasAbsence ? 'Sim' : 'Não'}
                    </TableCell>
                    <TableCell className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">
                      <RegistrarFaltaButton
                        generatedAppointmentId={item.id}
                        patientId={item.patient.id}
                        absenceDate={format(selectedDate, 'yyyy-MM-dd')}
                        disabled={item.hasAbsence}
                        onSuccess={() => {
                          setTodayAppointments(prev =>
                            prev.map(a =>
                              a.id === item.id
                                ? { ...a, hasAbsence: true }
                                : a
                            )
                          )
                        }}
                      />
                    </TableCell>
                  </TableRow>
                ))}
                {todayAppointments.length === 0 && (
                  <TableRow>
                    <TableCell colSpan={6} className="h-24 text-center text-muted-foreground">
                      Nenhum agendamento encontrado para esta data.
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </main>
    </div>
  );
}
