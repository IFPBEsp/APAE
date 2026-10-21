"use client";

import { Input } from "@/components/ui/input";
import { SearchIcon } from "lucide-react";

interface AbsenceSearchInputProps {
  searchName: string;
  onSearchChange: (value: string) => void;
}

export function AbsenceSearchInput({
  searchName,
  onSearchChange,
}: AbsenceSearchInputProps) {
  return (
    <div className="mb-6">
      <div className="relative w-full">
        <SearchIcon className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          type="text"
          placeholder="Buscar paciente por nome..."
          value={searchName}
          onChange={(e) => onSearchChange(e.target.value)}
          className="pl-10 pr-3 border-[#0D4F97]"
        />
      </div>
    </div>
  );
}
