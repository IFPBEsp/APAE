import { UUID } from '@/app/services/appointmentService';

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

export interface Patient {
  id: UUID;
  name: string;
  contact: string;
  birthDate: string;
}

export interface PatientWithAbsences {
  patient: Patient;
  absenceCount: number;
  lastAbsenceDate: string;
  absences: AbsenceResponseDTO[];
}
