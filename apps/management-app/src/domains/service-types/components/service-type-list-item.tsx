"use client";

import { Edit, Trash2 } from "lucide-react";

import { Button } from "@/components/ui/button";

import type { ServiceType } from "../service-types.types";

interface ServiceTypeListItemProps {
  onDelete: (service: ServiceType) => void;
  onEdit: () => void;
  service: ServiceType;
}

export function ServiceTypeListItem({ service, onEdit, onDelete }: ServiceTypeListItemProps) {
  return (
    <div className="flex items-center justify-between p-4 border-b hover:bg-gray-50 transition-colors">
      <div className="flex-1">
        <h3 className="font-bold text-base text-[#003B93]">{service.area}</h3>
      </div>
      <div className="flex items-center gap-2 ml-4">
        <Button
          variant="outline"
          size="icon"
          className="h-8 w-8"
          onClick={onEdit}
          aria-label="Editar"
        >
          <Edit className="h-4 w-4" />
        </Button>
        <Button
          variant="outline"
          size="icon"
          className="h-8 w-8 text-red-600 hover:text-red-700"
          onClick={() => onDelete(service)}
          aria-label="Excluir"
        >
          <Trash2 className="h-4 w-4" />
        </Button>
      </div>
    </div>
  );
}
