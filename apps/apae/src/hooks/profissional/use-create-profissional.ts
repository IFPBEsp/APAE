import { useState } from "react";
import { createProfessional } from "@/services/profissional-service";
import { useRouter } from "next/navigation";
import { FieldValues, UseFormSetError } from "react-hook-form";
import { handleBackendValidationErrors, professionalFieldMap, } from "@/lib/utils/form-errors";


export function useCreateProfessional<T extends FieldValues>(
  formSetError: UseFormSetError<T>,
) {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  async function create(formData: FormData) {
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      const response = await createProfessional(formData);
      const data = await response.json();

      if (!response.ok) {
        handleBackendValidationErrors(
          data,
          formSetError,
          professionalFieldMap,
        );

        setError(data.message || "Erro ao cadastrar profissional");
        return;
      }

      setSuccess(true);
      router.push("/professionals");
    } catch (err: unknown) {
      if(err instanceof Error) {
        setError(err.message || "Erro inesperado");
      } else {
        setError("Erro inesperado");
      }
      throw err;
    } finally {
      setLoading(false);
    }
  }

  return { create, loading, error, success };
}
