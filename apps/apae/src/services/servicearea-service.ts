
"use client";

import { bffFetchJson } from "@/lib/bff-client";

export async function getAllServiceAreas<T>() {
  return bffFetchJson<T>(`/apae-geral/api/service-areas`, { method: "GET" });
}

export async function createServiceArea<T>(area: string) {
  return bffFetchJson<T>(`/apae-geral/api/service-areas`, {
    method: "POST",
    body: JSON.stringify({ area }),
  });
}