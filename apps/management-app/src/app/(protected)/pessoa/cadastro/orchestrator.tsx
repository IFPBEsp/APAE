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
        router.push("/pessoa/cadastro/pessoal");
        break;
      case MembersRegisterStep.KINSHIPS:
        router.push("/pessoa/cadastro/parentes");
        break;
      case MembersRegisterStep.ADDRESS:
        router.push("/pessoa/cadastro/endereco");
        break;
      case MembersRegisterStep.ADDITIONALS:
        router.push("/pessoa/cadastro/adicional");
        break;
      case MembersRegisterStep.GUARDIAN:
        router.push("/pessoa/cadastro/responsavel");
        break;
      case MembersRegisterStep.PROFILE:
        router.push("/pessoa/cadastro/perfil");
        break;
    }
  }, [step, router]);

  return children;
}
