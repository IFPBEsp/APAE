import axios from "axios";
import { getApiBaseUrl } from "@/lib/client-service";
import { DocumentWithUrl } from "@/types/document";

const API_URL = getApiBaseUrl();

export async function getAllProfissionais(ativo?: boolean) {
  const url = 
    ativo === undefined
      ? `/api/professionals`
      : `/api/professionals?ativo=${ativo}`;

  console.log("[getAllProfissionais] ativo:", ativo, "| url:", url);
  return axios.get(url);
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
  try {
    const response = await axios.get(`/api/professionals/${id}`);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw new Error(error.response?.data?.message || error.message);
    }
    throw error;
  }
}

export async function getProfessionalDocuments(id: string) {
  try {
    const response = await axios.get(`${API_URL}/professionals/${id}/documents`);
    return response.data || [];
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw new Error(error.response?.data?.message || "Erro ao buscar documentos");
    }
    throw error;
  }
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

