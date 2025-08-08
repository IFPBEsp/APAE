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
} from "@/app/components/ui/Sidebar"
import { cn } from "@/lib/utils"

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

      <div
        className={cn(
          "hidden md:grid grid-cols-[auto_1fr_auto] items-center h-16 px-4 bg-[#14457A] text-white rounded-b-2xl shadow"
        )}
      >
        <div className="flex items-center gap-2">
          {state === "collapsed" && (
            <SidebarTrigger className="-ml-1 text-white hover:bg-white/10" aria-label="Abrir menu" />
          )}
          <Image
            src="/apae-logo.png"
            alt="Logo APAE"
            width={28}
            height={28}
            className="h-8 w-8 object-contain"
          />
        </div>
        <div className="flex items-center justify-center">
          <h1 className="text-lg font-semibold text-center">{title}</h1>
        </div>
        <div className="flex items-center justify-end">
          <Button variant="ghost" size="icon" aria-label="Perfil" className="text-white hover:bg-white/10">
            <User className="h-5 w-5" />
          </Button>
        </div>
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
        <main className="p-4">
          {children ?? (
            <div className="rounded-lg border bg-white p-6 text-sm text-muted-foreground">
              {}
            </div>
          )}
        </main>
      </SidebarInset>
    </SidebarProvider>
  )
}