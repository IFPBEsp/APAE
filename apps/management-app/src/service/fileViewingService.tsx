import axios from "axios";
import { AxiosRequestConfig } from "axios";

//const TOKEN = process.env.NEXT_PUBLIC_JWT_TOKEN;

const api = axios.create({
  baseURL: "/api/documents",
});

/*api.interceptors.request.use(
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
*/

interface Document {
  fileName: string;
  url: string;
  createdAt: string;
}

export interface DocumentsResponse {
  documents: Document[];
  patientId: string;
  urls: Array<{
    fileName: string;
    link: string;
  }>;
}

export async function listFilteredDocuments(
  patientId: string,
  category: string,
  year: number,
  type: string,
  token: string
): Promise<DocumentsResponse> {
  const params: Record<string, string | number> = { category, year };
  if (type) params.type = type;

  const endpoint = type ? `/${patientId}/type` : `/${patientId}`;

  const config: AxiosRequestConfig = {
    params,
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };

  try {
    const response = await api.get<DocumentsResponse>(endpoint, config);
    return response.data;

  } catch (error) {
    console.error("Erro ao buscar documentos:", error);
    throw new Error("Erro ao buscar documentos.");
  }
}

