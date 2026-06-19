
export async function getAllServiceAreas() {
  return fetch(`/apae-geral/api/service-types`, { method: "GET" });
}

export async function createServiceArea(area: string) {
  return fetch(`/apae-geral/api/service-types`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ name: area }),
  });
}
