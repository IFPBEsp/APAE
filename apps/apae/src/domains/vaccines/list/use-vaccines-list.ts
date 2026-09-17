"use client";

import { useVaccinesContext } from "@/hooks/use-vaccines";

export function useVaccinesList(searchName: string) {
  const { vaccines, loading, deleteVaccine: deleteVaccineFromContext } = useVaccinesContext();

  const filteredVaccines = vaccines.filter((v) =>
    v.name.toLowerCase().includes(searchName.toLowerCase()),
  );

  const deleteVaccine = async (id: string) => {
    try {
      await deleteVaccineFromContext({ id });
    } catch {
      // Erro já é exibido via feedback do provider (VaccinesLayoutClient)
    }
  };

  return { vaccines: filteredVaccines, loading, deleteVaccine };
}
