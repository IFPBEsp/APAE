import { useCallback, useEffect, useState } from "react";
import { DashboardOverview } from "@/domains/dashboard/dashboard.types";
import { PaginationInfo, PatientWithAbsences } from "@/types/absence";

export function useAbsencesList() {
  const [searchName, setSearchName] = useState("");
  const [patientsWithAbsences, setPatientsWithAbsences] = useState<
    PatientWithAbsences[]
  >([]);
  const [loading, setLoading] = useState(true);
  const [pagination, setPagination] = useState<PaginationInfo>({
    currentPage: 0,
    totalPages: 1,
    totalItems: 0,
    itemsPerPage: 6,
  });
  const [statistics, setStatistics] = useState<DashboardOverview>({
    totalPatients: 0,
    totalPatientsWithAbsences: 0,
  });

  const fetchData = useCallback(
    async (page: number, name: string) => {
      try {
        setLoading(true);
        const queryParams = new URLSearchParams({
          minAbsences: "3",
          page: page.toString(),
          size: pagination.itemsPerPage.toString(),
          name: name.trim(),
        });

        const [patientsRes, statsRes] = await Promise.all([
          fetch(`/apae-geral/api/patients/with-absences?${queryParams}`),
          fetch("/apae-geral/api/dashboard/overview?minAbsences=3"),
        ]);

        if (!patientsRes.ok || !statsRes.ok) {
          throw new Error("Erro ao buscar dados");
        }

        const patientsData = await patientsRes.json();
        const statsData: DashboardOverview = await statsRes.json();

        setPatientsWithAbsences(patientsData.content);
        setStatistics({
          totalPatients: statsData.totalPatients,
          totalPatientsWithAbsences: statsData.totalPatientsWithAbsences,
        });
        setPagination((prev) => ({
          ...prev,
          currentPage: page,
          totalItems: patientsData.totalElements,
          totalPages: patientsData.totalPages,
        }));
      } catch (error) {
        console.error("Error fetching data:", error);
      } finally {
        setLoading(false);
      }
    },
    [pagination.itemsPerPage],
  );

  useEffect(() => {
    const timer = setTimeout(() => {
      fetchData(0, searchName);
    }, 300);
    return () => clearTimeout(timer);
  }, [searchName, fetchData]);

  return {
    searchName,
    setSearchName,
    patientsWithAbsences,
    loading,
    pagination,
    statistics,
    fetchData,
  };
}
