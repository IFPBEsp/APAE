"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import { fetchVaccineApi } from "../vaccines.api";
import { useVaccinesContext } from "@/hooks/use-vaccines";
import type { Vaccine, UpdateVaccineParams } from "../vaccines.types";

export function useVaccineEdit(id: string) {
  const [vaccine, setVaccine] = useState<Vaccine | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const router = useRouter();
  const { updateVaccine: updateVaccineFromContext } = useVaccinesContext();

  useEffect(() => {
    if (!id) return;
    const load = async () => {
      try {
        const data = await fetchVaccineApi(id);
        setVaccine(data);
      } catch {
        toast.error("Erro ao carregar a vacina.");
        router.push("/vaccines");
      }
    };
    load();
  }, [id, router]);

  const updateVaccine = async (params: UpdateVaccineParams) => {
    try {
      setIsSubmitting(true);
      await updateVaccineFromContext(params);
      router.push("/vaccines");
    } catch {
      // Erro já é exibido via feedback do provider (VaccinesLayoutClient)
    } finally {
      setIsSubmitting(false);
    }
  };

  return { vaccine, updateVaccine, isSubmitting };
}
