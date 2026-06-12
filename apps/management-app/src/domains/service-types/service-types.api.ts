import type {
  CreateServiceTypeDTO,
  ServiceType,
  UpdateServiceTypeDTO,
} from "./service-types.types";

async function parseResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.message || "Erro ao processar tipos de atendimento.");
  }

  return response.json();
}

export async function listServiceTypes() {
  const response = await fetch("/api/service-types");
  return parseResponse<ServiceType[]>(response);
}

export async function getServiceType(id: string) {
  const response = await fetch(`/api/service-types/${id}`);
  return parseResponse<ServiceType>(response);
}

export async function createServiceType(payload: CreateServiceTypeDTO) {
  const response = await fetch("/api/service-types", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  return parseResponse<ServiceType>(response);
}

export async function updateServiceType(id: string, payload: UpdateServiceTypeDTO) {
  const response = await fetch(`/api/service-types/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  return parseResponse<ServiceType>(response);
}

export async function deleteServiceType(id: string | number) {
  const response = await fetch(`/api/service-types/${id}`, {
    method: "DELETE",
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.message || "Erro ao remover tipo de atendimento.");
  }
}
