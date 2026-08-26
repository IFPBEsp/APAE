"use client";

import { useCallback, useEffect, useState } from "react";

import { toast } from "react-toastify";

import {
  deleteVaccineApi,
  fetchVaccinesApi,
} from "../vaccines.api";

import type { DeleteVaccineParams, Vaccine } from "../vaccines.types";

export function useVaccinesList() {
  const [vaccines, setVaccines] = useState<Vaccine[]>([]);
  const [loading, setLoading] = useState(false);

  const loadVaccines = useCallback(async () => {
    try {
      setLoading(true);

      const data = await fetchVaccinesApi();

      setVaccines(data);
    } catch (error) {
      const message =
        error instanceof Error
          ? error.message
          : "Erro ao carregar vacinas.";

      toast.error(message);
    } finally {
      setLoading(false);
    }
  }, []);

  const deleteVaccine = useCallback(async (params: DeleteVaccineParams) => {
    try {
      await deleteVaccineApi(params);

      toast.success("Vacina excluída com sucesso.");

      await loadVaccines();
    } catch (error) {
      const message =
        error instanceof Error
          ? error.message
          : "Erro ao excluir vacina.";

      toast.error(message);
    }
  }, [loadVaccines]);

  useEffect(() => {
    loadVaccines();
  }, [loadVaccines]);

  return {
    vaccines,
    loading,
    loadVaccines,
    deleteVaccine,
  };
}