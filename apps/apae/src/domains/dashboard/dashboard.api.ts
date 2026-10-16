import type { TodayAppointment, AppointmentResponseDTO } from '@/domains/appointments/types/appointments.types';
import { Page } from '@/types/pagination';
import { listTodayAppointment, getAppointments } from '@/domains/appointments/appointments.api';

export async function fetchTodayAppointments(date: string): Promise<Page<TodayAppointment>> {
  return listTodayAppointment(date);
}

export async function fetchAllAppointments(): Promise<Page<AppointmentResponseDTO>> {
  return getAppointments();
}
