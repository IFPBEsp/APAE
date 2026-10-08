import type { UUID } from '@/domains/appointments/types/appointments.types';
import type { Patient } from '@/domains/patients/types/patient';

export interface CreateAbsenceDTO {
  generatedAppointmentId: UUID;
  absenceDate: string;
  justification: string;
  isJustified: boolean;
  justificationDocumentId: string | null;
}

export interface AbsenceResponseDTO {
  id: UUID;
  generatedAppointmentId: UUID;
  patientId: UUID;
  professionalId: UUID;
  absenceDate: string;
  justification: string;
  notified: boolean;
  justificationDocumentId: string;
  isJustified: boolean;
}

export interface PatientWithAbsences {
  patient: Patient;
  absenceCount: number;
  lastAbsenceDate: string;
  absences: AbsenceResponseDTO[];
}

export type FormDataType = {
  hasJustification: string;
  justificationText?: string;
};

export interface PaginationInfo {
  currentPage: number;
  totalPages: number;
  totalItems: number;
  itemsPerPage: number;
}
