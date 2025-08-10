import axios from "axios";

const BUCKET_API_URL = "http://localhost:8084/bucket";
const DOCUMENT_API_URL = "http://localhost:8081/api/v1/documentos-pessoais";

const TOKEN =
  "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIyQ1pDcTd4LTFnTm9FaGlsdUQ2ZERaRWltSFlVaVhzeDExVEtzQ205VURvIn0.eyJleHAiOjE3NTQ4NDkwODEsImlhdCI6MTc1NDg0ODc4MSwianRpIjoib25ydHJvOjYwM2UzOWQ5LTI0MmQtYjY5MS1jODM1LThmNGM0MDljNzcyNSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvb3JnLWFwYWUiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiYzA5M2NiODUtNTUwNS00ZmNjLWJmMTgtMTllY2JmMDIzNTE1IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiY2xpZW50LWFwYWUiLCJzaWQiOiI3NjkyOGUxMy0wNmM0LTQ5NzMtOWU0Mi04Y2RhNTY1ZTRkNzAiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLW9yZy1hcGFlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkpvaG4gRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiZGVtb19hZG1pbiIsImdpdmVuX25hbWUiOiJKb2huIiwiZmFtaWx5X25hbWUiOiJEb2UiLCJlbWFpbCI6InRlc3RlQHRlc3RlLmNvbSJ9.a1vcEL1km6PmvmhW9oOueUq3WKbdDX3_eHGaGLfJq17B6rubB_MoWLUeJ0JPo77XeVNM1IgXLFF8gIiqPAhv3_QgZFvUV6fXEjSFWNfXvpr05tEhsXdyixfBfOKt9C2kG4Cof2SHoJ2xXeWNUVRM6j_evy32EkoIwPghUGhMYakxc6fmPGotFnQwmo7LFPVMu1wk4P59vRLrNvfBWITRQ09p26QbsQkP8QMS0jT7rnXNaRVi1qKiXHveUA_UgSoBfKmJqoJZ--zTM0EgU_WxPaUXV0FT4_HDEWHt8kHRq-7ruCXz2PFnRVXX0PeyrmadrfE7EJn-S7QPwUkLaQRZig";

const bucketApi = axios.create({
  baseURL: BUCKET_API_URL,
});

bucketApi.interceptors.request.use(
  (config) => {
    if (TOKEN) {
      config.headers.Authorization = `Bearer ${TOKEN}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

const documentApi = axios.create({
  baseURL: DOCUMENT_API_URL,
});

documentApi.interceptors.request.use(
  (config) => {
    if (TOKEN) {
      config.headers.Authorization = `Bearer ${TOKEN}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

/**
 * @param patientId
 * @returns
 */
export async function verificarBucket(patientId: string): Promise<boolean> {
  const response = await bucketApi.get<boolean>(`/${patientId}/verificar`);
  return response.data;
}

/**
 * @param patientId
 */
export async function criarBucket(patientId: string): Promise<void> {
  await bucketApi.post(`${patientId}`);
}

/**
 * @param patientId
 * @param documentType
 * @param file
 */
export async function anexarDocumento(
  patientId: string,
  documentType: string,
  file: File
): Promise<void> {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("documentType", documentType);

  await documentApi.post(`/${patientId}`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
}
