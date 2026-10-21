"use client";

import { Button } from "@/components/ui/button";

interface AbsencePaginationProps {
  currentPage: number;
  totalPages: number;
  loading: boolean;
  onPageChange: (page: number) => void;
}

export function AbsencePagination({
  currentPage,
  totalPages,
  loading,
  onPageChange,
}: AbsencePaginationProps) {
  if (totalPages <= 1) return null;

  return (
    <div className="flex justify-between items-center mt-6 pt-4 border-t border-gray-200">
      <div className="text-sm text-gray-500">
        Página {currentPage + 1} de {totalPages}
      </div>
      <div className="flex gap-2">
        <Button
          variant="outline"
          size="sm"
          disabled={currentPage === 0 || loading}
          onClick={() => onPageChange(currentPage - 1)}
        >
          Anterior
        </Button>
        <Button
          variant="outline"
          size="sm"
          disabled={currentPage === totalPages - 1 || loading}
          onClick={() => onPageChange(currentPage + 1)}
        >
          Próxima
        </Button>
      </div>
    </div>
  );
}
