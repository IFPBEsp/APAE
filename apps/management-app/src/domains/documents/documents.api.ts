import type {
  ListPatientDocumentsParams,
  PatientDocument,
  UploadPatientDocumentParams,
} from "./documents.types";

async function parseResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.message || "Erro ao processar documentos.");
  }

  return response.json();
}

export async function listPatientDocuments(
  patientId: string,
  params: ListPatientDocumentsParams,
) {
  const searchParams = new URLSearchParams({
    category: params.category,
  });

  if (params.year) {
    searchParams.set("year", String(params.year));
  }

  if (params.type) {
    searchParams.set("type", params.type);
  }

  const response = await fetch(`/api/patients/${patientId}/documents?${searchParams.toString()}`);
  return parseResponse<PatientDocument[]>(response);
}

export async function uploadPatientDocument(
  patientId: string,
  params: UploadPatientDocumentParams,
) {
  const formData = new FormData();
  formData.append("file", params.file);
  formData.append("category", params.category);
  formData.append("type", params.type);

  if (params.year) {
    formData.append("year", String(params.year));
  }

  const response = await fetch(`/api/patients/${patientId}/documents`, {
    method: "POST",
    body: formData,
  });

  return parseResponse<PatientDocument>(response);
}
