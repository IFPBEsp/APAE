"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Pencil, Trash2 } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"; 
import type { Vaccine } from "../vaccines.types";

interface VaccineListItemProps {
  vaccine: Vaccine;
  onDelete: (id: string) => Promise<void>;
}

export function VaccineListItem({ vaccine, onDelete }: VaccineListItemProps) {
  const router = useRouter();
  const [isDeleting, setIsDeleting] = useState(false);
  const [isDialogOpen, setIsDialogOpen] = useState(false); 

  async function handleConfirmDelete() {
    setIsDeleting(true);
    await onDelete(vaccine.id);
    setIsDeleting(false);
    setIsDialogOpen(false);
  }

  return (
    <div className="flex items-center p-4 border-b hover:bg-gray-50 transition-colors justify-between">
      <div className="flex-1 flex flex-col gap-1">
        <h3 className="font-bold text-base text-[#003B93]">{vaccine.name}</h3>
        {vaccine.hasPatient && (
          <span className="text-xs text-red-500 font-medium">
            Vinculada a paciente (não pode ser excluída)
          </span>
        )}
      </div>

      <div className="flex gap-2">
        <Button
          size="icon"
          variant="ghost"
          onClick={() => router.push(`/vaccines/${vaccine.id}/edit`)}
          title="Editar vacina"
        >
          <Pencil className="h-4 w-4 text-muted-foreground hover:text-foreground" />
        </Button>

        <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
          <DialogTrigger asChild>
            <Button
              size="icon"
              variant="ghost"
              className="text-red-500 hover:text-red-600 disabled:opacity-40 disabled:text-muted-foreground"
              disabled={vaccine.hasPatient || isDeleting}
              title={
                vaccine.hasPatient
                  ? "Esta vacina não pode ser excluída pois está em uso por pacientes."
                  : "Excluir vacina"
              }
            >
              <Trash2 className="h-4 w-4" />
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle className="text-lg font-bold">Excluir Vacina</DialogTitle>
              <DialogDescription className="text-sm text-muted-foreground mt-2">
                Tem certeza de que deseja excluir a vacina &quot;{vaccine.name}&quot;? Esta ação é definitiva e não poderá ser desfeita.
              </DialogDescription>
            </DialogHeader>
            <DialogFooter className="flex gap-3 justify-end mt-4">
              <Button
                variant="outline"
                disabled={isDeleting}
                onClick={() => setIsDialogOpen(false)}
              >
                Cancelar
              </Button>
              <Button
                variant="destructive"
                disabled={isDeleting}
                onClick={handleConfirmDelete}
              >
                {isDeleting ? "Excluindo..." : "Confirmar Exclusão"}
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </div>
    </div>
  );
}