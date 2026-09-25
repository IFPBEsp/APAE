import type {
  UUID,
  CreateAppointmentDTO,
  AppointmentResponseDTO,
  UpdateAppointmentDTO,
  RescheduleGeneratedAppointmentDTO,
  CancelGeneratedAppointmentDTO,
  GeneratedAppointmentResponseDTO,
  Absence,
  Patient,
  Professional,
} from './types/appointments.types';
import type { Page } from '@/types/pagination';
import type { TodayAppointment } from '@/domains/appointments/types/appointments.types';

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
  const res = await fetch(`/apae-geral/api/appointments`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(dto),
  });

  if (!res.ok) {
    const errorData = await res.json().catch(() => null);

    if (errorData && errorData.message) {
      let cleanMessage = errorData.message;

      if (cleanMessage.includes("BAD_REQUEST") && cleanMessage.includes('"')) {
        cleanMessage = cleanMessage.split('"')[1] || cleanMessage;
      }

      throw new Error(cleanMessage);
    }

    throw new Error(`Erro ao criar agendamento: ${res.status}`);
  }
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

  const response = await fetch(`/apae-geral/api/appointments?${query}`);

  if (!response.ok) {
    throw new Error("Erro ao buscar agendamentos");
  }

  const res = await response.json();
  return ensurePageFormat<AppointmentResponseDTO>(res);
}

export async function getAppointmentById(
  id: UUID,
): Promise<AppointmentResponseDTO> {
  const response = await fetch(`/apae-geral/api/appointments/${id}`);

  if (!response.ok) {
    throw new Error("Erro ao buscar detalhes do agendamento");
  }

  return await response.json();
}

export async function updateAppointment(
  id: UUID,
  dto: UpdateAppointmentDTO,
): Promise<AppointmentResponseDTO> {
  const response = await fetch(`/apae-geral/api/appointments/${id}`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(dto),
  });

  if (!response.ok) {
    const error = await response.json();

    const message = error?.message || "Erro ao atualizar regra do agendamento";
    throw new Error(`Erro: ${message}`);
  }

  return await response.json();
}

export async function deleteAppointment(id: UUID): Promise<void> {
  const response = await fetch(`/apae-geral/api/appointments/${id}`, {
    method: "DELETE",
  });

  if (!response.ok) {
    throw new Error("Erro ao excluir agendamento");
  }
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

  const response = await fetch(`/apae-geral/api/appointments/generated/${id}/reschedule`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(backendDto),
  });

  if (!response.ok) {
    throw new Error("Erro ao reagendar consulta");
  }

  return await response.json();
}

export async function markAsPerformed(
  id: UUID,
): Promise<GeneratedAppointmentResponseDTO> {
  const response = await fetch(`/apae-geral/api/appointments/generated/${id}/performed`, {
    method: "PATCH",
  });

  if (!response.ok) {
    throw new Error(`Erro ao marcar consulta como realizada`);
  }

  return await response.json();
}

export async function cancelGeneratedAppointment(
  id: UUID,
  dto: CancelGeneratedAppointmentDTO,
): Promise<GeneratedAppointmentResponseDTO> {
  const response = await fetch(`/apae-geral/api/appointments/generated/${id}/cancel`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(dto),
  });

  if (!response.ok) {
    throw new Error(`Erro ao cancelar consulta`);
  }

  return await response.json();
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

  const response = await fetch(
    `/apae-geral/api/appointments/patient/${patientId}?${query}`,
  );

  if (!response.ok) {
    throw new Error("Erro ao buscar agendamentos do paciente");
  }

  const res = await response.json();
  return ensurePageFormat<GeneratedAppointmentResponseDTO>(res);
}

export async function registerAbsence(
  generatedAppointmentId: UUID,
  justification: string,
): Promise<Absence> {
  const dateObj = new Date();
  const year = dateObj.getFullYear();
  const month = String(dateObj.getMonth() + 1).padStart(2, "0");
  const day = String(dateObj.getDate()).padStart(2, "0");
  const localDateString = `${year}-${month}-${day}`;

  const body = {
    generatedAppointmentId,
    justification,
    date: localDateString,
    notified: false,
  };

  const res = await fetch(`/apae-geral/api/absences`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });

  if (!res.ok) {
    throw new Error(`Erro ao registrar ausência`);
  }

  return await res.json();
}

export async function getPatients(): Promise<Patient[]> {
  const response = await fetch(`/apae-geral/api/patients?page=0&size=100`);

  if (!response.ok) {
    throw new Error("Erro ao buscar pacientes");
  }

  const data = await response.json();
  return data.content || data || [];
}

export async function getHealthProfessionals(): Promise<Professional[]> {
  const response = await fetch(`/apae-geral/api/professionals?page=0&size=100`);

  if (!response.ok) {
    throw new Error("Erro ao buscar profissionais");
  }

  const data = await response.json();
  return data.content || data || [];
}

export async function getHealthProfessional(
  id: string,
): Promise<Professional> {
  const response = await fetch(`/apae-geral/api/professionals/${id}`);

  if (!response.ok) {
    throw new Error(`Profissional não encontrado (ID: ${id})`);
  }

  return await response.json();
}

export async function getServiceAreas(): Promise<string[]> {
  const professionals = await getHealthProfessionals();
  const areas = professionals.map((p) => p.healthSector);
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
  const res = await fetch(`/apae-geral/api/appointments/today/${id}`);

  if (!res.ok) {
    throw new Error(`Erro ao buscar agendamento do dia: ${res.status}`);
  }

  return await res.json();
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

  const res = await fetch(url, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });

  if (!res.ok) {
    throw new Error("Erro ao buscar agendamentos");
  }

  return res.json();
}
