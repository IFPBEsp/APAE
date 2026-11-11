"use client";

import { useRouter } from "next/navigation";
import { toast } from "react-toastify";

import { login } from "../auth.api";
import { FormLogin } from "../authSchema";
import { startSession } from "@/domains/auth/session-id";
import { clearAllPatientDrafts } from "@/domains/patients/utils/draft-storage";

export function useLogin() {
  const router = useRouter();

  async function submit(data: FormLogin) {
    try {
      const res = await login(data);

      const responseData = await res.json();

      if (res.ok) {
        clearAllPatientDrafts();
        startSession();

        toast.success(responseData.message);
        router.push("/");
        return;
      }

      toast.error(responseData.message);
    } catch {
      toast.error("Erro inesperado.");
    }
  }

  return {
    submit,
  };
}
