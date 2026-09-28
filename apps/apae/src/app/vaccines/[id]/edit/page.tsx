"use client";

import { useParams } from "next/navigation";
import { Loader2 } from "lucide-react";
import { VaccineForm } from "@/domains/vaccines/shared/vaccine-form";
import { useVaccineEdit } from "@/domains/vaccines/edit/use-vaccine-edit";

export default function EditVaccinePage() {
  const params = useParams();
  const id = typeof params.id === "string" ? params.id : "";
  const { vaccine, updateVaccine, isSubmitting } = useVaccineEdit(id);

  if (!vaccine) {
    return (
      <div className="flex justify-center items-center p-10">
        <Loader2 className="h-8 w-8 animate-spin text-gray-500" />
      </div>
    );
  }

  return (
    <VaccineForm
      title="Editar Vacina"
      submitLabel="Atualizar"
      submittingLabel="Atualizando..."
      isSubmitting={isSubmitting}
      defaultValues={{ name: vaccine.name }}
      onSubmit={(data) => updateVaccine({ id, name: data.name })}
    />
  );
}
