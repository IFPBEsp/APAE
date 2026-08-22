import type {
  Vaccine,
  CreateVaccineParams,
  UpdateVaccineParams,
  DeleteVaccineParams,
} from "./vaccines.types";

const BASE_URL = "/apae-geral/api/vaccines";

async function checkResponse(response: Response, defaultErrorMessage: string) {
  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.detail || errorData.message || defaultErrorMessage);
  }
}

export const fetchVaccinesApi = async (): Promise<Vaccine[]> => {
  const response = await fetch(BASE_URL);
  await checkResponse(response, "Erro ao buscar vacinas.");

  return response.json();
};

export const fetchVaccineApi = async (id: string): Promise<Vaccine> => {
  const response = await fetch(`${BASE_URL}/${id}`);
  await checkResponse(response, "Erro ao buscar dados da vacina.");

  return response.json();
};

export const createVaccineApi = async (params: CreateVaccineParams): Promise<Vaccine> => {
  const response = await fetch(BASE_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(params),
  });
  await checkResponse(response, "Erro ao criar vacina.");

  return response.json();
};

export const updateVaccineApi = async (params: UpdateVaccineParams): Promise<Vaccine> => {
  const response = await fetch(`${BASE_URL}/${params.id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name: params.name }),
  });
  await checkResponse(response, "Erro ao atualizar vacina.");

  return response.json();
};

export const deleteVaccineApi = async (params: DeleteVaccineParams): Promise<void> => {
  const response = await fetch(`${BASE_URL}/${params.id}`, {
    method: "DELETE",
  });
  await checkResponse(response, "Erro ao excluir vacina.");
};