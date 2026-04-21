"use client";

import {
  MembersRegisterStep,
  useMembersRegisterContext,
} from "@/hooks/use-members-register-context";
import { useRouter, usePathname } from "next/navigation"; 
import { useEffect } from "react";

export function PageOrchestrator({
  children,
}: {
  readonly children: React.ReactNode;
}) {
  const router = useRouter();
  const pathname = usePathname(); 
  const {
    state: { step },
  } = useMembersRegisterContext();

  useEffect(() => {
    const stepPaths: Record<string, string> = {
      [MembersRegisterStep.PERSONAL]: "personal",
      [MembersRegisterStep.KINSHIPS]: "kinships",
      [MembersRegisterStep.GUARDIAN]: "responsible",
      [MembersRegisterStep.ADDRESS]: "address",
      [MembersRegisterStep.ADDITIONALS]: "additional",
      [MembersRegisterStep.PROFILE]: "profile",
    };

    const targetSlug = stepPaths[step];
    const targetPath = `/person/register/${targetSlug}`;

    if (pathname === "/person/register" || (targetSlug && !pathname.includes(targetSlug))) {
      router.push(targetPath);
    }
  }, [step, router, pathname]);

  return children;
}