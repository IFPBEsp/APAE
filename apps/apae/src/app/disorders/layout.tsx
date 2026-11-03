"use client";

import { DisordersProvider } from "@/domains/disorders/use-disorders";

export default function DisordersLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return <DisordersProvider>{children}</DisordersProvider>;
}
