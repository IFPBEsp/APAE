import { useState } from "react";
import { updateProfissional } from "@/services/profissional-service";

interface ProfissionalAddress {
  state: string;
  city: string;
  neighborhood: string;
  street: string;
  number?: string;
  complement?: string;
  cep: string;
}

interface ProfissionalAvailability {
  day?: string;
  shift?: string;
}

export interface ProfissionalUpdateData {
  serviceArea: { area: string };
  phoneNumber: string;
  professionalDocument: string;
  email: string;
  name: string;
  identityDocument: string;
  address: ProfissionalAddress;
  availabilities: ProfissionalAvailability[];
}

export function useUpdateProfissional() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  async function doUpdate(id: string, data: ProfissionalUpdateData) {
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      const response = await updateProfissional(id, data as unknown as Record<string, string | number | boolean | null | undefined>);

      const contentType = response.headers.get("content-type");
      const body = contentType?.includes("application/json")
        ? await response.json().catch(() => ({}))
        : {};

      interface ErrorBody {
        message?: string;
      }

      if (!response.ok) {
        throw new Error((body as ErrorBody)?.message || "Erro ao atualizar profissional");
      }

      setSuccess(true);
      return true;
    } catch (err: unknown) {
      const e = err as { message?: string };
      setError(e.message || "Erro inesperado");
      return false;
    } finally {
      setLoading(false);
    }
  }

  return { updateProfissional: doUpdate, loading, error, success };
}