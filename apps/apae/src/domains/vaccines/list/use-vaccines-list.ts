"use client";

import { useEffect } from "react";
import { toast } from "react-toastify";
import { useVaccinesContext } from "@/hooks/use-vaccines";

export function useVaccinesList(searchName: string) {
  const { vaccines, loading, fetchVaccines, deleteVaccine: deleteVaccineFromContext } = useVaccinesContext();

  useEffect(() => {
    fetchVaccines();
  }, [fetchVaccines]);

  const filteredVaccines = vaccines.filter((v) =>
    v.name.toLowerCase().includes(searchName.toLowerCase()),
  );

  const deleteVaccine = async (id: string) => {
    try {
      await deleteVaccineFromContext({ id });
      toast.success("Vacina excluída com sucesso.");
    } catch (error) {
      const message = error instanceof Error ? error.message : "Erro ao excluir vacina.";
      toast.error(message);
    }
  };

  return { vaccines: filteredVaccines, loading, deleteVaccine };
}
