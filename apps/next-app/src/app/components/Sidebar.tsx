"use client";

import React, { useState } from "react";
import Image from "next/image";
import { LayoutDashboard, Users, LogOut, Menu } from "lucide-react";
import { Sheet, SheetTrigger, SheetContent } from "@/app/components/ui/sheet";

export default function Sidebar() {
  const [sheetOpen, setSheetOpen] = useState(false);
  const [active, setActive] = useState("pessoas");

  const menuItems = [
    { id: "pessoas", label: "Pessoas", icon: Users },
    { id: "dashboard", label: "Dashboard", icon: LayoutDashboard },
  ];

  return (
    <>
      {}
      <header className="flex items-center justify-between p-4 bg-[#B2D7EC] text-[#0D4F97] md:hidden">
        <button
          aria-label="Abrir menu"
          onClick={() => setSheetOpen(true)}
          className="p-2"
        >
          <Menu size={24} color="#0D4F97" />
        </button>
        <div className="flex-1 flex justify-center">
          <Image
            src="/apae-logo.png"
            alt="APAE Esperança"
            width={100}
            height={30}
            className="object-contain w-[80px] h-auto"
          />
        </div>
        <div className="w-6" />
      </header>

      {}
      <Sheet open={sheetOpen} onOpenChange={setSheetOpen}>
        <SheetTrigger asChild>
        </SheetTrigger>
        <SheetContent
          side="left"
          className="w-64 bg-[#B2D7EC] rounded-tr-[60px] p-6 flex flex-col justify-between h-full border-none"
        >
          {}
          <div className="flex justify-center mb-8">
            <Image
              src="/apae-logo.png"
              alt="APAE Esperança"
              width={100}
              height={30}
              className="object-contain w-[80px] h-auto"
            />
          </div>

          {}
          <nav className="flex flex-col gap-2">
            {menuItems.map(({ id, label, icon: Icon }) => (
              <button
                key={id}
                onClick={() => {
                  setActive(id);
                  setSheetOpen(false);
                }}
                className={`flex items-center gap-3 px-4 py-2 rounded-lg transition border-none outline-none ${
                  active === id
                    ? "text-[#0D4F97] font-semibold"
                    : "text-[#0D4F97] hover:bg-[#0D4F97]/10"
                }`}
              >
                <Icon size={20} color="#0D4F97" />
                <span>{label}</span>
              </button>
            ))}
          </nav>

          {}
          <button
            className="mt-auto flex items-center gap-2 text-[#0D4F97] hover:underline border-none outline-none bg-transparent px-4 py-2"
            onClick={() => alert("Sair clicado")}
          >
            <LogOut size={20} color="#0D4F97" />
            <span>Sair</span>
          </button>
        </SheetContent>
      </Sheet>

      {}
      <aside className="hidden md:flex flex-col bg-[#B2D7EC] rounded-tr-[60px] w-64 h-screen p-6 justify-between sticky top-0 border-none">
        {}
        <div className="flex justify-center mb-8">
          <Image
            src="/apae-logo.png"
            alt="APAE Esperança"
            width={100}
            height={30}
            className="object-contain w-[80px] h-auto"
          />
        </div>

        {}
        <nav className="flex flex-col gap-2">
          {menuItems.map(({ id, label, icon: Icon }) => (
            <button
              key={id}
              onClick={() => setActive(id)}
              className={`flex items-center gap-3 px-4 py-2 rounded-lg transition border-none outline-none ${
                active === id
                  ? "text-[#0D4F97] font-semibold"
                  : "text-[#0D4F97] hover:bg-[#0D4F97]/10"
              }`}
            >
              <Icon size={20} color="#0D4F97" />
              <span>{label}</span>
            </button>
          ))}
        </nav>

        {}
        <button
          className="mt-auto flex items-center gap-2 text-[#0D4F97] hover:underline border-none outline-none bg-transparent px-4 py-2"
          onClick={() => alert("Sair clicado")}
        >
          <LogOut size={20} color="#0D4F97" />
          <span>Sair</span>
        </button>
      </aside>
    </>
  );
}
