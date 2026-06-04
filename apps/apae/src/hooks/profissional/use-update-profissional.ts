import { useState } from "react";
import { updateProfessional } from "@/services/profissional-service";

export type UpdateProfessionalDto = {
  name: string;
  email: string;
  phoneNumber: string;
  address: {
    state: string;
    city: string;
    neighborhood: string;
    street: string;
    number: string;
    complement: string;
    cep: string;
  };
  professionalDocument: string | null;
  serviceArea: { area: string };
  identityDocument: string;
  availabilities?: { day?: string; shift?: string }[];
};


type ApiResponse = {
  message?: string;
};

export function useUpdateProfessional() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  async function doUpdate(id: string, data: UpdateProfessionalDto) {
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      const response = await updateProfessional(id, data);

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

  return { updateProfessional: doUpdate, loading, error, success };
}
