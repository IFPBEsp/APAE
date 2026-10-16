import { TodayAppointment } from '@/types/appointment';
import { Page } from '@/types/pagination';
import { bffFetchJson } from '@/lib/bff-client';
import {
  listTodayAppointment,
  getAppointments,
  type AppointmentResponseDTO,
} from '@/app/services/appointmentService';

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
