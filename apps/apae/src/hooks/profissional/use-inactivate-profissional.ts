import { inactivateProfessional } from "@/services/profissional-service";

export function useInactivateProfessional() {
  async function inactivate(id: string) {
    const response = await inactivateProfessional(id);

    if (!response.ok) {
      const data = await response.json().catch(() => ({}));
      throw new Error(data?.message || "Erro ao inativar profissional");
    }
  }

  return { inactivate };
}
