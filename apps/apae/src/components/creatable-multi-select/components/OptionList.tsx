import * as React from "react";
import { CheckIcon } from "lucide-react";
import { cn } from "@/lib/utils";
import { CommandEmpty, CommandGroup, CommandItem } from "@/components/ui/command";
import { OptionItem } from "./OptionItem";
import type { MultiSelectOption, MultiSelectGroup } from "../types";

interface OptionListProps {
  filteredOptions: MultiSelectOption[] | MultiSelectGroup[];
  selectedValues: string[];
  isGrouped: boolean;
  hideSelectAll: boolean;
  searchValue: string;
  allOptionsCount: number;
  allNonDisabledCount: number;
  emptyIndicator?: React.ReactNode;
  onToggle: (value: string) => void;
  onToggleAll: () => void;
}

export function OptionList({
  filteredOptions,
  selectedValues,
  isGrouped,
  hideSelectAll,
  searchValue,
  allOptionsCount,
  allNonDisabledCount,
  emptyIndicator,
  onToggle,
  onToggleAll,
}: OptionListProps) {
  const allSelected = selectedValues.length === allNonDisabledCount;

  return (
    <>
      <CommandEmpty>{emptyIndicator ?? "nenhuma opção encontrada"}</CommandEmpty>

      {!hideSelectAll && !searchValue && (
        <CommandGroup>
          <CommandItem
            key="all"
            onSelect={onToggleAll}
            role="option"
            aria-selected={allSelected}
            aria-label={`Select all ${allOptionsCount} options`}
            className="cursor-pointer"
          >
            <div
              className={cn(
                "mr-2 flex h-4 w-4 items-center justify-center rounded-sm border border-primary",
                allSelected ? "bg-primary text-primary-foreground" : "opacity-50 [&_svg]:invisible",
              )}
              aria-hidden="true"
            >
              <CheckIcon className="h-4 w-4" />
            </div>
            <span>
              (Selecionar Todos
              {allOptionsCount > 20 ? ` - ${allOptionsCount} opções` : ""})
            </span>
          </CommandItem>
        </CommandGroup>
      )}

      {isGrouped ? (
        (filteredOptions as MultiSelectGroup[]).map((group) => (
          <CommandGroup key={group.heading} heading={group.heading}>
            {group.options.map((option) => (
              <OptionItem
                key={option.value}
                option={option}
                isSelected={selectedValues.includes(option.value)}
                onToggle={onToggle}
              />
            ))}
          </CommandGroup>
        ))
      ) : (
        <CommandGroup>
          {(filteredOptions as MultiSelectOption[]).map((option) => (
            <OptionItem
              key={option.value}
              option={option}
              isSelected={selectedValues.includes(option.value)}
              onToggle={onToggle}
            />
          ))}
        </CommandGroup>
      )}
    </>
  );
}
