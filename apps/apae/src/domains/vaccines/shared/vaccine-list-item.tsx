"use client";

import Link from "next/link";
import type { Vaccine } from "../vaccines.types";

interface VaccineListItemProps {
  vaccine: Vaccine;
  onDelete?: (id: string) => void;
}

export function VaccineListItem({ vaccine, onDelete }: VaccineListItemProps) {
  return (
    <div className="flex items-center justify-between p-4 border-b hover:bg-gray-50 transition-colors">
      <div className="flex-1">
        <h3 className="font-bold text-base text-[#003B93]">{vaccine.name}</h3>
        {vaccine.hasPatient && (
          <span className="text-xs text-amber-700 bg-amber-50 px-2 py-0.5 rounded mt-1 inline-block font-normal">
            Vinculada a paciente
          </span>
        )}
      </div>

      <div className="flex items-center gap-2">
        <Link
          href={`/vaccines/${vaccine.id}/edit`}
          className="px-3 py-1 text-sm font-medium text-[#003B93] bg-blue-50 rounded hover:bg-blue-100 transition-colors"
        >
          Editar
        </Link>

        <button
          onClick={() => onDelete?.(vaccine.id)}
          disabled={vaccine.hasPatient}
          title={
            vaccine.hasPatient
              ? "Não é possível excluir vacina vinculada a paciente"
              : "Excluir vacina"
          }
          className="px-3 py-1 text-sm font-medium text-red-600 bg-red-50 rounded hover:bg-red-100 disabled:opacity-40 disabled:cursor-not-allowed transition-colors"
        >
          Excluir
        </button>
      </div>
    </div>
  );
}