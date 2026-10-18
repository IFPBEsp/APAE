"use client";

import type { DocumentDTO, FullPatientData } from "./annual-registry.types";
import { bffFetch, bffFetchJson } from "@/lib/bff-client";

const BASE_URL = "/apae-geral/api/patients";

export async function fetchDocumentsApi(patientId: string, year: string): Promise<DocumentDTO[]> {
  try {
    return await bffFetchJson<DocumentDTO[]>(`${BASE_URL}/${patientId}/documents?category=MEDICAL&year=${year}`);
  } catch {
    return [];
  }
}

export async function fetchPatientApi(patientId: string): Promise<FullPatientData | null> {
  try {
    return await bffFetchJson<FullPatientData>(`${BASE_URL}/${patientId}`);
  } catch {
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
  return bffFetch(`${BASE_URL}/${patientId}/registro-anual`, {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function updateAnnualRegistryApi(patientId: string, registryId: string, payload: unknown): Promise<Response> {
  return bffFetch(`${BASE_URL}/${patientId}/registro-anual/${registryId}`, {
    method: "PUT",
    body: JSON.stringify(payload),
  });
}

export async function updatePatientApi(patientId: string, payload: unknown): Promise<void> {
  await bffFetch(`${BASE_URL}/${patientId}`, {
    method: "PUT",
    body: JSON.stringify(payload),
  });
}
