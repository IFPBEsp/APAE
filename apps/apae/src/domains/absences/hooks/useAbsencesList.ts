import { useCallback, useEffect, useState } from "react";
import type {
  PaginationInfo,
  PatientWithAbsences,
} from "@/domains/absences/types/absences.types";
import { DashboardOverview } from "@/domains/dashboard/dashboard.types";
import { bffFetchJson } from "@/lib/bff-client";
import { Page } from "@/types/pagination";

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

        const [patientsData, statsData] = await Promise.all([
          bffFetchJson<Page<PatientWithAbsences>>(
            `/apae-geral/api/patients/with-absences?${queryParams}`,
          ),
          bffFetchJson<DashboardOverview>(
            "/apae-geral/api/dashboard/overview?minAbsences=3",
          ),
        ]);

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

  const handleDownload = async (patientId: string, documentName: string) => {
    try {
      const data = await bffFetchJson<{ url: string }>(
        `/apae-geral/api/patients/${patientId}/documents/download?name=${encodeURIComponent(documentName)}`,
      );
      window.open(data.url, "_blank", "noopener,noreferrer");
    } catch (error) {
      console.error("Erro ao baixar documento:", error);
    }
  };

  return {
    searchName,
    setSearchName,
    patientsWithAbsences,
    loading,
    pagination,
    fetchData,
    statistics,
    handleDownload,
  };
}
