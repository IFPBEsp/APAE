"use client";

import { toast } from "react-toastify";
import { useVaccinesContext } from "@/hooks/use-vaccines";

export function useVaccinesList() {
  const { vaccines, loading, deleteVaccine: deleteVaccineFromContext } = useVaccinesContext();

  const deleteVaccine = async (id: string) => {
    try {
      await deleteVaccineFromContext({ id });
      toast.success("Vacina excluída com sucesso.");
    } catch (error) {
      const message = error instanceof Error ? error.message : "Erro ao excluir vacina.";
      toast.error(message);
    }
  };

  return { vaccines, loading, deleteVaccine };
}
