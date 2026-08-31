import type { ResponsiveConfig, ScreenSize, ResolvedResponsiveSettings } from "../types";

const DEFAULT_RESPONSIVE = {
  mobile: { maxCount: 2, hideIcons: false, compactMode: true },
  tablet: { maxCount: 4, hideIcons: false, compactMode: false },
  desktop: { maxCount: 6, hideIcons: false, compactMode: false },
} as const;

export function useResponsiveSettings(
  responsive: ResponsiveConfig | undefined,
  maxCount: number,
  screenSize: ScreenSize,
): ResolvedResponsiveSettings {
  if (!responsive) {
    return { maxCount, hideIcons: false, compactMode: false };
  }

  if (responsive === true) {
    const cfg = DEFAULT_RESPONSIVE[screenSize];
    return {
      maxCount: cfg?.maxCount ?? maxCount,
      hideIcons: cfg?.hideIcons ?? false,
      compactMode: cfg?.compactMode ?? false,
    };
  }

  const cfg = responsive[screenSize];
  return {
    maxCount: cfg?.maxCount ?? maxCount,
    hideIcons: cfg?.hideIcons ?? false,
    compactMode: cfg?.compactMode ?? false,
  };
}
