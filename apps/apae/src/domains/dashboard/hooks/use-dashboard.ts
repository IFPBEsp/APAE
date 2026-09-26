'use client';

import { useEffect, useRef, useState } from 'react';
import { format } from 'date-fns';
import { TodayAppointment } from '@/types/appointment';
import { type AppointmentResponseDTO } from '@/app/services/appointmentService';
import {
  fetchTodayAppointments,
  fetchAllAppointments,
  fetchPatientsWithAbsences,
} from '../dashboard.api';

export function useDashboard() {
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());
  const [todayAppointments, setTodayAppointments] = useState<TodayAppointment[]>([]);
  const [allAppointments, setAllAppointments] = useState<AppointmentResponseDTO[]>([]);
  const [activeAppointments, setActiveAppointments] = useState<AppointmentResponseDTO[]>([]);
  const [inactiveAppointments, setInactiveAppointments] = useState<AppointmentResponseDTO[]>([]);
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

  useEffect(() => {
    setActiveAppointments(allAppointments.filter((a) => a.isActive === true));
    setInactiveAppointments(allAppointments.filter((a) => a.isActive === false));
  }, [allAppointments]);

  return {
    selectedDate,
    setSelectedDate,
    todayAppointments,
    setTodayAppointments,
    allAppointments,
    activeAppointments,
    inactiveAppointments,
    isCreateOpen,
    setIsCreateOpen,
    alertPatientIds,
  };
}