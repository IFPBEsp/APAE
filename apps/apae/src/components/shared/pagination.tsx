"use client";

import { Button } from "@/components/ui/button";

interface PaginationProps {
  readonly currentPage: number;
  readonly totalPages: number;
  readonly totalElements: number;
  readonly pageSize: number;
  readonly onPageChange: (page: number) => void;
  readonly onPageSizeChange: (size: number) => void;
}

export function Pagination({
  currentPage,
  totalPages,
  totalElements,
  pageSize,
  onPageChange,
  onPageSizeChange,
}: PaginationProps) {
  if (totalElements === 0) {
    return null;
  }

  const pages: number[] = [];
  const start = Math.max(0, currentPage - 2);
  const end = Math.min(Math.max(totalPages - 1, 0), currentPage + 2);

  for (let i = start; i <= end; i += 1) {
    pages.push(i);
  }

  return (
    <div className="mt-6 flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
      <div className="flex items-center gap-3">
        <p className="text-sm text-gray-600">
          Total de registros: {totalElements}
        </p>

        <div className="flex items-center gap-2">
          <span className="text-sm text-gray-600">Itens por página</span>
          <select
            className="h-9 rounded-md border border-gray-300 bg-white px-3 text-sm"
            value={pageSize}
            onChange={(e) => onPageSizeChange(Number(e.target.value))}
          >
            <option value={2}>2</option>
            <option value={5}>5</option>
            <option value={10}>10</option>
            <option value={20}>20</option>
            <option value={50}>50</option>
          </select>
        </div>
      </div>

      <div className="flex flex-wrap items-center gap-2">
        <Button
          variant="outline"
          disabled={currentPage === 0}
          onClick={() => onPageChange(currentPage - 1)}
        >
          Anterior
        </Button>

        {start > 0 && (
          <>
            <Button variant="outline" onClick={() => onPageChange(0)}>
              1
            </Button>
            {start > 1 && (
              <span className="px-1 text-sm text-gray-500">...</span>
            )}
          </>
        )}

        {pages.map((page) => (
          <Button
            key={page}
            variant={page === currentPage ? "default" : "outline"}
            onClick={() => onPageChange(page)}
            className={page === currentPage ? "!bg-[#0D4F97] text-white" : ""}
          >
            {page + 1}
          </Button>
        ))}

        {end < totalPages - 1 && (
          <>
            {end < totalPages - 2 && (
              <span className="px-1 text-sm text-gray-500">...</span>
            )}
            <Button
              variant="outline"
              onClick={() => onPageChange(totalPages - 1)}
            >
              {totalPages}
            </Button>
          </>
        )}

        <Button
          variant="outline"
          disabled={currentPage >= totalPages - 1 || totalPages === 0}
          onClick={() => onPageChange(currentPage + 1)}
        >
          Próxima
        </Button>
      </div>
    </div>
  );
}
