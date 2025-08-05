"use client"

import * as React from "react"
import { ChevronDownIcon } from "lucide-react"
import { format } from "date-fns"

import { Button } from "@/components/ui/button"
import { Calendar } from "@/components/ui/calendar"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover"

interface DateTimePickerProps {
  value?: Date;
  onChange: (date: Date) => void;
}

export function DateTimePicker({ value, onChange }: DateTimePickerProps) {
  const [dateTime, setDateTime] = React.useState<Date | undefined>(value);

  const handleDateSelect = (selectedDate: Date | undefined) => {
    if (!selectedDate) return;

    // Use a hora atual se não houver um dateTime definido
    const hours = dateTime ? dateTime.getHours() : new Date().getHours();
    const minutes = dateTime ? dateTime.getMinutes() : new Date().getMinutes();
    
    const newDateTime = new Date(
      selectedDate.getFullYear(),
      selectedDate.getMonth(),
      selectedDate.getDate(),
      hours,
      minutes
    );

    setDateTime(newDateTime);
    onChange(newDateTime);
  };

  const handleTimeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const timeValue = e.target.value;
    const [hours, minutes] = timeValue.split(':').map(Number);
    
    // Use a data atual se não houver um dateTime definido
    const currentDateTime = dateTime || new Date();

    const newDateTime = new Date(currentDateTime);
    newDateTime.setHours(hours, minutes);
    
    setDateTime(newDateTime);
    onChange(newDateTime);
  };

  React.useEffect(() => {
    setDateTime(value);
  }, [value]);

  return (
    <div className="flex gap-4">
      <div className="flex flex-col gap-3">
        <Label htmlFor="date-picker" className="px-1">
          Date
        </Label>
        <Popover>
          <PopoverTrigger asChild>
            <Button
              variant="outline"
              id="date-picker"
              className="w-32 justify-between font-normal"
            >
              {dateTime ? format(dateTime, "PPP") : "Select date"}
              <ChevronDownIcon className="size-4" />
            </Button>
          </PopoverTrigger>
          <PopoverContent className="w-auto p-0" align="start">
            <Calendar
              mode="single"
              selected={dateTime}
              captionLayout="dropdown"
              onSelect={handleDateSelect}
            />
          </PopoverContent>
        </Popover>
      </div>
      <div className="flex flex-col gap-3">
        <Label htmlFor="time-picker" className="px-1">
          Time
        </Label>
        <Input
          type="time"
          id="time-picker"
          defaultValue={dateTime ? format(dateTime, "HH:mm") : "12:00"}
          onChange={handleTimeChange}
          className="bg-background appearance-none [&::-webkit-calendar-picker-indicator]:hidden [&::-webkit-calendar-picker-indicator]:appearance-none"
        />
      </div>
    </div>
  )
}