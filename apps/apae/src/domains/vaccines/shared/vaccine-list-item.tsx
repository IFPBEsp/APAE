"use client";

import type { Vaccine } from "../vaccines.types";

interface VaccineListItemProps {
  vaccine: Vaccine;
}

export function VaccineListItem({ vaccine }: VaccineListItemProps) {
  return (
    <div className="flex items-center p-4 border-b hover:bg-gray-50 transition-colors">
      <div className="flex-1">
        <h3 className="font-bold text-base text-[#003B93]">{vaccine.name}</h3>
      </div>
    </div>
  );
}
