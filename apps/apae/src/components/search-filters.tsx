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

interface FilterDropdownProps {
  value: string;
  placeholder: string;
  options: string[];
  width?: string;
  onChange: (value: string) => void;
}

function FilterDropdown({
  value,
  placeholder,
  options,
  width = "w-[150px]",
  onChange,
}: FilterDropdownProps) {
  const triggerStyle = cn(
    "bg-white border border-gray-300 rounded-[5px] h-[36px]",
    "justify-between text-gray-600",
    "hover:bg-slate-50 hover:text-gray-700",
    "data-[state=open]:bg-slate-50",
    "flex w-full sm:" + width,
  );

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button className={triggerStyle}>
          <span className="flex-1 w-0 truncate text-left">
            {value || placeholder}
          </span>
          <ChevronDown className="h-4 w-4" />
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent
        align="end"
        className="w-[--radix-dropdown-menu-trigger-width]"
      >
        <DropdownMenuItem onClick={() => onChange("")}>
          Qualquer
        </DropdownMenuItem>
        {options.map((option) => (
          <DropdownMenuItem
            key={option}
            onClick={() => onChange(option)}
            className={cn({ "bg-slate-100": value === option })}
          >
            {option}
          </DropdownMenuItem>
        ))}
      </DropdownMenuContent>
    </DropdownMenu>
  );
}

interface FilterConfig {
  value: string;
  onChange: (value: string) => void;
  placeholder: string;
  options: string[];
  width?: string;
}

export interface SearchFiltersProps {
  readonly searchName?: string;
  readonly onSearchName?: (name: string) => void;
  readonly filters?: FilterConfig[];
}

export function SearchFilters({
  searchName,
  onSearchName,
  filters = [],
}: SearchFiltersProps) {
  const activeFilters = filters.filter((f) => f.onChange !== undefined);

  return (
    <div className="flex flex-col sm:flex-row items-start sm:items-center gap-2">
      <div className="relative w-full sm:flex-1">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-500" />
        <Input
          placeholder="Busque por nome"
          className="pl-10 h-[36px] border-2 border-[#0D4F97] rounded-[5px] placeholder-[#0D4F97]"
          value={searchName}
          onChange={(e) => onSearchName?.(e.target.value)}
        />
      </div>

      {activeFilters.length > 0 && (
        <div className="flex flex-wrap sm:flex-nowrap items-center gap-2 w-full sm:w-auto">
          {activeFilters.map((filter) => (
            <FilterDropdown
              key={filter.placeholder}
              value={filter.value}
              placeholder={filter.placeholder}
              options={filter.options}
              width={filter.width}
              onChange={filter.onChange}
            />
          ))}
        </div>
      )}
    </div>
  );
}