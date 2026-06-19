"use server";

import axios, {
  AxiosError,
  AxiosResponse,
  InternalAxiosRequestConfig,
  type AxiosInstance,
} from "axios";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";

const LOCAL_API_BASE_URL = "http://localhost:8090/apae-geral/api";

function trimTrailingSlash(baseURL?: string) {
  return baseURL?.replace(/\/+$/, "");
}

function getBaseApiURL() {
  return (
    trimTrailingSlash(process.env.API_URL) ||
    trimTrailingSlash(process.env.NEXT_PUBLIC_API_URL) ||
    LOCAL_API_BASE_URL
  );
}

function getDocumentsApiBaseURL() {
  const documentsApiURL = trimTrailingSlash(process.env.NEXT_PUBLIC_DOCUMENTS_API_URL);

  if (!documentsApiURL) {
    return getBaseApiURL();
  }

  if (documentsApiURL.includes(":8092")) {
    return getBaseApiURL();
  }

  if (documentsApiURL.endsWith("/documents")) {
    return documentsApiURL.slice(0, -"/documents".length);
  }

  return documentsApiURL;
}

function createAxiosInstance(baseURL: string) {
  return axios.create({
    baseURL,
    headers: {
      "Content-Type": "application/json",
    },
  });
}

function makeInterceptors(api: AxiosInstance) {
  api.interceptors.request.use(
    async (config: InternalAxiosRequestConfig) => {
      const session = (await cookies()).get("session")?.value;

      if (session && config.headers) {
        config.headers.Authorization = `Bearer ${session}`;
      }

      return config;
    },
    (error: AxiosError) => Promise.reject(error),
  );

  api.interceptors.response.use(
    (response: AxiosResponse) => response,
    async (error: AxiosError) => {
      if (error.response?.status === 401) {
        try {
          const cookieStore = await cookies();
          cookieStore.delete("session");
        } catch {
          // Preserve original response error.
        }

        if (typeof window === "undefined") {
          redirect("/auth/login");
        }
      }

      return Promise.reject(error);
    },
  );

  return api;
}

export async function createBaseApi() {
  return makeInterceptors(createAxiosInstance(getBaseApiURL()));
}

export async function createDocumentsAPI(): Promise<AxiosInstance> {
  return makeInterceptors(createAxiosInstance(getDocumentsApiBaseURL()));
}

export async function createDocumentsApi(): Promise<AxiosInstance> {
  return createDocumentsAPI();
}
