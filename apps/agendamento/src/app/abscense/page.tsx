"use client";

import { useEffect, useState } from "react";
import { InfoCard } from "@/components/shared/InfoCard";
import { Users, SearchIcon } from "lucide-react";
import { Input } from "@/components/ui/input";
import {
  Patient,
  Appointment,
  getAppointments,
  getPatients,
} from "@/app/services/agendamentoService";

interface HistoryEntry {
  id: string;
  consultationDate: string;
  wasPerformed: boolean;
  appointmentId: string;
}

interface PatientWithAbsences {
  patient: Patient;
  absenceCount: number;
  lastAbsenceDate: string;
}

export default function AbsenceDetails() {
  const [searchName, setSearchName] = useState("");
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [patientsWithAbsences, setPatientsWithAbsences] = useState<
    PatientWithAbsences[]
  >([]);
  const [loading, setLoading] = useState(true);
  const [allPatients, setAllPatients] = useState<Patient[]>([]);

  const getAppointmentHistory = async (
    appointmentId: string
  ): Promise<{ content: HistoryEntry[] }> => {
    try {
      const response = await fetch(
        `${
          process.env.API_BASE_URL || "http://localhost:8093"
        }/historico-consultas?appointmentId=${appointmentId}`
      );

      if (!response.ok) {
        throw new Error(`Erro ao buscar histórico: ${response.status}`);
      }

      return await response.json();
    } catch (error) {
      console.error(
        `Error fetching history for appointment ${appointmentId}:`,
        error
      );
      return { content: [] };
    }
  };

  const countPatientAbsences = async (
    appointments: Appointment[]
  ): Promise<PatientWithAbsences[]> => {
    const patientAbsenceMap = new Map<
      string,
      { patient: Patient; count: number; lastDate: string }
    >();

    for (const appointment of appointments) {
      try {
        const patientId = appointment.patient.id;
        if (!patientId) continue;

        const historyResponse = await getAppointmentHistory(appointment.id);
        const absences = historyResponse.content.filter(
          (history) => !history.wasPerformed
        );

        if (absences.length > 0) {
          const currentData = patientAbsenceMap.get(patientId) || {
            patient: appointment.patient,
            count: 0,
            lastDate: "",
          };

          currentData.count += absences.length;

          const latestAbsence = absences.reduce((latest, current) =>
            new Date(current.consultationDate) >
            new Date(latest.consultationDate)
              ? current
              : latest
          );

          if (
            !currentData.lastDate ||
            new Date(latestAbsence.consultationDate) >
              new Date(currentData.lastDate)
          ) {
            currentData.lastDate = latestAbsence.consultationDate;
          }

          patientAbsenceMap.set(patientId, currentData);
        }
      } catch (error) {
        console.error(
          `Error processing appointment for patient ${appointment.patient.name}:`,
          error
        );
      }
    }

    return Array.from(patientAbsenceMap.values())
      .filter((item) => item.count >= 3)
      .map((item) => ({
        patient: item.patient,
        absenceCount: item.count,
        lastAbsenceDate: item.lastDate,
      }))
      .sort((a, b) => b.absenceCount - a.absenceCount);
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);

        const [appointmentsData, patientsData] = await Promise.all([
          getAppointments(),
          getPatients(),
        ]);

        setAppointments(appointmentsData);
        setAllPatients(patientsData);

        const absencesData = await countPatientAbsences(appointmentsData);
        setPatientsWithAbsences(absencesData);
      } catch (error) {
        console.error("Error fetching data:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const filteredPatients = patientsWithAbsences.filter((patientWithAbsence) =>
    patientWithAbsence.patient.name
      .toLowerCase()
      .includes(searchName.toLowerCase())
  );

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("pt-BR");
  };

  if (loading) {
    return (
      <div className="min-h-screen w-full flex items-center justify-center">
        <div className="text-lg">Carregando dados...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen w-full text-sm overflow-x-hidden">
      <main className="flex-1 p-3 sm:p-6 w-full max-w-none">
        <div className="mb-4 flex flex-col justify-between gap-3 sm:mb-6 sm:flex-row sm:items-center">
          <h1 className="text-lg font-bold sm:text-2xl text-[#0D4F97]">
            Controle de Faltas
          </h1>
        </div>

        <div className="mb-6 grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-3">
          <InfoCard
            title="Total de Pacientes"
            icon={Users}
            value={allPatients.length}
            titleClassName="text-[#0D4F97]"
            valueClassName="text-[#0D4F97]"
          />
          <InfoCard
            title="Agendamentos Ativos"
            icon={Users}
            value={appointments.length}
            titleClassName="text-[#0D4F97]"
            valueClassName="text-[#0D4F97]"
          />
          <InfoCard
            title="Pacientes com 3+ Faltas"
            icon={Users}
            value={patientsWithAbsences.length}
            titleClassName="text-red-600"
            valueClassName="text-red-600"
          />
        </div>

        <div className="mb-6">
          <div className="relative w-full">
            <SearchIcon className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
            <Input
              type="text"
              placeholder="Buscar paciente por nome..."
              value={searchName}
              onChange={(e) => setSearchName(e.target.value)}
              className="pl-10 pr-3 border-[#0D4F97]"
            />
          </div>
        </div>

        <div className="bg-white rounded-lg shadow">
          <div className="p-6">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">
              Pacientes com 3 ou mais faltas
            </h2>

            {filteredPatients.length === 0 ? (
              <div className="text-center py-8 text-gray-500">
                {searchName
                  ? "Nenhum paciente encontrado com 3 ou mais faltas."
                  : "Nenhum paciente com 3 ou mais faltas encontrado."}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full min-w-[700px]">
                  <thead>
                    <tr className="border-b border-gray-200">
                      <th className="text-left py-3 px-4 font-medium text-gray-700">
                        Paciente
                      </th>
                      <th className="text-left py-3 px-4 font-medium text-gray-700">
                        Contato
                      </th>
                      <th className="text-center py-3 px-4 font-medium text-gray-700">
                        Total de Faltas
                      </th>
                      <th className="text-left py-3 px-4 font-medium text-gray-700">
                        Última Falta
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredPatients.map((patientWithAbsence, index) => (
                      <tr
                        key={patientWithAbsence.patient.id || index}
                        className="border-b border-gray-100 hover:bg-gray-50 transition-colors"
                      >
                        <td className="py-3 px-4">
                          <div>
                            <div className="font-medium text-gray-900">
                              {patientWithAbsence.patient.name}
                            </div>
                            <div className="text-sm text-gray-500">
                              {patientWithAbsence.patient.email}
                            </div>
                          </div>
                        </td>
                        <td className="py-3 px-4">
                          <div className="text-gray-700">
                            {patientWithAbsence.patient.phone}
                          </div>
                          <div className="text-sm text-gray-500">
                            {patientWithAbsence.patient.city}/
                            {patientWithAbsence.patient.state}
                          </div>
                        </td>
                        <td className="py-3 px-4 text-center">
                          <span
                            className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium ${
                              patientWithAbsence.absenceCount >= 5
                                ? "bg-red-100 text-red-800"
                                : "bg-orange-100 text-orange-800"
                            }`}
                          >
                            {patientWithAbsence.absenceCount} faltas
                          </span>
                        </td>
                        <td className="py-3 px-4 text-gray-700">
                          {formatDate(patientWithAbsence.lastAbsenceDate)}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
}
