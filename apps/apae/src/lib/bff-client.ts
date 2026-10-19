"use client";

import { endSession } from "./session";

export interface BffFetchOptions extends RequestInit {
  redirectOnUnauthorized?: boolean;
}

function isFormData(body: unknown): body is FormData {
  return typeof FormData !== "undefined" && body instanceof FormData;
}

export async function bffFetch(
  path: string,
  init: BffFetchOptions = {},
): Promise<Response> {
  const { redirectOnUnauthorized = true, ...fetchInit } = init;

  const headers: Record<string, string> = {
    ...fetchInit.headers as Record<string, string>,
  };

  if (!isFormData(fetchInit.body)) {
    headers["Content-Type"] = "application/json";
  }

  const response = await fetch(path, {
    ...fetchInit,
    headers,
  });

  if (response.status === 401 && redirectOnUnauthorized) {
    await endSession();
  }

  return response;
}

export async function bffFetchJson<T>(
  path: string,
  init: BffFetchOptions = {},
): Promise<T> {
  const response = await bffFetch(path, init);

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    const error = new Error(
      errorData.message || `HTTP error ${response.status}`,
    ) as Error & { status: number; data: unknown };
    error.status = response.status;
    error.data = errorData;
    throw error;
  }

  return response.json();
}