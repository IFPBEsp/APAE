"use client";

import { useCallback, useEffect, useState } from "react";
import { toast } from "react-toastify";
import { fetchDisordersApi, deleteDisorderApi } from "../disorders.api";
import type { Disorder } from "../disorders.types";

export function useDisordersList() {
  const [disorders, setDisorders] = useState<Disorder[]>([]);
  const [loading, setLoading] = useState(false);

  const loadDisorders = useCallback(async () => {
    try {
      setLoading(true);
      const data = await fetchDisordersApi();
      setDisorders(data);
    } catch (error) {
      const message = error instanceof Error ? error.message : "Erro ao carregar transtornos.";
      toast.error(message);
    } finally {
      setLoading(false);
    }
  }, []);

  const deleteDisorder = useCallback(
    async (id: string) => {
      try {
        await deleteDisorderApi({ id });
        toast.success("Transtorno excluído com sucesso.");
        await loadDisorders();
      } catch (error) {
        const message = error instanceof Error ? error.message : "Erro ao excluir transtorno.";
        toast.error(message);
      }
    },
    [loadDisorders],
  );

  useEffect(() => {
    loadDisorders();
  }, [loadDisorders]);

  return { disorders, loading, deleteDisorder };
}
