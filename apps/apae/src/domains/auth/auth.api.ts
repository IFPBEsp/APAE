import axios from "axios";
import { FormLogin } from "./authSchema";

const API_URL =
  process.env.NEXT_PUBLIC_API_URL ||
  "http://localhost:8090/apae-geral/api";

export async function login(data: FormLogin) {
  const response = await fetch("/apae-geral/api/auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  return response;
}

export async function requestPasswordRecovery(email: string) {
  return axios.post(
    `${API_URL}/auth/password-recovery/request`,
    { email }
  );
}

export async function resetPassword(data: {
  token: string;
  newPassword: string;
  confirmPassword: string;
}) {
  return axios.post(
    `${API_URL}/auth/password-recovery/reset`,
    data
  );
}