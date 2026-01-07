"use client";

import * as React from "react";
import { Check, ChevronsUpDown, X, Plus } from "lucide-react";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { Badge } from "@/components/ui/badge";

interface VaccineMultiSelectProps {
  value: string[];
  onChange: (value: string[]) => void;
  options: string[]; // Recebe as opções carregadas pelo Hook do pai
  isLoading?: boolean;
}

export function VaccineMultiSelect({ value = [], onChange, options = [], isLoading = false }: VaccineMultiSelectProps) {
  const [open, setOpen] = React.useState(false);
  const [inputValue, setInputValue] = React.useState("");
  
  // Combina opções do banco com as selecionadas para garantir que apareçam
  const allOptions = React.useMemo(() => {
    const uniqueOptions = new Set([...options, ...value]);
    return Array.from(uniqueOptions).sort();
  }, [options, value]);

  const handleSelect = (currentValue: string) => {
    const normalized = currentValue.toUpperCase();
    const isSelected = value.includes(normalized);
    if (isSelected) {
      onChange(value.filter((item) => item !== normalized));
    } else {
      onChange([...value, normalized]);
    }
  };

  const handleCreate = () => {
    if (!inputValue) return;
    const normalized = inputValue.trim().toUpperCase();
    if (!value.includes(normalized)) {
      onChange([...value, normalized]);
    }
    setInputValue("");
  };

  const handleUnselect = (itemToRemove: string) => {
    onChange(value.filter((item) => item !== itemToRemove));
  };

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button variant="outline" role="combobox" aria-expanded={open} disabled={isLoading} className="w-full justify-between h-auto min-h-[42px] px-3 py-2 bg-white">
          <div className="flex flex-wrap gap-1 items-center">
            {value.length > 0 ? (
              value.map((item) => (
                <Badge variant="secondary" key={item} className="mr-1 mb-1 font-normal bg-blue-50 text-blue-700 hover:bg-blue-100 border-blue-200">
                  {item}
                  <div className="ml-1 cursor-pointer" onMouseDown={(e) => { e.preventDefault(); e.stopPropagation(); }} onClick={() => handleUnselect(item)}>
                    <X className="h-3 w-3 text-blue-500 hover:text-blue-700" />
                  </div>
                </Badge>
              ))
            ) : (
              <span className="text-muted-foreground text-sm font-normal">{isLoading ? "Carregando..." : "Selecionar vacinas..."}</span>
            )}
          </div>
          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-[400px] p-0" align="start">
        <Command>
          <CommandInput placeholder="Buscar vacina..." value={inputValue} onValueChange={setInputValue} />
          <CommandList>
            <CommandEmpty className="py-2 px-2 text-sm">
               <p className="text-muted-foreground text-center mb-2">Não encontrada.</p>
               {inputValue && (
                 <Button variant="secondary" size="sm" className="w-full justify-start text-green-700 bg-green-50 hover:bg-green-100" onClick={handleCreate}>
                    <Plus className="mr-2 h-4 w-4" /> Criar "{inputValue.toUpperCase()}"
                 </Button>
               )}
            </CommandEmpty>
            <CommandGroup heading="Sugestões" className="max-h-[200px] overflow-y-auto">
              {allOptions.map((option) => (
                <CommandItem key={option} value={option} onSelect={() => handleSelect(option)}>
                  <Check className={cn("mr-2 h-4 w-4", value.includes(option) ? "opacity-100" : "opacity-0")} />
                  {option}
                </CommandItem>
              ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  );
}