import { Checkbox } from "@/components/ui/checkbox";
import { DAYS, SHIFTS, DAY_LABEL, SHIFT_LABEL } from "@/domains/professional/constants/availability.constants";

interface AvailabilityEntry {
  day?: string;
  shift?: string;
  checked?: boolean;
}

interface AvailabilityGridProps {
  matrix: AvailabilityEntry[];
}

export function AvailabilityGrid({ matrix }: AvailabilityGridProps) {
  if (!matrix || matrix.length === 0) return <p className="text-gray-700">—</p>;

  return (
    <div className="overflow-x-auto rounded-md border">
      <table className="w-full text-sm">
        <thead>
          <tr className="bg-slate-50">
            <th className="p-3 text-left font-semibold text-[#0D4F97]">Turno</th>
            {DAYS.map((day) => (
              <th key={day} className="p-3 text-center font-semibold text-[#0D4F97]">
                {DAY_LABEL[day]}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {SHIFTS.map((shift) => (
            <tr key={shift} className="border-t">
              <td className="p-3 font-medium text-gray-700">{SHIFT_LABEL[shift]}</td>
              {DAYS.map((day) => {
                const cell = matrix.find((d) => d?.day === day && d?.shift === shift);
                return (
                  <td key={`${day}-${shift}`} className="p-3 text-center">
                    <Checkbox checked={Boolean(cell?.checked)} disabled />
                  </td>
                );
              })}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}