
export async function getAllServiceTypes() {
  return fetch(`/apae-geral/api/service-types`, { method: "GET" });
}

export async function createServiceType(name: string) {
  return fetch(`/apae-geral/api/service-types`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ name }),
  });
}
