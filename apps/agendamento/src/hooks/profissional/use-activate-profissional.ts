import { activateProfissional } from "@/services/profissional-service";

export function useActivateProfissional() {
  async function activate(id: string) {
    try {
      await activateProfissional(id);
    } catch (error) {
      console.error("Erro ao ativar profissional");
    }
  }

  return { activate };
}
