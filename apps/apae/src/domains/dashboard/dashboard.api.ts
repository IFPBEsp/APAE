import type { TodayAppointment, AppointmentResponseDTO } from '@/domains/appointments/types/appointments.types';
import { Page } from '@/types/pagination';

import { bffFetchJson } from '@/lib/bff-client';
import { listTodayAppointment, getAppointments } from '@/domains/appointments/appointments.api';

export async function fetchTodayAppointments(date: string): Promise<Page<TodayAppointment>> {
  return listTodayAppointment(date);
}

export async function fetchAllAppointments(): Promise<Page<AppointmentResponseDTO>> {
  return getAppointments();
}

export async function fetchPatientsWithAbsences(): Promise<Set<string>> {
  const ids = await bffFetchJson<string[]>(
    `/apae-geral/api/patients/absence-alert-ids?minAbsences=3`
  );

  return new Set(ids);
}
