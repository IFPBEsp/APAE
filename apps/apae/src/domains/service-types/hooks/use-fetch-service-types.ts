"use client";

import { useEffect, useState } from "react";
import { bffFetchJson } from "@/lib/bff-client";
import { ServiceType } from "../service-types.types";

export function useFetchServiceTypes() {
  const [serviceTypes, setServiceTypes] = useState<ServiceType[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  async function refetch() {
    try {
      setLoading(true);
      setError(null);
      const data = await bffFetchJson<ServiceType[]>("/apae-geral/api/service-types");
      setServiceTypes(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Erro desconhecido");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    refetch();
  }, []);

  return { serviceTypes, loading, error, refetch };
}
