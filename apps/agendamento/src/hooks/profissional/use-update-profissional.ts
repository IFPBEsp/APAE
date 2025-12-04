import { useState } from "react";
import { updateProfissional } from "@/services/profissional-service";
import { useRouter } from "next/navigation";

export function useUpdateProfissional() {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  function formatErrorMessage(err: any) {
    if (err?.details) {
      return Object.values(err.details).join("; ");
    }
    return err?.message || "Erro desconhecido";
  }

  async function doUpdate(id: string, formData: FormData) {
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      await updateProfissional(id, formData);

      setSuccess(true);
      setLoading(false);
      router.push("/visualization-professional");
      return true;
    } catch (err: any) {
      const formattedMsg = formatErrorMessage(err);
      setError(formattedMsg);
      setLoading(false);
      return false;
    }
  }

  return { updateProfissional: doUpdate, loading, error, success };
}
