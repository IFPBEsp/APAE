"use client";

import { DisorderForm } from "@/domains/disorders/shared/disorder-form";
import { useDisorderCreate } from "@/domains/disorders/create/use-disorder-create";

export default function NewDisorderPage() {
  const { createDisorder, isSubmitting } = useDisorderCreate();

  return (
    <DisorderForm
      title="Novo Transtorno"
      submitText="Salvar"
      submittingText="Salvando..."
      isSubmitting={isSubmitting}
      onSubmit={createDisorder}
    />
  );
}
