import { useCallback, useEffect, useRef, useState } from "react";
import type { PaginationInfo, PatientWithAbsences } from "@/domains/absences/types/absences.types";
import { DashboardOverview } from "@/domains/dashboard/dashboard.types";
import { justifyAbsence, uploadJustificationDocument } from "@/domains/absences/absences.api";
import { bffFetchJson } from "@/lib/bff-client";
import { Page } from "@/types/pagination";

export function useAbsencePage() {
  const [searchName, setSearchName] = useState("");
  const [patientsWithAbsences, setPatientsWithAbsences] = useState<PatientWithAbsences[]>([]);
  const [loading, setLoading] = useState(true);
  const [expandedPatient, setExpandedPatient] = useState<string | null>(null);
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
  const [justifyingAbsence, setJustifyingAbsence] = useState<{ id: string; patientId: string } | null>(null);
  const [justificationText, setJustificationText] = useState("");
  const [isSubmittingJustification, setIsSubmittingJustification] = useState(false);
  const [file, setFile] = useState<File | null>(null);
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const fetchData = useCallback(async (page: number, name: string) => {
    try {
      setLoading(true);
      const queryParams = new URLSearchParams({
        minAbsences: "3",
        page: page.toString(),
        size: pagination.itemsPerPage.toString(),
        name: name.trim(),
      });

      const [patientsData, statsData] = await Promise.all([
        bffFetchJson<Page<PatientWithAbsences>>(`/apae-geral/api/patients/with-absences?${queryParams}`),
        bffFetchJson<DashboardOverview>("/apae-geral/api/dashboard/overview?minAbsences=3"),
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
  }, [pagination.itemsPerPage]);

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

  const handleJustifyAbsence = async () => {
    if (!justifyingAbsence || !justificationText.trim()) return;
    try {
      setIsSubmittingJustification(true);
      let documentId: string | null = null;

      if (file) {
        documentId = await uploadJustificationDocument(justifyingAbsence.patientId, file);
      }

      await justifyAbsence(justifyingAbsence.id, justificationText, documentId);
      window.location.reload();
    } catch (error: any) {
      console.error("Erro ao justificar falta:", error);
      alert(error.message || "Erro ao justificar falta.");
    } finally {
      setIsSubmittingJustification(false);
      setJustifyingAbsence(null);
      setJustificationText("");
      setFile(null);
    }
  };

  return {
    searchName, setSearchName,
    patientsWithAbsences,
    loading,
    expandedPatient, setExpandedPatient,
    pagination, fetchData,
    statistics,
    justifyingAbsence, setJustifyingAbsence,
    justificationText, setJustificationText,
    isSubmittingJustification,
    file, setFile,
    fileInputRef,
    handleDownload,
    handleJustifyAbsence,
  };
}
