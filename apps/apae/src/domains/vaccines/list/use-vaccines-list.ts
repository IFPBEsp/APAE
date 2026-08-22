<<<<<<< HEAD
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
=======
import { useCallback, useEffect, useState } from "react";
import { toast } from "react-toastify";
import { fetchVaccinesApi, deleteVaccineApi } from "@/domains/vaccines/vaccines.api";
import type { Vaccine } from "@/domains/vaccines/vaccines.types";

export function useVaccinesList() {
  const [vaccines, setVaccines] = useState<Vaccine[]>([]);
  const [loading, setLoading] = useState(true);

  const loadVaccines = useCallback(async () => {
    setLoading(true);

    try {
      const data = await fetchVaccinesApi();
      setVaccines(data);
    } catch (error) {
      toast.error(error instanceof Error ? error.message : "Erro ao carregar vacinas.");
>>>>>>> ea1a7055 (feat(vaccines): refatorar os formulários de criação e edição de vacinas)
    } finally {
      setLoading(false);
    }
  }, []);

<<<<<<< HEAD
<<<<<<< HEAD
  useEffect(() => {
    loadVaccines();
  }, [loadVaccines]);

  return { vaccines, loading };
}
=======
const deleteVaccine = useCallback(
=======
  const deleteVaccine = useCallback(
>>>>>>> a034b3e5 (fix(vaccines): corrigir a indentação e a formatação no hook useVaccinesList)
    async (id: string) => {
      try {
        await deleteVaccineApi({ id });
        toast.success("Vacina excluída com sucesso.");
        await loadVaccines();
      } catch (error) {
        const message =
          error instanceof Error ? error.message : "Erro ao excluir vacina.";
        toast.error(message);
      }
    },
    [loadVaccines]
  );
  useEffect(() => {
    loadVaccines();
  }, [loadVaccines]);

  return { vaccines, loading, deleteVaccine };
}
>>>>>>> ea1a7055 (feat(vaccines): refatorar os formulários de criação e edição de vacinas)
