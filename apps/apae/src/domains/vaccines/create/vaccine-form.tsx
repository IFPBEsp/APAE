"use client";

import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useVaccineCreate } from "./use-vaccine-create";

export function VaccineForm() {
  const { form, onSubmit, isLoading, router } = useVaccineCreate();
  const { register, formState: { errors } } = form;

  return (
    <form onSubmit={onSubmit} className="space-y-6 max-w-md">
      <div className="space-y-2">
        <label htmlFor="name" className="text-sm font-medium">
          Nome da Vacina
        </label>
        <Input
          id="name"
          type="text"
          placeholder="Ex: BCG, Tríplice Viral"
          disabled={isLoading}
          {...register("name")}
        />
        {errors.name && (
          <p className="text-sm text-red-500">{errors.name.message}</p>
        )}
      </div>

      <div className="flex gap-4">
        <Button
          type="button"
          variant="outline"
          disabled={isLoading}
          onClick={() => router.push("/vaccines")}
        >
          Cancelar
        </Button>
        <Button type="submit" disabled={isLoading}>
          {isLoading ? "Salvando..." : "Salvar"}
        </Button>
      </div>
    </form>
  );
}