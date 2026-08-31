"use client";

import * as React from "react";
import { WandSparkles, XCircle, XIcon, ChevronDown } from "lucide-react";
import { cn } from "@/lib/utils";
import { Separator } from "@/components/ui/separator";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Command, CommandInput, CommandList } from "@/components/ui/command";

import { A11yRegion } from "./components/A11yRegion";
import { SelectionBadge } from "./components/SelectionBadge";
import { OptionList } from "./components/OptionList";
import { FooterActions } from "./components/FooterActions";

import { useScreenSize } from "./hooks/useScreenSize";
import { useResponsiveSettings } from "./hooks/useResponsiveSettings";
import { useA11yAnnouncer } from "./hooks/useA11yAnnouncer";
import { useMultiSelectOptions } from "./hooks/useMultiSelectOptions";

import { multiSelectVariants } from "./shared/variants";
import type { MultiSelectProps, MultiSelectRef } from "./types";

export const CreatableMultiSelect = React.forwardRef<MultiSelectRef, MultiSelectProps>(
  (
    {
      options,
      onValueChange,
      variant,
      defaultValue = [],
      placeholder = "Select options",
      animation = 0,
      animationConfig,
      maxCount = 3,
      modalPopover = false,
      className,
      hideSelectAll = false,
      searchable = true,
      emptyIndicator,
      autoSize = false,
      singleLine = false,
      popoverClassName,
      disabled = false,
      responsive,
      minWidth,
      maxWidth,
      deduplicateOptions = false,
      resetOnDefaultValueChange = true,
      closeOnSelect = false,
      onCreate,
      ...props
    },
    ref,
  ) => {
    const [selectedValues, setSelectedValues] = React.useState<string[]>(defaultValue);
    const [isPopoverOpen, setIsPopoverOpen] = React.useState(false);
    const [isAnimating, setIsAnimating] = React.useState(false);
    const [searchValue, setSearchValue] = React.useState("");

    const screenSize = useScreenSize();
    const responsiveSettings = useResponsiveSettings(responsive, maxCount, screenSize);
    const { politeMessage, assertiveMessage, announce } = useA11yAnnouncer();
    const { allOptions, filteredOptions, getOptionByValue, isGrouped } = useMultiSelectOptions(
      options,
      searchValue,
      searchable,
      deduplicateOptions,
    );

    const multiSelectId = React.useId();
    const listboxId = `${multiSelectId}-listbox`;
    const triggerDescriptionId = `${multiSelectId}-description`;
    const selectedCountId = `${multiSelectId}-count`;

    const buttonRef = React.useRef<HTMLButtonElement>(null);
    const prevDefaultValueRef = React.useRef<string[]>(defaultValue);
    const prevSelectedCount = React.useRef(selectedValues.length);
    const prevIsOpen = React.useRef(isPopoverOpen);
    const prevSearchValue = React.useRef(searchValue);

    const arraysEqual = (a: string[], b: string[]) => {
      if (a.length !== b.length) return false;
      return [...a].sort().every((v, i) => v === [...b].sort()[i]);
    };

    const widthConstraints = React.useMemo(() => {
      const defaultMin = screenSize === "mobile" ? "0px" : "200px";
      return {
        minWidth: minWidth ?? defaultMin,
        maxWidth: maxWidth ?? "100%",
        width: autoSize ? "auto" : "100%",
      };
    }, [screenSize, minWidth, maxWidth, autoSize]);

    const applyChange = (next: string[]) => {
      setSelectedValues(next);
      onValueChange(next);
    };

    const toggleOption = (value: string) => {
      if (disabled) return;
      const option = getOptionByValue(value);
      if (option?.disabled) return;

      const next = selectedValues.includes(value)
        ? selectedValues.filter((v) => v !== value)
        : [...selectedValues, value];

      applyChange(next);
      if (closeOnSelect) setIsPopoverOpen(false);
    };

    const handleClear = () => {
      if (!disabled) applyChange([]);
    };

    const toggleAll = () => {
      if (disabled) return;
      const all = allOptions.filter((o) => !o.disabled);
      const next = selectedValues.length === all.length ? [] : all.map((o) => o.value);
      applyChange(next);
      if (closeOnSelect) setIsPopoverOpen(false);
    };

    const clearExtraOptions = () => {
      if (!disabled) applyChange(selectedValues.slice(0, responsiveSettings.maxCount));
    };

    const handleTogglePopover = () => {
      if (!disabled) setIsPopoverOpen((p) => !p);
    };

    const handleInputKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
      if (e.key === "Enter") {
        setIsPopoverOpen(true);
      } else if (e.key === "Backspace" && !e.currentTarget.value) {
        applyChange(selectedValues.slice(0, -1));
      }
    };

    React.useImperativeHandle(ref, () => ({
      reset: () => {
        setSelectedValues(defaultValue);
        setIsPopoverOpen(false);
        setSearchValue("");
        onValueChange(defaultValue);
      },
      getSelectedValues: () => selectedValues,
      setSelectedValues: (values) => applyChange(values),
      clear: () => applyChange([]),
      focus: () => {
        if (!buttonRef.current) return;
        buttonRef.current.focus();
        buttonRef.current.style.outline = "2px solid hsl(var(--ring))";
        buttonRef.current.style.outlineOffset = "2px";
        setTimeout(() => {
          if (buttonRef.current) {
            buttonRef.current.style.outline = "";
            buttonRef.current.style.outlineOffset = "";
          }
        }, 1000);
      },
    }));

    React.useEffect(() => {
      if (!resetOnDefaultValueChange) return;
      if (!arraysEqual(prevDefaultValueRef.current, defaultValue)) {
        if (!arraysEqual(selectedValues, defaultValue)) {
          setSelectedValues(defaultValue);
        }
        prevDefaultValueRef.current = [...defaultValue];
      }
    }, [defaultValue, selectedValues, resetOnDefaultValueChange]);

    React.useEffect(() => {
      if (!isPopoverOpen) setSearchValue("");
    }, [isPopoverOpen]);

    React.useEffect(() => {
      const total = allOptions.filter((o) => !o.disabled).length;

      if (selectedValues.length !== prevSelectedCount.current) {
        const diff = selectedValues.length - prevSelectedCount.current;
        if (diff > 0) {
          const added = selectedValues
            .slice(-diff)
            .map((v) => allOptions.find((o) => o.value === v)?.label)
            .filter(Boolean);
          announce(
            added.length === 1
              ? `${added[0]} selected. ${selectedValues.length} of ${total} options selected.`
              : `${added.length} options selected. ${selectedValues.length} of ${total} total selected.`,
          );
        } else {
          announce(`Option removed. ${selectedValues.length} of ${total} options selected.`);
        }
        prevSelectedCount.current = selectedValues.length;
      }

      if (isPopoverOpen !== prevIsOpen.current) {
        announce(
          isPopoverOpen
            ? `Dropdown opened. ${total} options available. Use arrow keys to navigate.`
            : "Dropdown closed.",
        );
        prevIsOpen.current = isPopoverOpen;
      }

      if (searchValue !== prevSearchValue.current && isPopoverOpen) {
        const count = allOptions.filter(
          (o) =>
            o.label.toLowerCase().includes(searchValue.toLowerCase()) ||
            o.value.toLowerCase().includes(searchValue.toLowerCase()),
        ).length;
        announce(`${count} option${count === 1 ? "" : "s"} found for "${searchValue}"`);
        prevSearchValue.current = searchValue;
      }
    }, [selectedValues, isPopoverOpen, searchValue, announce, allOptions]);

    return (
      <>
        <A11yRegion politeMessage={politeMessage} assertiveMessage={assertiveMessage} />

        <Popover open={isPopoverOpen} onOpenChange={setIsPopoverOpen} modal={modalPopover}>
          <div id={triggerDescriptionId} className="sr-only">
            Multi-select dropdown. Use arrow keys to navigate, Enter to select, and Escape to close.
          </div>
          <div id={selectedCountId} className="sr-only" aria-live="polite">
            {selectedValues.length === 0
              ? "No options selected"
              : `${selectedValues.length} option${
                  selectedValues.length === 1 ? "" : "s"
                } selected: ${selectedValues
                  .map((v) => getOptionByValue(v)?.label)
                  .filter(Boolean)
                  .join(", ")}`}
          </div>

          <PopoverTrigger asChild>
            <Button
              ref={buttonRef}
              {...props}
              onClick={handleTogglePopover}
              disabled={disabled}
              role="combobox"
              aria-expanded={isPopoverOpen}
              aria-haspopup="listbox"
              aria-controls={isPopoverOpen ? listboxId : undefined}
              aria-describedby={`${triggerDescriptionId} ${selectedCountId}`}
              aria-label={`Multi-select: ${selectedValues.length} of ${allOptions.length} options selected. ${placeholder}`}
              className={cn(
                "flex p-1 rounded-md border min-h-10 h-auto items-center justify-between bg-inherit hover:bg-inherit [&_svg]:pointer-events-auto",
                autoSize ? "w-auto" : "w-full",
                responsiveSettings.compactMode && "min-h-8 text-sm",
                screenSize === "mobile" && "min-h-12 text-base",
                disabled && "opacity-50 cursor-not-allowed",
                className,
              )}
              style={{
                ...widthConstraints,
                maxWidth: `min(${widthConstraints.maxWidth}, 100%)`,
              }}
            >
              {selectedValues.length > 0 ? (
                <div className="flex justify-between items-center w-full">
                  <div
                    className={cn(
                      "flex items-center gap-1",
                      singleLine ? "overflow-x-auto multiselect-singleline-scroll" : "flex-wrap",
                      responsiveSettings.compactMode && "gap-0.5",
                    )}
                    style={singleLine ? { paddingBottom: 4 } : undefined}
                  >
                    {selectedValues
                      .slice(0, responsiveSettings.maxCount)
                      .map((value) => {
                        const option = getOptionByValue(value);
                        if (!option) return null;
                        return (
                          <SelectionBadge
                            key={value}
                            option={option}
                            variant={variant}
                            animation={animation}
                            animationConfig={animationConfig}
                            isAnimating={isAnimating}
                            responsiveSettings={responsiveSettings}
                            screenSize={screenSize}
                            singleLine={singleLine}
                            onRemove={toggleOption}
                          />
                        );
                      })
                      .filter(Boolean)}

                    {selectedValues.length > responsiveSettings.maxCount && (
                      <Badge
                        className={cn(
                          "bg-transparent text-foreground border-foreground/1 hover:bg-transparent",
                          multiSelectVariants({ variant }),
                          responsiveSettings.compactMode && "text-xs px-1.5 py-0.5",
                          singleLine && "shrink-0 whitespace-nowrap",
                          "[&>svg]:pointer-events-auto",
                        )}
                      >
                        {`+ ${selectedValues.length - responsiveSettings.maxCount} more`}
                        <XCircle
                          className={cn(
                            "ml-2 h-4 w-4 cursor-pointer",
                            responsiveSettings.compactMode && "ml-1 h-3 w-3",
                          )}
                          onClick={(e) => {
                            e.stopPropagation();
                            clearExtraOptions();
                          }}
                        />
                      </Badge>
                    )}
                  </div>

                  <div className="flex items-center justify-between">
                    <div
                      role="button"
                      tabIndex={0}
                      onClick={(e) => {
                        e.stopPropagation();
                        handleClear();
                      }}
                      onKeyDown={(e) => {
                        if (e.key === "Enter" || e.key === " ") {
                          e.preventDefault();
                          e.stopPropagation();
                          handleClear();
                        }
                      }}
                      aria-label={`Clear all ${selectedValues.length} selected options`}
                      className="flex items-center justify-center h-4 w-4 mx-2 cursor-pointer text-muted-foreground hover:text-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-1 rounded-sm"
                    >
                      <XIcon className="h-4 w-4" />
                    </div>
                    <Separator orientation="vertical" className="flex min-h-6 h-full" />
                    <ChevronDown
                      className="h-4 mx-2 cursor-pointer text-muted-foreground"
                      aria-hidden="true"
                    />
                  </div>
                </div>
              ) : (
                <div className="flex items-center justify-between w-full mx-auto">
                  <span className="text-sm text-muted-foreground mx-3">{placeholder}</span>
                  <ChevronDown className="h-4 cursor-pointer text-muted-foreground mx-2" />
                </div>
              )}
            </Button>
          </PopoverTrigger>

          <PopoverContent
            id={listboxId}
            role="listbox"
            aria-multiselectable="true"
            aria-label="Available options"
            className={cn(
              "w-auto p-0",
              screenSize === "mobile" && "w-[85vw] max-w-[280px]",
              screenSize === "tablet" && "w-[70vw] max-w-md",
              screenSize === "desktop" && "min-w-[300px]",
              popoverClassName,
            )}
            style={{
              maxWidth: `min(${widthConstraints.maxWidth}, 85vw)`,
              maxHeight: screenSize === "mobile" ? "70vh" : "60vh",
              touchAction: "manipulation",
            }}
            align="start"
            onEscapeKeyDown={() => setIsPopoverOpen(false)}
          >
            <Command>
              {searchable && (
                <>
                  <CommandInput
                    placeholder="Buscar opções..."
                    onKeyDown={handleInputKeyDown}
                    value={searchValue}
                    onValueChange={setSearchValue}
                    aria-label="Search through available options"
                    aria-describedby={`${multiSelectId}-search-help`}
                  />
                  <div id={`${multiSelectId}-search-help`} className="sr-only">
                    Type to filter options. Use arrow keys to navigate results.
                  </div>
                </>
              )}

              <CommandList
                className={cn(
                  "max-h-[40vh] overflow-y-auto multiselect-scrollbar",
                  screenSize === "mobile" && "max-h-[50vh]",
                  "overscroll-behavior-y-contain",
                )}
              >
                <OptionList
                  filteredOptions={filteredOptions}
                  selectedValues={selectedValues}
                  isGrouped={isGrouped}
                  hideSelectAll={hideSelectAll}
                  searchValue={searchValue}
                  allOptionsCount={allOptions.length}
                  allNonDisabledCount={allOptions.filter((o) => !o.disabled).length}
                  emptyIndicator={emptyIndicator}
                  onToggle={toggleOption}
                  onToggleAll={toggleAll}
                />

                <FooterActions
                  hasSelection={selectedValues.length > 0}
                  onClear={handleClear}
                  onCreate={onCreate}
                  onClose={() => setIsPopoverOpen(false)}
                />
              </CommandList>
            </Command>
          </PopoverContent>

          {animation > 0 && selectedValues.length > 0 && (
            <WandSparkles
              className={cn(
                "cursor-pointer my-2 text-foreground bg-background w-3 h-3",
                !isAnimating && "text-muted-foreground",
              )}
              onClick={() => setIsAnimating((a) => !a)}
            />
          )}
        </Popover>
      </>
    );
  },
);

CreatableMultiSelect.displayName = "CreatableMultiSelect";

export type {
  MultiSelectOption,
  MultiSelectGroup,
  MultiSelectProps,
  MultiSelectRef,
} from "./types";
