"use client";

import { useRouter } from "next/navigation";
import { useEffect } from "react";

export function PageOrchestrator({ children, }: { readonly children: React.ReactNode;}) {
  const router = useRouter();

  useEffect(() => {
        router.push("/register-profissional");

  }, [router]);

  return children;
}
