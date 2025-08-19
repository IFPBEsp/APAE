"use client";

import React from "react";
import { User } from "lucide-react";
import { SidebarTrigger } from "@/components/ui/sidebar";

export default function Header() {
  return (
    <header className="flex h-16 shrink-0 items-center justify-between bg-[#0D4F97] px-4 transition-[width,height] ease-linear group-has-[[data-collapsible=icon]]/sidebar-wrapper:h-12 rounded-b-lg relative">
      <div className="flex items-center">
        <SidebarTrigger className="text-white hover:bg-white/10" />
      </div>

      <div className="flex-1 flex items-center justify-center">
        <img src="/apae-logo.png" alt="logo apae" className="h-10 w-8 mr-3" />
        <span className="text-white font-medium text-lg hidden md:block">Apae Esperança</span>
      </div>

      <div className="flex items-center">
        <User className="h-6 w-6 text-white" />
      </div>
    </header>
  );
}
