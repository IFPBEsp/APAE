import axios from "axios";

// precisa colocar a url correta
const API_BASE_URL = "http://localhost:8081/api/documents";

const TOKEN = process.env.NEXT_PUBLIC_JWT_TOKEN;

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
  (error) => {
    return Promise.reject(error);
  }
);

export interface DocumentsResponse {
    documents: Array<{
    fileName: string;
    url: string;
    createdAt: string;
  }>;
}

export async function listFilteredDocuments(
  patientId: string,
  category: string,
  year: number,
  type?: string
): Promise<DocumentsResponse> {
  const params: any = {
    category,
    year,
  };
  if (type) params.type = type;

  const endpoint = type ? `/${patientId}/type` : `/${patientId}`;

  const response = await api.get<DocumentsResponse>(endpoint, {
    params,
  });

  return response.data;
}

//Visualizar documento
export async function viewDocumentAsImage(patientId: string, path: string): Promise<string> {
  const response = await api.get<Blob>(`/${patientId}/view`, {
    params: { path },
    responseType: "blob",
  });

  return URL.createObjectURL(response.data);
}
