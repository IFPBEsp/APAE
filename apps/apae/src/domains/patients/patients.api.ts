import { bffFetchJson } from "@/lib/bff-client";
import { Page } from "@/types/pagination";
import { Patient } from "./types/patient";

export async function getPatients(): Promise<Patient[]> {
  const data = await bffFetchJson<Page<Patient>>(`/apae-geral/api/patients?page=0&size=100`);
  return data.content || [];
}
