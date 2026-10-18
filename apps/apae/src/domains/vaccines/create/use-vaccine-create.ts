"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useVaccinesContext } from "@/hooks/use-vaccines";
import type { CreateVaccineParams } from "../vaccines.types";

export function useVaccineCreate() {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const router = useRouter();
  const { createVaccine: createVaccineFromContext } = useVaccinesContext();

  const createVaccine = async (params: CreateVaccineParams) => {
    try {
      setIsSubmitting(true);
      await createVaccineFromContext(params);
      router.push("/vaccines");
    } catch {
      // Erro já é exibido via feedback do provider (VaccinesLayoutClient)
    } finally {
      setIsSubmitting(false);
    }
  };

  return { createVaccine, isSubmitting };
}
