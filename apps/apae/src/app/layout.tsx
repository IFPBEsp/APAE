import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";

import ToastProvider from "@/components/shared/ToastProvider";
import { SidebarWrapper } from "@/components/sidebar/SidebarWrapper";
import { NuqsAdapter } from "nuqs/adapters/next/app";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "APAE-ESP APP",
  description: "Aplicação de gerenciamento da APAE",
  icons: {
    icon: "apae-geral/favicon.png",
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased bg-white`}
      >
        <NuqsAdapter>
          <SidebarWrapper>
            <ToastProvider>{children}</ToastProvider>
          </SidebarWrapper>
        </NuqsAdapter>
      </body>
    </html>
  );
}
