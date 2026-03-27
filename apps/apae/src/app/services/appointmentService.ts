import { TodayAppointment } from '@/types/appointment';
import { Page } from '@/types/pagination';

export type UUID = string;

export interface AnnualRegistry {
  id: UUID;
  bpc: string;
  diseases: string;
  familyIncome: number;
  year: string;
  patient: Patient;
  disorders: Disorder;
  endDate: string;
}

export interface Appointment {
  id: UUID;
  professional: Professional;
  serviceId: UUID;
  annualRegistration: AnnualRegistry;
  frequencyDays: number;
  hour: string;
  initialDate: string;
  endDate: string;
  isActive: boolean;
  creationDate: string;
}

export interface CreateAppointmentDTO {
  professionalId: UUID;
  serviceId: UUID;
  patientId: UUID;
  frequencyDays: number;
  initialDate: string;
  hour: string;
}

export interface AppointmentResponseDTO {
  id: UUID;
  professional: Professional;
  serviceId: UUID;
  annualRegistration: AnnualRegistry;
  frequencyDays: number;
  hour: string;
  initialDate: string;
  endDate: string;
  isActive: boolean;
  creationDate: string;
}

export interface GeneratedAppointment {
  id: UUID;
  appointment?: Appointment;
  scheduledDateTime: string;
  overriddenDateTime: string;
  performed: boolean;
  cancelled: boolean;
  cancellationReason: string;
  patientId: UUID;
}

export interface GeneratedAppointmentResponseDTO {
  id: UUID;
  appointmentId: UUID;
  scheduledDateTime: string;
  overriddenDateTime: string;
  performed: boolean;
  cancelled: boolean;
  cancellationReason: string;
  patientId: UUID;
  effectiveDateTime: string;
}

export interface Absence {
  id: UUID;
  generatedAppointment?: GeneratedAppointment;
  absenceDate: string;
  justification: string;
  notified: boolean;
}

export interface Patient {
  id: string;
  name: string;
  fullName: string;
  birthplace: string;
  birthDate: string;
  contact: string;
  birthCertificateNumber: string;
  registryOffice: string;
  fls: string;
  book: string;
  rg: string;
  issueDate: string;
  issuingAgency: string;
  cpf: string;
  cns: string;
  nis: string;
  registrationDate: string;
  allergies: string;
  isStudent: boolean;
  address?: Address;
  guardian?: Guardian;
  parents?: Parent[];
  vaccines?: Vaccine[];
}

export interface Address {
  id: UUID;
  city: string;
  cep: string;
  state: string;
  neighborhood: string;
  street: string;
  number: string;
  complement: string;
}

export interface Guardian {
  id: UUID;
  name: string;
  contact: string;
  kinship: string;
  address?: Address;
}

export interface Parent {
  id: UUID;
  name: string;
  rg: string;
  cpf: string;
  isAlive: boolean;
  profession: string;
  kinship: string;
  patient?: Patient;
}

export interface Vaccine {
  id: UUID;
  name: string;
}

export interface UpdateAppointmentRuleDTO {
  newFrequency: number;
  newTime: string;
}

export interface RescheduleGeneratedAppointmentDTO {
  newDateTime: string;
}

export interface CancelGeneratedAppointmentDTO {
  reason: string;
}

export interface Professional {
  id: string;
  healthSector: string;
  phoneNumber: string;
  professionalDocument: string;
  email: string;
  name: string;
  identityDocument: string;
  address: Address;
}

export interface Disorder {
  id: UUID;
  name: string;
}

function ensurePageFormat<T>(data: any): Page<T> {
  if (data && Array.isArray(data.content)) {
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
    content: data ? [data] : [],
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

export const formatTimeForBackend = (timeString: string): string => {
  if (timeString.length === 5) {
    return `${timeString}:00`;
  }
  return timeString;
};

export const parseTimeFromBackend = (timeString: string): string => {
  return timeString.substring(0, 5);
};


export async function saveAppointment(dto: CreateAppointmentDTO): Promise<void> {
  const res = await fetch(`/api/appointments`, {
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
      
      if (cleanMessage.includes('BAD_REQUEST') && cleanMessage.includes('"')) {
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
  size: number = 20
): Promise<Page<AppointmentResponseDTO>> {
  const query = new URLSearchParams({
    page: `${page}`,
    size: `${size}`,
  });

  if (date) query.append("date", date);
  if (time) query.append("time", time);

  const response = await fetch(`/api/appointments?${query}`);
  
  if (!response.ok) {
    throw new Error("Erro ao buscar agendamentos");
  }
  
  const res = await response.json();
  return ensurePageFormat<AppointmentResponseDTO>(res);
}

export async function getAppointmentById(id: UUID): Promise<AppointmentResponseDTO> {
  const response = await fetch(`/api/appointments/${id}`);
  
  if (!response.ok) {
    throw new Error("Erro ao buscar detalhes do agendamento");
  }
  
  return await response.json();
}

export async function updateAppointmentRule(
  id: UUID,
  dto: UpdateAppointmentRuleDTO
): Promise<AppointmentResponseDTO> {
  const backendDto = {
    newFrequency: dto.newFrequency,
    newTime: `${dto.newTime}`,
  };

  const response = await fetch(`/api/appointments/${id}/rule`, {
    method: "PATCH",
    body: JSON.stringify(backendDto),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Erro ao atualizar regra do agendamento: ${response.status} - ${errorText}`);
  }

  return await response.json();
}

export async function deleteAppointment(id: UUID): Promise<void> {
  const response = await fetch(`/api/appointments/${id}`, {
    method: "DELETE",
  });

  if (!response.ok) {
    throw new Error("Erro ao excluir agendamento");
  }
}

export async function rescheduleGeneratedAppointment(
  id: UUID,
  dto: RescheduleGeneratedAppointmentDTO
): Promise<GeneratedAppointmentResponseDTO> {
  const backendDto = {
    newDateTime: new Date(dto.newDateTime).toISOString(),
  };

  const response = await fetch(`/api/generated/${id}/reschedule`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(backendDto),
  });

  if (!response.ok) {
    throw new Error("Erro ao reagendar consulta");
  }

  return await response.json();
}

export async function markAsPerformed(id: UUID): Promise<GeneratedAppointmentResponseDTO> {
  const response = await fetch(`/api/appointments/generated/${id}/performed`, {
    method: 'PATCH',
  });

  if (!response.ok) {
    throw new Error(`Erro ao marcar consulta como realizada`);
  }

  return await response.json();
}

export async function cancelGeneratedAppointment(
  id: UUID,
  dto: CancelGeneratedAppointmentDTO
): Promise<GeneratedAppointmentResponseDTO> {
  const response = await fetch(`/api/generated/${id}/cancel`, {
    method: "PUT",
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
  size: number = 20
): Promise<Page<GeneratedAppointmentResponseDTO>> {
  const query = new URLSearchParams({
    page: `${page}`,
    size: `${size}`,
  });

  if (start) query.append("start", start);
  if (end) query.append("end", end);

  const response = await fetch(`/api/appointments/patient/${patientId}?${query}`);
  
  if (!response.ok) {
    throw new Error("Erro ao buscar agendamentos do paciente");
  }
  
  const res = await response.json();
  return ensurePageFormat<GeneratedAppointmentResponseDTO>(res);
}

export async function registerAbsence(
  generatedAppointmentId: UUID,
  justification: string
): Promise<Absence> {
  const body = {
    generatedAppointmentId,
    justification,
    date: new Date().toISOString(),
    notified: false,
  };

  const res = await fetch(`/api/absences`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });

  if (!res.ok) {
    throw new Error(`Erro ao registrar ausência`);
  }
  
  return await res.json();
}

export async function getPacientes(): Promise<Patient[]> {
  const response = await fetch(`/api/pessoas?page=0&size=100`);
  
  if (!response.ok) {
    throw new Error("Erro ao buscar pacientes");
  }
  
  const data = await response.json();
  return data.content || data || [];
}

export async function getProfissionaisDaSaude(): Promise<Professional[]> {
  const response = await fetch(`/api/professionals?page=0&size=100`);
  
  if (!response.ok) {
    throw new Error("Erro ao buscar profissionais");
  }
  
  const data = await response.json();
  return data.content || data || [];
}

export async function getProfissionalDaSaude(id: string): Promise<Professional> {
  const response = await fetch(`/api/professionals/${id}`);
  
  if (!response.ok) {
    throw new Error(`Profissional não encontrado (ID: ${id})`);
  }
  
  return await response.json();
}

export async function getAreasDaSaude(): Promise<string[]> {
  const profissionais = await getProfissionaisDaSaude();
  const areas = profissionais.map(p => p.healthSector);
  return [...new Set(areas)].filter(Boolean) as string[];
}

export const toggleConfirmacao = async (id: UUID): Promise<void> => {
  const appointment = await getAppointmentById(id);

  if (!appointment.professional || !appointment.annualRegistration?.id) {
    throw new Error('Dados do agendamento incompletos para confirmação');
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

export async function getTodayAppointmentById(id: string): Promise<TodayAppointment> {
  const res = await fetch(`/api/appointments/today/${id}`);

  if (!res.ok) {
    throw new Error(`Erro ao buscar agendamento do dia: ${res.status}`);
  }

  return await res.json();
}

export async function listTodayAppointment(date?: string): Promise<Page<TodayAppointment>> {
  // Montamos a URL base
  let url = '/api/appointments/today';
  
  // Se a data foi passada, adicionamos ela na query string
  if (date) {
    url += `?date=${date}`;
  }

  // Adicionamos um cabeçalho para evitar que o Next.js faça cache agressivo (Fantasmas 👻)
  const res = await fetch(url, {
    headers: {
      'Cache-Control': 'no-cache, no-store, must-revalidate',
      Pragma: 'no-cache',
      Expires: '0',
    },
  });

  if (!res.ok) {
    throw new Error(`Erro ao buscar agendamentos do dia: ${res.status}`);
  }

  const data = await res.json();
  return ensurePageFormat<TodayAppointment>(data);
}
