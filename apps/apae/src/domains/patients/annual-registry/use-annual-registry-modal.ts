"use client";

import { useEffect, useRef, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "react-toastify";
import { annualRegistryFormSchema, type AnnualRegistryFormValues } from "./annual-registry.schema";
import {
  fetchDocumentsApi,
  fetchPatientApi,
  uploadDocumentApi,
  createAnnualRegistryApi,
  updateAnnualRegistryApi,
  updatePatientApi,
} from "./annual-registry.api";
import type {
  DocumentDTO,
  FullPatientData,
  AnnualRegistry,
  ServiceAreaItem,
} from "./annual-registry.types";

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
  const { documents, annualRegistry, createdAt, updatedAt, deleted, isDeleted, age, ...rest } =
    data;
  return rest;
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
  const [isLoadingDocs, setIsLoadingDocs] = useState(false);
  const [isUploading, setIsUploading] = useState(false);
  const [docType, setDocType] = useState("MEDICAL_REPORT");
  const [fullPatientData, setFullPatientData] = useState<FullPatientData | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);

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

  const loadDocuments = async () => {
    setIsLoadingDocs(true);
    try {
      const data = await fetchDocumentsApi(patientId, currentYear);
      setDocuments(data);
    } catch (error) {
      console.error(error);
    } finally {
      setIsLoadingDocs(false);
    }
  };

  const loadPatientData = async () => {
    const data = await fetchPatientApi(patientId);
    if (data) setFullPatientData(data);
  };

  useEffect(() => {
    if (isOpen && patientId) {
      if (mode === "edit") loadDocuments();
      else setDocuments([]);
      loadPatientData();
    }
  }, [isOpen, patientId, mode]);

  useEffect(() => {
    if (!isOpen) return;

    if (mode === "edit" && initialData) {
      const bpcString =
        initialData.bpc === true || String(initialData.bpc) === "true" ? "true" : "false";

      const vaccineList = Array.isArray(fullPatientData?.vaccineNames)
        ? fullPatientData.vaccineNames.map((v: unknown) =>
            typeof v === "string" ? { name: v } : v,
          )
        : [];

      const sourceServiceAreas =
        initialData.serviceArea || initialData.serviceAreas || initialData.serviceTypes || [];
      const serviceTypeList = Array.isArray(sourceServiceAreas)
        ? sourceServiceAreas.map((s: ServiceAreaItem) => ({
            id: s.id,
            area: s.area || s.name,
            name: s.name || s.area,
          }))
        : [];

      const medicationValue =
        initialData.continuousMedication ||
        initialData.medications ||
        initialData.medicamentos ||
        "";

      form.reset({
        year: currentYear,
        bpc: bpcString,
        familyIncome: initialData.familyIncome
          ? formatCurrencyForDisplay(initialData.familyIncome)
          : "",
        diseases: initialData.diseases ?? "",
        continuousMedication: medicationValue,
        allergies: fullPatientData?.allergies ?? "",
        disorders: initialData.disorders || [],
        vaccines: vaccineList,
        serviceTypes: serviceTypeList,
      });
    } else if (mode === "create") {
      const vaccineList = Array.isArray(fullPatientData?.vaccineNames)
        ? fullPatientData.vaccineNames.map((v: unknown) =>
            typeof v === "string" ? { name: v } : v,
          )
        : [];

      const existingMedication =
        fullPatientData?.additionals?.medications ||
        (fullPatientData as Record<string, unknown>)?.continuousMedication ||
        "";

      const existingDiseases =
        fullPatientData?.additionals?.diseases ||
        (fullPatientData as Record<string, unknown>)?.diseases ||
        "";

      form.reset({
        year: currentYearInt.toString(),
        bpc: "false",
        familyIncome: "",
        diseases: existingDiseases as string,
        continuousMedication: existingMedication as string,
        allergies: fullPatientData?.allergies ?? "",
        disorders: [],
        vaccines: vaccineList,
        serviceTypes: [],
      });
    }
  }, [initialData, fullPatientData, isOpen, mode, currentYear]);

  const handleFileUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
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
      if (fileInputRef.current) fileInputRef.current.value = "";
    } catch {
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
    if (value === "") {
      onChange("");
      return;
    }
    onChange(
      (parseFloat(value) / 100).toLocaleString("pt-BR", { style: "currency", currency: "BRL" }),
    );
  };

  const onSubmit = async (data: AnnualRegistryFormValues) => {
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
        disorders: (data.disorders || []).map((d: Record<string, unknown>) => ({
          name: d.name || d.label || d.value,
          id: d.id,
        })),
        serviceArea: (data.serviceTypes || []).map((s: Record<string, unknown>) => ({
          id: s.id,
          area: s.area || s.name || s.label,
        })),
        serviceAreas: (data.serviceTypes || []).map((s: Record<string, unknown>) => ({
          id: s.id,
          area: s.area || s.name || s.label,
        })),
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

        if (
          regRes.status === 409 ||
          details.includes("Conflito") ||
          details.includes("já existe") ||
          message.includes("já existe")
        ) {
          form.setError("year", {
            type: "manual",
            message: "Este ano já possui um registro cadastrado.",
          });
          throw new Error(`O ano ${data.year} já possui um registro. Escolha outro ano.`);
        }
        throw new Error("Erro ao salvar registro no servidor.");
      }

      if (fullPatientData) {
        const vaccineList = (data.vaccines || []).map((v: Record<string, unknown>) => ({
          name: v.name || v.label || v.value,
          id: v.id,
        }));
        const baseData = cleanPatientData(fullPatientData as Record<string, unknown>);
        await updatePatientApi(patientId, {
          ...baseData,
          allergies: data.allergies || "Nenhuma",
          vaccineNames: vaccineList,
          continuousMedication: data.continuousMedication || "Nenhum",
        });
      }

      onClose(mode === "create" ? data.year : undefined);
      toast.success(mode === "create" ? "Registro criado com sucesso!" : "Alterações salvas!");
    } catch (error: unknown) {
      const message = error instanceof Error ? error.message : "Erro ao salvar.";
      toast.error(message);
    }
  };

  return {
    form,
    documents,
    isLoadingDocs,
    isUploading,
    docType,
    setDocType,
    fileInputRef,
    handleFileUpload,
    handleMoneyChange,
    onSubmit,
    loadDocuments,
  };
}
