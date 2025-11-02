"use server";

import axios, {
  AxiosError,
  AxiosInstance,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from "axios";
import { redirect } from "next/navigation";
import { getTokenFromCookie, removeSessionCookie } from "./cookies";

function createAxiosInstance(baseURL: string) {
  return axios.create({
    baseURL,
    headers: {
      "Content-Type": "application/json",
    },
  });
}

const makeInterceptors = (api: AxiosInstance) => {
  api.interceptors.request.use(
    async (config: InternalAxiosRequestConfig) => {
      const token = await getTokenFromCookie();

      if (token && config.headers) {
        config.headers.Authorization = `Bearer ${token}`;
      }

      return config;
    },
    (error: AxiosError) => {
      return Promise.reject(error);
    }
  );

  api.interceptors.response.use(
    (response: AxiosResponse) => {
      return response;
    },
    async (error: AxiosError) => {
      if (error.response?.status === 401) {
        console.warn("Token expirado ou inválido. Removendo sessão...");

        await removeSessionCookie();
        if (typeof window === "undefined") {
          redirect("/auth/login");
        }
      }

      return Promise.reject(error);
    }
  );

  return api;
};

export const createDocumentsAPI = async () => {
  const api = createAxiosInstance("http://localhost:8092/api/documents");

  return makeInterceptors(api);
};

export const createBaseApi = async () => {
  const api = createAxiosInstance(
    process.env.NEXT_PUBLIC_API || "http://localhost:8090/api"
  );

  return makeInterceptors(api);
};

// export const fetchTipoAtendimentoOptions = async (): Promise<string[]> => {
//   const api = await createBaseApi(); 
//   const response = await api.get('/filtros/tipos-atendimento');
//   return response.data;
// };


export const fetchTranstornoOptions = async (): Promise<string[]> => {
  const api = await createBaseApi();
  const response = await api.get('/patients/filtros/transtornos');
  return response.data;
};


export const fetchAnoOptions = async (): Promise<string[]> => {
  const api = await createBaseApi();
  const response = await api.get('/patients/filtros/anos');
  return response.data;
};


export const fetchCidadeOptions = async (): Promise<string[]> => {
  const api = await createBaseApi();
  const response = await api.get('/patients/filtros/cidades');
  return response.data;
};
