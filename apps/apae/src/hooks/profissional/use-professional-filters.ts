import { useState, useMemo } from "react";

type StatusFilter = "activate" | "inactivate";

interface Professional {
  id: string;
  name: string;
  cpf?: string | null;
  professionalDocument?: string | null;
  serviceArea: { area: string };
  phoneNumber: string;
}

export function useProfessionalFilters(professionals: Professional[]) {
  const [searchTerm, setSearchTerm] = useState("");
  const [areaFilter, setAreaFilter] = useState("all");
  const [statusFilter, setStatusFilter] = useState<StatusFilter>("activate");

  const filteredProfessionals = useMemo(() => {
    return professionals.filter((prof) => {
      const name = prof.name?.toLowerCase() || "";
      const document = prof.professionalDocument?.toLowerCase() || "";
      const cpf = prof.cpf?.toLowerCase() || "";
      const term = searchTerm.toLowerCase();
      const matchesSearch = name.includes(term) || document.includes(term) || cpf.includes(term);
      const matchesArea = areaFilter === "all" || prof.serviceArea.area === areaFilter;
      return matchesSearch && matchesArea;
    });
  }, [professionals, searchTerm, areaFilter]);

  const uniqueAreas = useMemo(() => [
    "all",
    ...Array.from(new Set(professionals.map((p) => p.serviceArea.area))),
  ], [professionals]);

  return {
    searchTerm, setSearchTerm,
    areaFilter, setAreaFilter,
    statusFilter, setStatusFilter,
    filteredProfessionals,
    uniqueAreas,
  };
}
