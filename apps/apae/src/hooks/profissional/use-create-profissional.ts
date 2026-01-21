import { useState } from "react";
import { createProfissional } from "@/services/profissional-service";
import { useRouter } from "next/navigation";

export function useCreateProfissional() {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  async function create(formData: FormData) {
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      const response = await createProfissional(formData);

      if (!response.ok) {
        throw new Error("Erro ao salvar profissional");
      }

      setSuccess(true);
      router.push("/visualization-professional");
    } catch (err: any) {
      setError(err.message || "Erro inesperado");
      throw err;
    } finally {
      setLoading(false);
    }
  }

  return { create, loading, error, success };
}
