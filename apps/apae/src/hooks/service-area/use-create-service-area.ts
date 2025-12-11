import { useState } from "react";
import { createServiceArea } from "@/services/servicearea-service";

export function useCreateServiceArea() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function create(area: string) {
    setLoading(true);
    setError(null);

    try {
      const response = await createServiceArea(area);

      if (!response.ok) {
        throw new Error("Erro ao salvar área de atendimento");
      }

      const data = await response.json();
      return data;
    } catch (err: any) {
      setError(err.message || "Erro inesperado");
      throw err;
    } finally {
      setLoading(false);
    }
  }

  return { create, loading, error };
}
