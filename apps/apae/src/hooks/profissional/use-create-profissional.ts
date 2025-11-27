import { useState } from "react";
import { createProfissional } from "@/services/profissional-service";
import { useRouter } from "next/navigation";

export function useCreateProfissional() {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  async function create(data: any) {
    setLoading(true);
    setError(null);
    setSuccess(false);
    try {
      await createProfissional(data);
      setSuccess(true);
      router.push("/visualization-professional");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Erro desconhecido");
      throw err;
    } finally {
      setLoading(false);
    }
  }

  return { create, loading, error, success };
}
