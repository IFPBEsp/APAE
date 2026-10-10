import { useState } from "react";

export function useCreateServiceType() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function create(area: string) {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch("/apae-geral/api/service-types", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ area }),
      });
      const data = await response.json();
      if (!response.ok) {
        throw new Error(data.message ?? "Erro ao criar tipo de atendimento.");
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
