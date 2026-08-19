"use client";

import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useVaccineEdit } from "./use-vaccine-edit";

export function VaccineForm() {
  const { form, onSubmit, isLoading, isSubmitting, router } = useVaccineEdit();
  const { register, formState: { errors } } = form;

  if (isLoading) {
    return <p className="text-sm text-muted-foreground">Carregando dados da vacina...</p>;
  }

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
          disabled={isSubmitting}
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
          disabled={isSubmitting}
          onClick={() => router.push("/vaccines")}
        >
          Cancelar
        </Button>
        <Button type="submit" disabled={isSubmitting}>
          {isSubmitting ? "Salvando..." : "Salvar"}
        </Button>
      </div>
    </form>
  );
}