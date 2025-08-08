"use client"

import Image from "next/image"
import { User, Menu } from 'lucide-react'
import { Button } from "@/components/ui/button"
import {
SidebarProvider,
SidebarInset,
SidebarTrigger,
useSidebar,
AppSidebar,
} from "@/components/ui/sidebar"

type ApaeLayoutProps = {
title?: string
children?: React.ReactNode
}

function Header({ title = "Dashboard" }: { title?: string }) {
const { state } = useSidebar()

return (
  <header className="relative">
    <SidebarTrigger
      className="md:hidden absolute left-2 top-2 h-9 w-9 text-white hover:bg-white/10"
      aria-label="Abrir menu"
    >
      <Menu className="h-5 w-5" />
    </SidebarTrigger>

    <div className="md:hidden h-14 flex items-center justify-center bg-[#14457A] text-white rounded-b-2xl shadow">
      <Image
        src="/apae-logo.png"
        alt="Logo APAE"
        width={28}
        height={28}
        className="h-7 w-7 object-contain"
      />
    </div>

    <div className="hidden md:flex relative h-16 items-center justify-center bg-[#14457A] text-white rounded-b-2xl shadow">
      {state === "collapsed" && (
        <div className="absolute left-2">
          <SidebarTrigger className="text-white hover:bg-white/10" aria-label="Abrir menu" />
        </div>
      )}


      <Image
        src="/apae-logo.png"
        alt="Logo APAE"
        width={28}
        height={28}
        className="h-8 w-8 object-contain"
      />


      <div className="absolute right-2">
        <Button variant="ghost" size="icon" aria-label="Perfil" className="text-white hover:bg-white/10">
          <User className="h-5 w-5" />
        </Button>
      </div>

      
      <span className="sr-only">{title}</span>
    </div>
  </header>
)
}

export default function ApaeLayout({ title = "Dashboard", children }: ApaeLayoutProps) {
return (
  <SidebarProvider defaultOpen>
    <AppSidebar />
    <SidebarInset>
      <Header title={title} />
    </SidebarInset>
  </SidebarProvider>
)
}
