"use client";

import * as React from "react";
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
import { Input } from "@/components/ui/input";
import { Plus, Check, X } from "lucide-react";
import { HEALTH_AREAS } from "@/lib/health-areas";

interface HealthAreaSelectProps {
  value?: string;
  onChange: (value: string) => void;
  className?: string;
}

export default function HealthAreaSelect({
  value,
  onChange,
  className,
}: HealthAreaSelectProps) {
  const [open, setOpen] = React.useState(false);
  const [areas, setAreas] = React.useState<string[]>(() => [...HEALTH_AREAS]);
  const [creating, setCreating] = React.useState(false);
  const [newArea, setNewArea] = React.useState("");

  const formatArea = (s: string) =>
    s.trim().length === 0
      ? ""
      : s.trim()[0].toUpperCase() + s.trim().slice(1).toLowerCase();

  const handleCreate = () => {
    const formatted = formatArea(newArea);
    if (!formatted) return;
    if (!areas.includes(formatted)) {
      setAreas((p) => [...p, formatted]);
    }
    onChange(formatted);
    setNewArea("");
    setCreating(false);
    setOpen(false);
  };

  const handleSelect = (area: string) => {
    onChange(area);
    setOpen(false);
  };

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          className={`w-full justify-between ${className ?? ""}`}
        >
          {value ? value : "Selecione a área"}
        </Button>
      </PopoverTrigger>

      <PopoverContent className="w-[320px] p-5">
        {creating ? (
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h3 className="text-sm font-semibold">
                Adicione uma nova área da saúde
              </h3>
              <button
                aria-label="Fechar"
                onClick={() => {
                  setCreating(false);
                  setNewArea("");
                }}
                className="rounded-full p-1 hover:bg-gray-100"
              >
                <X className="h-4 w-4" />
              </button>
            </div>

            <div>
              <label className="block text-sm mb-2">Título</label>
              <Input
                value={newArea}
                onChange={(e) => setNewArea(e.target.value)}
                placeholder="Ex: Fisioterapia"
                onKeyDown={(e) => {
                  if (e.key === "Enter") {
                    e.preventDefault();
                    handleCreate();
                  } else if (e.key === "Escape") {
                    setCreating(false);
                    setNewArea("");
                  }
                }}
              />
            </div>

            <div className="flex justify-end">
              <Button
                onClick={handleCreate}
                disabled={formatArea(newArea).length === 0}
              >
                Criar
              </Button>
            </div>
          </div>
        ) : (
          <Command>
            <div className="flex items-center gap-2 px-2 py-1">
              <div className="flex-1">
                <CommandInput placeholder="Encontre a área..." />
              </div>

              <div>
                <Button
                  type="button"
                  size="icon"
                  aria-label="Adicionar área"
                  onClick={() => setCreating(true)}
                >
                  <Plus className="h-4 w-4" />
                </Button>
              </div>
            </div>

            <CommandList>
              <CommandEmpty>Nenhuma área encontrada.</CommandEmpty>

              <CommandGroup>
                {areas.map((area) => (
                  <CommandItem
                    key={area}
                    value={area}
                    onSelect={() => handleSelect(area)}
                    className="flex items-center justify-between"
                  >
                    <span className="flex items-center gap-2">
                      <Check
                        className={`h-4 w-4 ${
                          area === value ? "opacity-100" : "opacity-0"
                        }`}
                      />
                      {area}
                    </span>
                  </CommandItem>
                ))}
              </CommandGroup>
            </CommandList>
          </Command>
        )}
      </PopoverContent>
    </Popover>
  );
}
