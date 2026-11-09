"use client";

import type { DocumentDTO, FullPatientData } from "./annual-registry.types";
import { bffFetch, bffFetchJson } from "@/lib/bff-client";

const BASE_URL = "/apae-geral/api/patients";

export async function fetchDocumentsApi(patientId: string, year: string): Promise<DocumentDTO[]> {
  try {
    return await bffFetchJson<DocumentDTO[]>(`${BASE_URL}/${patientId}/documents?category=MEDICAL&year=${year}`);
  } catch (error) {
    console.error("[fetchDocumentsApi] Erro ao buscar documentos:", error);
    return [];
  }
}

export async function fetchPatientApi(patientId: string): Promise<FullPatientData | null> {
  try {
    return await bffFetchJson<FullPatientData>(`${BASE_URL}/${patientId}`);
  } catch (error) {
    console.error("[fetchPatientApi] Erro ao buscar paciente:", error);
    return null;
  }
}

export async function uploadDocumentApi(patientId: string, formData: FormData): Promise<void> {
  await bffFetch(`${BASE_URL}/${patientId}/documents`, {
    method: "POST",
    body: formData,
  });
}

export async function createAnnualRegistryApi(patientId: string, payload: unknown): Promise<Response> {
  return bffFetch(`${BASE_URL}/${patientId}/annual-registry`, {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function updateAnnualRegistryApi(patientId: string, registryId: string, payload: unknown): Promise<Response> {
  return bffFetch(`${BASE_URL}/${patientId}/annual-registry/${registryId}`, {
    method: "PUT",
    body: JSON.stringify(payload),
  });
}

export async function updatePatientApi(patientId: string, payload: unknown): Promise<void> {
  const res = await bffFetch(`${BASE_URL}/${patientId}`, {
    method: "PUT",
    body: JSON.stringify(payload),
  });
  if (!res.ok) {
    const errorData = await res.json().catch(() => ({}));
    const backendError = errorData?.message;
    const fieldMessage =
      typeof backendError === "object" && Array.isArray(backendError?.fields)
        ? backendError.fields[0]?.message
        : undefined;
    const errorMessage =
      fieldMessage ||
      (typeof backendError === "string" ? backendError : backendError?.message) ||
      "Erro ao atualizar dados do paciente.";
    throw new Error(errorMessage);
  }
}
