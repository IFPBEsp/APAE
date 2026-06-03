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
          redirect("/apae-geral/auth/login");
        }
      }

      return Promise.reject(error);
    }
  );

  return api;
};

export const createDocumentsAPI = async () => {
  const api = createAxiosInstance(
    process.env.NEXT_PUBLIC_DOCUMENTS_API_URL ||
      "http://localhost:8092/apae-geral/api/documents"
  );
  return makeInterceptors(api);
};

export const createBaseApi = async () => {
  // Esta função roda no servidor ("use server"). Em produção/container o
  // backend é alcançado por uma URL absoluta (API_URL), pois NEXT_PUBLIC_API_URL
  // é relativa (/apae-geral/api) e o axios server-side não resolve URL relativa.
  const api = createAxiosInstance(
    process.env.API_URL ||
      process.env.NEXT_PUBLIC_API_URL ||
      "http://localhost:8090/apae-geral/api"
  );

  return makeInterceptors(api);
};