'use client';

import { useEffect, useRef, useState } from 'react';
import { format } from 'date-fns';
import { TodayAppointment } from '@/types/appointment';
import { type AppointmentResponseDTO } from '@/app/services/appointmentService';
import {
  fetchTodayAppointments,
  fetchAllAppointments,
} from '../dashboard.api';
import { useAbsenceAlerts } from '@/hooks/use-absence-alerts';

export function useDashboard() {
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());
  const [todayAppointments, setTodayAppointments] = useState<TodayAppointment[]>([]);
  const [allAppointments, setAllAppointments] = useState<AppointmentResponseDTO[]>([]);
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const { alertPatientIds } = useAbsenceAlerts();
  const lastFetchedDate = useRef<string | null>(null);

  useEffect(() => {
    const dateKey = format(selectedDate, 'yyyy-MM-dd');

    if (lastFetchedDate.current === dateKey) return;
    lastFetchedDate.current = dateKey;

    fetchTodayAppointments(dateKey)
      .then((page) => setTodayAppointments(page.content || []))
      .catch(console.error);

    fetchAllAppointments()
      .then((page) => setAllAppointments(page.content || []))
      .catch(console.error);
  }, [selectedDate]);

  return {
    selectedDate,
    setSelectedDate,
    todayAppointments,
    setTodayAppointments,
    allAppointments,
    isCreateOpen,
    setIsCreateOpen,
    alertPatientIds,
  };
}
