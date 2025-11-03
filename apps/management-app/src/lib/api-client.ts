"use client"

import { AxiosInstance, InternalAxiosRequestConfig, AxiosError, AxiosResponse } from 'axios';
import Cookies from 'js-cookie';
import { createAxiosInstance } from './axios';


function getClientToken(): string | null {
  const sessionCookie = Cookies.get("session");
  if (!sessionCookie) return null;
  
  try {
    const sessionData = JSON.parse(sessionCookie);
    return sessionData.accessToken || null;
  } catch (error) {
    console.error("Erro ao parsear cookie de sessão no cliente:", error);
    return null;
  }
}

function removeClientSession() {
  Cookies.remove("session");
}

const makeClientInterceptors = (api: AxiosInstance) => {
  api.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
      const token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcGFlLXVzZXIiLCJzdWIiOiJscmlja2VsbWVAZ21haWwuY29tIiwiZXhwIjoxNzYyMjg1MjIwfQ.7lpiNe--BJ6N4TyQM7PTDWZvtDrcH9iou-HSY0OC9DU"; // Síncrono
      console.log("Token obtido do cookie (cliente):", token);
      if (token && config.headers) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    }
  );

  api.interceptors.response.use(
    (response) => response,
    (error: AxiosError) => {
      if (error.response?.status === 401) {
        console.warn("Token expirado ou inválido (cliente). Redirecionando...");
        removeClientSession();
        window.location.href = '/auth/login';
      }
      return Promise.reject(error);
    }
  );

  return api;
};


export const createClientApi = () => {
  const api = createAxiosInstance(
    process.env.NEXT_PUBLIC_API || "http://localhost:8090/api"
  );
  return makeClientInterceptors(api);
};