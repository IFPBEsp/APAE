"use client";

import { usePathname } from "next/navigation";
import Sidebar from "@/components/Sidebar";

export default function RootLayout({ children }: { readonly children: React.ReactNode }) {
  const path = usePathname();
  const shouldShowSidebar = !path.startsWith("/auth");

  return (
    <div>
      {shouldShowSidebar ? (
        <Sidebar>{children}</Sidebar>
      ) : (
        <main>{children}</main>
      )}
    </div>
  );
}
