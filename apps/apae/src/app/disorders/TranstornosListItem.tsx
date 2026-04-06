"use client";

import { Button } from "@/components/ui/button";
import { Edit, Trash2 } from "lucide-react";
import { Transtorno } from "@/schemas/transtornosSchema";
import { ConfirmModal } from "@/components/ui/confirm-modal";
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@/components/ui/tooltip";

interface TranstornoListItemProps {
  transtorno: Transtorno;
  onEdit: () => void;
  onDelete: () => void;
}

export function TranstornoListItem({
  transtorno,
  onEdit,
  onDelete,
}: TranstornoListItemProps) {
  return (
    <div className="flex items-center justify-between p-4 border-b hover:bg-gray-50 transition-colors">
      <div className="flex-1">
        <h3 className="font-bold text-base text-[#003B93]">{transtorno.name}</h3>
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

        {transtorno.hasPatient ? (
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>
                <span className="cursor-not-allowed">
                  <Button
                    variant="outline"
                    size="icon"
                    disabled
                    className="h-8 w-8 pointer-events-none opacity-50"
                  >
                    <Trash2 className="h-4 w-4 text-red-500" />
                  </Button>
                </span>
              </TooltipTrigger>
              <TooltipContent side="top">
                <p>Não é possível excluir transtorno pois está associado a um paciente!</p>
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>
        ) : (
          <ConfirmModal
            title="Tem certeza?"
            description={<>Essa ação não pode ser desfeita. Isso irá excluir permanentemente o transtorno <strong>{transtorno.name}</strong>.</>}
            onConfirm={onDelete}
            trigger={
              <Button
                variant="outline"
                size="icon"
                className="h-8 w-8 hover:border-red-500"
              >
                <Trash2 className="h-4 w-4 text-red-500 " />
              </Button>
            }
          />
        )}
      </div>
    </div>
  );
}
