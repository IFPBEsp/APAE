"use client";

import { useCallback } from "react";
import { toast } from "react-toastify";
import { useDisordersContext } from "@/hooks/use-disorders";

export function useDisordersList() {
  const {
    disorders,
    loading,
    deleteDisorder: deleteDisorderCtx,
  } = useDisordersContext();

  const deleteDisorder = useCallback(
    async (id: string) => {
      try {
        await deleteDisorderCtx({ id });
        toast.success("Transtorno excluído com sucesso.");
      } catch (error) {
        const message =
          error instanceof Error ? error.message : "Erro ao excluir transtorno.";
        toast.error(message);
      }
    },
    [deleteDisorderCtx],
  );

  return { disorders, loading, deleteDisorder };
}
