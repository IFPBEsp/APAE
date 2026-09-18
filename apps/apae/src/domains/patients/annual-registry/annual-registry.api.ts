import type { DocumentDTO, FullPatientData } from "./annual-registry.types";

const BASE_URL = "/apae-geral/api/patients";

export async function fetchDocumentsApi(patientId: string, year: string): Promise<DocumentDTO[]> {
  const response = await fetch(`${BASE_URL}/${patientId}/documents?category=MEDICAL&year=${year}`);
  if (!response.ok) return [];
  const data = await response.json().catch(() => []);
  return Array.isArray(data) ? data : [];
}

export async function fetchPatientApi(patientId: string): Promise<FullPatientData | null> {
  const response = await fetch(`${BASE_URL}/${patientId}`);
  if (!response.ok) return null;
  return response.json();
}

export async function uploadDocumentApi(patientId: string, formData: FormData): Promise<void> {
  const response = await fetch(`${BASE_URL}/${patientId}/documents`, {
    method: "POST",
    body: formData,
  });
  if (!response.ok) throw new Error("Erro ao enviar documento.");
}

export async function createAnnualRegistryApi(patientId: string, payload: unknown): Promise<Response> {
  return fetch(`${BASE_URL}/${patientId}/registro-anual`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
}

export async function updateAnnualRegistryApi(patientId: string, registryId: string, payload: unknown): Promise<Response> {
  return fetch(`${BASE_URL}/${patientId}/registro-anual/${registryId}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
}

export async function updatePatientApi(patientId: string, payload: unknown): Promise<void> {
  await fetch(`${BASE_URL}/${patientId}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
}
