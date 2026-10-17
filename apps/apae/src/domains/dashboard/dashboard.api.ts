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
