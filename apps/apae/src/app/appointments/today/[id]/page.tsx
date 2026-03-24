'use client';

import { format } from 'date-fns';
import { useParams } from 'next/navigation';
import { useEffect, useState } from 'react';

import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Badge } from '@/components/ui/badge';
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';

import { getTodayAppointmentById } from '@/app/services/appointmentService';
import { TodayAppointment } from '@/types/appointment';

export default function ViewTodayAppointment() {
  const { id } = useParams<{ id: string }>();
  const [appointment, setAppointment] = useState<TodayAppointment | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!id) return;

    async function loadTodayAppointment() {
      try {
        const data = await getTodayAppointmentById(id);
        setAppointment(data);
      } catch (error) {
        console.error('[ViewTodayAppointment]', error);
        setAppointment(null);
      } finally {
        setLoading(false);
      }
    }

    loadTodayAppointment();
  }, [id]);

  if (loading) {
    return <p className="mt-20 text-center">Carregando...</p>;
  }

  if (!appointment) {
    return <p className="mt-20 text-center">Agendamento não encontrado</p>;
  }

  return (
    <div className="mt-20 max-w-6xl mx-auto px-6 space-y-8">
      {/* HEADER */}
      <header className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Avatar className="w-16 h-16 bg-gray-100">
            <AvatarImage src="https://cdn-icons-png.flaticon.com/512/266/266033.png" />
            <AvatarFallback>
              {appointment.patient.fullName.charAt(0)}
            </AvatarFallback>
          </Avatar>

          <h1 className="text-xl font-bold text-[#0D4F97]">
            {appointment.patient.fullName}
          </h1>
        </div>

        <Badge
            className={`px-5 py-2 font-semibold border ${
              appointment.performed
                ? 'border-green-600 bg-green-50 text-green-700'
                : 'border-red-600 bg-red-50 text-red-700'
            }`}>
            {appointment.performed ? 'Realizada' : 'Pendente'}
        </Badge>
      </header>

      {/* AGENDAMENTO GERADO */}
      <Card className="border border-blue-100">
        <CardHeader>
          <CardTitle className="text-center text-[#0D4F97]">
            Agendamento
          </CardTitle>
        </CardHeader>

        <CardContent className="grid grid-cols-1 md:grid-cols-2 gap-6 text-sm text-[#0D4F97]">
            <div className="space-y-2">
              <p><strong>Área de atendimento:</strong> {appointment.professional.healthSector || '—'}</p>
              <p>
                <strong>Status:</strong>{' '}
                <span
                  className={
                    appointment.performed
                      ? 'text-green-600 font-semibold'
                      : 'text-red-600 font-semibold'
                  }
                >
                  {appointment.performed ? 'Realizada' : 'Pendente'}
                </span>
              </p>
          </div>

          <div className="space-y-2">
            <p><strong>Data:</strong> {format(new Date(appointment.effectiveDateTime), 'dd/MM/yyyy')}</p>
            <p><strong>Horário:</strong> {format(new Date(appointment.effectiveDateTime), 'HH:mm')}</p>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
