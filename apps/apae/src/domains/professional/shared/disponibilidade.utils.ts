import {
  daysOfWeek,
  AvailabilityType,
  shifts,
} from "@/types/profissional";

export function generateAvailabilityMatrix(lista: AvailabilityType[]): AvailabilityType[] {
  return daysOfWeek.flatMap((day) =>
    shifts.map((shift) => {
      const existente = lista.find((d) => {
        return d.day === day.id && d.shift === shift.id;
      });

      return (
        existente ?? {
          day: day.id,
          shift: shift.id,
          checked: false,
        }
      );
    }),
  );
}