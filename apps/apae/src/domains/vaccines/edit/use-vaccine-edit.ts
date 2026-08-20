import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import { fetchVaccineApi, updateVaccineApi } from "../vaccines.api";
import { UpdateVaccineFormData } from "../vaccines.schema";

export function useVaccineEdit(id: string) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [defaultName, setDefaultName] = useState<string>("");

  useEffect(() => {
    async function loadVaccine() {
      try {
        setIsLoading(true);
        const vaccine = await fetchVaccineApi(id);
        setDefaultName(vaccine.name);
      } catch (err) {
        setError("Erro ao carregar dados da vacina.");
      } finally {
        setIsLoading(false);
      }
    }

    if (id) {
      loadVaccine();
    }
  }, [id]);

  async function handleUpdate(data: UpdateVaccineFormData) {
    try {
      setIsSaving(true);
      setError(null);
      await updateVaccineApi({ id, name: data.name });
      toast.success("Vacina atualizada com sucesso!");
      router.push("/vaccines");
      router.refresh();
    } catch (err) {
      const message = err instanceof Error ? err.message : "Erro ao atualizar vacina.";
      setError(message);
      toast.error(message);
    } finally {
      setIsSaving(false);
    }
  }

  return {
    defaultName,
    handleUpdate,
    isLoading,
    isSaving,
    error,
  };
}