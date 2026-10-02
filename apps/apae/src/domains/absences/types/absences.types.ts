import type { UUID } from '@/domains/appointments/types/appointments.types';
import type { Patient } from '@/domains/patients/types/patient';

export interface CreateAbsenceDTO {
  generatedAppointmentId: UUID;
  absenceDate: string;
  justification: string;
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