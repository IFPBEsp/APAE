import type { Disorder, CreateDisorderParams, UpdateDisorderParams, DeleteDisorderParams } from "./disorders.types";
import { bffFetchJson } from "@/lib/bff-client";

const BASE_URL = "/apae-geral/api/disorders";

export async function fetchDisordersApi(): Promise<Disorder[]> {
  return bffFetchJson<Disorder[]>(BASE_URL);
}

export async function fetchDisorderApi(id: string): Promise<Disorder> {
  return bffFetchJson<Disorder>(`${BASE_URL}/${id}`);
}

export async function createDisorderApi(params: CreateDisorderParams): Promise<void> {
  await bffFetchJson(BASE_URL, {
    method: "POST",
    body: JSON.stringify(params),
  });
}

export async function updateDisorderApi(params: UpdateDisorderParams): Promise<void> {
  const { id, ...body } = params;
  await bffFetchJson(`${BASE_URL}/${id}`, {
    method: "PUT",
    body: JSON.stringify(body),
  });
}

export async function deleteDisorderApi(params: DeleteDisorderParams): Promise<void> {
  await bffFetchJson(`${BASE_URL}/${params.id}`, {
    method: "DELETE",
  });
}
