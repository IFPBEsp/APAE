import * as React from "react";
import { Separator } from "@/components/ui/separator";
import { CommandGroup, CommandItem, CommandSeparator } from "@/components/ui/command";

interface FooterActionsProps {
  hasSelection: boolean;
  onClear: () => void;
  onCreate?: () => Promise<void> | void;
  onClose: () => void;
}

export function FooterActions({
  hasSelection,
  onClear,
  onCreate,
  onClose,
}: FooterActionsProps) {
  return (
    <>
      <CommandSeparator />
      <CommandGroup>
        <div className="flex items-center justify-between">
          {hasSelection && (
            <>
              <CommandItem
                onSelect={onClear}
                className="flex-1 justify-center cursor-pointer"
              >
                Limpar
              </CommandItem>
              <Separator orientation="vertical" className="flex min-h-6 h-full" />
            </>
          )}

          {onCreate && (
            <>
              <CommandItem
                onSelect={onCreate}
                className="flex-1 justify-center cursor-pointer"
              >
                Criar
              </CommandItem>

              <Separator orientation="vertical" className="flex min-h-6 h-full" />
            </>
          )}

          <CommandItem
            onSelect={onClose}
            className="flex-1 justify-center cursor-pointer"
          >
            Fechar
          </CommandItem>
        </div>
      </CommandGroup>
    </>
  );
}
