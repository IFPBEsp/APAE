import type { Vaccine, CreateVaccineParams, UpdateVaccineParams, DeleteVaccineParams } from "./vaccines.types";

const BASE_URL = "/apae-geral/api/vaccines";

export async function fetchVaccinesApi(): Promise<Vaccine[]> {
  const response = await fetch(BASE_URL);
  if (!response.ok) throw new Error("Ocorreu um erro ao carregar as vacinas.");
  return response.json();
}

export async function fetchVaccineApi(id: string): Promise<Vaccine> {
  const response = await fetch(`${BASE_URL}/${id}`);
  if (!response.ok) throw new Error("Ocorreu um erro ao carregar a vacina.");
  return response.json();
}

export async function createVaccineApi(params: CreateVaccineParams): Promise<Vaccine> {
  const response = await fetch(BASE_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(params),
  });
  if (!response.ok) throw new Error("Ocorreu um erro ao criar a vacina.");
  return response.json();
}

export async function updateVaccineApi(params: UpdateVaccineParams): Promise<Vaccine> {
  const { id, ...body } = params;
  const response = await fetch(`${BASE_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!response.ok) throw new Error("Ocorreu um erro ao atualizar a vacina.");
  return response.json();
}

export async function deleteVaccineApi(params: DeleteVaccineParams): Promise<void> {
  const response = await fetch(`${BASE_URL}/${params.id}`, {
    method: "DELETE",
  });
  if (!response.ok) {
    const errorData = await response.json().catch(() => null);
    throw new Error(errorData?.message || "Ocorreu um erro ao excluir a vacina.");
  }
}
