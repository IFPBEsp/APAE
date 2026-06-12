'use client';

import { useEffect, useState, useRef } from 'react';
import { useParams } from 'next/navigation';

import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Badge } from '@/components/ui/badge';
import {
  Card,
  CardAction,
  CardContent,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
  DialogDescription,
} from '@/components/ui/dialog';
import { Pencil, AlertTriangle } from 'lucide-react';
import { Button } from '@/components/ui/button';
import {
  getAppointmentById,
  Appointment,
  getProfessionalAreaName,
} from '@/app/services/appointmentService';
import { AppointmentForm } from '@/components/forms/AppointmentForm';
import TrashButton from '@/components/buttons/trashButton';
import { formatDatePTBR, separateAndTransformIntoNumber } from '@/lib/utils';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '@/components/ui/tooltip';

export default function ViewAppointment() {
  const { id } = useParams<{ id: string }>();
  const [appointment, setAppointment] = useState<Appointment | null>(null);
  const [loading, setLoading] = useState(true);
  const initialized = useRef(false);
  const [isEditOpen, setIsEditOpen] = useState(false);
  
  const [alertPatientIds, setAlertPatientIds] = useState<Set<string>>(new Set());

  useEffect(() => {
    if (!id) return;
    if (initialized.current) return;

    async function loadData() {
      try {
        initialized.current = true;
        
        // 1. Retrieve scheduling data
        const appointmentData = await getAppointmentById(id);
        setAppointment(appointmentData);

        // 2. Search the list of patients with missed appointments (direct fetch).
        const response = await fetch('/apae-geral/api/patients/with-absences?minAbsences=3');
        if (response.ok) {
          const data = await response.json();
          const absencesList = data.content || [];
          const idsSet = new Set<string>(absencesList.map((item: any) => item.patient.id));
          setAlertPatientIds(idsSet);
        }

      } catch (error) {
        console.error("Erro ao carregar dados:", error);
        initialized.current = false;
      } finally {
        setLoading(false);
      }
    }

    loadData();
  }, [id]);

  if (loading) {
    return <p className="mt-20 ml-10">Carregando...</p>;
  }

  if (!appointment) {
    return <p className="mt-20 ml-10">Agendamento não encontrado</p>;
  }

  const patient = appointment.annualRegistration.patient;
  
  const hasAbsenceAlert = alertPatientIds.has(patient.id);

  const [year, month, day] = separateAndTransformIntoNumber(appointment.initialDate, '-');
  const [hour, minute, second] = separateAndTransformIntoNumber(appointment.hour, ':');

  const dateTime =
    !isNaN(year) && !isNaN(month) && !isNaN(day) && !isNaN(hour) && !isNaN(minute) && !isNaN(second)
      ? new Date(year, month - 1, day, hour, minute, second)
      : null;

  return (
    <div className="mt-20 flex-1 mr-17 ml-10">
      <header className="flex flex-row items-center justify-between">
        <div className="flex items-center">
          <div className="bg-gray-200 rounded-full w-17 h-17 md:w-23 md:h-23">
            <Avatar className="p-5 w-17 h-17 md:w-23 md:h-23">
              <AvatarImage
                src="https://cdn-icons-png.flaticon.com/512/266/266033.png"
                alt="avatar"
              />
              <AvatarFallback>
                {patient.fullName.charAt(0) || '?'}
              </AvatarFallback>
            </Avatar>
          </div>
          <div className="flex items-center ml-10 gap-2">
            <h1 className="text-[#0D4F97] text-xl md:text-2xl font-bold mr-5">
              {patient.fullName}
            </h1>
            
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
          className={`${
            appointment.isActive
              ? 'bg-[#E6F6EC] border-l-4 border-[#16A34A] text-[#166534]'
              : 'bg-[#FEEAEA] border-l-4 border-[#DC2626] text-[#7F1D1D]'
          } px-4 py-2 w-32 flex flex-col items-center justify-center rounded-l-md shadow-sm font-bold text-sm text-center`}
        >
          <p className="text-sm font-bold">
            {appointment.isActive ? 'Ativa' : 'Não ativa'}
          </p>
        </Badge>
      </header>

      <main className="mt-7 mb-15">
        {/* Card Agendamento */}
        <Card className="text-[#0D4F97] mb-5">
          <CardHeader className="relative">
            <CardTitle className="absolute left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 font-bold text-center text-lg md:text-xl">
              Agendamento
            </CardTitle>
            <CardAction className="flex gap-1">
              <Dialog open={isEditOpen} onOpenChange={setIsEditOpen}>
                <DialogTrigger asChild>
                  <Button className="bg-transparent cursor-pointer border-1 border-[#0D4F97] text-[#0D4F97] hover:text-[#0d4f55] active:text-[#0d4ffe] hover:bg-[rgba(0,0,0,0.1)] transition-colors rounded-full overflow-hidden">
                    <Pencil />
                  </Button>
                </DialogTrigger>
                <DialogContent className="w-full sm:max-w-[425px]">
                  <DialogHeader>
                    <DialogTitle>Editar Agendamento</DialogTitle>
                    <DialogDescription>
                      Edite os detalhes abaixo para agendar uma consulta.
                    </DialogDescription>
                  </DialogHeader>
                  {isEditOpen && <AppointmentForm editAppointment={appointment}/>}
                </DialogContent>
              </Dialog>
              <div className="rounded-full overflow-hidden border-1 border-[#0D4F97]">
                <TrashButton id={id} realized={false} />
              </div>
            </CardAction>
          </CardHeader>
          <CardContent>
            <div className="flex justify-between">
              <div className="flex flex-col gap-1">
                <div className="flex">
                  <p className="font-medium mr-2">Data:</p>
                  <p>{dateTime ? new Intl.DateTimeFormat('pt-BR').format(dateTime) : '—'}</p>
                </div>
                <div className="flex">
                  <p className="font-medium mr-2">Horário:</p>
                  <p>{dateTime ? dateTime.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' }) : '—'}</p>
                </div>
                <div className="flex">
                  <p className="font-medium mr-2">Área de atendimento:</p>
                  <p>{getProfessionalAreaName(appointment.professional) || '—'}</p>
                </div>
                <div className="flex">
                  <p className="font-medium mr-2">Status:</p>
                  <p className={`font-bold ${appointment.isActive ? 'text-green-700' : 'text-red-700'}`}>
                    {appointment.isActive ? 'Ativo' : 'Não ativo'}
                  </p>
                </div>
              </div>
              <div className="flex flex-col items-start w-1/3">
                <div className="flex items-center">
                  <p className="font-medium mr-2">Período:</p>
                  <p>{appointment.frequencyDays !== undefined ? `${appointment.frequencyDays} dias` : '—'}</p>
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

      </main>
    </div>
  );
}
