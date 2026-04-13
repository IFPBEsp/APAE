import { useState } from "react";
import { updateProfissional } from "@/services/profissional-service";

type UpdateProfissionalDto = {
  name: string;
  email: string;
  phoneNumber: string;
  address: string;
  professionalDocument: string;
  serviceArea: string;
  identityDocument: string;
};

type ApiResponse = {
  message?: string;
};

export function useUpdateProfissional() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  async function doUpdate(id: string, data: UpdateProfissionalDto) {
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      const response = await updateProfissional(id, data);

      const contentType = response.headers.get("content-type");
      const body: ApiResponse = contentType?.includes("application/json")
        ? await response.json().catch(() => ({}))
        : {};

      if (!response.ok) {
        throw new Error(body.message || "Erro ao atualizar profissional");
      }

      setSuccess(true);
      return true;
    } catch (err: unknown) {
      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError("Erro inesperado");
      }
      return false;
    } finally {
      setLoading(false);
    }
  }

  return { updateProfissional: doUpdate, loading, error, success };
}
