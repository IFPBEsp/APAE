"use client";

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
import { toast } from "react-toastify"
import { useState } from "react";

interface RegistrarFaltaButtonProps {
  generatedAppointmentId: string;
  absenceDate: string;
  disabled?: boolean;
  onSuccess?: () => void;
}

export function RegistrarFaltaButton({
  generatedAppointmentId,
  absenceDate,
  disabled,
  onSuccess,
}: RegistrarFaltaButtonProps) {
  const [motivo, setMotivo] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [open, setOpen] = useState(false);

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

      const response = await fetch("/api/absence", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(dto),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message);
      }

      setMotivo("");
      setOpen(false);
      onSuccess?.();
      toast.success("Salvo com sucesso!");
    } catch (error) {
      console.error("Erro ao registrar falta:", error);

      const errorMessage = error instanceof Error ? error.message : String(error);
      const message =
        errorMessage.includes("Já existe uma falta")
          ? "Esta consulta já possui uma falta registrada."
          : errorMessage || "Erro ao registrar a falta. Tente novamente.";

          toast.error(message);
          setOpen(false);
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