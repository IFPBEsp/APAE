import { useState, useMemo } from "react";

import {
  filterProfessionals,
  getProfessionalAreaOptions,
} from "@/domains/professional/shared/professional.utils";
import { type ProfessionalStatusFilter, type Professional } from "@/types/profissional";

export function useProfessionalFilters(professionals: Professional[]) {
  const [searchTerm, setSearchTerm] = useState("");
  const [areaFilter, setAreaFilter] = useState("all");
  const [statusFilter, setStatusFilter] = useState<ProfessionalStatusFilter>("activate");

  const filteredProfessionals = useMemo(
    () => filterProfessionals(professionals, { searchTerm, areaFilter }),
    [professionals, searchTerm, areaFilter],
  );

  const uniqueAreas = useMemo(
    () => getProfessionalAreaOptions(professionals),
    [professionals],
  );

  return {
    searchTerm,
    setSearchTerm,
    areaFilter,
    setAreaFilter,
    statusFilter,
    setStatusFilter,
    filteredProfessionals,
    uniqueAreas,
  };
}
