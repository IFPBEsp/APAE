import { getApiBaseUrl } from "@/lib/client-service";

const API_URL = getApiBaseUrl();

export async function getAllProfissionais() {
  const response = await fetch(API_URL + "/professionals", { method: "GET" });
  return response;
}

export async function deleteProfissional(id: string) {
  const response = await fetch(API_URL + `/professionals/${id}`, {
    method: "DELETE",
  });
  return response;
}

export async function createProfissional(formData: FormData) {
  const response = await fetch(API_URL + "/professionals", {
    method: "POST",
    body: formData,
  });

  return response;
}

export async function updateProfissional(id: string, data: any) {
  const response = await fetch(API_URL + `/professionals/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  return response;
}

export async function getProfissionalById(id: string) {
  const response = await fetch(`${API_URL}/professionals/${id}`, {
    method: "GET",
  });

  const data = await response.json();

  if (!response.ok) {
    const responseMessage = data.message;
    throw new Error(responseMessage);
  }

  return data;
}
