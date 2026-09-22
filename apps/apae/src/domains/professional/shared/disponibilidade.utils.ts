import { DAYS, SHIFTS, type AvailabilityType } from "@/types/profissional";

type AvailabilityInput = Partial<AvailabilityType> & {
  day?: string;
  shift?: string;
  checked?: boolean;
};

export function generateAvailabilityMatrix(list: AvailabilityInput[] = []) {
  return DAYS.flatMap((day) =>
    SHIFTS.map((shift) => {
      const existing = list.find(
        (item) =>
          String(item.day ?? "").toLowerCase() === day &&
          String(item.shift ?? "").toLowerCase() === shift,
      );

      return {
        day,
        shift,
        checked: Boolean(existing?.checked),
      };
    }),
  );
}

export function toAvailabilityMatrix(availabilities: AvailabilityInput[] = []) {
  return generateAvailabilityMatrix(
    availabilities.map((availability) => ({
      day: String(availability.day ?? "").toLowerCase(),
      shift: String(availability.shift ?? "").toLowerCase(),
      checked: true,
    })),
  );
}

export function extractCheckedAvailabilities(list: AvailabilityInput[] = []) {
  return list
    .filter((item) => item.checked)
    .map((item) => ({
      day: String(item.day ?? ""),
      shift: String(item.shift ?? ""),
    }));
}
