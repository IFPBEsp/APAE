"use client";

import type {
  PaginationInfo,
  PatientWithAbsences,
} from "@/domains/absences/types/absences.types";
import { Users } from "lucide-react";
import { AbsencePagination } from "./AbsencePagination";
import { PatientAbsenceRow } from "./PatientAbsenceRow";

interface PatientsAbsenceTableProps {
  patientsWithAbsences: PatientWithAbsences[];
  loading: boolean;
  pagination: PaginationInfo;
  expandedPatient: string | null;
  onToggleExpand: (patientId: string) => void;
  onPageChange: (page: number) => void;
  onJustify: (absence: { id: string; patientId: string }) => void;
  onDownload: (patientId: string, documentName: string) => void;
}

export function PatientsAbsenceTable({
  patientsWithAbsences,
  loading,
  pagination,
  expandedPatient,
  onToggleExpand,
  onPageChange,
  onJustify,
  onDownload,
}: PatientsAbsenceTableProps) {
  return (
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
                  <PatientAbsenceRow
                    key={p.patient.id}
                    patientWithAbsences={p}
                    isExpanded={expandedPatient === p.patient.id}
                    onToggleExpand={() => onToggleExpand(p.patient.id!)}
                    onJustify={onJustify}
                    onDownload={onDownload}
                  />
                ))}
              </tbody>
            </table>
          </div>

          <AbsencePagination
            currentPage={pagination.currentPage}
            totalPages={pagination.totalPages}
            loading={loading}
            onPageChange={onPageChange}
          />
        </>
      )}
    </div>
  );
}
