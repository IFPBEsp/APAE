"use client";

import { useEffect } from "react";
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
    await deleteVaccineFromContext({ id });
  };

  return { vaccines: filteredVaccines, loading, deleteVaccine };
}
