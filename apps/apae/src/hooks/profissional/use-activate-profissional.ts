import { activateProfissional } from "@/services/profissional-service";

export function useActivateProfissional() {
  async function activate(id: string) {
    const response = await activateProfissional(id);

    if (!response.ok) {
      const data = await response.json().catch(() => ({}));
      throw new Error(data?.message || "Erro ao reativar profissional");
    }
  }

  return { activate };
}
