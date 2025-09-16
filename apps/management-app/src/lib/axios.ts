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

export const createAuthAPI = async () => {
  const api = createAxiosInstance(
    process.env.NEXT_PUBLIC_API_URL_AUTH || "http://localhost:8091/api/auth"
  );

  return makeInterceptors(api);
};

export const createDocumentsAPI = async () => {
  const api = createAxiosInstance(
    process.env.NEXT_PUBLIC_API_URL_DOCUMENTS ||
      "http://localhost:8092/api/documents"
  );

  return makeInterceptors(api);
};

export const createPersonApi = async () => {
  const api_pessoas = createAxiosInstance(
    process.env.NEXT_PUBLIC_API_URL_PERSON || "http://localhost:8090/pessoas"
  );

  return makeInterceptors(api_pessoas);
};
