"use client";

import { Button } from "@/components/ui/button";
import { ConfirmModal } from "@/components/ui/confirm-modal";
import { Edit, Trash2 } from "lucide-react";
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@radix-ui/react-tooltip";
import type { Vaccine } from "../vaccines.types";

interface VaccineListItemProps {
  vaccine: Vaccine;
  onEdit: () => void;
  onDelete: () => void;
}

export function VaccineListItem({ vaccine, onEdit, onDelete }: VaccineListItemProps) {
  return (
    <div className="flex items-center justify-between p-4 border-b hover:bg-gray-50 transition-colors">
      <div className="flex-1">
        <h3 className="font-bold text-base text-[#003B93]">{vaccine.name}</h3>
      </div>
      <div className="flex items-center gap-2 ml-4">
        <Button
          variant="outline"
          size="icon"
          className="h-8 w-8"
          onClick={onEdit}
          aria-label="Editar"
        >
          <Edit className="h-4 w-4" />
        </Button>

        {vaccine.hasPatient ? (
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>
                <span className="cursor-not-allowed">
                  <Button
                    variant="outline"
                    size="icon"
                    disabled
                    className="opacity-50 pointer-events-none h-8 w-8"
                  >
                    <Trash2 className="h-4 w-4 text-red-500" />
                  </Button>
                </span>
              </TooltipTrigger>
              <TooltipContent side="top" className="bg-slate-800 text-white p-2 rounded shadow-lg">
                <p>Não é possível excluir uma vacina associada a um paciente!</p>
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>
        ) : (
          <ConfirmModal
            title="Tem certeza?"
            description={
              <>
                Essa ação não pode ser desfeita. Isso irá excluir permanentemente a vacina{" "}
                <strong>{vaccine.name}</strong>.
              </>
            }
            onConfirm={onDelete}
            trigger={
              <Button variant="outline" size="icon" className="h-8 w-8 hover:bg-red-50">
                <Trash2 className="h-4 w-4 text-red-500" />
              </Button>
            }
          />
        )}
      </div>
    </div>
  );
}
