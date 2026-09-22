import {
  daysOfWeek,
  AvailabilityType,
  shifts,
  AvailabilityDTO,
} from "@/types/profissional";

export function generateAvailabilityMatrix(list: AvailabilityType[]): AvailabilityType[] {
  return daysOfWeek.flatMap((day) =>
    shifts.map((shift) => {
      const existing = list.find((d) => {
        return d.day === day.id && d.shift === shift.id;
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

export function buildAvailabilityMatrixFromDTOs(
  avs: AvailabilityDTO[] = []
): AvailabilityType[] {
  return generateAvailabilityMatrix(
    avs.map((a) => ({
      day: a.day?.toLowerCase() || "",
      shift: a.shift?.toLowerCase() || "",
      checked: true,
    })),
  );
}