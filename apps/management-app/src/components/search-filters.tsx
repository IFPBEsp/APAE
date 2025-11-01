"use client";

import { Input } from "@/components/ui/input";
import { Search } from "lucide-react";

interface SearchFiltersProps {
  readonly searchName?: string;
  readonly setSearchName?: (name: string) => void;
  readonly tipoAtendimento?: string;
  readonly setTipoAtendimento?: (filter: string) => void;
  readonly transtorno?: string;
  readonly setTranstorno?: (status: string) => void;
  readonly ano?: string;
  readonly setAno?: (status: string) => void;
  readonly cidade?: string;
  readonly setCidade?: (status: string) => void;
}

export function SearchFilters({
  searchName,
  setSearchName,
  tipoAtendimento,
  setTipoAtendimento,
  transtorno,
  setTranstorno,
  ano,
  setAno,
  cidade,
  setCidade,
}: SearchFiltersProps) {

  return (
    <div className="grid grid-cols-1 md:grid-cols-5 gap-4 items-center">
      <div className="relative md:col-span-2">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-500" />
        <Input
          placeholder="Buscar por Nome..."
          className="pl-10 h-[36px] border-2 border-[#0D4F97] rounded-[5px] placeholder-[#0D4F97] w-full"
          value={searchName}
          onChange={(e) => setSearchName?.(e.target.value)}
        />
      </div>

      <Input
        placeholder="Tipo Atendimento"
        className="h-[36px] border rounded-[5px] placeholder-gray-600 w-full"
        value={tipoAtendimento}
        onChange={(e) => setTipoAtendimento?.(e.target.value)}
      />
      <Input
        placeholder="Transtorno"
        className="h-[36px] border rounded-[5px] placeholder-gray-600 w-full"
        value={transtorno}
        onChange={(e) => setTranstorno?.(e.target.value)}
      />
      <Input
        placeholder="Ano"
        type="number"
        className="h-[36px] border rounded-[5px] placeholder-gray-600 w-full"
        value={ano}
        onChange={(e) => setAno?.(e.target.value)}
      />
      <Input
        placeholder="Cidade"
        className="h-[36px] border rounded-[5px] placeholder-gray-600 w-full"
        value={cidade}
        onChange={(e) => setCidade?.(e.target.value)}
      />
    </div>
  );
}