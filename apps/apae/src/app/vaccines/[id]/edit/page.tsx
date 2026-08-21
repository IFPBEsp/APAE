import { VaccineEditForm } from "@/domains/vaccines/edit/vaccine-form";

type Props = {
  params: Promise<{
    id: string;
  }>;
};

export default async function EditVaccinePage({ params }: Props) {
  const { id } = await params;

  return <VaccineEditForm id={id} />;
}
