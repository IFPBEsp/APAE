import { getApiBaseUrl } from "@/lib/client-service";

const API_URL = getApiBaseUrl();

export async function getAllServiceAreas() {
  return fetch(`${API_URL}/service-areas`, { method: "GET" });
}

export async function createServiceArea(area: string) {
  return fetch(`${API_URL}/service-areas`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ area }),
  });
}
