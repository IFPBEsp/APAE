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
  readonly year: string;
  readonly type: string;
  readonly onYearChange: (year: string) => void;
  readonly onTypeChange: (type: string) => void;
};

const years = [2023, 2024, 2025];
const types = ["laudo", "encaminhamento", "escolar", "pessoal"];

const selectStyle =
  "w-48 border-2 border-[#0d4f97] rounded-md focus:outline-none focus:ring-2 focus:ring-[#0d4f97]";
const textColor = "text-[#0d4f97]";

export default function FileFilter({ year, type, onYearChange, onTypeChange }: FileFilterProps) {
  console.log(type);
  return (
    <div className="flex justify-center gap-4 items-center">
      <Select onValueChange={(value) => onYearChange(value)} value={year}>
        <SelectTrigger className={`${selectStyle} ${textColor}`}>
          <SelectValue className={textColor} placeholder="Ano" />
        </SelectTrigger>
        <SelectContent>
          {years.map((y) => (
            <SelectItem key={y} value={String(y)} className={textColor}>
              {y}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>

      <Select onValueChange={(value) => onTypeChange(value)} value={type.toLowerCase()}>
        <SelectTrigger className={`${selectStyle} ${textColor}`}>
          <SelectValue className={textColor} placeholder="Tipo" />
        </SelectTrigger>
        <SelectContent>
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
