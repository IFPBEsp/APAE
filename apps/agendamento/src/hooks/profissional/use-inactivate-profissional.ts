import { inactivateProfissional } from "@/services/profissional-service";

export function useInactivateProfissional() {
  async function inactivate(id: string) {
    try {
      await inactivateProfissional(id);
    } catch (error) {
      console.error("Erro ao inativar profissional");
    }
  }

  return { inactivate };
}
