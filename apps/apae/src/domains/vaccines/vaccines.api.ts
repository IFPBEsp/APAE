import type { Vaccine } from "./vaccines.types";

const BASE_URL = "/apae-geral/api/vaccines";

export async function fetchVaccinesApi(): Promise<Vaccine[]> {
  const response = await fetch(BASE_URL);
  if (!response.ok) throw new Error("Ocorreu um erro ao carregar as vacinas.");
  return response.json();
}
