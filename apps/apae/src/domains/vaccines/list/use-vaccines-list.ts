"use client";

import { useCallback, useEffect, useRef, useState } from "react";
import { toast } from "react-toastify";
import { fetchVaccinesApi, deleteVaccineApi } from "@/domains/vaccines/vaccines.api";
import type { Vaccine } from "@/domains/vaccines/vaccines.types";

export function useVaccinesList() {
  const [vaccines, setVaccines] = useState<Vaccine[]>([]);
  const [loading, setLoading] = useState(true);
  const deletingIdsRef = useRef(new Set<string>());

  const loadVaccines = useCallback(async () => {
    setLoading(true);

    try {
      const data = await fetchVaccinesApi();
      setVaccines(data);
    } catch (error) {
      toast.error(error instanceof Error ? error.message : "Erro ao carregar vacinas.");
    } finally {
      setLoading(false);
    }
  }, []);

  const deleteVaccine = useCallback(
    async (id: string) => {
      if (deletingIdsRef.current.has(id)) return;
      deletingIdsRef.current.add(id);

      try {
        await deleteVaccineApi({ id });
        toast.success("Vacina excluída com sucesso.");
        await loadVaccines();
      } catch (error) {
        const message =
          error instanceof Error ? error.message : "Erro ao excluir vacina.";
        toast.error(message);
      } finally {
        deletingIdsRef.current.delete(id);
      }
    },
    [loadVaccines]
  );

  useEffect(() => {
    loadVaccines();
  }, [loadVaccines]);

  return { vaccines, loading, deleteVaccine };
}