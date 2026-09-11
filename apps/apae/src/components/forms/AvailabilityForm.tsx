"use client";

import {
  FormField,
  FormItem,
  FormControl,
  FormLabel,
  FormDescription,
} from "@/components/ui/form";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Checkbox } from "@/components/ui/checkbox";
import { daysOfWeek, shifts } from "@/types/profissional";
import { Control, UseFormWatch, FieldValues, Path } from "react-hook-form";

type Props<T extends FieldValues> = {
  control: Control<T>;
  watch: UseFormWatch<T>;
};

export default function AvailabilityForm<T extends FieldValues>({ control, watch }: Readonly<Props<T>>) {
  const availability = watch("availability" as Path<T>) || [];

  return (
    <div className="space-y-4">
      <FormLabel>Disponibilidade</FormLabel>
      <FormDescription>
        Marque os dias e turnos em que o profissional está disponível.
      </FormDescription>

      <div className="border rounded-md">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead className="w-[100px]">Turno</TableHead>
              {daysOfWeek.map((day) => (
                <TableHead key={day.id} className="text-center">
                  {day.label}
                </TableHead>
              ))}
            </TableRow>
          </TableHeader>

          <TableBody>
            {shifts.map((shift) => (
              <TableRow key={shift.id}>
                <TableCell className="font-medium">{shift.label}</TableCell>

                {daysOfWeek.map((day) => {
                  const index = availability.findIndex(
                    (d: { day: string; shift: string }) => d.day === day.id && d.shift === shift.id
                  );

                  return (
                    <TableCell key={day.id} className="text-center">
                      <FormField
                        control={control}
                        name={`availability.${index}.checked` as Path<T>}
                        render={({ field }) => (
                          <FormItem className="flex items-center justify-center">
                            <FormControl>
                              <Checkbox
                                checked={field.value}
                                onCheckedChange={field.onChange}
                              />
                            </FormControl>
                          </FormItem>
                        )}
                      />
                    </TableCell>
                  );
                })}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </div>
  );
}
