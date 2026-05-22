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

  readonly disorder?: string;
  readonly setDisorder?: (status: string) => void;
  readonly year?: string;
  readonly setYear?: (status: string) => void;
  readonly city?: string;
  readonly setCity?: (status: string) => void;
  readonly serviceArea?: string;
  readonly setServiceArea?: (status: string) => void;

  readonly disorderOptions?: string[];
  readonly anoOptions?: string[];
  readonly cityOptions?: string[];
  readonly serviceAreaOptions?: string[];
}

export function SearchFilters({
  searchName,
  setSearchName,
  disorder,
  setDisorder,
  year,
  setYear,
  city,
  setCity,
  serviceArea,
  setServiceArea,
  disorderOptions = [],
  anoOptions = [],
  cityOptions = [],
  serviceAreaOptions = [],
}: SearchFiltersProps) {
  const dropdownTriggerStyle = cn(
    "bg-white border border-gray-300 rounded-[5px] h-[36px]",
    "justify-between text-gray-600",
    "hover:bg-slate-50 hover:text-gray-700",
    "data-[state=open]:bg-slate-50",
    "flex",
  );

  const showDisorderFilter = setDisorder !== undefined;
  const showAnoFilter = setYear !== undefined;
  const showCityFilter = setCity !== undefined;
  const showServiceAreaFilter = setServiceArea !== undefined;

  return (
    <div className="flex flex-col sm:flex-row items-start sm:items-center gap-2">
      <div className="relative w-full sm:flex-1">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-500" />
        <Input
          placeholder="Busque por nome"
          className="pl-10 h-[36px] border-2 border-[#0D4F97] rounded-[5px] placeholder-[#0D4F97]"
          value={searchName}
          onChange={(e) => setSearchName?.(e.target.value)}
        />
      </div>

      {(showDisorderFilter ||
        showAnoFilter ||
        showCityFilter ||
        showServiceAreaFilter) && (
        <div className="flex flex-wrap sm:flex-nowrap items-center gap-2 w-full sm:w-auto">
          {showServiceAreaFilter && (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button className={cn(dropdownTriggerStyle, "w-full sm:w-[150px]")}>
                  <span className="flex-1 w-0 truncate text-left">
                    {serviceArea || "Tipo de Atendimento"}
                  </span>
                  <ChevronDown className="h-4 w-4" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent
                align="end"
                className="w-[--radix-dropdown-menu-trigger-width]"
              >
                <DropdownMenuItem onClick={() => setServiceArea?.("")}>
                  Qualquer
                </DropdownMenuItem>
                {serviceAreaOptions.map((option) => (
                  <DropdownMenuItem
                    key={option}
                    onClick={() => setServiceArea?.(option)}
                    className={cn({
                      "bg-slate-100": serviceArea === option,
                    })}
                  >
                    {option}
                  </DropdownMenuItem>
                ))}
              </DropdownMenuContent>
            </DropdownMenu>
          )}

          {showDisorderFilter && (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button className={cn(dropdownTriggerStyle, "w-full sm:w-[150px]")}>
                  <span className="flex-1 w-0 truncate text-left">
                    {disorder || "Transtorno"}
                  </span>
                  <ChevronDown className="h-4 w-4" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent
                align="end"
                className="w-[--radix-dropdown-menu-trigger-width]"
              >
                <DropdownMenuItem onClick={() => setDisorder?.("")}>
                  Qualquer
                </DropdownMenuItem>
                {disorderOptions.map((option) => (
                  <DropdownMenuItem
                    key={option}
                    onClick={() => setDisorder?.(option)}
                    className={cn({ "bg-slate-100": disorder === option })}
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
                <Button className={cn(dropdownTriggerStyle, "w-full sm:w-[90px]")}>
                  <span className="flex-1 w-0 truncate text-left">
                    {year || "Ano"}
                  </span>
                  <ChevronDown className="h-4 w-4" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-[100px]">
                <DropdownMenuItem onClick={() => setYear?.("")}>
                  Qualquer
                </DropdownMenuItem>
                {anoOptions.map((option) => (
                  <DropdownMenuItem
                    key={option}
                    onClick={() => setYear?.(option)}
                    className={cn({ "bg-slate-100": year === option })}
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
                <Button className={cn(dropdownTriggerStyle, "w-full sm:w-[150px]")}>
                  <span className="flex-1 w-0 truncate text-left">
                    {city || "Cidade"}
                  </span>
                  <ChevronDown className="h-4 w-4" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent
                align="end"
                className="w-[--radix-dropdown-menu-trigger-width]"
              >
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
