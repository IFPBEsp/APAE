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

  readonly tipoAtendimento?: string; 
  readonly setTipoAtendimento?: (filter: string) => void;
  readonly transtorno?: string;
  readonly setTranstorno?: (status: string) => void;
  readonly ano?: string;
  readonly setAno?: (status: string) => void;
  readonly cidade?: string;
  readonly setCidade?: (status: string) => void;

  readonly tipoAtendimentoOptions?: string[];
  readonly transtornoOptions?: string[];
  readonly anoOptions?: string[];
  readonly cidadeOptions?: string[];
}

export function SearchFilters({
  searchName,
  setSearchName,
  // tipoAtendimento,
  // setTipoAtendimento,
  transtorno,
  setTranstorno,
  ano,
  setAno,
  cidade,
  setCidade,
  // tipoAtendimentoOptions = [], 
  transtornoOptions = [], 
  anoOptions = [],
  cidadeOptions = [],
}: SearchFiltersProps) {
  
  const dropdownTriggerStyle = cn(
    "bg-white border border-gray-300 rounded-[5px] h-[36px]", 
    "justify-between text-gray-600", 
    "hover:bg-slate-50 hover:text-gray-700", 
    "data-[state=open]:bg-slate-50" 
  );

  return (
    <div className="flex items-center justify-between gap-2">
      <div className="relative flex-1">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-500" />
        <Input
          placeholder="Busque por nome"
          className="pl-10 h-[36px] border-2 border-[#0D4F97] rounded-[5px] placeholder-[#0D4F97]"
          value={searchName} 
          onChange={(e) => setSearchName?.(e.target.value)}
        />
      </div>

      {/* <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button
            className={cn(dropdownTriggerStyle, "w-[120px]")}
          >
            <span>{
              (tipoAtendimento 
                ? tipoAtendimentoOptions.find(o => o.toLowerCase() === tipoAtendimento) 
                : null) 
              || "Tipo Atend."
            }</span>
            <ChevronDown className="h-4 w-4" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end" className="w-[150px]">
          <DropdownMenuItem onClick={() => setTipoAtendimento?.("")}>
            Qualquer
          </DropdownMenuItem>
          {tipoAtendimentoOptions.map((option) => (
            <DropdownMenuItem
              key={option}
              onClick={() => setTipoAtendimento?.(option.toLowerCase())}
              className={cn({ "bg-slate-100": tipoAtendimento === option.toLowerCase() })}
            >
              {option}
            </DropdownMenuItem>
          ))}
        </DropdownMenuContent>
      </DropdownMenu> */}

      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button
            className={cn(dropdownTriggerStyle, "w-[120px]")}
          >
            <span>{transtorno || "Transtorno"}</span>
            <ChevronDown className="h-4 w-4" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end" className="w-[150px]">
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

      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button
            className={cn(dropdownTriggerStyle, "w-[80px]")}
          >
            <span>{ano || "Ano"}</span>
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
      
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button
            className={cn(dropdownTriggerStyle, "w-[120px]")}
          >
            <span>{cidade || "Cidade"}</span>
            <ChevronDown className="h-4 w-4" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end" className="w-[150px]">
          <DropdownMenuItem onClick={() => setCidade?.("")}>
            Qualquer
          </DropdownMenuItem>
          {cidadeOptions.map((option) => (
            <DropdownMenuItem
              key={option}
              onClick={() => setCidade?.(option)}
              className={cn({ "bg-slate-100": cidade === option })}
            >
              {option}
            </DropdownMenuItem>
          ))}
        </DropdownMenuContent>
      </DropdownMenu>

    </div>
  );
}