"use client";

import type { AnnualRegistry, DocumentDTO, FullPatientData } from "./annual-registry.types";
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
    return await bffFetchJson<FullPatientData>(`${BASE_URL}/${patientId}`, { cache: "no-store" });
  } catch (error) {
    console.error("[fetchPatientApi] Erro ao buscar paciente:", error);
    return null;
  }
}

export async function fetchLatestAnnualRegistryApi(patientId: string): Promise<AnnualRegistry | null> {
  try {
    const years = await bffFetchJson<number[]>(`${BASE_URL}/${patientId}/annual-registry/years-list`, {
      cache: "no-store",
    });
    if (!Array.isArray(years) || years.length === 0) {
      return null;
    }

    const validYears = years
      .filter((year) => year !== null && year !== undefined && String(year).trim() !== "")
      .map(Number)
      .filter((year) => Number.isInteger(year) && year > 0);

    if (validYears.length === 0) {
      return null;
    }

    const latestYear = Math.max(...validYears);
    return await bffFetchJson<AnnualRegistry>(`${BASE_URL}/${patientId}/annual-registry/${latestYear}`, {
      cache: "no-store",
    });
  } catch (error) {
    console.error("[fetchLatestAnnualRegistryApi] Erro ao buscar registro anual mais recente:", error);
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
