import {
  Vaccine,
  CreateVaccineParams,
  UpdateVaccineParams,
  DeleteVaccineParams,
} from "./vaccines.types";

const BASE_URL = "/apae-geral/api/vaccines";

export async function fetchVaccinesApi(): Promise<Vaccine[]> {
  const response = await fetch(BASE_URL);
  if (!response.ok) {
    throw new Error("Erro ao buscar vacinas.");
  }
  return response.json();
}

export async function fetchVaccineApi(id: string): Promise<Vaccine> {
  const response = await fetch(`${BASE_URL}/${id}`);
  if (!response.ok) {
    throw new Error("Erro ao buscar dados da vacina.");
  }
  return response.json();
}

export async function createVaccineApi(
  params: CreateVaccineParams
): Promise<Vaccine> {
  const response = await fetch(BASE_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(params),
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.detail || errorData.message || "Erro ao criar vacina.");
  }

  return response.json();
}

export async function updateVaccineApi(
  params: UpdateVaccineParams
): Promise<Vaccine> {
  const response = await fetch(`${BASE_URL}/${params.id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name: params.name }),
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.detail || errorData.message || "Erro ao atualizar vacina.");
  }

  return response.json();
}

export async function deleteVaccineApi(
  params: DeleteVaccineParams
): Promise<void> {
  const response = await fetch(`${BASE_URL}/${params.id}`, {
    method: "DELETE",
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.detail || errorData.message || "Erro ao excluir vacina.");
  }
}