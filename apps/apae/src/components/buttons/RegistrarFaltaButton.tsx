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
import { useState } from "react";
import { AbsenceForm } from "../forms/AbsenceForm";

interface RegistrarFaltaButtonProps {
  generatedAppointmentId: string;
  patientId: string,
  absenceDate: string;
  disabled?: boolean;
  onSuccess?: () => void;
}

export function RegistrarFaltaButton({
  generatedAppointmentId,
  patientId,
  absenceDate,
  disabled,
  onSuccess,
}: RegistrarFaltaButtonProps) {
  const [open, setOpen] = useState(false);

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
        <AbsenceForm generatedAppointmentId={generatedAppointmentId} absenceDate={absenceDate} onSuccess={onSuccess} patientId={patientId} />
      </DialogContent>
    </Dialog>
  );
}