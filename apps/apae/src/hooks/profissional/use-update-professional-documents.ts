"use client";

import { useState } from "react";
import { updateProfessionalDocuments } from "@/services/profissional-service";

type ApiError = {
  message?: string;
};

export function useUpdateProfessionalDocuments() {
  const [loadingDocs, setLoadingDocs] = useState(false);
  const [errorDocs, setErrorDocs] = useState<string | null>(null);
  const [successDocs, setSuccessDocs] = useState(false);

  async function upload(id: string, formData: FormData) {
    setLoadingDocs(true);
    setErrorDocs(null);
    setSuccessDocs(false);

    try {
      const response = await updateProfessionalDocuments(id, formData);

      const contentType = response.headers.get("content-type");
      const data: ApiError =
        contentType?.includes("application/json")
          ? await response.json().catch(() => ({}))
          : {};

      if (!response.ok) {
        throw new Error((data as any)?.message || "Erro ao enviar documentos");
      }

      setSuccessDocs(true);
      return data;
    } catch (err) {
      setErrorDocs(err instanceof Error ? err.message : "Erro desconhecido");
      throw err;
    } finally {
      setLoadingDocs(false);
    }
  }

  return { upload, loadingDocs, errorDocs, successDocs };
}
