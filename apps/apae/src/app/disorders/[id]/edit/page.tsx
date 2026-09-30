"use client";

import { useParams } from "next/navigation";
import { Loader2 } from "lucide-react";

import { DisorderForm } from "@/domains/disorders/shared/disorder-form";
import { useDisorderEdit } from "@/domains/disorders/edit/use-disorder-edit";

export default function EditDisorderPage() {
  const params = useParams();
  const id = typeof params.id === "string" ? params.id : "";

   const { disorder, updateDisorder, isSubmitting } = useDisorderEdit(id);

  if (!disorder) {
    return (
      <div className="flex justify-center items-center p-10">
        <Loader2 className="h-8 w-8 animate-spin text-gray-500" />
      </div>
    );
  }

  return (
    <DisorderForm
      initialValues={{ name: disorder.name }}
      title="Editar Transtorno"
      submitText="Atualizar"
      submittingText="Atualizando..."
      isSubmitting={isSubmitting}
      onSubmit={(data) => updateDisorder({ id, name: data.name })}
    />
  );
}
