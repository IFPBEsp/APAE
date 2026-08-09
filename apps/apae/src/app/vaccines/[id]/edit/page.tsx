import { VaccineEditForm } from "@/domains/vaccines/edit/vaccine-form";

type Props = {
  params: Promise<{
    id: string;
  }>;
};

export default async function EditVaccinePage({ params }: Props) {
  const { id } = await params;

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">Editar Vacina</h1>
      <VaccineEditForm id={id} />
    </div>
  );
}