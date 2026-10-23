import * as React from "react";
import type { MultiSelectOption } from "../types";
import {
  getPopoverMessage,
  getSearchMessage,
  getSelectionChangeMessage,
} from "../shared/a11yMessages";

interface UseSelectionAnnouncementsParams {
  selectedValues: string[];
  isPopoverOpen: boolean;
  searchValue: string;
  allOptions: MultiSelectOption[];
  announce: (message: string) => void;
}

export function useSelectionAnnouncements({
  selectedValues,
  isPopoverOpen,
  searchValue,
  allOptions,
  announce,
}: UseSelectionAnnouncementsParams): void {
  const prevSelectedCount = React.useRef(selectedValues.length);
  const prevIsOpen = React.useRef(isPopoverOpen);
  const prevSearchValue = React.useRef(searchValue);

  React.useEffect(() => {
    const total = allOptions.filter((o) => !o.disabled).length;

    if (selectedValues.length !== prevSelectedCount.current) {
      announce(
        getSelectionChangeMessage(
          selectedValues,
          prevSelectedCount.current,
          allOptions,
          total,
        ),
      );
      prevSelectedCount.current = selectedValues.length;
    }

    if (isPopoverOpen !== prevIsOpen.current) {
      announce(getPopoverMessage(isPopoverOpen, total));
      prevIsOpen.current = isPopoverOpen;
    }

    if (searchValue !== prevSearchValue.current && isPopoverOpen) {
      announce(getSearchMessage(searchValue, allOptions));
      prevSearchValue.current = searchValue;
    }
  }, [selectedValues, isPopoverOpen, searchValue, announce, allOptions]);
}
