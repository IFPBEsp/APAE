"use client";

import { DisordersProvider } from "@/hooks/use-disorders";

export default function DisordersLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return <DisordersProvider>{children}</DisordersProvider>;
}
