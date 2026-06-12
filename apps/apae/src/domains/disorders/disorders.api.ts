import type { Disorder, CreateDisorderParams, UpdateDisorderParams, DeleteDisorderParams } from "./disorders.types";

const BASE_URL = "/apae-geral/api/disorders";

export async function fetchDisordersApi(): Promise<Disorder[]> {
  const response = await fetch(BASE_URL);
  if (!response.ok) throw new Error("Ocorreu um erro ao carregar os transtornos.");
  return response.json();
}

export async function fetchDisorderApi(id: string): Promise<Disorder> {
  const response = await fetch(`${BASE_URL}/${id}`);
  if (!response.ok) throw new Error("Ocorreu um erro ao carregar o transtorno.");
  return response.json();
}

export async function createDisorderApi(params: CreateDisorderParams): Promise<void> {
  const response = await fetch(BASE_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(params),
  });
  if (!response.ok) throw new Error("Ocorreu um erro ao criar o transtorno.");
}

export async function updateDisorderApi(params: UpdateDisorderParams): Promise<void> {
  const { id, ...body } = params;
  const response = await fetch(`${BASE_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!response.ok) throw new Error("Ocorreu um erro ao atualizar o transtorno.");
}

export async function deleteDisorderApi(params: DeleteDisorderParams): Promise<void> {
  const response = await fetch(`${BASE_URL}/${params.id}`, {
    method: "DELETE",
  });
  if (!response.ok) {
    const errorData = await response.json().catch(() => null);
    throw new Error(errorData?.message || "Ocorreu um erro ao excluir o transtorno.");
  }
}
