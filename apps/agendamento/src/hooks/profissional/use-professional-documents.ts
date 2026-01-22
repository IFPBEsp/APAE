"use client";

import { useEffect, useState } from "react";
import {
  getProfessionalDocuments,
  updateProfessionalDocuments,
} from "@/services/profissional-service";
import { DocumentWithUrl } from "@/types/document";

export function useProfessionalDocuments(id?: string) {
  const [documents, setDocuments] = useState<DocumentWithUrl[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) {
      setDocuments([]);
      setLoading(false);
      setError(null);
      return;
    }

    let cancelled = false;

    async function fetchDocs() {
      try {
        setLoading(true);
        setError(null);

        const response = await getProfessionalDocuments(id);

        const contentType = response.headers.get("content-type");
        const data = contentType?.includes("application/json")
          ? await response.json().catch(() => [])
          : [];

        if (!response.ok) {
          const message = (data as any)?.message || "Erro ao buscar documentos";
          throw new Error(message);
        }

        if (!cancelled) setDocuments((data ?? []) as DocumentWithUrl[]);
      } catch (err) {
        if (!cancelled) {
          setError(err instanceof Error ? err.message : "Erro desconhecido");
          setDocuments([]);
        }
      } finally {
        if (!cancelled) setLoading(false);
      }
    }

    fetchDocs();

    return () => {
      cancelled = true;
    };
  }, [id]);

  return { documents, loading, error };
}

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
      const data = contentType?.includes("application/json")
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
