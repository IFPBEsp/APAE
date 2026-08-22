"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import { fetchVaccineApi, updateVaccineApi } from "@/domains/vaccines/vaccines.api";
import type { Vaccine, UpdateVaccineParams } from "@/domains/vaccines/vaccines.types";

export function useVaccineEdit(id: string) {
  const [vaccine, setVaccine] = useState<Vaccine | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const router = useRouter();

  useEffect(() => {
    if (!id) return;

    const load = async () => {
      setIsLoading(true);

      try {
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
    setIsSubmitting(true);

    try {
      await updateVaccineApi(params);
      toast.success("Vacina atualizada com sucesso.");
      router.push("/vaccines");
      router.refresh();
    } catch (error) {
      toast.error(error instanceof Error ? error.message : "Erro ao atualizar vacina.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return { vaccine, updateVaccine, isLoading, isSubmitting };
}