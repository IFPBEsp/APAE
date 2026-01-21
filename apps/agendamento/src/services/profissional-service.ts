import { getApiBaseUrl } from "@/lib/client-service";

const API_URL = getApiBaseUrl();

export async function getAllProfissionais(ativo?: boolean) {
  const url = 
    ativo === undefined
      ? `${API_URL}/professionals`
      : `${API_URL}/professionals?ativo=${ativo}`;

  console.log("[getAllProfissionais] ativo:", ativo, "| url:", url);
  //const response = await fetch(API_URL + "/professionals", { method: "GET" });
  return fetch(url, { method: "GET" });
}

/*export async function deleteProfissional(id: string) {
  const response = await fetch(API_URL + `/professionals/${id}`, {
    method: "DELETE",
  });
  return response;
}*/

export async function inactivateProfissional(id: string) {
  const response = await fetch(`${API_URL}/professionals/${id}/inactivate`, {
    method: "PUT",
  });
  return response;
}

export async function activateProfissional(id: string) {
  const response = await fetch(`${API_URL}/professionals/${id}/activate`, {
    method: "PUT",
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
