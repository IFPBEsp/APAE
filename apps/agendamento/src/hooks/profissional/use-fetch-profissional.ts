import { getAllProfissionais } from "@/services/profissional-service";
import { Profissional } from "@/types/profissional";
import { useEffect, useState } from "react";

interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
}

export function useFetchProfessionals() {
  const [profissionais, setProfissionais] = useState<Profissional[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchProfessionals() {
      try {
        setLoading(true);
        const response = await getAllProfissionais();

        if (!response.ok) {
          const errorData = await response.json();
          const errorMessage = errorData.message;
          throw new Error(errorMessage);
        }

        const data: PaginatedResponse<Profissional> = await response.json();
        setProfissionais(data.content);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Erro desconhecido");
      } finally {
        setLoading(false);
      }
    }

    fetchProfessionals();
  }, []);

  return { profissionais, loading, error, setProfissionais };
}
