import type { Vaccine, CreateVaccineParams, UpdateVaccineParams } from "./vaccines.types";
import { bffFetchJson } from "@/lib/bff-client";

const BASE_URL = "/apae-geral/api/vaccines";

export async function fetchVaccineApi(id: string): Promise<Vaccine> {
  return bffFetchJson<Vaccine>(`${BASE_URL}/${id}`);
}

export async function createVaccineApi(params: CreateVaccineParams): Promise<void> {
  await bffFetchJson(BASE_URL, {
    method: "POST",
    body: JSON.stringify(params),
  });
}

export async function updateVaccineApi(params: UpdateVaccineParams): Promise<void> {
  const { id, ...body } = params;
  await bffFetchJson(`${BASE_URL}/${id}`, {
    method: "PUT",
    body: JSON.stringify(body),
  });
}