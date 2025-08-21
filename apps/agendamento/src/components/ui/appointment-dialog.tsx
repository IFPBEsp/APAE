"use client";

import { useState } from "react";
import {
  Dialog,
  DialogTrigger,
  DialogContent,
  DialogTitle,
  DialogDescription,
  DialogHeader,
} from "@/components/ui/dialog";
import {
  Appointment,
  AppointmentForm,
} from "@/components/forms/AppointmentForm";
import { Button } from "@/components/ui/button";
import { SquareArrowOutUpRight } from "lucide-react";

interface AppointmentDialogProps {
  mode: "create" | "edit";
  appointment?: Appointment;
  onSuccess?: () => void;
  children?: React.ReactNode;
}

export function AppointmentDialog({
  mode,
  appointment,
  onSuccess,
  children,
}: AppointmentDialogProps) {
  const [open, setOpen] = useState(false);

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        {children || (
          <Button
            variant="ghost"
            className="text-blue-800 hover:text-blue-900 p-2"
          >
            <SquareArrowOutUpRight />
          </Button>
        )}
      </DialogTrigger>
      <DialogContent className="w-full max-w-full sm:max-w-sm md:max-w-md mx-auto border-none shadow-none p-4 sm:p-6">
        <DialogHeader>
          <DialogTitle>
            {mode === "create"
              ? "Cadastrar novo agendamento"
              : "Editar agendamento"}
          </DialogTitle>
          <DialogDescription>
            {mode === "create"
              ? "Preencha os detalhes abaixo para agendar uma consulta."
              : "Preencha os detalhes abaixo para editar a consulta."}
          </DialogDescription>
        </DialogHeader>
        <AppointmentForm
          mode={mode}
          appointment={appointment}
          onSuccess={() => {
            setOpen(false);
            if (onSuccess) onSuccess();
          }}
        />
      </DialogContent>
    </Dialog>
  );
}
