"use client";

import { usePathname } from "next/navigation";
import { SidebarInset } from "@/components/ui/sidebar";
import { SidebarProvider } from "@/components/ui/sidebar";
import { AppSidebar } from "./sidebar";
import Header from "./Header";

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
        </>
      )}
      <SidebarInset>
        <Header />
        <div>{children}</div>
      </SidebarInset>
    </SidebarProvider>
  );
}