"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { updateVaccineSchema, UpdateVaccineFormData } from "../vaccines.schema";
import { useVaccineEdit } from "./use-vaccine-edit";
import Link from "next/link";
import { useEffect } from "react";

type Props = {
  id: string;
};

export function VaccineEditForm({ id }: Props) {
  const { defaultName, handleUpdate, isLoading, isSaving, error } =
    useVaccineEdit(id);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<UpdateVaccineFormData>({
    resolver: zodResolver(updateVaccineSchema),
  });

  useEffect(() => {
    if (defaultName) {
      reset({ name: defaultName });
    }
  }, [defaultName, reset]);

  if (isLoading) {
    return <div className="p-4 text-gray-500">Carregando dados da vacina...</div>;
  }

  return (
    <form onSubmit={handleSubmit(handleUpdate)} className="space-y-4 max-w-md">
      {error && (
        <div className="p-3 bg-red-100 text-red-700 rounded-md text-sm">
          {error}
        </div>
      )}

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Nome da Vacina *
        </label>
        <input
          type="text"
          {...register("name")}
          className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          placeholder="Ex: BCG, Tríplice Viral"
        />
        {errors.name && (
          <p className="text-red-500 text-xs mt-1">{errors.name.message}</p>
        )}
      </div>

      <div className="flex gap-2">
        <button
          type="submit"
          disabled={isSaving}
          className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50"
        >
          {isSaving ? "Salvando..." : "Atualizar Vacina"}
        </button>

        <Link
          href="/vaccines"
          className="px-4 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300"
        >
          Cancelar
        </Link>
      </div>
    </form>
  );
}