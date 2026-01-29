"use client";

import {
  MembersRegisterStep,
  useMembersRegisterContext,
} from "@/hooks/use-members-register-context";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export function PageOrchestrator({
  children,
}: {
  readonly children: React.ReactNode;
}) {
  const router = useRouter();
  const {
    state: { step },
  } = useMembersRegisterContext();

  useEffect(() => {
    switch (step) {
      case MembersRegisterStep.PERSONAL:
        router.push("/person/register/personal");
        break;
      case MembersRegisterStep.ADDRESS:
        router.push("/person/register/address");
        break;
      case MembersRegisterStep.ADDITIONALS:
        router.push("/person/register/additional");
        break;
      case MembersRegisterStep.GUARDIANS:
        router.push("/person/register/responsible");
        break;
      case MembersRegisterStep.PROFILE:
        router.push("/person/register/profile");
        break;
    }
  }, [step, router]);

  return children;
}
