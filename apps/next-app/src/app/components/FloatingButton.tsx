"use client";

import { cn } from "@/lib/utils";
import { Plus } from "lucide-react";
import React from "react";

type FloatingButtonProps = {
  onClick: () => void;
  className?: string;
  icon?: React.ReactNode;
  label?: string; // acessibilidade
};

export default function FloatingButton({
  onClick,
  className,
  icon,
  label = "Adicionar",
}: FloatingButtonProps) {
  return (
    <button
      type="button"
      onClick={onClick}
      aria-label={label}
      className={cn(
        "fixed bottom-6 right-6 z-50",
        "w-14 h-14 !p-0 !rounded-full !bg-[#165BAA] !text-white",
        "flex items-center justify-center shadow-lg hover:!bg-[#0e4a8c] transition-colors",
        "focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-[#165BAA]",
        className
      )}
    >
      {icon ?? <Plus className="w-6 h-6 text-white" />}
    </button>
  );
}
