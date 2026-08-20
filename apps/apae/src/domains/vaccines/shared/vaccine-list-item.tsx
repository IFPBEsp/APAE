"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import { Edit, Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { deleteVaccineApi } from "../vaccines.api";
import type { Vaccine } from "../vaccines.types";

interface VaccineListItemProps {
  vaccine: Vaccine;
  onDeleteSuccess: () => void;
}

export function VaccineListItem({ vaccine, onDeleteSuccess }: VaccineListItemProps) {
  const router = useRouter();
  const [isDeleting, setIsDeleting] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);

  const handleDelete = async () => {
    try {
      setIsDeleting(true);
      await deleteVaccineApi({ id: vaccine.id });
      toast.success("Vacina excluída com sucesso.");
      setShowConfirm(false);
      onDeleteSuccess();
    } catch (error) {
      const message = error instanceof Error ? error.message : "Erro ao excluir vacina.";
      toast.error(message);
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <div className="flex items-center justify-between p-4 border-b hover:bg-gray-50 transition-colors">
      <div className="flex-1">
        <h3 className="font-bold text-base text-[#003B93]">{vaccine.name}</h3>
        {vaccine.hasPatient && (
          <span className="text-xs text-amber-700 bg-amber-100 px-2 py-1 rounded mt-1 inline-block font-medium">
            Em uso por pacientes
          </span>
        )}
      </div>
      
      <div className="flex items-center gap-2">
        <Button
          variant="outline"
          size="icon"
          title="Editar Vacina"
          onClick={() => router.push(`/vaccines/${vaccine.id}/edit`)}
        >
          <Edit className="h-4 w-4 text-slate-600" />
        </Button>
        <Button
          variant="outline"
          size="icon"
          title={vaccine.hasPatient ? "Não é possível excluir vacina em uso" : "Excluir Vacina"}
          disabled={vaccine.hasPatient || isDeleting}
          onClick={() => setShowConfirm(true)}
          className="border-red-200 hover:bg-red-50"
        >
          <Trash2 className="h-4 w-4 text-red-500" />
        </Button>
      </div>

      {showConfirm && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
          <div className="bg-white rounded-xl p-6 max-w-sm w-full shadow-lg">
            <h3 className="text-lg font-bold text-slate-900 mb-2">Tem certeza?</h3>
            <p className="text-gray-600 text-sm mb-6">
              Essa ação não pode ser desfeita. Isso irá excluir permanentemente a vacina <strong>{vaccine.name}</strong>.
            </p>
            <div className="flex justify-end gap-2">
              <Button variant="outline" onClick={() => setShowConfirm(false)} disabled={isDeleting}>
                Cancelar
              </Button>
              <Button className="!bg-[#0D4F97] !hover:bg-[#0b427d] text-white" onClick={handleDelete} disabled={isDeleting}>
                {isDeleting ? "Confirmando..." : "Confirmar"}
              </Button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}