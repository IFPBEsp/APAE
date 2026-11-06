"use client";

import { Button } from "@/components/ui/button";
import type { PatientWithAbsences } from "@/domains/absences/types/absences.types";
import { AbsenceHistoryItem } from "./AbsenceHistoryItem";

interface PatientAbsenceRowProps {
  patientWithAbsences: PatientWithAbsences;
  isExpanded: boolean;
  onToggleExpand: () => void;
  onJustify: (absence: { id: string; patientId: string }) => void;
  onDownload: (patientId: string, documentName: string) => void;
}

export function PatientAbsenceRow({
  patientWithAbsences,
  isExpanded,
  onToggleExpand,
  onJustify,
  onDownload,
}: PatientAbsenceRowProps) {
  const { patient, absenceCount, absences } = patientWithAbsences;

  return (
    <>
      <tr className="border-b border-gray-100 hover:bg-gray-50 transition-colors">
        <td className="py-3 px-4">
          <div className="font-medium text-gray-900">{patient.fullName}</div>
          <div className="text-sm text-gray-500">{patient.contact}</div>
        </td>
        <td className="py-3 px-4 text-center">
          <span
            className={`px-3 py-1 rounded-full text-xs font-medium ${
              absenceCount >= 5
                ? "bg-red-100 text-red-800"
                : "bg-orange-100 text-orange-800"
            }`}
          >
            {absenceCount} faltas
          </span>
        </td>
        <td className="py-3 px-4 text-center">
          <Button
            variant="outline"
            size="sm"
            onClick={onToggleExpand}
            className="text-[#0D4F97] border-[#0D4F97] hover:bg-[#0D4F97] hover:text-white"
          >
            {isExpanded ? "Ocultar" : "Ver Faltas"}
          </Button>
        </td>
      </tr>

      {isExpanded && (
        <tr className="bg-gray-50">
          <td colSpan={3} className="py-4 px-4">
            <div className="pl-4 border-l-4 border-red-400 space-y-2">
              <h4 className="font-medium text-gray-900 mb-3">
                Histórico de Faltas
              </h4>
              {absences.map((abs) => (
                <AbsenceHistoryItem
                  key={abs.id}
                  absence={abs}
                  patientId={patient.id!}
                  onJustify={onJustify}
                  onDownload={onDownload}
                />
              ))}
            </div>
          </td>
        </tr>
      )}
    </>
  );
}
