"use client";

import { format, isValid } from "date-fns";
import { ptBR } from "date-fns/locale";
import { CalendarIcon } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Calendar } from "@/components/ui/calendar";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { cn } from "@/lib/utils";

interface AppointmentDatePickerProps {
  date: Date | undefined;
  isCalendarOpen: boolean;
  hasError: boolean;
  isDayDisabled: (day: Date) => boolean;
  onDateChange: (date: Date | undefined) => void;
  onOpenChange: (open: boolean) => void;
}

export function AppointmentDatePicker({
  date,
  isCalendarOpen,
  hasError,
  isDayDisabled,
  onDateChange,
  onOpenChange,
}: AppointmentDatePickerProps) {
  return (
    <div className="flex flex-col space-y-2">
      <Popover open={isCalendarOpen} onOpenChange={onOpenChange}>
        <PopoverTrigger asChild>
          <Button
            variant={"outline"}
            className={cn(
              "w-full justify-start text-left font-normal",
              !date && "text-muted-foreground",
              hasError && "border-red-500",
            )}
          >
            <CalendarIcon className="mr-2 h-4 w-4" />
            {date && isValid(date) ? (
              format(date, "PPP", { locale: ptBR })
            ) : (
              <span>Selecione uma data</span>
            )}
          </Button>
        </PopoverTrigger>

        <PopoverContent className="w-auto p-0" align="start">
          <Calendar
            mode="single"
            selected={date}
            onSelect={(d) => {
              onDateChange(d);
              onOpenChange(false);
            }}
            disabled={isDayDisabled}
            initialFocus
          />
        </PopoverContent>
      </Popover>
    </div>
  );
}