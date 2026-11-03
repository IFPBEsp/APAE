"use client";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { cn } from "@/lib/utils";

interface AppointmentTimeSelectProps {
  date: Date | undefined;
  selectedTime: string;
  validTimeSlots: string[];
  hasError: boolean;
  onTimeChange: (time: string) => void;
}

export function AppointmentTimeSelect({
  date,
  selectedTime,
  validTimeSlots,
  hasError,
  onTimeChange,
}: AppointmentTimeSelectProps) {
  return (
    <div className="flex flex-col space-y-2">
      <Select
        disabled={!date}
        onValueChange={onTimeChange}
        value={selectedTime}
      >
        <SelectTrigger
          className={cn("w-full", hasError && "border-red-500")}
        >
          <SelectValue
            placeholder={date ? "Escolha o horário" : "Selecione a data primeiro"}
          />
        </SelectTrigger>

        <SelectContent>
          {validTimeSlots.length === 0 ? (
            <div className="p-2 text-sm text-muted-foreground text-red-500">
              Nenhum horário disponível
            </div>
          ) : (
            validTimeSlots.map((slot) => (
              <SelectItem key={slot} value={slot}>
                {slot}
              </SelectItem>
            ))
          )}
        </SelectContent>
      </Select>
    </div>
  );
}