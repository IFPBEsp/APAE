"use server";

import {
  AxiosError,
  AxiosInstance,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from "axios";
import { redirect } from "next/navigation";
import { getTokenFromCookie, removeSessionCookie } from "./cookies";
import { createAxiosInstance } from "./axios";

const makeServerInterceptors = (api: AxiosInstance) => {
  api.interceptors.request.use(
    async (config: InternalAxiosRequestConfig) => {
      const token = await getTokenFromCookie();
      console.log("Token obtido do cookie (servidor):", token);
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
        console.warn("Token expirado ou inválido (servidor). Removendo sessão...");
        await removeSessionCookie();
        redirect('/auth/login'); 
      }
      return Promise.reject(error);
    }
  );

  return api;
};


export const createServerApi = async () => {
  const api = createAxiosInstance(
    process.env.NEXT_PUBLIC_API || "http://localhost:8090/api"
  );
  return makeServerInterceptors(api);
};