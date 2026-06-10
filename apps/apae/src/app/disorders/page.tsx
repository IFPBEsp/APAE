"use client";

import { useEffect, useRef, useState, Suspense } from "react";
import Link from "next/link";
import { useQueryState, parseAsString, parseAsInteger } from "nuqs";
import { Button } from "@/components/ui/button";
import { Search, Edit, Trash2, Loader2 } from "lucide-react";
import { ConfirmModal } from "@/components/ui/confirm-modal";
import { toast } from "react-toastify";
import { useDebounce } from "@/hooks/use-debounce";
import { api } from "@/lib/api";
import axios from "axios";

interface Transtorno {
  id: string;
  nome: string;
}

interface PaginatedResponse {
  data: Transtorno[];
  total: number;
  totalPages: number;
}

function TranstornosPageContent() {
  // nuqs URL query states
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

  // Local state for smooth typing and UI loading
  const [localSearch, setLocalSearch] = useState(search);
  const [transtornos, setTranstornos] = useState<Transtorno[]>([]);
  const [totalElements, setTotalElements] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [isLoading, setIsLoading] = useState(true);

  // Apply debounce to local search input value
  const debouncedSearch = useDebounce(localSearch, 500);
  const latestRequestKeyRef = useRef("");

  // Sync debounced search with URL query state and reset page to 1
  useEffect(() => {
    if (debouncedSearch !== search) {
      void setSearch(debouncedSearch);
      void setPage(1);
    }
  }, [debouncedSearch, search, setSearch, setPage]);

  // Sync local input search state when URL query state changes (e.g. browser navigation)
  useEffect(() => {
    setLocalSearch(search);
  }, [search]);

  // Fetch paginated disorders data
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

        const response = await api.get<PaginatedResponse>(`/transtornos?${requestKey}`, {
          signal: controller.signal,
        });

        // Prevents out-of-order state updates if another request is already in progress
        if (latestRequestKeyRef.current !== requestKey) {
          return;
        }

        setTranstornos(response.data.data);
        setTotalElements(response.data.total);
        setTotalPages(response.data.totalPages);
      } catch (err) {
        if (axios.isCancel(err)) {
          return;
        }
        console.error("Erro ao carregar transtornos:", err);
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
  }, [search, page, limit]);

  // Handles limit change (items per page) and resets page to 1
  const handleLimitChange = (newLimit: number) => {
    void setLimit(newLimit);
    void setPage(1);
  };

  // Handles disorder deletion
  const handleDelete = async (id: string) => {
    try {
      await api.delete(`/disorders/${id}`);
      toast.success("Transtorno excluído com sucesso.");
      
      // If we are on a page higher than 1 and deleting the last item on the page, go back a page
      const isLastItemOnPage = transtornos.length === 1;
      if (isLastItemOnPage && page > 1) {
        void setPage(page - 1);
      } else {
        // Otherwise, trigger data re-fetch by keeping the query params the same but modifying state manually or triggering fetch
        // Since dependencies search, page, limit are checked, we can manually refetch or force state refresh
        // Let's do a manual fetch call to refresh list
        const params = new URLSearchParams();
        if (search) params.append("search", search);
        params.append("page", String(page));
        params.append("limit", String(limit));
        const response = await api.get<PaginatedResponse>(`/transtornos?${params.toString()}`);
        setTranstornos(response.data.data);
        setTotalElements(response.data.total);
        setTotalPages(response.data.totalPages);
      }
    } catch (err: any) {
      console.error("Erro ao excluir transtorno:", err);
      const errorMsg = err.response?.data?.message || "Erro ao excluir o transtorno.";
      toast.error(errorMsg);
    }
  };

  // Render page number buttons
  const renderPageNumbers = () => {
    const pages: number[] = [];
    const start = Math.max(1, page - 2);
    const end = Math.min(totalPages, page + 2);

    for (let i = start; i <= end; i += 1) {
      pages.push(i);
    }

    return (
      <>
        {start > 1 && (
          <>
            <Button
              variant="outline"
              size="sm"
              onClick={() => void setPage(1)}
              className="text-gray-600 border-gray-300"
            >
              1
            </Button>
            {start > 2 && <span className="px-2 text-sm text-gray-500">...</span>}
          </>
        )}

        {pages.map((p) => (
          <Button
            key={p}
            variant={p === page ? "default" : "outline"}
            size="sm"
            onClick={() => void setPage(p)}
            className={
              p === page
                ? "bg-[#004b8d] text-white hover:bg-[#003d73]"
                : "text-gray-600 border-gray-300"
            }
          >
            {p}
          </Button>
        ))}

        {end < totalPages && (
          <>
            {end < totalPages - 1 && <span className="px-2 text-sm text-gray-500">...</span>}
            <Button
              variant="outline"
              size="sm"
              onClick={() => void setPage(totalPages)}
              className="text-gray-600 border-gray-300"
            >
              {totalPages}
            </Button>
          </>
        )}
      </>
    );
  };

  return (
    <div className="bg-slate-50 min-h-screen">
      <main className="container mx-auto p-4 md:p-6 max-w-6xl">
        {/* Barra de Busca (Topo) */}
        <div className="bg-white rounded-xl shadow-sm p-4 mb-6 border border-gray-100">
          <div className="relative w-full">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-400" />
            <input
              type="text"
              placeholder="Pesquisar transtornos..."
              className="w-full pl-10 pr-4 py-2 bg-white rounded-lg outline-none focus:ring-2 focus:ring-[#004b8d]/20 transition-all border border-gray-200 text-gray-700"
              value={localSearch}
              onChange={(e) => setLocalSearch(e.target.value)}
            />
          </div>
        </div>

        {/* Card Principal (Listagem) */}
        <section className="bg-white rounded-xl shadow-md border border-gray-100 overflow-hidden">
          {/* Cabeçalho do Card */}
          <div className="flex justify-between items-center p-6 border-b border-gray-100">
            <div>
              <h2 className="text-xl font-bold text-[#004b8d]">Transtornos Cadastrados</h2>
              <p className="text-sm text-gray-500 mt-1">
                {totalElements} {totalElements === 1 ? "transtorno encontrado" : "transtornos encontrados"}
              </p>
            </div>
            <Button asChild className="bg-[#004b8d] hover:bg-[#003d73] text-white font-medium px-5">
              <Link href="/disorders/new">Adicionar</Link>
            </Button>
          </div>

          {/* Corpo do Card */}
          <div className="divide-y divide-gray-100 min-h-[200px] flex flex-col justify-start">
            {isLoading ? (
              <div className="flex-1 flex justify-center items-center py-16">
                <Loader2 className="h-8 w-8 animate-spin text-[#004b8d]" />
              </div>
            ) : transtornos.length === 0 ? (
              <div className="flex-1 flex justify-center items-center py-16">
                <p className="text-gray-500 font-medium">Nenhum transtorno encontrado</p>
              </div>
            ) : (
              transtornos.map((transtorno) => (
                <div
                  key={transtorno.id}
                  className="flex justify-between items-center px-6 py-4 hover:bg-slate-50 transition-colors border-b last:border-b-0"
                >
                  <span className="text-[#004b8d] font-medium">{transtorno.nome}</span>
                  <div className="flex gap-2">
                    <Button
                      asChild
                      variant="outline"
                      size="icon"
                      className="h-8 w-8 border-gray-200 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-md transition-colors"
                      title="Editar"
                    >
                      <Link href={`/disorders/${transtorno.id}/edit`}>
                        <Edit className="h-4 w-4" />
                      </Link>
                    </Button>
                    <ConfirmModal
                      title="Excluir Transtorno"
                      description={
                        <>
                          Essa ação não pode ser desfeita. Isso irá excluir permanentemente o transtorno{" "}
                          <strong>{transtorno.nome}</strong>.
                        </>
                      }
                      onConfirm={() => handleDelete(transtorno.id)}
                      trigger={
                        <Button
                          variant="outline"
                          size="icon"
                          className="h-8 w-8 border-gray-200 text-red-500 hover:text-red-700 hover:bg-red-50 rounded-md transition-colors"
                          title="Excluir"
                        >
                          <Trash2 className="h-4 w-4" />
                        </Button>
                      }
                    />
                  </div>
                </div>
              ))
            )}
          </div>

          {/* Rodapé do Card (Paginação) */}
          {!isLoading && transtornos.length > 0 && (
            <div className="bg-gray-50 px-6 py-4 flex flex-col sm:flex-row justify-between items-center gap-4 border-t border-gray-100">
              <div className="flex flex-col sm:flex-row items-start sm:items-center gap-4 w-full sm:w-auto">
                <span className="text-sm text-gray-600 font-medium">Total de registros: {totalElements}</span>
                <div className="flex items-center gap-2">
                  <span className="text-sm text-gray-600">Itens por página</span>
                  <select
                    className="h-9 rounded-md border border-gray-300 bg-white px-2 py-1 text-sm outline-none focus:ring-2 focus:ring-[#004b8d]/20 text-gray-700 font-medium"
                    value={limit}
                    onChange={(e) => handleLimitChange(Number(e.target.value))}
                  >
                    <option value={10}>10</option>
                    <option value={20}>20</option>
                    <option value={50}>50</option>
                  </select>
                </div>
              </div>

              <div className="flex items-center gap-1">
                <Button
                  variant="outline"
                  size="sm"
                  disabled={page === 1}
                  onClick={() => void setPage(page - 1)}
                  className="text-gray-600 border-gray-300 font-medium"
                >
                  Anterior
                </Button>

                {renderPageNumbers()}

                <Button
                  variant="outline"
                  size="sm"
                  disabled={page >= totalPages}
                  onClick={() => void setPage(page + 1)}
                  className="text-gray-600 border-gray-300 font-medium"
                >
                  Próxima
                </Button>
              </div>
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

export default function DisordersPage() {
  return (
    <Suspense
      fallback={
        <div className="min-h-screen bg-slate-50 flex items-center justify-center">
          <Loader2 className="h-8 w-8 animate-spin text-[#004b8d]" />
        </div>
      }
    >
      <TranstornosPageContent />
    </Suspense>
  );
}
