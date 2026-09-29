import { useState } from "react";
import { createProfissional } from "@/services/profissional-service";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";

export function useCreateProfissional() {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  async function create(formData: FormData, photo?: File | null) {
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      const response = await createProfissional(formData);
      const data = await response.json();

      if (!response.ok) {
        const errorMessage = data.message;
        throw new Error(errorMessage);
      }

      if (photo) {
        try {
          const photoData = new FormData();
          photoData.append("file", photo);

          const photoResponse = await fetch(`/apae-geral/api/professionals/${data.id}/photo`, {
            method: "PATCH",
            body: photoData,
          });

          if (!photoResponse.ok) {
            throw new Error("Erro ao enviar foto");
          }
        } catch {
          toast.warning("Profissional cadastrado, mas a foto não foi enviada.");
          setSuccess(true);
          router.push("/professionals");
          return;
        }
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
