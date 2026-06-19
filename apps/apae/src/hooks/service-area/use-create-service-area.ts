import { useState } from "react";
import { createServiceType } from "@/services/service-type-service";

export function useCreateServiceArea() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function create(area: string) {
    setLoading(true);
    setError(null);

    try {
      const response = await createServiceType(area);
      const data = await response.json();

      if (!response.ok) {
        const errorMessage = data.message;
        throw new Error(errorMessage);
      }

      return data;
    } catch (err: unknown) {
      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError("Erro inesperado");
      }
      throw err;
    } finally {
      setLoading(false);
    }
  }

  return { create, loading, error };
}
