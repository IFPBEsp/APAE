import { VaccineEditForm } from "@/domains/vaccines/edit/vaccine-form";

export default async function EditVaccinePage({
                                                  params,
                                              }: {
    params: Promise<{ id: string }>;
}) {
    const { id } = await params;
    return <VaccineEditForm id={id} />;
}