import * as React from "react";
import { type VariantProps } from "class-variance-authority";
import { multiSelectVariants } from "../shared/variants";

export interface AnimationConfig {
  badgeAnimation?: "bounce" | "pulse" | "wiggle" | "fade" | "slide" | "none";
  popoverAnimation?: "scale" | "slide" | "fade" | "flip" | "none";
  optionHoverAnimation?: "highlight" | "scale" | "glow" | "none";
  duration?: number;
  delay?: number;
}

export interface MultiSelectOption {
  label: string;
  value: string;
  icon?: React.ComponentType<{ className?: string }>;
  disabled?: boolean;
  style?: {
    badgeColor?: string;
    iconColor?: string;
    gradient?: string;
  };
}

export interface MultiSelectGroup {
  heading: string;
  options: MultiSelectOption[];
}

export type ScreenSize = "mobile" | "tablet" | "desktop";

export interface ResponsiveSizeConfig {
  maxCount?: number;
  hideIcons?: boolean;
  compactMode?: boolean;
}

export type ResponsiveConfig =
  | boolean
  | {
      mobile?: ResponsiveSizeConfig;
      tablet?: ResponsiveSizeConfig;
      desktop?: ResponsiveSizeConfig;
    };

export interface ResolvedResponsiveSettings {
  maxCount: number;
  hideIcons: boolean;
  compactMode: boolean;
}

export interface MultiSelectProps
  extends
    Omit<React.ButtonHTMLAttributes<HTMLButtonElement>, "animationConfig">,
    VariantProps<typeof multiSelectVariants> {
  options: MultiSelectOption[] | MultiSelectGroup[];
  onValueChange: (value: string[]) => void;
  defaultValue?: string[];
  placeholder?: string;
  animation?: number;
  animationConfig?: AnimationConfig;
  maxCount?: number;
  modalPopover?: boolean;
  asChild?: boolean;
  className?: string;
  hideSelectAll?: boolean;
  searchable?: boolean;
  emptyIndicator?: React.ReactNode;
  autoSize?: boolean;
  singleLine?: boolean;
  popoverClassName?: string;
  disabled?: boolean;
  responsive?: ResponsiveConfig;
  minWidth?: string;
  maxWidth?: string;
  deduplicateOptions?: boolean;
  resetOnDefaultValueChange?: boolean;
  closeOnSelect?: boolean;
  onCreate?: () => Promise<void> | void;
}

export interface MultiSelectRef {
  reset: () => void;
  getSelectedValues: () => string[];
  setSelectedValues: (values: string[]) => void;
  clear: () => void;
  focus: () => void;
}
