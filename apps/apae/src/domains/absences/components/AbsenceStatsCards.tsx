"use client";

import { InfoCard } from "@/components/shared/InfoCard";
import type { DashboardOverview } from "@/domains/dashboard/dashboard.types";
import { Users } from "lucide-react";

interface AbsenceStatsCardsProps {
  statistics: DashboardOverview;
}

export function AbsenceStatsCards({ statistics }: AbsenceStatsCardsProps) {
  return (
    <div className="mb-6 grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-4">
      <InfoCard
        title="Total de Pacientes"
        icon={Users}
        value={statistics.totalPatients}
        titleClassName="text-[#0D4F97]"
        valueClassName="text-[#0D4F97]"
      />
      <InfoCard
        title="Pacientes com 3+ Faltas"
        icon={Users}
        value={statistics.totalPatientsWithAbsences}
        titleClassName="text-red-600"
        valueClassName="text-red-600"
      />
    </div>
  );
}
