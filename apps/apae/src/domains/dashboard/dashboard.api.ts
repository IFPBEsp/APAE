import { TodayAppointment } from '@/types/appointment';
import { Page } from '@/types/pagination';
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
  const response = await fetch('/apae-geral/api/patients/with-absences?minAbsences=3');

  if (!response.ok) {
    throw new Error('Erro ao buscar pacientes com faltas');
  }

  const data = await response.json();
  const list = data.content || [];
  return new Set<string>(list.map((item: any) => item.patient.id));
}