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
        router.push("/members/register/personal");
        break;
      case MembersRegisterStep.ADDRESS:
        router.push("/members/register/address");
        break;
      case MembersRegisterStep.ADDITIONALS:
        router.push("/members/register/additionals");
        break;
      case MembersRegisterStep.GUARDIANS:
        router.push("/members/register/guardians");
        break;
      case MembersRegisterStep.PROFILE:
        router.push("/members/register/profile");
        break;
    }
  }, [step, router]);

  return children;
}
