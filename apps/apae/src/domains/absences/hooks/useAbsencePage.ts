import { useCallback, useEffect, useRef, useState } from "react";
import { PatientWithAbsences } from "@/domains/absences/types/absences.types";
import { DashboardOverview } from "@/domains/dashboard/dashboard.types";
import { justifyAbsence } from "@/domains/absences/absences.api";

interface PaginationInfo {
  currentPage: number;
  totalPages: number;
  totalItems: number;
  itemsPerPage: number;
}

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

      const [patientsRes, statsRes] = await Promise.all([
        fetch(`/apae-geral/api/patients/with-absences?${queryParams}`),
        fetch("/apae-geral/api/dashboard/overview?minAbsences=3"),
      ]);

      if (!patientsRes.ok || !statsRes.ok) throw new Error("Erro ao buscar dados");

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
  }, [pagination.itemsPerPage]);

  useEffect(() => {
    const timer = setTimeout(() => {
      fetchData(0, searchName);
    }, 300);
    return () => clearTimeout(timer);
  }, [searchName, fetchData]);

  const formatDate = (dateString: string) =>
    new Date(dateString + "Z").toLocaleDateString("pt-BR", { timeZone: "UTC" });

  const handleDownload = async (patientId: string, documentName: string) => {
    try {
      const res = await fetch(
        `/apae-geral/api/patients/${patientId}/documents/download?name=${encodeURIComponent(documentName)}`,
      );
      if (!res.ok) throw new Error("Erro ao buscar URL");
      const data = await res.json();
      window.open(data.url, "_blank");
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
        const docFormData = new FormData();
        docFormData.append("file", file);
        docFormData.append("category", "ABSENCE");
        docFormData.append("type", "ATTACHMENTANY");
        docFormData.append("year", String(new Date().getFullYear()));

        const docResponse = await fetch(
          `/apae-geral/api/patients/${justifyingAbsence.patientId}/documents`,
          { method: "POST", body: docFormData },
        );

        if (!docResponse.ok) {
          const errorData = await docResponse.json();
          throw new Error(errorData.message || "Erro ao fazer upload do documento.");
        }

        const document = await docResponse.json();
        documentId = document.name;
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
    formatDate,
    handleDownload,
    handleJustifyAbsence,
  };
}