import type { Disorder } from "./disorders.types";

export function filterDisordersByName(disorders: Disorder[], searchName: string): Disorder[] {
  const normalized = searchName.toLowerCase();
  return disorders.filter((d) => d.name.toLowerCase().includes(normalized));
}
