import type * as React from "react";
import { cn } from "@/lib/utils";
import type { ScreenSize } from "../types";

interface TriggerClassNameParams {
  autoSize: boolean;
  compactMode: boolean;
  screenSize: ScreenSize;
  disabled: boolean;
  className?: string;
}

export function getTriggerClassName({
  autoSize,
  compactMode,
  screenSize,
  disabled,
  className,
}: TriggerClassNameParams): string {
  return cn(
    "flex p-1 rounded-md border min-h-10 h-auto items-center justify-between bg-inherit hover:bg-inherit [&_svg]:pointer-events-auto",
    autoSize ? "w-auto" : "w-full",
    compactMode && "min-h-8 text-sm",
    screenSize === "mobile" && "min-h-12 text-base",
    disabled && "opacity-50 cursor-not-allowed",
    className,
  );
}

export function getPopoverContentClassName(
  screenSize: ScreenSize,
  popoverClassName?: string,
): string {
  return cn(
    "w-auto p-0",
    screenSize === "mobile" && "w-[85vw] max-w-[280px]",
    screenSize === "tablet" && "w-[70vw] max-w-md",
    screenSize === "desktop" && "min-w-[300px]",
    popoverClassName,
  );
}

export function getPopoverContentStyle(
  maxWidth: string,
  screenSize: ScreenSize,
): React.CSSProperties {
  return {
    maxWidth: `min(${maxWidth}, 85vw)`,
    maxHeight: screenSize === "mobile" ? "70vh" : "60vh",
    touchAction: "manipulation",
  };
}
