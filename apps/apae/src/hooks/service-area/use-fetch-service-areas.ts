import { useEffect, useState } from "react";
import { getAllServiceTypes } from "@/services/service-type-service";
import { ServiceType } from "@/types/service-type";

export function useFetchServiceAreas() {
  const [areas, setAreas] = useState<ServiceType[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  async function fetchData() {
    try {
      setLoading(true);
      const response = await getAllServiceTypes();

      if (!response.ok) {
        const errorData = await response.json();
        const errorMessage = errorData.message;
        throw new Error(errorMessage.message);
      }

      const data: ServiceType[] = await response.json();
      setAreas(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Erro desconhecido");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    fetchData();
  }, []);

  return { areas, loading, error, fetchCares: fetchData };
}
