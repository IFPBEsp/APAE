import { VaccineCreateForm } from "@/domains/vaccines/create/vaccine-form";

export default function NewVaccinePage() {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">Cadastrar Nova Vacina</h1>
      <VaccineCreateForm />
    </div>
  );
}