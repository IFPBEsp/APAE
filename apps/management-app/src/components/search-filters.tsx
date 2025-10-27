"use client";

import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { ChevronDown, Search } from "lucide-react";
import { cn } from "@/lib/utils";

interface SearchFiltersProps {
  readonly searchName?: string;
  readonly setSearchName?: (name: string) => void;
  readonly activeFilter?: string;
  readonly setActiveFilter?: (filter: string) => void;
  readonly activeStatus?: string;
  readonly setActiveStatus?: (status: string) => void;
}

export function SearchFilters({
  searchName,
  setSearchName,
  activeFilter,
  setActiveFilter,
  activeStatus,
  setActiveStatus,
}: SearchFiltersProps) {
  const statusItems = ["Todos", "Ativo", "Inativo", "Em Fila"];
  const hasStatusFilter = activeStatus !== undefined && setActiveStatus !== undefined;
  const hasTypeFilter = activeFilter !== undefined && setActiveFilter !== undefined;

  return (
    <div className="flex items-center justify-between gap-4 md:gap-8">
      <div className="relative flex-1">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-500" />
        <Input
          placeholder="Busque aqui"
          className="pl-10 h-[36px] border-2 border-[#0D4F97] rounded-[5px] placeholder-[#0D4F97]"
          value={searchName} 
          onChange={(e) => setSearchName?.(e.target.value)}
        />
      </div>

      <div className="flex items-center flex-shrink-0 gap-2">
        {hasStatusFilter && (
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button
                variant="outline"
                className="w-[98px] h-[36px] border-2 border-[#003B93] rounded-[5px] justify-between text-[#003B93] hover:text-[#003B93] hover:bg-slate-50"
              >
                <span>{activeStatus}</span>
                <ChevronDown className="h-4 w-4" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="w-[120px]">
              {statusItems.map((status) => (
                <DropdownMenuItem
                  key={status}
                  onClick={() => setActiveStatus(status)}
                  className={cn({ "bg-slate-100": activeStatus === status })}
                >
                  {status}
                </DropdownMenuItem>
              ))}
            </DropdownMenuContent>
          </DropdownMenu>
        )}
        {hasTypeFilter && (
          <div className="flex items-center gap-2">
            <Button
              className={`${
                activeFilter === "paciente"
                  ? "bg-[#0D4F97] text-white"
                  : "bg-white text-[#0D4F97] border border-[#0D4F97]"
              } h-[36px] px-4 rounded-[5px] hover:bg-[#0b427d] hover:text-white`}
              onClick={() => {
                setActiveFilter("paciente");
                setActiveStatus?.("Ativo"); 
              }}
            >
              Pacientes
            </Button>
            <Button
              className={`${
                activeFilter === "aluno"
                  ? "bg-[#0D4F97] text-white"
                  : "bg-white text-[#0D4F97] border border-[#0D4F97]"
              } h-[36px] px-4 rounded-[5px] hover:bg-[#0b427d] hover:text-white`}
              onClick={() => {
                setActiveFilter("aluno");
                setActiveStatus?.("Todos");
              }}
            >
              Alunos
            </Button>
          </div>
        )}

      </div>
    </div>
  );
}