import { deleteProfissional } from "@/services/profissional-service";

export function useRemoveProfissional() {
  async function remove(id: string) {
    try {
      await deleteProfissional(id);
    } catch (err) {
      console.error(err);
    }
  }

  return { remove };
}
