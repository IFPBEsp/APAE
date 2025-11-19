import { 
  Absence, 
  PatientWithAbsences, 
  PatientHistoryEntry, 
} from '@/types/absence';
import { getAppointments, Appointment, Patient } from './appointmentService';

export type UUID = string;

async function getPatients(): Promise<Patient[]> {
  return [];
}

async function getAppointmentHistory(appointmentId: UUID): Promise<{ content: PatientHistoryEntry[] }> {
  return { content: [] };
}

export async function getAbsenceStatistics(): Promise<{
  totalPatients: number;
  totalAppointments: number;
  patientsWithMinAbsences: number;
}> {
  try {
    const [patients, appointments, patientsWithAbsences] = await Promise.all([
      getPatients(),
      getAppointments(), 
      getPatientsWithAbsences(3),
    ]);

    return {
      totalPatients: patients.length,
      totalAppointments: appointments.length,
      patientsWithMinAbsences: patientsWithAbsences.length,
    };
  } catch (error) {
    console.error("Error fetching absence statistics:", error);
    throw error;
  }
}

export async function getPatientsWithAbsences(
  minAbsences: number = 3
): Promise<PatientWithAbsences[]> {
  try {
    const appointmentsPage = await getAppointments(); 
    const appointments = appointmentsPage.content;   

    const patientAbsenceMap = new Map<
      string,
      {
        patient: Patient;
        count: number;
        lastDate: string;
        absences: PatientHistoryEntry[];
      }
    >();

    for (const appointment of appointments) {
      try {
        const patient = appointment.annualRegistration.patient;
        const patientId = patient.id;
        if (!patientId) continue;

        const historyResponse = await getAppointmentHistory(appointment.id);
        const absences = historyResponse.content.filter(
          (history) => !history.performed || history.cancelled
        );

        if (absences.length > 0) {
          const currentData = patientAbsenceMap.get(patientId) || {
            patient,
            count: 0,
            lastDate: "",
            absences: [] as PatientHistoryEntry[], // tipagem explícita
          };

          currentData.count += absences.length;
          currentData.absences.push(...absences);

          const latestAbsence = absences.reduce((latest, current) =>
            new Date(current.effectiveDateTime) >
            new Date(latest.effectiveDateTime)
              ? current
              : latest
          );

          if (
            !currentData.lastDate ||
            new Date(latestAbsence.effectiveDateTime) >
              new Date(currentData.lastDate)
          ) {
            currentData.lastDate = latestAbsence.effectiveDateTime;
          }

          patientAbsenceMap.set(patientId, currentData);
        }
      } catch (error) {
        console.error(
          `Erro processando appointment para paciente ${appointment.annualRegistration.patient.name}:`,
          error
        );
      }
    }

    return Array.from(patientAbsenceMap.values())
      .filter((item) => item.count >= minAbsences)
      .map((item) => ({
        patient: item.patient,
        absenceCount: item.count,
        lastAbsenceDate: item.lastDate,
        absences: item.absences.sort(
          (a, b) =>
            new Date(b.effectiveDateTime).getTime() -
            new Date(a.effectiveDateTime).getTime()
        ),
      }))
      .sort((a, b) => b.absenceCount - a.absenceCount);
  } catch (error) {
    console.error("Erro buscando pacientes com ausências:", error);
    throw error;
  }
}

