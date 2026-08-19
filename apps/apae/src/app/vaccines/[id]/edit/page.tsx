"use client";

import { VaccineForm } from "@/domains/vaccines/edit/vaccine-form";

export default function EditVaccinePage() {
  return (
    <div className="space-y-6 p-6">
      <div>
        <h1 className="text-2xl font-bold tracking-tight">Editar Vacina</h1>
        <p className="text-sm text-muted-foreground">
          Altere o nome da vacina no sistema.
        </p>
      </div>
      <VaccineForm />
    </div>
  );
}