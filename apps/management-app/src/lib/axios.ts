"use server";

import axios, {
  AxiosError,
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

export const createAuthAPI = async () => {
  const api = createAxiosInstance(
    process.env.NEXT_PUBLIC_API_URL_AUTH || "http://localhost:9999/api/auth/"
  );

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
