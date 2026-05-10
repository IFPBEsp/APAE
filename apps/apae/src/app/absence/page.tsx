"use client";

import { InfoCard } from "@/components/shared/InfoCard";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { PatientWithAbsences } from "@/types/absence";
import { DashboardOverview } from "@/domains/dashboard/dashboard.types";
import { Calendar, SearchIcon, Users } from "lucide-react";
import React, { useEffect, useState, useCallback, useRef } from "react";
import AbsenceService from "../services/absenceService";

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
  const [loading, setLoading] = useState(true);
  const [expandedPatient, setExpandedPatient] = useState<string | null>(null);
  const [pagination, setPagination] = useState<PaginationInfo>({
    currentPage: 0,
    totalPages: 1,
    totalItems: 0,
    itemsPerPage: 6,
  });
  const [statistics, setStatistics] = useState<DashboardOverview>({
    totalPatients: 0,
    totalPatientsWithAbsences: 0,
  });

  const [justifyingAbsence, setJustifyingAbsence] = useState<{ id: string, patientId: string } | null>(null);
  const [justificationText, setJustificationText] = useState("");
  const [isSubmittingJustification, setIsSubmittingJustification] = useState(false);
  const [file, setFile] = useState<File | null>(null);
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const handleJustifyAbsence = async () => {
    if (!justifyingAbsence || !justificationText.trim()) return;

    try {
      setIsSubmittingJustification(true);
      let documentId: string | null = null;

      if (file) {
        const docFormData = new FormData();
        docFormData.append("file", file);
        docFormData.append("category", "ABSENCE");
        docFormData.append("type", "ATTACHMENTANY");
        docFormData.append("year", String(new Date().getFullYear()));

        const docResponse = await fetch(`/api/patients/${justifyingAbsence.patientId}/documents`, {
          method: "POST",
          body: docFormData,
        });

        if (!docResponse.ok) {
          const errorData = await docResponse.json();
          throw new Error(errorData.message || "Erro ao fazer upload do documento.");
        }

        const document = await docResponse.json();
        documentId = document.name;
      }

      await AbsenceService.justifyAbsence(justifyingAbsence.id, justificationText, documentId);

      window.location.reload();
    } catch (error: any) {
      console.error("Erro ao justificar falta:", error);
      alert(error.message || "Erro ao justificar falta.");
    } finally {
      setIsSubmittingJustification(false);
      setJustifyingAbsence(null);
      setJustificationText("");
      setFile(null);
    }
  };

  const fetchData = useCallback(
    async (page: number, name: string) => {
      try {
        setLoading(true);
        const queryParams = new URLSearchParams({
          minAbsences: "3",
          page: page.toString(),
          size: pagination.itemsPerPage.toString(),
          name: name.trim(),
        });

        const [patientsRes, statsRes] = await Promise.all([
          fetch(`/api/patients/with-absences?${queryParams}`),
          fetch("/api/dashboard/overview?minAbsences=3"),
        ]);

        if (!patientsRes.ok || !statsRes.ok) {
          throw new Error("Erro ao buscar dados");
        }

        const patientsData = await patientsRes.json();
        const statsData: DashboardOverview = await statsRes.json();

        setPatientsWithAbsences(patientsData.content);
        setStatistics({
          totalPatients: statsData.totalPatients,
          totalPatientsWithAbsences: statsData.totalPatientsWithAbsences,
        });
        setPagination((prev) => ({
          ...prev,
          currentPage: page,
          totalItems: patientsData.totalElements,
          totalPages: patientsData.totalPages,
        }));
      } catch (error) {
        console.error("Error fetching data:", error);
      } finally {
        setLoading(false);
      }
    },
    [pagination.itemsPerPage],
  );

  useEffect(() => {
    const timer = setTimeout(() => {
      fetchData(0, searchName);
    }, 300);
    return () => clearTimeout(timer);
  }, [searchName, fetchData]);

  const formatDate = (dateString: string) => {
    return new Date(dateString + "Z").toLocaleDateString("pt-BR", {
      timeZone: "UTC",
    });
  };

  const handleDownload = async (patientId: string, documentName: string) => {
    try {
      const res = await fetch(
        `/api/patients/${patientId}/documents/download?name=${encodeURIComponent(documentName)}`,
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
            title="Pacientes com 3+ Faltas"
            icon={Users}
            value={statistics.totalPatientsWithAbsences}
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

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-lg font-semibold text-gray-900">
              Pacientes com 3 ou mais faltas
            </h2>
            <div className="text-sm text-gray-500">
              {pagination.totalItems} paciente(s) no total
            </div>
          </div>

          {loading && patientsWithAbsences.length === 0 ? (
            <div className="text-center py-12 text-gray-500">Carregando...</div>
          ) : patientsWithAbsences.length === 0 ? (
            <div className="text-center py-12 text-gray-500">
              <Users className="mx-auto h-12 w-12 text-gray-400 mb-4" />
              <p className="text-lg font-medium mb-2">
                Nenhum paciente encontrado.
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
                    {patientsWithAbsences.map((p) => (
                      <React.Fragment key={p.patient.id}>
                        <tr className="border-b border-gray-100 hover:bg-gray-50 transition-colors">
                          <td className="py-3 px-4">
                            <div className="font-medium text-gray-900">
                              {p.patient.fullName}
                            </div>
                            <div className="text-sm text-gray-500">
                              {p.patient.contact}
                            </div>
                          </td>
                          <td className="py-3 px-4 text-center">
                            <span
                              className={`px-3 py-1 rounded-full text-xs font-medium ${p.absenceCount >= 5 ? "bg-red-100 text-red-800" : "bg-orange-100 text-orange-800"}`}
                            >
                              {p.absenceCount} faltas
                            </span>
                          </td>
                          <td className="py-3 px-4 text-center">
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() =>
                                setExpandedPatient(
                                  expandedPatient === p.patient.id
                                    ? null
                                    : p.patient.id!,
                                )
                              }
                              className="text-[#0D4F97] border-[#0D4F97] hover:bg-[#0D4F97] hover:text-white"
                            >
                              {expandedPatient === p.patient.id
                                ? "Ocultar"
                                : "Ver Faltas"}
                            </Button>
                          </td>
                        </tr>

                        {expandedPatient === p.patient.id && (
                          <tr className="bg-gray-50">
                            <td colSpan={3} className="py-4 px-4">
                              <div className="pl-4 border-l-4 border-red-400 space-y-2">
                                <h4 className="font-medium text-gray-900 mb-3">
                                  Histórico de Faltas
                                </h4>
                                {p.absences.map((abs) => (
                                  <div
                                    key={abs.id}
                                    className="flex flex-col md:flex-row md:items-start md:justify-between gap-4 p-3 bg-white rounded border"
                                  >
                                    <div className="flex items-center gap-4">
                                      <Calendar className="h-4 w-4 text-red-500" />
                                      <span className="font-medium">
                                        {formatDate(abs.absenceDate)}
                                      </span>
                                    </div>
                                    <div className="flex items-center gap-2 flex-wrap">

                                      {abs.isJustified ? (
                                        <div className="text-sm text-gray-600 max-w-md break-words whitespace-pre-wrap">
                                          <span className="font-medium text-green-600">Falta Justificada: </span>
                                          {abs.justification}
                                        </div>
                                      ) : (
                                        <div className="flex items-center gap-3">
                                          {abs.justification && (
                                            <div className="text-sm text-gray-500 max-w-xs truncate" title={abs.justification}>
                                              <span className="font-medium text-gray-700">Motivo original: </span>
                                              {abs.justification}
                                            </div>
                                          )}

                                          <Button
                                            size="sm"
                                            variant="outline"
                                            className="text-orange-600 border-orange-600 hover:bg-orange-600 hover:text-white"
                                            onClick={() => setJustifyingAbsence({ id: abs.id, patientId: p.patient.id! })}
                                          >
                                            Justificar Falta
                                          </Button>
                                        </div>
                                      )}

                                      {abs.justificationDocumentId && (
                                        <Button
                                          size="sm"
                                          variant="outline"
                                          onClick={() =>
                                            handleDownload(
                                              p.patient.id!,
                                              abs.justificationDocumentId!,
                                            )
                                          }
                                        >
                                          Baixar documento
                                        </Button>
                                      )}

                                    </div>
                                  </div>
                                ))}
                              </div>
                            </td>
                          </tr>
                        )}
                      </React.Fragment>
                    ))}
                  </tbody>
                </table>
              </div>

              {pagination.totalPages > 1 && (
                <div className="flex justify-between items-center mt-6 pt-4 border-t border-gray-200">
                  <div className="text-sm text-gray-500">
                    Página {pagination.currentPage + 1} de{" "}
                    {pagination.totalPages}
                  </div>
                  <div className="flex gap-2">
                    <Button
                      variant="outline"
                      size="sm"
                      disabled={pagination.currentPage === 0 || loading}
                      onClick={() =>
                        fetchData(pagination.currentPage - 1, searchName)
                      }
                    >
                      Anterior
                    </Button>
                    <Button
                      variant="outline"
                      size="sm"
                      disabled={
                        pagination.currentPage === pagination.totalPages - 1 ||
                        loading
                      }
                      onClick={() =>
                        fetchData(pagination.currentPage + 1, searchName)
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

        {justifyingAbsence && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
            <div className="bg-white rounded-xl p-6 max-w-md w-full shadow-2xl border border-gray-100">

              <div className="mb-6">
                <h3 className="text-xl font-bold text-[#0D4F97] mb-2">Justificar Falta</h3>
                <p className="text-sm text-gray-500">
                  Esta falta deixará de contar para a lista vermelha do paciente.
                </p>
              </div>

              <div className="space-y-4">
                <div className="space-y-2">
                  <label className="text-sm font-medium text-gray-700">
                    Descrição da justificativa <span className="text-red-500">*</span>
                  </label>
                  <textarea
                    className="w-full border border-gray-300 rounded-md p-3 min-h-[100px] outline-none focus:ring-2 focus:ring-[#0D4F97] focus:border-transparent text-sm"
                    placeholder="Descreva o motivo da justificativa..."
                    value={justificationText}
                    onChange={(e) => setJustificationText(e.target.value)}
                  />
                </div>

                <div className="space-y-2">
                  <label className="text-sm font-medium text-gray-700">Anexar documento (Opcional)</label>
                  <Input
                    ref={fileInputRef}
                    type="file"
                    className="cursor-pointer"
                    onChange={(e) => {
                      if (e.target.files?.[0]) {
                        setFile(e.target.files[0]);
                      }
                    }}
                  />

                  {file && (
                    <div className="flex items-center justify-between text-sm bg-gray-50 p-2 rounded border mt-2">
                      <span className="text-muted-foreground truncate max-w-[200px]">
                        {file.name}
                      </span>
                      <Button
                        type="button"
                        variant="ghost"
                        size="sm"
                        className="text-red-500 hover:text-red-700 hover:bg-red-50 h-8"
                        onClick={() => {
                          setFile(null);
                          if (fileInputRef.current) {
                            fileInputRef.current.value = "";
                          }
                        }}
                      >
                        Remover
                      </Button>
                    </div>
                  )}
                </div>
              </div>

              <div className="flex justify-end gap-3 mt-8 pt-4 border-t border-gray-100">
                <Button
                  variant="outline"
                  className="w-full sm:w-auto"
                  onClick={() => {
                    setJustifyingAbsence(null);
                    setJustificationText("");
                    setFile(null);
                  }}
                  disabled={isSubmittingJustification}
                >
                  Cancelar
                </Button>
                <Button
                  className="w-full sm:w-auto bg-[#0D4F97] hover:bg-blue-900 text-white"
                  onClick={handleJustifyAbsence}
                  disabled={isSubmittingJustification || !justificationText.trim()}
                >
                  {isSubmittingJustification ? "Salvando..." : "Registrar Justificativa"}
                </Button>
              </div>

            </div>
          </div>
        )}
      </main>
    </div>
  );
}
