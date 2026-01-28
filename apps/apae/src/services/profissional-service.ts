import { getApiBaseUrl } from "@/lib/client-service";
import { DocumentWithUrl } from "@/types/document";

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
  const response = await fetch("/api/professionals", {
    method: "POST",
    body: formData,
  });

  return response;
}

export async function updateProfissional(id: string, data: any) {
  const response = await fetch(`/api/professionals/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  return response;
}

export async function getProfissionalById(id: string) {
  const response = await fetch(`/api/professionals/${id}`, {
    method: "GET",
  });

  const data = await response.json();

  if (!response.ok) {
    const responseMessage = data.message;
    throw new Error(responseMessage);
  }

  return data;
}

export async function getProfessionalDocuments(id: string) {
  const res = await fetch(`${API_URL}/professionals/${id}/documents`, { method: "GET" });

  const data = await res.json().catch(() => null);

  if (!res.ok) {
    throw new Error((data as any)?.message || "Erro ao buscar documentos");
  }

  return (data ?? []) as DocumentWithUrl[];
}


export async function updateProfessionalDocuments(id: string, formData: FormData) {
  return fetch(`${API_URL}/professionals/${id}/documents`, {
    method: "PATCH",
    body: formData,
  });
}

export async function removeProfessionalDocument(
  professionalId: string,
  documentId: string
) {
  const response = await fetch(
    `${API_URL}/professionals/${professionalId}/documents/${documentId}`,
    { method: "DELETE" }
  );

  if (!response.ok) {
    const contentType = response.headers.get("content-type");
    const data = contentType?.includes("application/json")
      ? await response.json().catch(() => ({}))
      : {};
    throw new Error((data as any)?.message || "Erro ao remover documento");
  }

  return true;
}

