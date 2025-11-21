import { GeneratedAppointment, Patient, UUID } from "@/app/services/appointmentService";


export interface PatientHistoryEntry {
  id: UUID;
  scheduledDateTime: string;
  effectiveDateTime: string;
  performed: boolean;
  cancelled: boolean;
  cancellationReason?: string;
  appointmentId: UUID;
  patientId: UUID;
}

export interface PatientHistory {
  patient: Patient;
  appointments: PatientHistoryEntry[];
  absenceCount: number;
  lastAbsenceDate?: string;
}

export interface PatientWithAbsences {
  patient: Patient;
  absenceCount: number;
  lastAbsenceDate: string;
  absences: PatientHistoryEntry[];
}

export interface Absence {
  id: UUID;
  generatedAppointment?: GeneratedAppointment;
  absenceDate: string;
  justification: string;
  notified: boolean;
}