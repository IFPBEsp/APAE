"use client";

import type { ReactNode } from "react";
import {
  SidebarProvider,
  AppSidebar,
  SidebarInset,
} from "@/components/ui/sidebar";
import Header from "@/components/Header";

export default function Sidebar({
  children,
}: {
  readonly children?: ReactNode;
}) {
  return (
    <SidebarProvider>
      <AppSidebar />
      <SidebarInset>
        <Header />
        <div>{children}</div>
      </SidebarInset>
    </SidebarProvider>
  );
}
