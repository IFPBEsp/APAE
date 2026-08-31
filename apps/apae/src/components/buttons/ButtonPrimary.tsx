"use client";
import React from "react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

interface PrimaryButtonProps {
  readonly children: React.ReactNode;
  readonly onClick?: () => void;
  readonly disabled?: boolean;
  readonly loading?: boolean;
  readonly type?: "button" | "submit" | "reset";
  readonly variant?: "default" | "destructive" | "outline" | "secondary" | "ghost" | "link";
  readonly size?: "default" | "sm" | "lg" | "icon";
  readonly className?: string;
  readonly fullWidth?: boolean;
}

export function PrimaryButton({
  children,
  onClick,
  disabled = false,
  loading = false,
  type = "button",
  variant = "default",
  size = "default",
  className,
  fullWidth = true,
  ...props
}: PrimaryButtonProps) {
  return (
    <div className={cn("flex justify-center mt-6 sm:mt-12", fullWidth && "w-full")}>
      <Button
        type={type}
        variant={variant}
        size={size}
        onClick={onClick}
        disabled={disabled || loading}
        className={cn(
          "w-[144px] h-[48px] rounded-md px-4 py-2",
          "!bg-[#0D4F97] hover:!bg-[#0D4F97]/90 active:!bg-[#0D4F97]/80",
          "!border-none !shadow-none !outline-none",
          "!text-white font-medium text-[16px] leading-[20px]",
          "transition-all duration-200",
          "disabled:!opacity-50 disabled:!cursor-not-allowed",
          "focus:!outline-none focus:!ring-2 focus:!ring-[#0D4F97]/50 focus:!ring-offset-2",
          "[&]:!bg-[#0D4F97] [&:hover]:!bg-[#0D4F97]/90 [&:active]:!bg-[#0D4F97]/80",
          className,
        )}
        style={{
          backgroundColor: "#0D4F97",
          color: "white",
          border: "none",
          boxShadow: "none",
        }}
        {...props}
      >
        {loading ? (
          <div className="flex items-center gap-2">
            <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
            <span>Carregando...</span>
          </div>
        ) : (
          <span className="font-medium text-[16px] leading-[20px]">{children}</span>
        )}
      </Button>
    </div>
  );
}
