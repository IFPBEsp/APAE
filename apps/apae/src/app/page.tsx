'use client';

import { useDashboard } from '@/domains/dashboard/hooks/use-dashboard';
import { DashboardHeader } from '@/domains/dashboard/components/DashboardHeader';
import { DashboardSummaryCards } from '@/domains/dashboard/components/DashboardSummaryCards';
import { AppointmentsTable } from '@/domains/dashboard/components/AppointmentsTable';

export default function DashboardPage() {
  const {
    selectedDate, setSelectedDate,
    todayAppointments, setTodayAppointments,
    allAppointments,
    isCreateOpen, setIsCreateOpen,
    alertPatientIds,
  } = useDashboard();

  return (
    <div className="min-h-screen w-full text-sm overflow-x-hidden">
      <main className="flex-1 p-3 sm:p-6 max-w-[100vw] mx-auto">
        <DashboardHeader
          selectedDate={selectedDate}
          setSelectedDate={setSelectedDate}
          isCreateOpen={isCreateOpen}
          setIsCreateOpen={setIsCreateOpen}
        />
        <DashboardSummaryCards
          todayAppointments={todayAppointments}
          allAppointments={allAppointments}
        />
        <AppointmentsTable
          todayAppointments={todayAppointments}
          alertPatientIds={alertPatientIds}
          selectedDate={selectedDate}
          setTodayAppointments={setTodayAppointments}
        />
      </main>
    </div>
  );
}