import type {
  CreateVaccineParams,
  DeleteVaccineParams,
  UpdateVaccineParams,
  Vaccine,
} from "./vaccines.types";

const BASE_URL = "/apae-geral/api/vaccines";

type ApiErrorResponse = {
  message?: string;
  error?: string;
};

async function getErrorMessage(
  response: Response,
  fallbackMessage: string,
): Promise<string> {
  const data = (await response
    .json()
    .catch(() => null)) as ApiErrorResponse | null;

  return data?.message ?? data?.error ?? fallbackMessage;
}

export async function fetchVaccinesApi(): Promise<Vaccine[]> {
  const response = await fetch(BASE_URL);

  if (!response.ok) {
    throw new Error("Ocorreu um erro ao carregar as vacinas.");
  }

  return response.json();
}

export async function fetchVaccineApi(id: string): Promise<Vaccine> {
  const response = await fetch(`${BASE_URL}/${id}`);

  if (!response.ok) {
    throw new Error("Ocorreu um erro ao carregar a vacina.");
  }

  return response.json();
}

export async function createVaccineApi(
  params: CreateVaccineParams,
): Promise<Vaccine> {
  const response = await fetch(BASE_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      name: params.name,
    }),
  });

  if (!response.ok) {
    throw new Error(
      await getErrorMessage(response, "Ocorreu um erro ao criar a vacina."),
    );
  }

  return response.json();
}

export async function updateVaccineApi(
  params: UpdateVaccineParams,
): Promise<Vaccine> {
  const response = await fetch(`${BASE_URL}/${params.id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      name: params.name,
    }),
  });

  if (!response.ok) {
    throw new Error(
      await getErrorMessage(response, "Ocorreu um erro ao atualizar a vacina."),
    );
  }

  return response.json();
}

export async function deleteVaccineApi(
  params: DeleteVaccineParams,
): Promise<void> {
  const response = await fetch(`${BASE_URL}/${params.id}`, {
    method: "DELETE",
  });

  if (!response.ok) {
    throw new Error(
      await getErrorMessage(response, "Ocorreu um erro ao excluir a vacina."),
    );
  }
}