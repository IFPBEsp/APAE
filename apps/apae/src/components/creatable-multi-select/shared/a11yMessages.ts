import type { MultiSelectOption } from "../types";

function matchesSearch(
  option: MultiSelectOption,
  searchValue: string,
): boolean {
  const search = searchValue.toLowerCase();
  return (
    option.label.toLowerCase().includes(search) ||
    option.value.toLowerCase().includes(search)
  );
}

export function getSelectionChangeMessage(
  selectedValues: string[],
  previousCount: number,
  allOptions: MultiSelectOption[],
  total: number,
): string {
  const selectedCount = selectedValues.length;
  const diff = selectedCount - previousCount;

  if (diff <= 0) {
    return `Option removed. ${selectedCount} of ${total} options selected.`;
  }

  const added = selectedValues
    .slice(-diff)
    .map((v) => allOptions.find((o) => o.value === v)?.label)
    .filter(Boolean);

  if (added.length === 1) {
    return `${added[0]} selected. ${selectedCount} of ${total} options selected.`;
  }
  return `${added.length} options selected. ${selectedCount} of ${total} total selected.`;
}

export function getPopoverMessage(isOpen: boolean, total: number): string {
  return isOpen
    ? `Dropdown opened. ${total} options available. Use arrow keys to navigate.`
    : "Dropdown closed.";
}

export function getSearchMessage(
  searchValue: string,
  allOptions: MultiSelectOption[],
): string {
  const count = allOptions.filter((o) => matchesSearch(o, searchValue)).length;
  const plural = count === 1 ? "" : "s";
  return `${count} option${plural} found for "${searchValue}"`;
}

export function getSelectedCountText(
  selectedValues: string[],
  getOptionByValue: (value: string) => MultiSelectOption | undefined,
): string {
  if (selectedValues.length === 0) return "No options selected";

  const labels = selectedValues
    .map((v) => getOptionByValue(v)?.label)
    .filter(Boolean)
    .join(", ");
  const plural = selectedValues.length === 1 ? "" : "s";
  return `${selectedValues.length} option${plural} selected: ${labels}`;
}
