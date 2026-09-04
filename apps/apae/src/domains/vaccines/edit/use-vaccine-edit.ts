"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";

import { fetchVaccineApi, updateVaccineApi } from "../vaccines.api";

import type {
  Vaccine,
  UpdateVaccineParams,
} from "../vaccines.types";

export function useVaccineEdit(id: string) {
  const [vaccine, setVaccine] = useState<Vaccine | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const router = useRouter();

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

      await updateVaccineApi(params);

      toast.success("Vacina atualizada com sucesso.");

      router.push("/vaccines");
    } catch (error) {
      const message =
        error instanceof Error
          ? error.message
          : "Erro ao atualizar vacina.";

      toast.error(message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return {
    vaccine,
    updateVaccine,
    isSubmitting,
  };
}