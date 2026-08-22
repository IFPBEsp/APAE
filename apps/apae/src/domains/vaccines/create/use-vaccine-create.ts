
"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import { createVaccineApi } from "../vaccines.api";
import type { CreateVaccineParams } from "../vaccines.types";
export function useVaccineCreate() {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const router = useRouter();
  const createVaccine = async (params: CreateVaccineParams) => {
    try {
      setIsSubmitting(true);
      await createVaccineApi(params);
      toast.success("Vacina criada com sucesso.");
      router.push("/vaccines");
      router.refresh();
    } catch (error) {
      const message = error instanceof Error ? error.message : "Erro ao criar vacina.";
      toast.error(message);
    } finally {
      setIsSubmitting(false);
    }
  };
  return { createVaccine, isSubmitting };
}