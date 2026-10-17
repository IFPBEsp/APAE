import { useState } from "react";
import { createServiceArea } from "@/services/servicearea-service";

interface ServiceAreaResponse {
  area: string;
}

export function useCreateServiceArea() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function create(area: string): Promise<ServiceAreaResponse> {
    setLoading(true);
    setError(null);

    try {
      const data = await createServiceArea<ServiceAreaResponse>(area);
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
