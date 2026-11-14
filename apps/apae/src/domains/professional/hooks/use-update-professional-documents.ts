"use client";

import { useState } from "react";
import { updateProfessionalDocuments } from "@/domains/professional/profissional-service";

type ApiError = {
  message?: string;
};

export function useUpdateProfessionalDocuments() {
  const [loadingDocs, setLoadingDocs] = useState(false);
  const [errorDocs, setErrorDocs] = useState<string | null>(null);
  const [successDocs, setSuccessDocs] = useState(false);

  async function upload(id: string, formData: FormData): Promise<boolean> {
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
        setErrorDocs(data?.message || "Erro ao enviar documentos");
        return false;
      }

      setSuccessDocs(true);
      return true;
    } catch (err) {
      setErrorDocs(err instanceof Error ? err.message : "Erro desconhecido");
      return false;
    } finally {
      setLoadingDocs(false);
    }
  }

  return { upload, loadingDocs, errorDocs, successDocs };
}
