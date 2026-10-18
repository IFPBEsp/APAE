import { activateProfessional } from "@/services/profissional-service";

export function useActivateProfessional() {
  async function activate(id: string) {
    const response = await activateProfessional(id);

    if (!response.ok) {
      const data = await response.json().catch(() => ({}));
      throw new Error(data?.message || "Erro ao reativar profissional");
    }
  }

  return { activate };
}
