"use client";

import { useState } from "react";
import { bffFetchJson } from "@/lib/bff-client";
import type { ServiceType } from "../service-types.types";

export function useCreateServiceType() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function create(area: string): Promise<ServiceType> {
    setLoading(true);
    setError(null);
    try {
      return await bffFetchJson<ServiceType>("/apae-geral/api/service-types", {
        method: "POST",
        body: JSON.stringify({ area }),
      });
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Erro inesperado");
      throw err;
    } finally {
      setLoading(false);
    }
  }

  return { create, loading, error };
}
