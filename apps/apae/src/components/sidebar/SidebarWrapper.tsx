"use client";

import { usePathname } from "next/navigation";
import { SidebarTrigger } from "@/components/ui/sidebar";
import { SidebarProvider } from "@/components/ui/sidebar";
import { AppSidebar } from "./sidebar";

interface SidebarWrapperProps {
  children: React.ReactNode;
}

export function SidebarWrapper({ children }: SidebarWrapperProps) {
  const pathname = usePathname();
  const isAuthRoute = pathname.startsWith('/auth');

  return (
    <SidebarProvider>
      {!isAuthRoute && (
        <>
          <AppSidebar />
          <SidebarTrigger />
        </>
      )}
      {children}
    </SidebarProvider>
  );
}