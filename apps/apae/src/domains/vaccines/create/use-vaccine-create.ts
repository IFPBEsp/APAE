import { useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import { createVaccineApi } from "../vaccines.api";
import { CreateVaccineFormData } from "../vaccines.schema";

export function useVaccineCreate() {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleCreate(data: CreateVaccineFormData) {
    try {
      setIsLoading(true);
      setError(null);
      await createVaccineApi(data);
      toast.success("Vacina criada com sucesso!");
      router.push("/vaccines");
      router.refresh();
    } catch (err) {
      const message = err instanceof Error ? err.message : "Erro ao criar vacina.";
      setError(message);
      toast.error(message);
    } finally {
      setIsLoading(false);
    }
  }

  return {
    handleCreate,
    isLoading,
    error,
  };
}