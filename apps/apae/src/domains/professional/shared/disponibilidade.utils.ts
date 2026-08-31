import { daysOfWeek, AvailabilityType, shifts } from "@/types/profissional";

export function generateAvailabilityMatrix(list: AvailabilityType[]) {
  return daysOfWeek.flatMap((day) =>
    shifts.map((shift) => {
      const existing = list.find((d) => {
        return d.day == day.id && d.shift == shift.id;
      });

      return (
        existing ?? {
          day: day.id,
          shift: shift.id,
          checked: false,
        }
      );
    }),
  );
}
