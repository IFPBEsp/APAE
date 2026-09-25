'use client';

import { Card, CardContent } from '@/components/ui/card';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '@/components/ui/tooltip';
import { RegisterAbsenceButton } from '@/components/buttons/RegisterAbsenceButton';
import { TodayAppointment } from '@/types/appointment';
import { format } from 'date-fns';
import { AlertTriangle } from 'lucide-react';
import Link from 'next/link';

interface AppointmentsTableProps {
  todayAppointments: TodayAppointment[];
  alertPatientIds: Set<string>;
  selectedDate: Date;
  setTodayAppointments: React.Dispatch<React.SetStateAction<TodayAppointment[]>>;
}

export function AppointmentsTable({
  todayAppointments,
  alertPatientIds,
  selectedDate,
  setTodayAppointments,
}: AppointmentsTableProps) {
  return (
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
                  {item.effectiveDateTime
                    ? format(new Date(item.effectiveDateTime), 'HH:mm')
                    : '—'}
                </TableCell>
                <TableCell className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">
                  <div className="flex items-center gap-2">
                    <span className="truncate">{item.patient.fullName}</span>
                    {alertPatientIds.has(item.patient.id) && (
                      <TooltipProvider>
                        <Tooltip>
                          <TooltipTrigger asChild>
                            <AlertTriangle className="h-4 w-4 text-amber-500 shrink-0 cursor-help" />
                          </TooltipTrigger>
                          <TooltipContent>
                            <p>Paciente com 3+ faltas não justificadas</p>
                          </TooltipContent>
                        </Tooltip>
                      </TooltipProvider>
                    )}
                  </div>
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
                  <RegisterAbsenceButton
                    generatedAppointmentId={item.id}
                    patientId={item.patient.id}
                    absenceDate={format(selectedDate, 'yyyy-MM-dd')}
                    disabled={item.hasAbsence}
                    onSuccess={() => {
                      setTodayAppointments((prev) =>
                        prev.map((a) =>
                          a.id === item.id ? { ...a, hasAbsence: true } : a
                        )
                      );
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
  );
}