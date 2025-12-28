"use client";

import { Button } from "@/components/ui/button";
import { Edit } from "lucide-react";
import { ServiceType } from "@/schemas/service-type-schemas";

interface ServiceTypeListItemProps {
  service: ServiceType; 
  onEdit: () => void;
}

export function ServiceTypeListItemItem({
  service,
  onEdit,
}: ServiceTypeListItemProps) {
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
      </div>
    </div>
  );
}