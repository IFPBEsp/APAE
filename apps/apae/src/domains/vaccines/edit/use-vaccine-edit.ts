"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import { updateVaccineApi, fetchVaccineApi } from "../vaccines.api";
import type { UpdateVaccineParams, Vaccine } from "../vaccines.types";

export function useVaccineEdit(id: string) {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [vaccine, setVaccine] = useState<Vaccine | null>(null);
  const router = useRouter();

  useEffect(() => {
    const loadVaccine = async () => {
      try {
        setIsLoading(true);
        const data = await fetchVaccineApi(id);
        setVaccine(data);
      } catch (error) {
        toast.error("Erro ao carregar a vacina.");
        router.push("/vaccines");
      } finally {
        setIsLoading(false);
      }
    };
    if (id) {
      loadVaccine();
    }
  }, [id, router]);

  const updateVaccine = async (params: UpdateVaccineParams) => {
    try {
      setIsSubmitting(true);
      await updateVaccineApi(params);
      toast.success("Vacina atualizada com sucesso.");
      router.push("/vaccines");
    } catch (error) {
      const message = error instanceof Error ? error.message : "Erro ao atualizar vacina.";
      toast.error(message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return { updateVaccine, isSubmitting, isLoading, vaccine };
}