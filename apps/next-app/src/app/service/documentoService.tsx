import axios from "axios";

const API_BASE_URL = "http://localhost:8081/api/documents";
const TOKEN = process.env.NEXT_PUBLIC_JWT_TOKEN;

export interface DocumentObjectRequest {
  patientId: string;
  year: number;
  documentCategory: string;
  documentType: string;
}

export interface UploadDocumentParams extends DocumentObjectRequest {
  file: File;
}

export interface DocumentsResponse {
  documents: Array<{
    fileName: string;
    url: string;
    createdAt: string;
  }>;
}

export interface ListDocumentsParams {
  patientId: string;
  category: string;
  year: number;
}

export interface ListDocumentsByTypeParams extends ListDocumentsParams {
  type: string;
}


const api = axios.create({
  baseURL: API_BASE_URL,
});

api.interceptors.request.use(
  (config) => {
    if (TOKEN) {
      config.headers.Authorization = `Bearer ${TOKEN}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export async function createBucket(patientId: string): Promise<void> {
  await api.post(`/bucket/${patientId}`);
}

export async function deleteBucket(patientId: string): Promise<void> {
  await api.delete(`/bucket/${patientId}`);
}

export async function uploadDocument(params: UploadDocumentParams): Promise<void> {
  const { file, ...documentDto } = params;
  const formData = new FormData();

  const documentBlob = new Blob([JSON.stringify(documentDto)], {
    type: "application/json",
  });

  formData.append("document", documentBlob);
  formData.append("file", file, file.name);

  await api.post("/upload", formData); // NÃO definir Content-Type aqui!
}

export async function listDocuments(params: ListDocumentsParams): Promise<DocumentsResponse> {
  const { patientId, ...queryParams } = params;
  const response = await api.get<DocumentsResponse>(`/${patientId}`, {
    params: queryParams,
  });
  return response.data;
}

export async function listDocumentsByType(params: ListDocumentsByTypeParams): Promise<DocumentsResponse> {
  const { patientId, ...queryParams } = params;
  const response = await api.get<DocumentsResponse>(`/${patientId}/type`, {
    params: queryParams,
  });
  return response.data;
}

export async function getDocumentHistory(
  patientId: string,
  category: string,
  type: string
): Promise<DocumentsResponse> {
  const response = await api.get<DocumentsResponse>(`/${patientId}/history`, {
    params: { category, type },
  });
  return response.data;
}

export async function viewDocumentAsImage(patientId: string, path: string): Promise<Blob> {
  const response = await api.get(`/${patientId}/view`, {
    params: { path },
    responseType: 'blob',
  });
  return response.data;
}

export async function deleteDocument(patientId: string, fileName: string): Promise<void> {
  await api.delete(`/${patientId}/delete`, {
    params: { fileName },
  });
}