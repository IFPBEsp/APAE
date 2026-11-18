"use client";

import { useEffect, useState } from "react";
import { getProfessionalDocuments } from "@/domains/professional/profissional-service";
import { DocumentWithUrl } from "@/domains/documents/documents.types";

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

        const data = await getProfessionalDocuments<DocumentWithUrl[]>(id as string);

        if (!cancelled) setDocuments(data ?? []);
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
