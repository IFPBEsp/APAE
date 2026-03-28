"use client";

import { Button } from "@/components/ui/button";
import { Edit, Trash2 } from "lucide-react";
import { Transtorno } from "@/schemas/transtornosSchema";
import { ConfirmModal } from "@/components/ui/confirm-modal";


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
        
        <ConfirmModal 

          title="Tem certeza?"
          description={`Essa ação não pode ser desfeita. Isso irá excluir permanentemente o transtorno ${transtorno.name}`}
          onConfirm={onDelete}
          trigger={<Button
                            variant="outline"
                            size="icon"
                            className="h-8 w-8 hover:bg-red-50 hover:border-red-500"
                            aria-label="Excluir"
                        >
                            <Trash2 className="h-4 w-4 text-red-500" />
                        </Button>}
        />

      </div>
    </div>
  );
}
