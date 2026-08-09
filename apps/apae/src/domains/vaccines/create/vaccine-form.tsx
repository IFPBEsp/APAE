"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { createVaccineSchema, CreateVaccineFormData } from "../vaccines.schema";
import { useVaccineCreate } from "./use-vaccine-create";
import Link from "next/link";

export function VaccineCreateForm() {
  const { handleCreate, isLoading, error } = useVaccineCreate();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateVaccineFormData>({
    resolver: zodResolver(createVaccineSchema),
  });

  return (
    <form onSubmit={handleSubmit(handleCreate)} className="space-y-4 max-w-md">
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
          disabled={isLoading}
          className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50"
        >
          {isLoading ? "Salvando..." : "Salvar Vacina"}
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