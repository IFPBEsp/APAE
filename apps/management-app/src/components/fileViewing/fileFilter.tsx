"use client";

import * as React from "react";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

type FileFilterProps = {
  year: string | null;
  type: string | null;
  onYearChange: (year: string | null) => void;
  onTypeChange: (type: string | null) => void;
};

const years = [2023, 2024, 2025];
const types = ["laudo", "encaminhamento", "escolar", "pessoal"];

const selectStyle =
  "w-48 border-2 border-[#0d4f97] rounded-md focus:outline-none focus:ring-2 focus:ring-[#0d4f97]";
const textColor = "text-[#0d4f97]";

export default function FileFilter({
  onYearChange,
  onTypeChange,
}: FileFilterProps) {
  return (
    <div className="flex justify-center gap-4 mb-4">
      {/* Filtro de ano */}
      <Select onValueChange={onYearChange}>
        <SelectTrigger className={`${selectStyle} ${textColor}`}>
          <SelectValue className={textColor} placeholder="Ano" />
        </SelectTrigger>
        <SelectContent>
          <SelectItem value="all" className={textColor}>
            Ano
          </SelectItem>
          {years.map((y) => (
            <SelectItem key={y} value={String(y)} className={textColor}>
              {y}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>

      {/* Filtro de tipo */}
      <Select onValueChange={onTypeChange}>
        <SelectTrigger className={`${selectStyle} ${textColor}`}>
          <SelectValue className={textColor} placeholder="Tipo" />
        </SelectTrigger>
        <SelectContent>
          <SelectItem value="all" className={textColor}>
            Tipo
          </SelectItem>
          {types.map((t) => (
            <SelectItem key={t} value={t} className={textColor}>
              {t.charAt(0).toUpperCase() + t.slice(1)}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>
    </div>
  );
}
