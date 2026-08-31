import { getAllProfessionals } from "@/services/profissional-service";
import { Professional } from "@/types/profissional";
import { useEffect, useState } from "react";
import axios from "axios";

interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
}

export function useFetchProfessionals(active: boolean) {
  const [professionals, setProfessionals] = useState<Professional[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchProfessionals() {
      try {
        setLoading(true);
        setError(null);

        console.log("[useFetchProfessionals] disparou fetch com ativo =", active);

        const response = await getAllProfessionals(active);

        const data: PaginatedResponse<Professional> = response.data;
        setProfessionals(data.content);
      } catch (err) {
        if (axios.isAxiosError(err)) {
          setError(err.response?.data?.message || err.message);
        } else {
          setError(err instanceof Error ? err.message : "Erro desconhecido");
        }
      } finally {
        setLoading(false);
      }
    }

    fetchProfessionals();
  }, [active]);

  return { professionals, loading, error, setProfessionals };
}
