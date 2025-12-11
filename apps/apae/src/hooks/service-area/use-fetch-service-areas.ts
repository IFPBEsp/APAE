import { useEffect, useState } from "react";
import { getAllServiceAreas } from "@/services/servicearea-service";
import { ServiceArea } from "@/types/service-area";

export function useFetchServiceAreas() {
  const [areas, setAreas] = useState<ServiceArea[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchData() {
      try {
        setLoading(true);
        const response = await getAllServiceAreas();

        if (!response.ok) {
          throw new Error(`Erro: ${response.status}`);
        }

        const data: ServiceArea[] = await response.json();
        setAreas(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Erro desconhecido");
      } finally {
        setLoading(false);
      }
    }

    fetchData();
  }, []);

  return { areas, loading, error };
}
