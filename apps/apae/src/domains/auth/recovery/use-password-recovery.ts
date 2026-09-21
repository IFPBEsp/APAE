"use client";

import { toast } from "react-toastify";

import { requestPasswordRecovery } from "../auth.api";
import { FormRecovery } from "../authSchema";

export function usePasswordRecovery() {
  async function submit(data: FormRecovery) {
    try {
      await requestPasswordRecovery(data.email);

      toast.success(
        "Se o e-mail existir, um link de recuperação foi enviado."
      );
    } catch {
      toast.error(
        "Erro ao solicitar recuperação de senha."
      );
    }
  }

  return {
    submit,
  };
}