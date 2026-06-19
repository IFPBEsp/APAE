import { useState, useMemo } from "react";

type StatusFilter = "activate" | "inactivate";

interface Professional {
  id: string;
  name: string;
  professionalDocument?: string;
  serviceType: { name: string };
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
      const term = searchTerm.toLowerCase();
      const matchesSearch = name.includes(term) || document.includes(term);
      const matchesArea = areaFilter === "all" || prof.serviceType.name === areaFilter;
      return matchesSearch && matchesArea;
    });
  }, [professionals, searchTerm, areaFilter]);

  const uniqueAreas = useMemo(() => [
    "all",
    ...Array.from(new Set(professionals.map((p) => p.serviceType.name))),
  ], [professionals]);

  return {
    searchTerm, setSearchTerm,
    areaFilter, setAreaFilter,
    statusFilter, setStatusFilter,
    filteredProfessionals,
    uniqueAreas,
  };
}
