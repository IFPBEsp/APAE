"use client";

import { Button } from "@/components/ui/button";

interface PaginationProps {
  readonly currentPage: number;
  readonly totalPages: number;
  readonly totalElements: number;
  readonly onPageChange: (page: number) => void | Promise<unknown>;
}

export function Pagination({
  currentPage,
  totalPages,
  totalElements,
  onPageChange,
}: PaginationProps) {
  if (totalPages <= 1) return null;

  const pages: number[] = [];
  const start = Math.max(0, currentPage - 2);
  const end = Math.min(totalPages - 1, currentPage + 2);

  for (let i = start; i <= end; i += 1) {
    pages.push(i);
  }

  return (
    <div className="mt-6 flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
      <p className="text-sm text-gray-600">
        Exibindo página {currentPage + 1} de {totalPages} • {totalElements}{" "}
        resultado(s)
      </p>

      <div className="flex flex-wrap items-center gap-2">
        <Button
          variant="outline"
          onClick={() => onPageChange(currentPage - 1)}
          disabled={currentPage === 0}
        >
          Anterior
        </Button>

        {start > 0 && (
          <>
            <Button variant="outline" onClick={() => onPageChange(0)}>
              1
            </Button>
            {start > 1 && <span className="px-1 text-gray-500">...</span>}
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
              <span className="px-1 text-gray-500">...</span>
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
          onClick={() => onPageChange(currentPage + 1)}
          disabled={currentPage >= totalPages - 1}
        >
          Próxima
        </Button>
      </div>
    </div>
  );
}
