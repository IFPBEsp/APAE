import { useState } from "react";
import { toast } from "react-toastify";
import { updateProfessional } from "@/domains/professional/profissional-service";
import { handleBackendValidationErrors, professionalFieldMap } from "@/lib/utils/form-errors";
import { FieldValues, UseFormSetError } from "react-hook-form";

export type UpdateProfessionalDto = {
  name: string;
  email: string;
  phoneNumber: string;
  professionalDocument: string | null;
  serviceArea: { area: string };
  identityDocument: string | null;
  availabilities?: { day?: string; shift?: string }[];
};


type ApiResponse = {
  message?: string;
  fields?: {
    field: string;
    message: string;
  }[];
};

export function useUpdateProfessional<T extends FieldValues>(
  formSetError: UseFormSetError<T>,
) {
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

      console.log(body);


      if (!response.ok) {
        handleBackendValidationErrors(
          body,
          formSetError,
          professionalFieldMap,
        );

        const msg = body.message || "Erro ao atualizar profissional";
        setError(msg);
        toast.error(msg);
        return false;
}

      setSuccess(true);
      toast.success("Profissional atualizado com sucesso!");
      return true;
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : "Erro inesperado";
      setError(msg);
      toast.error(msg);
      return false;
    } finally {
      setLoading(false);
    }
  }

  return { updateProfessional: doUpdate, loading, error, success };
}
