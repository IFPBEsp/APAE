"use client";

import { useCallback, useEffect, useRef, useState } from "react";
import { useVaccinesContext } from "@/hooks/use-vaccines";
import { bffFetchJson } from "@/lib/bff-client";
import type { Page } from "@/types/pagination";
import type { Vaccine } from "../vaccines.types";

interface UseVaccinesListParams {
  name: string;
  page: number;
  size: number;
}

export function useVaccinesList({ name, page, size }: UseVaccinesListParams) {
  const { deleteVaccine: deleteVaccineFromContext } = useVaccinesContext();
  const [vaccines, setVaccines] = useState<Vaccine[]>([]);
  const [loading, setLoading] = useState(true);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const latestRequestKeyRef = useRef("");

  const fetchPage = useCallback(
    async (signal?: AbortSignal) => {
      setLoading(true);

      try {
        const params = new URLSearchParams();

        if (name) {
          params.append("name", name);
        }

        params.append("page", String(page));
        params.append("size", String(size));

        const requestKey = params.toString();
        latestRequestKeyRef.current = requestKey;

        const data = await bffFetchJson<Page<Vaccine>>(
          `/apae-geral/api/vaccines?${requestKey}`,
          { signal },
        );

        if (latestRequestKeyRef.current !== requestKey) {
          return;
        }

        setVaccines(data.content);
        setTotalPages(data.totalPages);
        setTotalElements(data.totalElements);
      } catch (err) {
        if (err instanceof DOMException && err.name === "AbortError") {
          return;
        }

        console.error("Erro ao buscar vacinas:", err);
      } finally {
        if (!signal?.aborted) {
          setLoading(false);
        }
      }
    },
    [name, page, size],
  );

  useEffect(() => {
    const controller = new AbortController();

    void fetchPage(controller.signal);

    return () => {
      controller.abort();
    };
  }, [fetchPage]);

  const deleteVaccine = async (id: string) => {
    try {
      await deleteVaccineFromContext({ id });
      await fetchPage();
    } catch {
      // Erro já é exibido via feedback do provider (VaccinesLayoutClient)
    }
  };

  return {
    vaccines,
    loading,
    totalPages,
    totalElements,
    deleteVaccine,
  };
}
