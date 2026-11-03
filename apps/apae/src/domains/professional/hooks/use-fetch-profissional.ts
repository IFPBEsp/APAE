"use client";

import { getAllProfessionals } from "@/domains/professional/profissional-service";
import { Professional } from "@/domains/professional/profissional.types";
import { useEffect, useState } from "react";
import { toast } from "react-toastify";

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
        
        const data = await getAllProfessionals<PaginatedResponse<Professional>>(active);

        setProfessionals(data.content);
      } catch (err) {
        const msg = err instanceof Error ? err.message : "Erro desconhecido";
        setError(msg);
        toast.error(msg);
      } finally {
        setLoading(false);
      }
    }

    fetchProfessionals();
  }, [active]);

  return { professionals, loading, error, setProfessionals };
}