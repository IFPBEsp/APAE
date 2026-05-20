// src/components/search-filters.tsx

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

  readonly transtorno?: string;
  readonly setTranstorno?: (status: string) => void;
  readonly ano?: string;
  readonly setAno?: (status: string) => void;
  readonly city?: string;
  readonly setCity?: (status: string) => void;
  readonly tipoAtendimento?: string;
  readonly setTipoAtendimento?: (status: string) => void;

  readonly transtornoOptions?: string[];
  readonly anoOptions?: string[];
  readonly cityOptions?: string[];
  readonly tipoAtendimentoOptions?: string[];
}

export function SearchFilters({
  searchName,
  setSearchName,
  transtorno,
  setTranstorno,
  ano,
  setAno,
  city,
  setCity,
  tipoAtendimento,
  setTipoAtendimento,
  transtornoOptions = [], 
  anoOptions = [],
  cityOptions = [],
  tipoAtendimentoOptions = []
}: SearchFiltersProps) {
  
  const dropdownTriggerStyle = cn(
    "bg-white border border-gray-300 rounded-[5px] h-[36px]", 
    "justify-between text-gray-600", 
    "hover:bg-slate-50 hover:text-gray-700", 
    "data-[state=open]:bg-slate-50",
    "flex"
  );

  const showTranstornoFilter = setTranstorno !== undefined;
  const showAnoFilter = setAno !== undefined;
  const showCityFilter = setCity !== undefined;
  const showTipoAtendimentoFilter = setTipoAtendimento !== undefined;

  return (
    <div className="flex items-center gap-2">
      <div className="relative flex-1">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-500" />
        <Input
          placeholder="Busque por nome"
          className="pl-10 h-[36px] border-2 border-[#0D4F97] rounded-[5px] placeholder-[#0D4F97]"
          value={searchName} 
          onChange={(e) => setSearchName?.(e.target.value)}
        />
      </div>

      {(showTranstornoFilter || showAnoFilter || showCityFilter || showTipoAtendimentoFilter) && (
        <div className="flex flex-shrink-0 items-center gap-2">

          {showTipoAtendimentoFilter && (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button
                  className={cn(dropdownTriggerStyle, "w-[150px]")}
                >
                  <span className="flex-1 w-0 truncate text-left">
                    {tipoAtendimento || "Tipo de Atendimento"}
                  </span>
                  <ChevronDown className="h-4 w-4" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-[--radix-dropdown-menu-trigger-width]">
                <DropdownMenuItem onClick={() => setTipoAtendimento?.("")}>
                  Qualquer
                </DropdownMenuItem>
                {tipoAtendimentoOptions.map((option) => (
                  <DropdownMenuItem
                    key={option}
                    onClick={() => setTipoAtendimento?.(option)}
                    className={cn({ "bg-slate-100": tipoAtendimento === option })}
                  >
                    {option}
                  </DropdownMenuItem>
                ))}
              </DropdownMenuContent>
            </DropdownMenu>
          )}
          
          {showTranstornoFilter && (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button
                  className={cn(dropdownTriggerStyle, "w-[150px]")}
                >
                  <span className="flex-1 w-0 truncate text-left">
                    {transtorno || "Transtorno"}
                  </span>
                  <ChevronDown className="h-4 w-4" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-[--radix-dropdown-menu-trigger-width]">
                <DropdownMenuItem onClick={() => setTranstorno?.("")}>
                  Qualquer
                </DropdownMenuItem>
                {transtornoOptions.map((option) => (
                  <DropdownMenuItem
                    key={option}
                    onClick={() => setTranstorno?.(option)}
                    className={cn({ "bg-slate-100": transtorno === option })}
                  >
                    {option}
                  </DropdownMenuItem>
                ))}
              </DropdownMenuContent>
            </DropdownMenu>
          )}

          {showAnoFilter && (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button
                  className={cn(dropdownTriggerStyle, "w-[90px]")}
                >
                  <span className="flex-1 w-0 truncate text-left">
                    {ano || "Ano"}
                  </span>
                  <ChevronDown className="h-4 w-4" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-[100px]">
                <DropdownMenuItem onClick={() => setAno?.("")}>
                  Qualquer
                </DropdownMenuItem>
                {anoOptions.map((option) => (
                  <DropdownMenuItem
                    key={option}
                    onClick={() => setAno?.(option)}
                    className={cn({ "bg-slate-100": ano === option })}
                  >
                    {option}
                  </DropdownMenuItem>
                ))}
              </DropdownMenuContent>
            </DropdownMenu>
          )}
          
          {showCityFilter && (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button
                  className={cn(dropdownTriggerStyle, "w-[150px]")}
                >
                  <span className="flex-1 w-0 truncate text-left">
                    {city || "Cidade"}
                  </span>
                  <ChevronDown className="h-4 w-4" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-[--radix-dropdown-menu-trigger-width]">
                <DropdownMenuItem onClick={() => setCity?.("")}>
                  Qualquer
                </DropdownMenuItem>
                {cityOptions.map((option) => (
                  <DropdownMenuItem
                    key={option}
                    onClick={() => setCity?.(option)}
                    className={cn({ "bg-slate-100": city === option })}
                  >
                    {option}
                  </DropdownMenuItem>
                ))}
              </DropdownMenuContent>
            </DropdownMenu>
          )}

        </div>
      )}
    </div>
  );
}
