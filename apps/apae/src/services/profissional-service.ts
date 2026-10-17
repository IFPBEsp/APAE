"use client";

import { bffFetch, bffFetchJson } from "@/lib/bff-client";

export async function getAllProfessionals<T>(active?: boolean) {
  const url =
    active === undefined
      ? `/apae-geral/api/professionals`
      : `/apae-geral/api/professionals?ativo=${active}`;

  console.log("[getAllProfessionals] ativo:", active, "| url:", url);
  return bffFetchJson<T>(url);
}

/*export async function deleteProfessional(id: string) {
  const response = await fetch(API_URL + `/professionals/${id}`, {
    method: "DELETE",
  });
  return response;
}*/

export async function inactivateProfessional(id: string) {
  return bffFetch(`/apae-geral/api/professionals/${id}/inactivate`, {
    method: "PUT",
  });
}

export async function activateProfessional(id: string) {
  return bffFetch(`/apae-geral/api/professionals/${id}/activate`, {
    method: "PUT",
  });
}

export async function createProfessional(formData: FormData) {
  return bffFetch("/apae-geral/api/professionals", {
    method: "POST",
    body: formData,
  });
}

export async function updateProfessional(id: string, data: unknown) {
  return bffFetch(`/apae-geral/api/professionals/${id}`, {
    method: "PUT",
    body: JSON.stringify(data),
  });
}

export async function getProfessionalById<T>(id: string) {
  return bffFetchJson<T>(`/apae-geral/api/professionals/${id}`);
}

export async function getProfessionalDocuments<T>(id: string) {
  try {
    return await bffFetchJson<T>(`/apae-geral/api/professionals/${id}/documents`);
  } catch {
    return [] as T;
  }
}


export async function updateProfessionalDocuments(id: string, formData: FormData) {
  return bffFetch(`/apae-geral/api/professionals/${id}/documents`, {
    method: "PATCH",
    body: formData,
  });
}

export async function removeProfessionalDocument(
  professionalId: string,
  documentId: string,
) {
  const response = await bffFetch(
    `/apae-geral/api/professionals/${professionalId}/documents/${documentId}`,
    { method: "DELETE" },
  );

  if (!response.ok) {
    const data = await response.json().catch(() => ({}));
    throw new Error(data.message || "Erro ao remover documento");
  }
}
