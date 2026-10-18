"use client";

import { VaccineForm } from "@/domains/vaccines/shared/vaccine-form";
import { useVaccineCreate } from "@/domains/vaccines/create/use-vaccine-create";

export default function NewVaccinePage() {
  const { createVaccine, isSubmitting } = useVaccineCreate();

  return (
    <VaccineForm
      title="Nova Vacina"
      submitLabel="Salvar"
      submittingLabel="Salvando..."
      isSubmitting={isSubmitting}
      onSubmit={createVaccine}
    />
  );
}
