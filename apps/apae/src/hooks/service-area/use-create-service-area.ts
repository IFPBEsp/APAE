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
      const data = await response.json();

      if (!response.ok) {
        const errorMessage = data.message;
        throw new Error(errorMessage);
      }

      return data;
    } catch (err: unknown) {
      const error = err as { message?: string };
      setError(error.message || "Erro inesperado");
      throw err;
    } finally {
      setLoading(false);
    }
  }

  return { create, loading, error };
}
