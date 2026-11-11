"use client";

import { Button } from "@/components/ui/button";
import type { AbsenceResponseDTO } from "@/domains/absences/types/absences.types";
import { formatDateShortPTBR } from "@/lib/utils";
import { Calendar } from "lucide-react";

interface AbsenceHistoryItemProps {
  absence: AbsenceResponseDTO;
  patientId: string;
  onJustify: (absence: { id: string; patientId: string }) => void;
  onDownload: (patientId: string, documentName: string) => void;
}

export function AbsenceHistoryItem({
  absence,
  patientId,
  onJustify,
  onDownload,
}: AbsenceHistoryItemProps) {
  return (
    <div className="flex flex-col md:flex-row md:items-start md:justify-between gap-4 p-3 bg-white rounded border">
      <div className="flex items-center gap-4">
        <Calendar className="h-4 w-4 text-red-500" />
        <span className="font-medium">
          {formatDateShortPTBR(absence.absenceDate)}
        </span>
      </div>
      <div className="flex items-center gap-2 flex-wrap">
        {absence.isJustified ? (
          <div className="text-sm text-gray-600 max-w-md break-words whitespace-pre-wrap">
            <span className="font-medium text-green-600">
              Falta Justificada:{" "}
            </span>
            {absence.justification}
          </div>
        ) : (
          <div className="flex items-center gap-3">
            {absence.justification && (
              <div
                className="text-sm text-gray-500 max-w-xs truncate"
                title={absence.justification}
              >
                <span className="font-medium text-gray-700">
                  Motivo original:{" "}
                </span>
                {absence.justification}
              </div>
            )}
            <Button
              size="sm"
              variant="outline"
              className="text-orange-600 border-orange-600 hover:bg-orange-600 hover:text-white"
              onClick={() => onJustify({ id: absence.id, patientId })}
            >
              Justificar Falta
            </Button>
          </div>
        )}
        {absence.justificationDocumentId && (
          <Button
            size="sm"
            variant="outline"
            onClick={() =>
              onDownload(patientId, absence.justificationDocumentId!)
            }
          >
            Baixar documento
          </Button>
        )}
      </div>
    </div>
  );
}
