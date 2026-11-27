"use client";

import { Button } from "@/components/ui/button";
import { DocumentUpload } from "@/components/shared/DocumentUpload";

interface JustifyAbsenceDialogProps {
  isOpen: boolean;
  justificationText: string;
  onJustificationTextChange: (text: string) => void;
  file: File | null;
  onFileChange: (file: File | null) => void;
  isSubmitting: boolean;
  onCancel: () => void;
  onSubmit: () => void;
}

export function JustifyAbsenceDialog({
  isOpen,
  justificationText,
  onJustificationTextChange,
  file,
  onFileChange,
  isSubmitting,
  onCancel,
  onSubmit,
}: JustifyAbsenceDialogProps) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-xl p-6 max-w-md w-full shadow-2xl border border-gray-100">
        <div className="mb-6">
          <h3 className="text-xl font-bold text-[#0D4F97] mb-2">
            Justificar Falta
          </h3>
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
              onChange={(e) => onJustificationTextChange(e.target.value)}
            />
          </div>
          <DocumentUpload
            label="Anexar documento (Opcional)"
            file={file}
            onFileChange={onFileChange}
            disabled={isSubmitting}
          />
        </div>
        <div className="flex justify-end gap-3 mt-8 pt-4 border-t border-gray-100">
          <Button
            variant="outline"
            className="w-full sm:w-auto"
            onClick={onCancel}
            disabled={isSubmitting}
          >
            Cancelar
          </Button>
          <Button
            className="w-full sm:w-auto bg-[#0D4F97] hover:bg-blue-900 text-white"
            onClick={onSubmit}
            disabled={isSubmitting || !justificationText.trim()}
          >
            {isSubmitting ? "Salvando..." : "Registrar Justificativa"}
          </Button>
        </div>
      </div>
    </div>
  );
}
