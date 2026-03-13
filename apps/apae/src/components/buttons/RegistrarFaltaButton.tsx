"use client";

import AbsenceService from "@/app/services/absenceService";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { useState } from "react";

interface RegistrarFaltaButtonProps {
  generatedAppointmentId: string;
  absenceDate: string;
  disabled?: boolean;
}

export function RegistrarFaltaButton({
  generatedAppointmentId,
  absenceDate,
  disabled,
}: RegistrarFaltaButtonProps) {
  const [motivo, setMotivo] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [open, setOpen] = useState(false); // controla abertura do dialog

  const handleConfirm = async () => {
    if (!motivo.trim()) {
      return;
    }

    setIsLoading(true);

    try {
      const dto = {
        generatedAppointmentId,
        absenceDate,
        justification: motivo.trim(),
      };

      await AbsenceService.registerAbsence(dto);
      setMotivo("");
      setOpen(false);
    } catch (error: any) {
      console.error("Erro ao registrar falta:", error);

      const message =
        error.message?.includes("Já existe uma falta")
          ? "Esta consulta já possui uma falta registrada."
          : error.message || "Erro ao registrar a falta. Tente novamente.";
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button
          className="bg-[#0D4F97] hover:bg-[#0b417f] text-white font-semibold px-4 py-2 rounded-md text-sm"
          size="sm"
          disabled={disabled}
        >
          Registrar falta
        </Button>
      </DialogTrigger>

      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>Registrar Falta</DialogTitle>
          <DialogDescription>
            Confirme o registro da falta para o agendamento.
          </DialogDescription>
        </DialogHeader>

        <div className="grid gap-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="motivo">Motivo da falta</Label>
            <Textarea
              id="motivo"
              placeholder="Ex: Paciente não compareceu, problema de saúde, esquecimento..."
              value={motivo}
              onChange={(e) => setMotivo(e.target.value)}
              className="min-h-[100px] resize-none"
              disabled={isLoading}
            />
          </div>
        </div>

        <DialogFooter className="flex gap-3 sm:justify-end">
          <DialogClose asChild>
            <Button variant="outline" disabled={isLoading}>
              Cancelar
            </Button>
          </DialogClose>

          <Button
            onClick={handleConfirm}
            disabled={isLoading || !motivo.trim()}
            className="bg-[#0D4F97] hover:bg-[#0b417f] text-white"
          >
            {isLoading ? "Registrando..." : "Confirmar Falta"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}