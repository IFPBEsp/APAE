"use client";

import { useCallback, useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "react-toastify";
import { validateDocumentFile } from "@/schemas/shared/document.schema";
import { annualRegistryFormSchema, type AnnualRegistryFormValues } from "./annual-registry.schema";
import {
  fetchDocumentsApi,
  fetchPatientApi,
  fetchLatestAnnualRegistryApi,
  uploadDocumentApi,
  createAnnualRegistryApi,
  updateAnnualRegistryApi,
  updatePatientApi,
} from "./annual-registry.api";
import type { DocumentDTO, FullPatientData, AnnualRegistry, ServiceTypeItem } from "./annual-registry.types";

interface UseAnnualRegistryModalParams {
  isOpen: boolean;
  patientId: string;
  currentYear: string;
  initialData: AnnualRegistry | null;
  mode: "create" | "edit";
  onClose: (savedYear?: string) => void;
}

const currentYearInt = new Date().getFullYear();
export const availableYears = Array.from({ length: 32 }, (_, i) =>
  (currentYearInt + 1 - i).toString(),
);

function formatCurrencyForDisplay(value: number | string) {
  if (!value) return "";
  return Number(value).toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

function cleanCurrency(value: string) {
  if (!value) return "0.00";
  return value.replace(/[^\d,]/g, "").replace(",", ".");
}

function cleanPatientData(data: Record<string, unknown>) {
  if (!data) return {};
  const { documents, annualRegistry, createdAt, updatedAt, deleted, isDeleted, age, ...rest } = data;
  return rest;
}

function mapRegistryToFormValues(
  year: string,
  registry: AnnualRegistry | null,
  patient: FullPatientData | null,
): AnnualRegistryFormValues {
  const bpcString =
    registry?.bpc === true || String(registry?.bpc).toLowerCase() === "true"
      ? "true"
      : "false";

  const vaccineList = Array.isArray(patient?.vaccineNames)
    ? patient.vaccineNames.map((v: unknown) => (typeof v === "string" ? { name: v } : v))
    : [];

  const sourceServiceAreas = registry?.serviceAreas ?? [];
  const serviceTypeList = Array.isArray(sourceServiceAreas)
    ? sourceServiceAreas.map((s: ServiceTypeItem) => ({
        id: s.id,
        area: s.area || s.name,
        name: s.name || s.area,
      }))
    : [];

  const medicationValue =
    registry?.continuousMedication ||
    registry?.medications ||
    registry?.medicamentos ||
    "";

  return {
    year,
    bpc: bpcString,
    familyIncome: registry?.familyIncome ? formatCurrencyForDisplay(registry.familyIncome) : "",
    diseases: registry?.diseases ?? "",
    continuousMedication: medicationValue,
    allergies: patient?.allergies ?? "",
    disorders: registry?.disorders || [],
    vaccines: vaccineList,
    serviceTypes: serviceTypeList,
  };
}

export function useAnnualRegistryModal({
  isOpen,
  patientId,
  currentYear,
  initialData,
  mode,
  onClose,
}: UseAnnualRegistryModalParams) {
  const [documents, setDocuments] = useState<DocumentDTO[]>([]);
  const [isInitialDataLoaded, setIsInitialDataLoaded] = useState(false);
  const [isLoadingDocs, setIsLoadingDocs] = useState(false);
  const [isUploading, setIsUploading] = useState(false);
  const [docType, setDocType] = useState("MEDICAL_REPORT");
  const [fullPatientData, setFullPatientData] = useState<FullPatientData | null>(null);

  const isLoadingInitialData = isOpen && !isInitialDataLoaded;

  const form = useForm<AnnualRegistryFormValues>({
    resolver: zodResolver(annualRegistryFormSchema),
    mode: "onChange",
    defaultValues: {
      year: currentYear || currentYearInt.toString(),
      bpc: "false",
      familyIncome: "",
      diseases: "",
      continuousMedication: "",
      disorders: [],
      allergies: "",
      vaccines: [],
      serviceTypes: [],
    },
  });

  const { reset } = form;

  const loadDocuments = useCallback(async () => {
    setIsLoadingDocs(true);
    try {
      const data = await fetchDocumentsApi(patientId, currentYear);
      setDocuments(data);
    } catch (error) {
      console.error(error);
    } finally {
      setIsLoadingDocs(false);
    }
  }, [patientId, currentYear]);

  useEffect(() => {
    if (!isOpen || !patientId) {
      setIsInitialDataLoaded(false);
      setFullPatientData(null);
      return;
    }

    let isCancelled = false;
    setIsInitialDataLoaded(false);

    if (mode === "edit") {
      loadDocuments();
    } else {
      setDocuments([]);
    }

    const loadInitialData = async () => {
      try {
        const [patientData, latestRegistry] = await Promise.all([
          fetchPatientApi(patientId),
          mode === "create" ? fetchLatestAnnualRegistryApi(patientId) : Promise.resolve(null),
        ]);

        if (isCancelled) return;

        setFullPatientData(patientData);

        const registrySource = mode === "edit" ? initialData : latestRegistry;
        const targetYear = mode === "create" ? currentYearInt.toString() : currentYear;

        reset(mapRegistryToFormValues(targetYear, registrySource, patientData));
      } finally {
        if (!isCancelled) {
          setIsInitialDataLoaded(true);
        }
      }
    };

    loadInitialData();

    return () => {
      isCancelled = true;
    };
  }, [isOpen, patientId, mode, currentYear, initialData, loadDocuments, reset]);

  const handleFileUpload = async (file: File | null) => {
    if (!file) return;
    if (validateDocumentFile(file)) return;
    setIsUploading(true);
    const formData = new FormData();
    formData.append("file", file);
    formData.append("category", "MEDICAL");
    formData.append("type", docType);
    formData.append("year", currentYear);
    try {
      await uploadDocumentApi(patientId, formData);
      toast.success("Documento anexado!");
      await loadDocuments();
    } catch (error) {
      console.error("[handleFileUpload] Erro ao enviar documento:", error);
      toast.error("Erro ao enviar documento.");
    } finally {
      setIsUploading(false);
    }
  };

  const handleMoneyChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    onChange: (value: string) => void,
  ) => {
    const value = e.target.value.replace(/\D/g, "");
    if (value === "") { onChange(""); return; }
    onChange((parseFloat(value) / 100).toLocaleString("pt-BR", { style: "currency", currency: "BRL" }));
  };

  const onSubmit = async (data: AnnualRegistryFormValues) => {
    if (isLoadingInitialData) return;
    toast.dismiss();
    try {
      const registryId = initialData?.id;
      const income = parseFloat(cleanCurrency(data.familyIncome));

      const regPayload = {
        bpc: data.bpc === "true",
        familyIncome: income,
        diseases: data.diseases || "Nenhuma",
        continuousMedication: data.continuousMedication || "Nenhum",
        medications: data.continuousMedication || "Nenhum",
        medicamentos: data.continuousMedication || "Nenhum",
        disorders: (data.disorders || []).map((d: Record<string, unknown>) => ({ name: d.name || d.label || d.value, id: d.id })),
        serviceArea: (data.serviceTypes || []).map((s: Record<string, unknown>) => ({ id: s.id, area: s.area || s.name || s.label })),
        serviceAreas: (data.serviceTypes || []).map((s: Record<string, unknown>) => ({ id: s.id, area: s.area || s.name || s.label })),
        ano: parseInt(data.year),
        year: parseInt(data.year),
      };

      let regRes: Response;
      if (mode === "create") {
        regRes = await createAnnualRegistryApi(patientId, regPayload);
      } else {
        if (!registryId) throw new Error("ID do registro não encontrado.");
        regRes = await updateAnnualRegistryApi(patientId, registryId, regPayload);
      }

      if (!regRes.ok) {
        const errorData = await regRes.json().catch(() => ({}));
        const details = errorData.details || "";
        const message = errorData.message || "";

        if (regRes.status === 409 || details.includes("Conflito") || details.includes("já existe") || message.includes("já existe")) {
          form.setError("year", { type: "manual", message: "Este ano já possui um registro cadastrado." });
          throw new Error(`O ano ${data.year} já possui um registro. Escolha outro ano.`);
        }
        throw new Error("Erro ao salvar registro no servidor.");
      }

      if (fullPatientData) {
        const vaccineList = (data.vaccines || []).map((v: Record<string, unknown>) => ({ name: v.name || v.label || v.value, id: v.id }));
        const baseData = cleanPatientData(fullPatientData as Record<string, unknown>);
        await updatePatientApi(patientId, {
          ...baseData,
          nationality: baseData.nationality || baseData.birthplace || "",
          allergies: data.allergies || "Nenhuma",
          vaccineNames: vaccineList,
          continuousMedication: data.continuousMedication || "Nenhum",
        });
      }

      onClose(mode === "create" ? data.year : undefined);
      toast.success(mode === "create" ? "Registro criado com sucesso!" : "Alterações salvas!");
    } catch (error: unknown) {
      console.error("[onSubmit] Erro ao salvar registro anual:", error);
      const message = error instanceof Error ? error.message : "Erro ao salvar.";
      toast.error(message);
    }
  };

  return {
    form,
    documents,
    isLoadingInitialData,
    isLoadingDocs,
    isUploading,
    docType,
    setDocType,
    handleFileUpload,
    handleMoneyChange,
    onSubmit,
    loadDocuments,
  };
}
