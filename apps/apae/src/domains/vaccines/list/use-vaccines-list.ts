"use client";

import { useCallback, useEffect, useState } from "react";
import { toast } from "react-toastify";
import { fetchVaccinesApi } from "../vaccines.api";
import type { Vaccine } from "../vaccines.types";

export function useVaccinesList() {
  const [vaccines, setVaccines] = useState<Vaccine[]>([]);
  const [loading, setLoading] = useState(false);

  const loadVaccines = useCallback(async () => {
    try {
      setLoading(true);
      const data = await fetchVaccinesApi();
      setVaccines(data);
    } catch (error) {
      const message = error instanceof Error ? error.message : "Erro ao carregar vacinas.";
      toast.error(message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadVaccines();
  }, [loadVaccines]);

  return { vaccines, loading };
}
