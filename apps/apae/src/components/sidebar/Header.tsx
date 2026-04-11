"use client";

import React from "react";
import Image from "next/image";
import { User } from "lucide-react";
import { SidebarTrigger } from "@/components/ui/sidebar";
import Logo from "../../assets/logo.png";

export default function Header() {
  return (
    <header className="flex h-16 shrink-0 items-center justify-between bg-[#0D4F97] px-4 transition-[width,height] ease-linear group-has-data-[collapsible=icon]/sidebar-wrapper:h-12 rounded-lg relative my-4 mx-10 shadow-lg">
      <div className="flex items-center">
        <SidebarTrigger className="text-white hover:bg-white/10" />
      </div>

      <div className="flex-1 flex items-center justify-center">
        <Image
          src={Logo}
          alt="logo apae"
          width={32}
          height={40}
          className="mr-3 h-10 w-8 object-contain"
        />
        <span className="text-white font-medium text-lg hidden md:block">
          Apae Esperança
        </span>
      </div>

      <div className="flex items-center">
        <User className="h-6 w-6 text-white" />
      </div>
    </header>
  );
}
