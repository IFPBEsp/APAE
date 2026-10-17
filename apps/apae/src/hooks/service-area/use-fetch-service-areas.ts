import { useEffect, useState } from "react";
import { getAllServiceAreas } from "@/services/servicearea-service";
import { ServiceArea } from "@/types/service-area";

export function useFetchServiceAreas() {
  const [areas, setAreas] = useState<ServiceArea[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  async function fetchData() {
    try {
      setLoading(true);
      const data = await getAllServiceAreas<ServiceArea[]>();
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
