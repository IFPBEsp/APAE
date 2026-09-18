import type { Vaccine, CreateVaccineParams, UpdateVaccineParams } from "./vaccines.types";

const BASE_URL = "/apae-geral/api/vaccines";

export async function fetchVaccineApi(id: string): Promise<Vaccine> {
  const response = await fetch(`${BASE_URL}/${id}`);
  if (!response.ok) throw new Error("Ocorreu um erro ao carregar a vacina.");
  return response.json();
}

export async function createVaccineApi(params: CreateVaccineParams): Promise<void> {
  const response = await fetch(BASE_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(params),
  });
  if (!response.ok) throw new Error("Ocorreu um erro ao criar a vacina.");
}

export async function updateVaccineApi(params: UpdateVaccineParams): Promise<void> {
  const { id, ...body } = params;
  const response = await fetch(`${BASE_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!response.ok) throw new Error("Ocorreu um erro ao atualizar a vacina.");
}