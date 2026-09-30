"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import { createDisorderApi } from "../disorders.api";
import type { CreateDisorderParams } from "../disorders.types";
import { useDisordersContext } from "@/hooks/use-disorders";

export function useDisorderCreate() {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const router = useRouter();
  const { fetchDisorders } = useDisordersContext();

  const createDisorder = async (params: CreateDisorderParams) => {
    try {
      setIsSubmitting(true);
      await createDisorderApi(params);
      await fetchDisorders();
      toast.success("Transtorno criado com sucesso.");
      router.push("/disorders");
    } catch (error) {
      const message = error instanceof Error ? error.message : "Erro ao criar transtorno.";
      toast.error(message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return { createDisorder, isSubmitting };
}
