"use client";

import { VaccineForm } from "@/domains/vaccines/create/vaccine-form";

export default function CreateVaccinePage() {
  return (
    <div className="space-y-6 p-6">
      <div>
        <h1 className="text-2xl font-bold tracking-tight">Nova Vacina</h1>
        <p className="text-sm text-muted-foreground">
          Cadastre uma nova vacina no sistema para uso dos pacientes.
        </p>
      </div>
      <VaccineForm />
    </div>
  );
}