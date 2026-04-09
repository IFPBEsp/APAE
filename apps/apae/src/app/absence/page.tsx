"use client";

import AbsenceService from "@/app/services/absenceService";
import { InfoCard } from "@/components/shared/InfoCard";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { PatientWithAbsences } from "@/types/absence";
import { Calendar, SearchIcon, Users } from "lucide-react";
import { useEffect, useRef,useState } from "react";

interface PaginationInfo {
  currentPage: number;
  totalPages: number;
  totalItems: number;
  itemsPerPage: number;
}

export default function AbsenceDetails() {
  const [searchName, setSearchName] = useState("");
  const [patientsWithAbsences, setPatientsWithAbsences] = useState<
    PatientWithAbsences[]
  >([]);
  const [filteredPatients, setFilteredPatients] = useState<
    PatientWithAbsences[]
  >([]);
  const [loading, setLoading] = useState(true);
  const [expandedPatient, setExpandedPatient] = useState<string | null>(null);
  const [pagination, setPagination] = useState<PaginationInfo>({
    currentPage: 1,
    totalPages: 1,
    totalItems: 0,
    itemsPerPage: 6,
  });
  const [statistics, setStatistics] = useState({
    totalPatients: 0,
    totalAppointments: 0,
    patientsWithMinAbsences: 0,
  });

  const initialized = useRef(false);

  useEffect(() => {

    if (initialized.current) return;

    const fetchData = async () => {
      try {

        initialized.current = true;

        setLoading(true);

        const [absencesData, statsData] = await Promise.all([
          AbsenceService.getPatientsWithAbsences(3),
          AbsenceService.getAbsenceStatistics(),
        ]);

        console.dir(absencesData, {depht: null})

        setPatientsWithAbsences(absencesData);
        setFilteredPatients(absencesData);
        setStatistics(statsData);

        setPagination((prev) => ({
          ...prev,
          totalItems: absencesData.length,
          totalPages: Math.ceil(absencesData.length / prev.itemsPerPage),
        }));
      } catch (error) {
        console.error("Error fetching data:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  useEffect(() => {
    const filtered = patientsWithAbsences.filter((patientWithAbsence) =>
      patientWithAbsence.patient.name
        .toLowerCase()
        .includes(searchName.toLowerCase())
    );

    setFilteredPatients(filtered);
    setPagination((prev) => ({
      ...prev,
      currentPage: 1,
      totalItems: filtered.length,
      totalPages: Math.ceil(filtered.length / prev.itemsPerPage),
    }));
  }, [searchName, patientsWithAbsences]);

  const currentPagePatients = filteredPatients.slice(
    (pagination.currentPage - 1) * pagination.itemsPerPage,
    pagination.currentPage * pagination.itemsPerPage
  );

  const formatDate = (dateString: string) => {

  return new Date(dateString + "Z").toLocaleDateString("pt-BR", {
    timeZone: "UTC",
  });
};

  const formatTime = (timeString: string) => {
    return timeString.substring(0, 5);
  };

  const togglePatientExpansion = (patientId: string) => {
    setExpandedPatient(expandedPatient === patientId ? null : patientId);
  };

  const handlePageChange = (page: number) => {
    setPagination((prev) => ({ ...prev, currentPage: page }));
  };

  if (loading) {
    return (
      <div className="min-h-screen w-full flex items-center justify-center">
        <div className="text-lg">Carregando dados...</div>
      </div>
    );
  }

  const handleDownload = async (patientId: string, documentName: string) => {
  try {
    const res = await fetch(
      `/api/pessoas/${patientId}/documentos/download?name=${encodeURIComponent(documentName)}`
    );

    if (!res.ok) throw new Error("Erro ao buscar URL");

    const data = await res.json();

    window.open(data.url, "_blank");

  } catch (error) {
    console.error("Erro ao baixar documento:", error);
  }
};

  return (
    <div className="min-h-screen w-full text-sm overflow-x-hidden">
      <main className="flex-1 p-3 sm:p-6 w-full max-w-none">
        <div className="mb-4 flex flex-col justify-between gap-3 sm:mb-6 sm:flex-row sm:items-center">
          <h1 className="text-lg font-bold sm:text-2xl text-[#0D4F97]">
            Controle de Faltas
          </h1>
        </div>

        <div className="mb-6 grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-4">
          <InfoCard
            title="Total de Pacientes"
            icon={Users}
            value={statistics.totalPatients}
            titleClassName="text-[#0D4F97]"
            valueClassName="text-[#0D4F97]"
          />
          <InfoCard
            title="Agendamentos Ativos"
            icon={Calendar}
            value={statistics.totalAppointments}
            titleClassName="text-[#0D4F97]"
            valueClassName="text-[#0D4F97]"
          />
          <InfoCard
            title="Pacientes com 3+ Faltas"
            icon={Users}
            value={statistics.patientsWithMinAbsences}
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
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-lg font-semibold text-gray-900">
                Pacientes com 3 ou mais faltas
              </h2>
              <div className="text-sm text-gray-500">
                {filteredPatients.length} paciente(s) encontrado(s)
              </div>
            </div>

            {filteredPatients.length === 0 ? (
              <div className="text-center py-12 text-gray-500">
                <Users className="mx-auto h-12 w-12 text-gray-400 mb-4" />
                <p className="text-lg font-medium mb-2">
                  {searchName
                    ? "Nenhum paciente encontrado com 3 ou mais faltas."
                    : "Nenhum paciente com 3 ou mais faltas encontrado."}
                </p>
                <p className="text-sm">
                  {searchName
                    ? "Tente ajustar os termos da busca."
                    : "Todos os pacientes estão com menos de 3 faltas registradas."}
                </p>
              </div>
            ) : (
              <>
                <div className="overflow-x-auto">
                  <table className="w-full min-w-[700px]">
                    <thead>
                      <tr className="border-b border-gray-200">
                        <th className="text-left py-3 px-4 font-medium text-gray-700">
                          Paciente
                        </th>
                        <th className="text-center py-3 px-4 font-medium text-gray-700">
                          Total de Faltas
                        </th>
                        <th className="text-center py-3 px-4 font-medium text-gray-700">
                          Ações
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {currentPagePatients.map((patientWithAbsence, index) => (
                        <>
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
                                  {patientWithAbsence.patient.contact}
                                </div>
                              </div>
                            </td>
                            <td className="py-3 px-4 text-center">
                              <span
                                className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium ${
                                  patientWithAbsence.absenceCount >= 5
                                    ? "bg-red-100 text-red-800"
                                    : patientWithAbsence.absenceCount >= 3
                                    ? "bg-orange-100 text-orange-800"
                                    : "bg-yellow-100 text-yellow-800"
                                }`}
                              >
                                {patientWithAbsence.absenceCount} faltas
                              </span>
                            </td>
                            <td className="py-3 px-4 text-center">
                              <Button
                                variant="outline"
                                size="sm"
                                onClick={() =>
                                  togglePatientExpansion(
                                    patientWithAbsence.patient.id!
                                  )
                                }
                                className="text-[#0D4F97] border-[#0D4F97] hover:bg-[#0D4F97] hover:text-white"
                              >
                                {expandedPatient ===
                                patientWithAbsence.patient.id
                                  ? "Ocultar"
                                  : "Ver Faltas"}
                              </Button>
                            </td>
                          </tr>

                          {expandedPatient ===
                            patientWithAbsence.patient.id && (
                            <tr className="bg-gray-50">
                              <td colSpan={5} className="py-4 px-4">
                                <div className="pl-4 border-l-4 border-red-400">
                                  <h4 className="font-medium text-gray-900 mb-3">
                                    Histórico de Faltas
                                  </h4>
                                  <div className="space-y-2">
                                    {patientWithAbsence.absences.map(
                                      (absence, idx) => (
                                        <div
                                          key={absence.id}
                                          className="flex flex-col md:flex-row md:items-start md:justify-between gap-4 p-3 bg-white rounded border"
                                        >
                                        <div className="flex items-center gap-4">
                                        <Calendar className="h-4 w-4 text-red-500" />
                                          <span className="font-medium">
                                            {formatDate(absence.absenceDate)}
                                          </span>
                                        </div>

                                      <div className="flex items-center gap-2 flex-wrap">
                                        {absence.justification && (
                                          <div className="text-sm text-gray-600 max-w-md break-words whitespace-pre-wrap">
                                            <span className="font-medium">Justificativa: </span>
                                            {absence.justification}
                                          </div>
                                        )}

                                        {absence.justificationDocumentId && (
                                          <Button
                                            size="sm"
                                            variant="outline"
                                            onClick={() =>
                                              handleDownload(
                                                patientWithAbsence.patient.id!,
                                                absence.justificationDocumentId
                                              )
                                            }
                                          >
                                            Baixar documento
                                          </Button>
                                        )}
                                      </div>
                                        </div>
                                      )
                                    )}
                                  </div>
                                </div>
                              </td>
                            </tr>
                          )}
                        </>
                      ))}
                    </tbody>
                  </table>
                </div>

                {pagination.totalPages > 1 && (
                  <div className="flex justify-between items-center mt-6 pt-4 border-t border-gray-200">
                    <div className="text-sm text-gray-500">
                      Mostrando{" "}
                      {(pagination.currentPage - 1) * pagination.itemsPerPage +
                        1}{" "}
                      a{" "}
                      {Math.min(
                        pagination.currentPage * pagination.itemsPerPage,
                        filteredPatients.length
                      )}{" "}
                      de {filteredPatients.length} pacientes
                    </div>
                    <div className="flex gap-2">
                      <Button
                        variant="outline"
                        size="sm"
                        disabled={pagination.currentPage === 1}
                        onClick={() =>
                          handlePageChange(pagination.currentPage - 1)
                        }
                      >
                        Anterior
                      </Button>
                      {Array.from(
                        { length: pagination.totalPages },
                        (_, i) => i + 1
                      )
                        .filter(
                          (page) =>
                            page === 1 ||
                            page === pagination.totalPages ||
                            Math.abs(page - pagination.currentPage) <= 1
                        )
                        .map((page, index, array) => (
                          <div key={page} className="flex items-center">
                            {index > 0 && array[index - 1] !== page - 1 && (
                              <span className="px-2">...</span>
                            )}
                            <Button
                              variant={
                                pagination.currentPage === page
                                  ? "default"
                                  : "outline"
                              }
                              size="sm"
                              onClick={() => handlePageChange(page)}
                              className={
                                pagination.currentPage === page
                                  ? "bg-[#0D4F97] text-white"
                                  : ""
                              }
                            >
                              {page}
                            </Button>
                          </div>
                        ))}
                      <Button
                        variant="outline"
                        size="sm"
                        disabled={
                          pagination.currentPage === pagination.totalPages
                        }
                        onClick={() =>
                          handlePageChange(pagination.currentPage + 1)
                        }
                      >
                        Próxima
                      </Button>
                    </div>
                  </div>
                )}
              </>
            )}
          </div>
        </div>
      </main>
    </div>
  );
}
