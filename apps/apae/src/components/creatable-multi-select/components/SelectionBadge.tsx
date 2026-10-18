import * as React from "react";
import { XCircle } from "lucide-react";
import { cn } from "@/lib/utils";
import { Badge } from "@/components/ui/badge";
import { multiSelectVariants } from "../shared/variants";
import type {
  MultiSelectOption,
  AnimationConfig,
  ResolvedResponsiveSettings,
} from "../types";
import type { VariantProps } from "class-variance-authority";

interface SelectionBadgeProps
  extends VariantProps<typeof multiSelectVariants> {
  option: MultiSelectOption;
  animation: number;
  animationConfig?: AnimationConfig;
  isAnimating: boolean;
  responsiveSettings: ResolvedResponsiveSettings;
  screenSize: "mobile" | "tablet" | "desktop";
  singleLine: boolean;
  onRemove: (value: string) => void;
}

export function SelectionBadge({
  option,
  variant,
  animation,
  animationConfig,
  isAnimating,
  responsiveSettings,
  screenSize,
  singleLine,
  onRemove,
}: SelectionBadgeProps) {
  const { style: customStyle, icon: IconComponent } = option;

  const badgeAnimationClass = isAnimating
    ? "animate-bounce"
    : "hover:-translate-y-1 hover:scale-110";

  const badgeStyle: React.CSSProperties = {
    animationDuration: `${animationConfig?.duration ?? animation}s`,
    animationDelay: `${animationConfig?.delay ?? 0}s`,
    ...(customStyle?.badgeColor && { backgroundColor: customStyle.badgeColor }),
    ...(customStyle?.gradient && {
      background: customStyle.gradient,
      color: "white",
    }),
  };

  return (
    <Badge
      className={cn(
        badgeAnimationClass,
        multiSelectVariants({ variant }),
        customStyle?.gradient && "text-white border-transparent",
        responsiveSettings.compactMode && "text-xs px-1.5 py-0.5",
        screenSize === "mobile" && "max-w-[120px] truncate",
        singleLine && "shrink-0 whitespace-nowrap",
        "[&>svg]:pointer-events-auto",
      )}
      style={badgeStyle}
    >
      {IconComponent && !responsiveSettings.hideIcons && (
        <IconComponent
          className={cn(
            "h-4 w-4 mr-2",
            responsiveSettings.compactMode && "h-3 w-3 mr-1",
          )}
          {...(customStyle?.iconColor && {
            style: { color: customStyle.iconColor },
          })}
        />
      )}

      <span className={cn(screenSize === "mobile" && "truncate")}>
        {option.label}
      </span>

      <div
        role="button"
        tabIndex={0}
        onClick={(e) => {
          e.stopPropagation();
          onRemove(option.value);
        }}
        onKeyDown={(e) => {
          if (e.key === "Enter" || e.key === " ") {
            e.preventDefault();
            e.stopPropagation();
            onRemove(option.value);
          }
        }}
        aria-label={`Remove ${option.label} from selection`}
        className="ml-2 h-4 w-4 cursor-pointer hover:bg-white/20 rounded-sm p-0.5 -m-0.5 focus:outline-none focus:ring-1 focus:ring-white/50"
      >
        <XCircle
          className={cn("h-3 w-3", responsiveSettings.compactMode && "h-2.5 w-2.5")}
        />
      </div>
    </Badge>
  );
}
