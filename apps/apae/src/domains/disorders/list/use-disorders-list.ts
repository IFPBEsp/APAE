"use client";

import { useEffect, useRef, useState } from "react";
import { useQueryState, parseAsString, parseAsInteger } from "nuqs";
import { toast } from "react-toastify";

interface Transtorno {
  id: string;
  nome: string;
}

export function useDisordersList() {
  const [search, setSearch] = useQueryState(
    "search",
    parseAsString.withDefault("").withOptions({ shallow: false })
  );
  const [page, setPage] = useQueryState(
    "page",
    parseAsInteger.withDefault(1).withOptions({ shallow: false })
  );
  const [limit, setLimit] = useQueryState(
    "limit",
    parseAsInteger.withDefault(10).withOptions({ shallow: false })
  );

  const [localSearch, setLocalSearch] = useState(search);
  const [transtornos, setTranstornos] = useState<Transtorno[]>([]);
  const [totalElements, setTotalElements] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const latestRequestKeyRef = useRef("");

  useEffect(() => {
    const timer = setTimeout(() => {
      if (localSearch !== search) {
        void setSearch(localSearch);
        void setPage(1);
      }
    }, 500);

    return () => clearTimeout(timer);
  }, [localSearch, search, setSearch, setPage]);

  useEffect(() => {
    setLocalSearch(search);
  }, [search]);

  useEffect(() => {
    const controller = new AbortController();

    async function loadData() {
      setIsLoading(true);
      try {
        const params = new URLSearchParams();
        if (search) params.append("search", search);
        params.append("page", String(page));
        params.append("limit", String(limit));

        const requestKey = params.toString();
        latestRequestKeyRef.current = requestKey;

        const response = await fetch(`/apae-geral/api/disorders?${requestKey}`, {
          signal: controller.signal,
        });

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || "Erro ao buscar dados");
        }

        const data = await response.json();

        if (!Array.isArray(data)) {
          throw new Error("Formato de resposta inválido do servidor.");
        }

        const mapped = data.map((item: any) => ({
          id: item.id,
          nome: item.name || "",
        }));

        const filtered = mapped.filter((item) =>
          item.nome.toLowerCase().includes(search.toLowerCase())
        );

        const total = filtered.length;
        const totalPagesCount = Math.ceil(total / limit);

        const startIndex = (page - 1) * limit;
        const paginatedData = filtered.slice(startIndex, startIndex + limit);

        if (latestRequestKeyRef.current !== requestKey) {
          return;
        }

        setTranstornos(paginatedData);
        setTotalElements(total);
        setTotalPages(totalPagesCount);
      } catch (err) {
        if (err instanceof DOMException && err.name === "AbortError") {
          return;
        }
        console.error(err);
        toast.error("Não foi possível carregar os transtornos.");
      } finally {
        if (!controller.signal.aborted) {
          setIsLoading(false);
        }
      }
    }

    void loadData();

    return () => {
      controller.abort();
    };
  }, [search, page, limit, refreshTrigger]);

  const handleLimitChange = (newLimit: number) => {
    void setLimit(newLimit);
    void setPage(1);
  };

  const handleDelete = async (id: string) => {
    try {
      const response = await fetch(`/apae-geral/api/disorders/${id}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao excluir o transtorno.");
      }

      toast.success("Transtorno excluído com sucesso.");

      const isLastItemOnPage = transtornos.length === 1;
      if (isLastItemOnPage && page > 1) {
        void setPage(page - 1);
      } else {
        setRefreshTrigger((prev) => prev + 1);
      }
    } catch (err: any) {
      console.error(err);
      toast.error(err.message || "Erro ao excluir o transtorno.");
    }
  };

  return {
    page,
    setPage,
    limit,
    localSearch,
    setLocalSearch,
    transtornos,
    totalElements,
    totalPages,
    isLoading,
    handleLimitChange,
    handleDelete,
  };
}
