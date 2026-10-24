import { TodayAppointment } from "@/types/appointment";
import { Page } from "@/types/pagination";
import { bffFetchJson } from "@/lib/bff-client";

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
  annualRegistration: AnnualRegistry;
  frequencyDays: number;
  hour: string;
  initialDate: string;
  endDate: string;
  isActive: boolean;
  creationDate: string;
  replacedByDate?: string;
  updatedFromDate?: string;
}

export interface CreateAppointmentDTO {
  professionalId: UUID;
  patientId: UUID;
  frequencyDays: number;
  initialDate: string;
  hour: string;
}

export interface AppointmentResponseDTO {
  id: UUID;
  professional: Professional;
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

export interface UpdateAppointmentDTO {
  professionalId?: string;
  annualRegistrationId?: string;
  frequencyDays?: number;
  initialDate?: string;
  hour?: string;
  endDate?: string;
}

export interface RescheduleGeneratedAppointmentDTO {
  newDateTime: string;
}

export interface CancelGeneratedAppointmentDTO {
  reason: string;
}

export interface Professional {
  id: string;
  serviceArea?: {
    id?: string | number;
    area?: string;
  };
  healthSector?: string | null;
  phoneNumber?: string;
  professionalDocument?: string | null;
  email: string;
  cpf?: string;
  name: string;
  identityDocument?: string;
  address?: Address;
}

export interface Disorder {
  id: UUID;
  name: string;
}

export function getProfessionalAreaName(
  professional?: Pick<Professional, "serviceArea" | "healthSector"> | null,
): string {
  return professional?.serviceArea?.area ?? professional?.healthSector ?? "";
}

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

export const formatTimeForBackend = (timeString: string): string => {
  if (timeString.length === 5) {
    return `${timeString}:00`;
  }
  return timeString;
};

export const parseTimeFromBackend = (timeString: string): string => {
  return timeString.substring(0, 5);
};

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

  return bffFetchJson<Absence>(`/apae-geral/api/absences`, {
    method: "POST",
    body: JSON.stringify(body),
  });
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
