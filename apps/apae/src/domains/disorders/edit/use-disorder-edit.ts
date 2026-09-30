"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import { fetchDisorderApi, updateDisorderApi } from "../disorders.api";
import type { Disorder, UpdateDisorderParams } from "../disorders.types";
import { useDisordersContext } from "@/hooks/use-disorders";

export function useDisorderEdit(id: string) {
  const [disorder, setDisorder] = useState<Disorder | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const router = useRouter();
  const { fetchDisorders } = useDisordersContext();

  useEffect(() => {
    if (!id) return;
    const load = async () => {
      try {
        const data = await fetchDisorderApi(id);
        setDisorder(data);
      } catch {
        toast.error("Erro ao carregar o transtorno.");
        router.push("/disorders");
      }
    };
    load();
  }, [id, router]);

  const updateDisorder = async (params: UpdateDisorderParams) => {
    try {
      setIsSubmitting(true);
      await updateDisorderApi(params);
      await fetchDisorders();
      toast.success("Transtorno atualizado com sucesso.");
      router.push("/disorders");
    } catch (error) {
      const message = error instanceof Error ? error.message : "Erro ao atualizar transtorno.";
      toast.error(message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return { disorder, updateDisorder, isSubmitting };
}
