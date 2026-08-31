import * as React from "react";
import { CheckIcon } from "lucide-react";
import { cn } from "@/lib/utils";
import { CommandItem } from "@/components/ui/command";
import type { MultiSelectOption } from "../types";

interface OptionItemProps {
  option: MultiSelectOption;
  isSelected: boolean;
  onToggle: (value: string) => void;
}

export function OptionItem({ option, isSelected, onToggle }: OptionItemProps) {
  return (
    <CommandItem
      key={option.value}
      onSelect={() => onToggle(option.value)}
      role="option"
      aria-selected={isSelected}
      aria-disabled={option.disabled}
      aria-label={`${option.label}${isSelected ? ", selected" : ", not selected"}${
        option.disabled ? ", disabled" : ""
      }`}
      className={cn("cursor-pointer", option.disabled && "opacity-50 cursor-not-allowed")}
      disabled={option.disabled}
    >
      <div
        className={cn(
          "mr-2 flex h-4 w-4 items-center justify-center rounded-sm border border-primary",
          isSelected ? "bg-primary text-primary-foreground" : "opacity-50 [&_svg]:invisible",
        )}
        aria-hidden="true"
      >
        <CheckIcon className="h-4 w-4" />
      </div>

      {option.icon && (
        <option.icon className="mr-2 h-4 w-4 text-muted-foreground" aria-hidden="true" />
      )}

      <span>{option.label}</span>
    </CommandItem>
  );
}
