'use client';

import { InfoCard } from '@/components/shared/InfoCard';
import { TodayAppointment } from '@/types/appointment';
import { type AppointmentResponseDTO } from '@/app/services/appointmentService';
import { Users, UserRoundCheck, UserRoundX } from 'lucide-react';

interface DashboardSummaryCardsProps {
  todayAppointments: TodayAppointment[];
  allAppointments: AppointmentResponseDTO[];
}

export function DashboardSummaryCards({
  todayAppointments,
  allAppointments,
}: DashboardSummaryCardsProps) {
  return (
    <div className="mb-4 grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-4">
      <InfoCard
        title="Agendados pro dia"
        icon={Users}
        value={todayAppointments.length}
        subtitle={`${todayAppointments.filter((a) => a.performed).length} realizados`}
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
      <InfoCard
        title="Ativos"
        icon={UserRoundCheck}
        value={allAppointments.filter((a) => a.isActive).length}
        titleClassName="text-[#0D4F97]"
        valueClassName="text-[#0D4F97]"
      />
      <InfoCard
        title="Inativos"
        icon={UserRoundX}
        value={allAppointments.filter((a) => !a.isActive).length}
        titleClassName="text-[#0D4F97]"
        valueClassName="text-[#0D4F97]"
      />
    </div>
  );
}