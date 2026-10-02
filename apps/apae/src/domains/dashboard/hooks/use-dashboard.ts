'use client';

import { useEffect, useRef, useState } from 'react';
import { format } from 'date-fns';
import type { TodayAppointment, AppointmentResponseDTO } from '@/domains/appointments/types/appointments.types';
import {
  fetchTodayAppointments,
  fetchAllAppointments,
  fetchPatientsWithAbsences,
} from '../dashboard.api';

export function useDashboard() {
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());
  const [todayAppointments, setTodayAppointments] = useState<TodayAppointment[]>([]);
  const [allAppointments, setAllAppointments] = useState<AppointmentResponseDTO[]>([]);
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const [alertPatientIds, setAlertPatientIds] = useState<Set<string>>(new Set());
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

    fetchPatientsWithAbsences()
      .then(setAlertPatientIds)
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
