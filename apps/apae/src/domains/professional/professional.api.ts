import { bffFetchJson } from "@/lib/bff-client";
import { Page } from "@/types/pagination";
import { Professional } from "@/types/profissional";

export async function getHealthProfessionals(): Promise<Professional[]> {
  const data = await bffFetchJson<Page<Professional>>(`/apae-geral/api/professionals?page=0&size=100`);
  return data.content || [];
}

export async function getHealthProfessional(
  id: string,
): Promise<Professional> {
  return bffFetchJson<Professional>(`/apae-geral/api/professionals/${id}`);
}

export async function getServiceAreas(): Promise<string[]> {
  const professionals = await getHealthProfessionals();
  const areas = professionals.map((p) => p.serviceArea?.area);
  return [...new Set(areas)].filter(Boolean) as string[];
}
