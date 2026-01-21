import { inactivateProfissional } from "@/services/profissional-service";

export function useInactivateProfissional() {
  async function inactivate(id: string) {
    const response = await inactivateProfissional(id);

    if (!response.ok) {
      const data = await response.json().catch(() => ({}));
      throw new Error(data?.message || "Erro ao inativar profissional");
    }
  }

  return { inactivate };
}
