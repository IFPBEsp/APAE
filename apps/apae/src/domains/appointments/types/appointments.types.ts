import type { Patient } from '@/domains/patients/types/patient';
export type { Patient } from '@/domains/patients/types/patient';

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
  replacedByDate?: string;
  updatedFromDate?: string;
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

export interface UpdateAppointmentDTO {
  professionalId?: string;
  annualRegistrationId?: string;
  serviceId?: string;
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

// ATENÇÃO: MUDANÇA POSTERIOR

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
export interface Disorder {
  id: UUID;
  name: string;
}
export interface Vaccine {
  id: UUID;
  name: string;
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
}

export interface TodayAppointment {
	id: UUID;
	patient: Patient;
	professional: Professional;
	scheduledDateTime: Date;
	overriddenDateTime: Date;
	performed: boolean;
	cancelled: boolean;
	cancellationReason: string;
	effectiveDateTime: Date;
	ruleId: UUID;
	hasAbsence: boolean;
	isActive: boolean;
}