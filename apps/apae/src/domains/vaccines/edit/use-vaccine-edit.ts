"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";

import {
  fetchVaccineApi,
  updateVaccineApi,
} from "../vaccines.api";

import type {
  UpdateVaccineParams,
  Vaccine,
} from "../vaccines.types";

export function useVaccineEdit(id: string) {
  const [vaccine, setVaccine] = useState<Vaccine | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const router = useRouter();

  useEffect(() => {
    const loadVaccine = async () => {
      try {
        setIsLoading(true);

        const data = await fetchVaccineApi(id);

        setVaccine(data);
      } catch (error) {
        const message =
          error instanceof Error
            ? error.message
            : "Erro ao carregar vacina.";

        toast.error(message);
      } finally {
        setIsLoading(false);
      }
    };

    loadVaccine();
  }, [id]);

  const updateVaccine = async (
    params: Omit<UpdateVaccineParams, "id">,
  ) => {
    try {
      setIsSubmitting(true);

      await updateVaccineApi({
        id,
        ...params,
      });

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
    isLoading,
    isSubmitting,
    updateVaccine,
  };
}