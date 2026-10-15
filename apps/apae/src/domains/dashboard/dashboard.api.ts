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
  const data = await bffFetchJson<{ content: Array<{ patient: { id: string } }> }>(
    '/apae-geral/api/patients/with-absences?minAbsences=3'
  );
  const list = data.content || [];
  return new Set<string>(list.map((item: { patient: { id: string } }) => item.patient.id));
}
