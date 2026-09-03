import axios from "axios";

export const clientApi = axios.create({
  headers: {
    "Content-Type": "application/json",
  },
});

clientApi.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 && typeof window !== "undefined") {
      window.location.href = "/apae-geral/auth/login";
    }

    return Promise.reject(error);
  },
);