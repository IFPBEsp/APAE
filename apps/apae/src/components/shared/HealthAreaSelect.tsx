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
import { useFetchServiceAreas } from "@/hooks/service-area/use-fetch-service-areas";
import { useCreateServiceArea } from "@/hooks/service-area/use-create-service-area";

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
  const { areas, loading, error } = useFetchServiceAreas();
  const {
    create,
    loading: creating,
    error: errorCreate,
  } = useCreateServiceArea();

  const [open, setOpen] = React.useState(false);
  const [creatingMode, setCreatingMode] = React.useState(false);
  const [newArea, setNewArea] = React.useState("");

  const normalizedAreas = React.useMemo(() => {
    return areas.map((a) => a.name);
  }, [areas]);

  const formatArea = (s: string) =>
    s.trim().length === 0
      ? ""
      : s.trim()[0].toUpperCase() + s.trim().slice(1).toLowerCase();

  const handleCreate = async () => {
    const formatted = formatArea(newArea);
    if (!formatted) return;

    try {
      const response = await create(formatted);

      onChange(response.name);
      setNewArea("");
      setCreatingMode(false);
      setOpen(false);
    } catch (err) {
      console.error("Erro ao criar área", err);
    }
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
          disabled={loading}
          className={`w-full justify-between ${className ?? ""}`}
        >
          {loading ? "Carregando..." : value || "Selecione a área"}
        </Button>
      </PopoverTrigger>

      <PopoverContent className="w-[320px] p-5">
        {creatingMode ? (
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h3 className="text-sm font-semibold">Nova área da saúde</h3>
              <button
                aria-label="Fechar"
                onClick={() => {
                  setCreatingMode(false);
                  setNewArea("");
                }}
                className="rounded-full p-1 hover:bg-gray-100"
              >
                <X className="h-4 w-4" />
              </button>
            </div>

            <Input
              value={newArea}
              onChange={(e) => setNewArea(e.target.value)}
              placeholder="Ex: Fisioterapia"
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  e.preventDefault();
                  handleCreate();
                }
              }}
            />

            <div className="flex justify-end">
              <Button
                onClick={handleCreate}
                disabled={formatArea(newArea).length === 0 || creating}
              >
                {creating ? "Salvando..." : "Criar"}
              </Button>
            </div>

            {errorCreate && (
              <p className="text-red-600 text-sm">{errorCreate}</p>
            )}
          </div>
        ) : (
          <Command>
            <div className="flex items-center gap-2 px-2 py-1">
              <CommandInput placeholder="Buscar área..." />
              <Button
                type="button"
                size="icon"
                aria-label="Adicionar área"
                onClick={() => setCreatingMode(true)}
              >
                <Plus className="h-4 w-4" />
              </Button>
            </div>

            <CommandList>
              {error && (
                <CommandEmpty>Erro ao carregar áreas da saúde.</CommandEmpty>
              )}

              {!error && (
                <>
                  <CommandEmpty>Nenhuma área encontrada.</CommandEmpty>

                  <CommandGroup>
                    {normalizedAreas.map((area) => (
                      <CommandItem
                        key={area}
                        value={area}
                        onSelect={() => handleSelect(area)}
                        className="flex items-center justify-between"
                      >
                        <Check
                          className={`h-4 w-4 ${
                            area === value ? "opacity-100" : "opacity-0"
                          }`}
                        />
                        {area}
                      </CommandItem>
                    ))}
                  </CommandGroup>
                </>
              )}
            </CommandList>
          </Command>
        )}
      </PopoverContent>
    </Popover>
  );
}
