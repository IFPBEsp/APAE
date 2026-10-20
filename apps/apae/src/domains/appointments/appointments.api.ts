import type {
  UUID,
  CreateAppointmentDTO,
  AppointmentResponseDTO,
  UpdateAppointmentDTO,
  RescheduleGeneratedAppointmentDTO,
  CancelGeneratedAppointmentDTO,
  GeneratedAppointmentResponseDTO,
  Patient,
  Professional,
} from './types/appointments.types';
import type { Page } from '@/types/pagination';
import type { TodayAppointment } from '@/domains/appointments/types/appointments.types';
import { bffFetchJson } from '@/lib/bff-client';

function ensurePageFormat<T>(data: unknown): Page<T> {
  if (data && Array.isArray((data as Page<T>).content)) {
    return data as Page<T>;
  }
  if (Array.isArray(data)) {
    return {
      content: data,
      totalElements: data.length,
      totalPages: 1,
      size: data.length,
      number: 0,
      first: true,
      last: true,
      empty: data.length === 0,
      length: data.length,
    } as Page<T>;
  }
  return {
    content: data ? [data as T] : [],
    totalElements: data ? 1 : 0,
    totalPages: 1,
    size: 1,
    number: 0,
    first: true,
    last: true,
    empty: !data,
    length: data ? 1 : 0,
  } as Page<T>;
}

export async function saveAppointment(
  dto: CreateAppointmentDTO,
): Promise<void> {
  await bffFetchJson(`/apae-geral/api/appointments`, {
    method: "POST",
    body: JSON.stringify(dto),
  });
}

export async function getAppointments(
  date?: string,
  time?: string,
  page: number = 0,
  size: number = 100,
): Promise<Page<AppointmentResponseDTO>> {
  const query = new URLSearchParams({
    page: `${page}`,
    size: `${size}`,
  });

  if (date) query.append("date", date);
  if (time) query.append("time", time);

  const res = await bffFetchJson<Page<AppointmentResponseDTO>>(`/apae-geral/api/appointments?${query}`);
  return ensurePageFormat<AppointmentResponseDTO>(res);
}

export async function getAppointmentById(
  id: UUID,
): Promise<AppointmentResponseDTO> {
  return bffFetchJson<AppointmentResponseDTO>(`/apae-geral/api/appointments/${id}`);
}

export async function updateAppointment(
  id: UUID,
  dto: UpdateAppointmentDTO,
): Promise<AppointmentResponseDTO> {
  return bffFetchJson<AppointmentResponseDTO>(`/apae-geral/api/appointments/${id}`, {
    method: "PATCH",
    body: JSON.stringify(dto),
  });
}

export async function deleteAppointment(id: UUID): Promise<void> {
  await bffFetchJson(`/apae-geral/api/appointments/${id}`, {
    method: "DELETE",
  });
}

export async function rescheduleGeneratedAppointment(
  id: UUID,
  dto: RescheduleGeneratedAppointmentDTO,
): Promise<GeneratedAppointmentResponseDTO> {
  const dateObj = new Date(dto.newDateTime);

  const year = dateObj.getFullYear();
  const month = String(dateObj.getMonth() + 1).padStart(2, "0");
  const day = String(dateObj.getDate()).padStart(2, "0");
  const hours = String(dateObj.getHours()).padStart(2, "0");
  const minutes = String(dateObj.getMinutes()).padStart(2, "0");
  const seconds = String(dateObj.getSeconds()).padStart(2, "0");

  const localDateTimeString = `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;

  const backendDto = {
    newDateTime: localDateTimeString,
  };

  return bffFetchJson<GeneratedAppointmentResponseDTO>(
    `/apae-geral/api/appointments/generated/${id}/reschedule`,
    {
      method: "PATCH",
      body: JSON.stringify(backendDto),
    }
  );
}

export async function markAsPerformed(
  id: UUID,
): Promise<GeneratedAppointmentResponseDTO> {
  return bffFetchJson<GeneratedAppointmentResponseDTO>(
    `/apae-geral/api/appointments/generated/${id}/performed`,
    {
      method: "PATCH",
    }
  );
}

export async function cancelGeneratedAppointment(
  id: UUID,
  dto: CancelGeneratedAppointmentDTO,
): Promise<GeneratedAppointmentResponseDTO> {
  return bffFetchJson<GeneratedAppointmentResponseDTO>(
    `/apae-geral/api/appointments/generated/${id}/cancel`,
    {
      method: "PATCH",
      body: JSON.stringify(dto),
    }
  );
}

export async function listByPatient(
  patientId: UUID,
  start: string,
  end: string,
  page: number = 0,
  size: number = 20,
): Promise<Page<GeneratedAppointmentResponseDTO>> {
  const query = new URLSearchParams({
    page: `${page}`,
    size: `${size}`,
  });

  if (start) query.append("start", start);
  if (end) query.append("end", end);

  const res = await bffFetchJson<Page<GeneratedAppointmentResponseDTO>>(
    `/apae-geral/api/appointments/patient/${patientId}?${query}`
  );
  return ensurePageFormat<GeneratedAppointmentResponseDTO>(res);
}

export async function getPatients(): Promise<Patient[]> {
  const data = await bffFetchJson<Page<Patient>>(`/apae-geral/api/patients?page=0&size=100`);
  return data.content || [];
}

export async function getHealthProfessionals(): Promise<Professional[]> {
  const data = await bffFetchJson<Page<Professional>>(`/apae-geral/api/professionals?page=0&size=100`);
  return data.content || [];
}

export async function getHealthProfessional(
  id: string,
): Promise<Professional> {
  return bffFetchJson<Professional>(`/apae-geral/api/professionals/${id}`);
}

export async function getServiceAreas(): Promise<string[]> {
  const professionals = await getHealthProfessionals();
  const areas = professionals.map((p) => p.serviceArea?.area);
  return [...new Set(areas)].filter(Boolean) as string[];
}

export const toggleConfirmation = async (id: UUID): Promise<void> => {
  const appointment = await getAppointmentById(id);

  if (!appointment.professional || !appointment.annualRegistration?.id) {
    throw new Error("Dados do agendamento incompletos para confirmaÃ§Ã£o");
  }

  const dto: CreateAppointmentDTO = {
    professionalId: appointment.professional.id,
    serviceId: appointment.serviceId,
    patientId: appointment.annualRegistration.patient.id,
    frequencyDays: appointment.frequencyDays,
    initialDate: appointment.initialDate,
    hour: appointment.hour.replace(":00", ""),
  };

  await saveAppointment(dto);
};

export async function getTodayAppointmentById(
  id: string,
): Promise<TodayAppointment> {
  return bffFetchJson<TodayAppointment>(`/apae-geral/api/appointments/today/${id}`);
}

export async function listTodayAppointment(
  date?: string,
  page: number = 0,
  size: number = 100
): Promise<Page<TodayAppointment>> {
  
  const query = new URLSearchParams({
    page: `${page}`,
    size: `${size}`,
  });

  if (date) {
    query.append("date", date);
  }

  const url = `/apae-geral/api/appointments/today?${query.toString()}`;

  const res = await bffFetchJson<Page<TodayAppointment>>(url);

  return res;
}
