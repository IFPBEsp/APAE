import * as React from "react";
import type { MultiSelectOption, MultiSelectGroup } from "../types";

function isGroupedOptions(
  opts: MultiSelectOption[] | MultiSelectGroup[],
): opts is MultiSelectGroup[] {
  return opts.length > 0 && "heading" in opts[0];
}

interface UseMultiSelectOptionsReturn {
  allOptions: MultiSelectOption[];
  filteredOptions: MultiSelectOption[] | MultiSelectGroup[];
  getOptionByValue: (value: string) => MultiSelectOption | undefined;
  isGrouped: boolean;
}

export function useMultiSelectOptions(
  options: MultiSelectOption[] | MultiSelectGroup[],
  searchValue: string,
  searchable: boolean,
  deduplicateOptions: boolean,
): UseMultiSelectOptionsReturn {
  const isGrouped = React.useMemo(
    () => isGroupedOptions(options),
    [options],
  );

  const allOptions = React.useMemo((): MultiSelectOption[] => {
    if (options.length === 0) return [];

    let flat: MultiSelectOption[];
    if (isGroupedOptions(options)) {
      flat = options.flatMap((g) => g.options);
    } else {
      flat = options as MultiSelectOption[];
    }

    const seen = new Set<string>();
    const duplicates: string[] = [];
    const result: MultiSelectOption[] = [];

    for (const opt of flat) {
      if (seen.has(opt.value)) {
        duplicates.push(opt.value);
        if (!deduplicateOptions) result.push(opt);
      } else {
        seen.add(opt.value);
        result.push(opt);
      }
    }

    if (process.env.NODE_ENV === "development" && duplicates.length > 0) {
      const action = deduplicateOptions ? "automatically removed" : "detected";
      console.warn(
        `MultiSelect: Duplicate option values ${action}: ${duplicates.join(", ")}. ` +
          (deduplicateOptions
            ? "Duplicates have been removed automatically."
            : "Consider setting 'deduplicateOptions={true}' or ensure all option values are unique."),
      );
    }

    return result;
  }, [options, deduplicateOptions]);

  const getOptionByValue = React.useCallback(
    (value: string): MultiSelectOption | undefined => {
      const opt = allOptions.find((o) => o.value === value);
      if (!opt && process.env.NODE_ENV === "development") {
        console.warn(`MultiSelect: Option with value "${value}" not found`);
      }
      return opt;
    },
    [allOptions],
  );

  const filteredOptions = React.useMemo(() => {
    if (!searchable || !searchValue) return options;
    if (options.length === 0) return [];

    const matches = (o: MultiSelectOption) =>
      o.label.toLowerCase().includes(searchValue.toLowerCase()) ||
      o.value.toLowerCase().includes(searchValue.toLowerCase());

    if (isGroupedOptions(options)) {
      return options
        .map((g) => ({ ...g, options: g.options.filter(matches) }))
        .filter((g) => g.options.length > 0);
    }

    return (options as MultiSelectOption[]).filter(matches);
  }, [options, searchValue, searchable]);

  return { allOptions, filteredOptions, getOptionByValue, isGrouped };
}
