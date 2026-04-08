
export async function getAllServiceAreas() {
  return fetch(`/api/service-areas`, { method: "GET" });
}

export async function createServiceArea(area: string) {
  return fetch(`/api/service-areas`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ area }),
  });
}