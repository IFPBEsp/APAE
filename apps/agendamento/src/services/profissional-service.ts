import { getApiBaseUrl } from "@/lib/client-service";

const API_URL = getApiBaseUrl();

export async function getAllProfissionais() {
  const response = await fetch(API_URL + "/profissionais", { method: "GET" });
  return response;
}

export async function deleteProfissional(id: string) {
  const response = await fetch(API_URL + `/profissionais/${id}`, {
    method: "DELETE",
  });
  return response;
}

export async function createProfissional(data: any) {
  const response = await fetch(API_URL + "/profissionais", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  return response;
}

export async function updateProfissional(id: string, data: any) {
  const response = await fetch(API_URL + `/profissionais/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => null);
    const message = errorData?.mensagem || `Erro HTTP ${response.status}`;

    const error = new Error(message);
    (error as any).details = errorData?.detalhes;
    (error as any).status = errorData?.status;
    throw error;
  }
  return response;
}

export async function getProfissionalById(id: string) {
  const response = await fetch(`${API_URL}/profissionais/${id}`, {
    method: "GET",
  });

  if (!response.ok) {
    throw new Error("Erro ao buscar profissional");
  }

  return response.json();
}
