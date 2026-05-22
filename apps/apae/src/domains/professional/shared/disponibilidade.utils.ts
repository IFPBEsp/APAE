import {
  diasDaSemana,
  AvailabilityType,
  shifts,
} from "@/types/profissional";

export function generateAvailabilityMatrix(list: AvailabilityType[]) {
  return diasDaSemana.flatMap((day) =>
    shifts.map((shift) => {
      const existente = list.find((d) => {
        return d.day == day.id && d.shift == shift.id;
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
