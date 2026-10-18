"use client";

import { useRouter } from "next/navigation";
import { toast } from "react-toastify";

import { resetPassword } from "../auth.api";

import {
  FormNewPasswordSchema,
} from "../authSchema";

export function useResetPassword(
  token: string
) {
  const router = useRouter();

  async function submit(
    data: FormNewPasswordSchema
  ) {
    try {
      await resetPassword({
        token,
        newPassword: data.password,
        confirmPassword: data.confirmPassword,
      });

      toast.success(
        "Senha alterada com sucesso."
      );

      router.push("/auth/login");
    } catch {
      toast.error("Erro inesperado.");
    }
  }

  return {
    submit,
  };
}