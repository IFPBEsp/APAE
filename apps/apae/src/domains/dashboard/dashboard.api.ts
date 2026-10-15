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
  const response = await fetch(
    `/apae-geral/api/patients/absence-alert-ids?minAbsences=3`
  );

  if (!response.ok) {
    throw new Error("Erro ao buscar pacientes com alerta de faltas");
  }

  const ids: string[] = await response.json();
  return new Set(ids);
}
