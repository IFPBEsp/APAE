import { useState, useEffect } from "react";
import { useParams } from "next/navigation";
import { getProfissionalById } from "@/services/profissional-service";
import { Professional } from "@/types/profissional";

export function useGetByIdProfissional() {
  const params = useParams();
  let id = params?.id;
  if (Array.isArray(id)) {
    id = id[0];
  }
  if (!id) {
    id = "";
  }

  const [profissional, setProfissional] = useState<Professional | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;

    setLoading(true);
    setError(null);

    getProfissionalById(id)
      .then((data: Professional) => {
        setProfissional(data);
      })
      .catch((err) => {
        setError(err.message || "Erro desconhecido");
      })
      .finally(() => {
        setLoading(false);
      });
  }, [id]);

  return { profissional, loading, error };
}
