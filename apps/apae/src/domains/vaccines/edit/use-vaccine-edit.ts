"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import { fetchVaccineApi, updateVaccineApi } from "../vaccines.api";
import type { Vaccine, UpdateVaccineParams } from "../vaccines.types";

export function useVaccineEdit(id: string) {
  const [vaccine, setVaccine] = useState<Vaccine | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const router = useRouter();

  useEffect(() => {
    if (!id) return;
    const load = async () => {
      try {
        setIsLoading(true);
        const data = await fetchVaccineApi(id);
        setVaccine(data);
      } catch {
        toast.error("Erro ao carregar os dados da vacina.");
        router.push("/vaccines");
      } finally {
        setIsLoading(false);
      }
    };
    load();
  }, [id, router]);

  const updateVaccine = async (params: UpdateVaccineParams) => {
    try {
      setIsSubmitting(true);
      await updateVaccineApi(params);
      toast.success("Vacina atualizada com sucesso.");
      router.push("/vaccines");
      router.refresh();
    } catch (error) {
      const message =
        error instanceof Error ? error.message : "Erro ao atualizar vacina.";
      toast.error(message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return { vaccine, updateVaccine, isLoading, isSubmitting };
}
