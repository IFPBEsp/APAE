"use client";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { LucideIcon } from "lucide-react";

interface InfoCardProps {
  title: string;
  icon: LucideIcon;
  value: string | number;
  subtitle?: string;
  iconColor?: string;
  titleClassName?: string;
  valueClassName?: string;
  subtitleClassName?: string;
}

export function InfoCard({
  title,
  icon: Icon,
  value,
  subtitle,
  iconColor,
  titleClassName,
  valueClassName,
  subtitleClassName,
}: InfoCardProps) {
  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between padding10">
        <CardTitle className={`text-sm font-medium sm:text-base ${titleClassName ?? "text-black"}`}>
          {title}
        </CardTitle>
        <Icon className={`h-4 w-4 sm:h-5 sm:w-5 ${iconColor ?? "text-gray-400"}`} />
      </CardHeader>
      <CardContent className="padding10 pt-0 leading-tight">
        <div className={`text-lg font-bold sm:text-xl ${valueClassName ?? "text-black"}`}>
          {value}
        </div>
        {subtitle && (
          <div
            className={`text-xs text-muted-foreground sm:text-sm ${subtitleClassName ?? "text-black"}`}
          >
            {subtitle}
          </div>
        )}
      </CardContent>
    </Card>
  );
}
