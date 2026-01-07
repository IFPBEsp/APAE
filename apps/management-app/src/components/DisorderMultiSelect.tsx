// src/components/DisorderMultiSelect.tsx
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

// Interface compatível com o Backend (Lista de Objetos)
interface DisorderItem {
  id?: string;
  name: string;
}

interface DisorderMultiSelectProps {
  value: DisorderItem[]; 
  onChange: (value: DisorderItem[]) => void;
  isLoading?: boolean;
}

export function DisorderMultiSelect({ value = [], onChange, isLoading = false }: DisorderMultiSelectProps) {
  const [open, setOpen] = React.useState(false);
  const [inputValue, setInputValue] = React.useState("");
  const [fetchedOptions, setFetchedOptions] = React.useState<DisorderItem[]>([]);
  const [isFetching, setIsFetching] = React.useState(false);

  // Busca opções do backend automaticamente ao montar
  React.useEffect(() => {
    setIsFetching(true);
    fetch("/api/transtornos")
      .then((res) => {
        if (res.ok) return res.json();
        return [];
      })
      .then((data) => {
        if (Array.isArray(data)) setFetchedOptions(data);
      })
      .catch((err) => console.error("Erro ao buscar transtornos", err))
      .finally(() => setIsFetching(false));
  }, []);

  // Combina opções do banco com o que já está selecionado (caso seja um novo item ainda não salvo)
  const allOptions = React.useMemo(() => {
    // Extrai apenas os nomes para garantir unicidade visual
    const existingNames = new Set(fetchedOptions.map(o => o.name.toUpperCase()));
    
    // Adiciona os valores selecionados que talvez não estejam na lista do banco ainda
    const merged = [...fetchedOptions];
    value.forEach(v => {
      if (!existingNames.has(v.name.toUpperCase())) {
        merged.push(v);
        existingNames.add(v.name.toUpperCase());
      }
    });

    // Ordena alfabeticamente
    return merged.sort((a, b) => a.name.localeCompare(b.name));
  }, [fetchedOptions, value]);

  const handleSelect = (optionName: string) => {
    const normalized = optionName.toUpperCase();
    const exists = value.some((item) => item.name.toUpperCase() === normalized);
    
    if (exists) {
      onChange(value.filter((item) => item.name.toUpperCase() !== normalized));
    } else {
      onChange([...value, { name: normalized }]);
    }
    // Não fecha o popover para permitir seleção múltipla rápida
    setInputValue("");
  };

  const handleCreate = () => {
    if (!inputValue) return;
    const normalized = inputValue.trim().toUpperCase();
    
    // Verifica se já está selecionado
    if (!value.some(v => v.name.toUpperCase() === normalized)) {
      onChange([...value, { name: normalized }]);
    }
    setInputValue("");
  };

  const handleUnselect = (nameToRemove: string) => {
    onChange(value.filter((item) => item.name.toUpperCase() !== nameToRemove.toUpperCase()));
  };

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          disabled={isLoading || isFetching}
          className="w-full justify-between h-auto min-h-[42px] px-3 py-2 bg-white border-slate-300"
        >
          <div className="flex flex-wrap gap-1 items-center">
            {value.length > 0 ? (
              value.map((item, index) => (
                <Badge 
                  variant="secondary" 
                  key={`${item.name}-${index}`} 
                  className="mr-1 mb-1 font-normal bg-blue-50 text-blue-700 hover:bg-blue-100 border-blue-200"
                >
                  {item.name}
                  <div
                    className="ml-1 ring-offset-background rounded-full outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 cursor-pointer"
                    onMouseDown={(e) => { e.preventDefault(); e.stopPropagation(); }}
                    onClick={() => handleUnselect(item.name)}
                  >
                    <X className="h-3 w-3 text-blue-500 hover:text-blue-700" />
                  </div>
                </Badge>
              ))
            ) : (
              <span className="text-slate-500 text-sm font-normal">
                {isFetching ? "Carregando opções..." : "Selecione ou digite para criar..."}
              </span>
            )}
          </div>
          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-[400px] p-0" align="start">
        <Command>
          <CommandInput 
            placeholder="Buscar transtorno..." 
            value={inputValue}
            onValueChange={setInputValue}
            onKeyDown={(e) => {
                if (e.key === 'Enter') {
                    e.preventDefault();
                    handleCreate();
                }
            }}
          />
          <CommandList>
            <CommandEmpty className="py-2 px-2 text-sm">
               <p className="text-slate-500 text-center mb-2">Não encontrado.</p>
               {inputValue && (
                 <Button 
                    variant="secondary" 
                    size="sm" 
                    className="w-full justify-start text-blue-700 bg-blue-50 hover:bg-blue-100"
                    onClick={handleCreate}
                 >
                    <Plus className="mr-2 h-4 w-4" />
                    Criar "{inputValue.toUpperCase()}"
                 </Button>
               )}
            </CommandEmpty>
            
            <CommandGroup heading="Sugestões" className="max-h-[200px] overflow-y-auto custom-scrollbar">
              {allOptions.map((option) => (
                <CommandItem
                  key={option.id || option.name}
                  value={option.name}
                  onSelect={() => handleSelect(option.name)}
                >
                  <Check
                    className={cn(
                      "mr-2 h-4 w-4",
                      value.some(v => v.name.toUpperCase() === option.name.toUpperCase()) 
                        ? "opacity-100" 
                        : "opacity-0"
                    )}
                  />
                  {option.name}
                </CommandItem>
              ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  );
}