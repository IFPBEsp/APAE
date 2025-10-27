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

// 1. TORNAMOS AS PROPS OPCIONAIS COM '?'
interface SearchFiltersProps {
  readonly searchName?: string;
  readonly setSearchName?: (name: string) => void;
  readonly activeFilter?: string; // <--- Opcional
  readonly setActiveFilter?: (filter: string) => void; // <--- Opcional
  readonly activeStatus?: string; // <--- Opcional
  readonly setActiveStatus?: (status: string) => void; // <--- Opcional
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

  // 2. VERIFICAMOS SE AS PROPS FORAM FORNECIDAS
  const hasStatusFilter = activeStatus !== undefined && setActiveStatus !== undefined;
  const hasTypeFilter = activeFilter !== undefined && setActiveFilter !== undefined;

  return (
    // Ajustado para 'justify-between' para que a busca se expanda
    <div className="flex items-center justify-between gap-4 md:gap-8">
      
      {/* --- SEÇÃO DE BUSCA (SEMPRE APARECE) --- */}
      <div className="relative flex-1">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-500" />
        <Input
          placeholder="Busque aqui"
          className="pl-10 h-[36px] border-2 border-[#0D4F97] rounded-[5px] placeholder-[#0D4F97]"
          value={searchName} // Use 'value' para um componente controlado
          onChange={(e) => setSearchName?.(e.target.value)}
        />
      </div>

      {/* --- SEÇÃO DE FILTROS (OPCIONAL) --- */}
      <div className="flex items-center flex-shrink-0 gap-2">
        
        {/* 3. RENDERIZAÇÃO CONDICIONAL DO STATUS */}
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

        {/* 4. RENDERIZAÇÃO CONDICIONAL DOS BOTÕES */}
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
                // Usamos '?' para chamar setStatus opcionalmente
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
                // Usamos '?' para chamar setStatus opcionalmente
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