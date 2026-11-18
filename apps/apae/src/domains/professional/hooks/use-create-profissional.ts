import { useState } from "react";
import { toast } from "react-toastify";
import { createProfessional } from "@/domains/professional/profissional-service";
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

        const msg = data.message || "Erro ao cadastrar profissional";
        setError(msg);
        toast.error(msg);
        return;
      }

      setSuccess(true);
      toast.success("Profissional cadastrado com sucesso!");
      router.push("/professionals");
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message || "Erro inesperado" : "Erro inesperado";
      setError(msg);
      toast.error(msg);
      throw err;
    } finally {
      setLoading(false);
    }
  }

  return { create, loading, error, success };
}
