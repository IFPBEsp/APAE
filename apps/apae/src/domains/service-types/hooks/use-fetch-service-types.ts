import { useEffect, useState } from "react";
import { ServiceType } from "../service-types.types";

export function useFetchServiceTypes() {
  const [serviceTypes, setServiceTypes] = useState<ServiceType[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  async function refetch() {
    try {
      setLoading(true);
      setError(null);
      const response = await fetch("/apae-geral/api/service-types");
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message ?? "Erro ao buscar tipos de atendimento.");
      }
      const data: ServiceType[] = await response.json();
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
