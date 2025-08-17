import axios from "axios";
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

export interface DocumentsResponse {
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
  const params: any = { category, year };
  if (type) params.type = type;

  const endpoint = type ? `/${patientId}/type` : `/${patientId}`;
  const response = await api.get<DocumentsResponse>(endpoint, {
    params,
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
}

