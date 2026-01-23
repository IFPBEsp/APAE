import { useState } from "react";
import { updateProfissional } from "@/services/profissional-service";
import { useRouter } from "next/navigation";

export function useUpdateProfissional() {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  async function doUpdate(id: string, data: any) {
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      const response = await updateProfissional(id, data);

      if (!response.ok) {
        const updateData = await response.json();
        const errorMessage = updateData.message;
        throw new Error(errorMessage);
      }

      setSuccess(true);
      router.push("/visualization-professional");

      return true;
    } catch (err: any) {
      setError(err.message || "Erro inesperado");
      return false;
    } finally {
      setLoading(false);
    }
  }

  return { updateProfissional: doUpdate, loading, error, success };
}
