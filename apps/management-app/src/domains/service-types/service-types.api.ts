import type {
  CreateServiceTypeDTO,
  ServiceType,
  UpdateServiceTypeDTO,
} from "./service-types.types";

interface ServiceTypeApiDTO {
  area?: string;
  id: string | number;
  name?: string;
}

function normalizeServiceType(data: ServiceTypeApiDTO): ServiceType {
  return {
    id: data.id,
    name: data.name ?? data.area ?? "",
  };
}

async function parseResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.message || "Erro ao processar tipos de atendimento.");
  }

  return response.json();
}

export async function listServiceTypes() {
  const response = await fetch("/api/service-types");
  const data = await parseResponse<ServiceTypeApiDTO[]>(response);
  return data.map(normalizeServiceType);
}

export async function getServiceType(id: string) {
  const response = await fetch(`/api/service-types/${id}`);
  const data = await parseResponse<ServiceTypeApiDTO>(response);
  return normalizeServiceType(data);
}

export async function createServiceType(payload: CreateServiceTypeDTO) {
  const response = await fetch("/api/service-types", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  const data = await parseResponse<ServiceTypeApiDTO>(response);
  return normalizeServiceType(data);
}

export async function updateServiceType(id: string, payload: UpdateServiceTypeDTO) {
  const response = await fetch(`/api/service-types/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  const data = await parseResponse<ServiceTypeApiDTO>(response);
  return normalizeServiceType(data);
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
