import axios from "axios";
import type { LoginPayload, LoginResponse } from "./interfaces";

const API_BASE_URL = "http://localhost:9001/api/auth/signin";

export const login = async (payload: LoginPayload): Promise<LoginResponse> => {
  try {
    const response = await axios.post<LoginResponse>(API_BASE_URL, payload);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      console.error("Erro na resposta do servidor:", error.response.data);
    }
    throw new Error("Falha no login. Verifique suas credenciais.");
  }
};
