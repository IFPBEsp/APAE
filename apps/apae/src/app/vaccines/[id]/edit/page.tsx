import { VaccineEditForm } from "@/domains/vaccines/edit/vaccine-form";

interface PageProps {
  params: Promise<{ id: string }>;
}

export default async function EditVaccinePage({ params }: PageProps) {
  const { id } = await params;
  return <VaccineEditForm id={id} />;
}