"use client";

import { bffFetch } from "@/lib/bff-client";
import { FormLogin } from "./authSchema";

export async function login(data: FormLogin) {
  const response = await bffFetch("/apae-geral/api/auth/login", {
    method: "POST",
    body: JSON.stringify(data),
    redirectOnUnauthorized: false,
  });

  return response;
}

const API_URL =
  process.env.NEXT_PUBLIC_API_URL ||
  "http://localhost:8090/apae-geral/api";

export async function requestPasswordRecovery(email: string) {
  const response = await fetch(`${API_URL}/auth/password-recovery/request`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email }),
  });
  return response;
}

export async function resetPassword(data: {
  token: string;
  newPassword: string;
  confirmPassword: string;
}) {
  const response = await fetch(`${API_URL}/auth/password-recovery/reset`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  return response;
}