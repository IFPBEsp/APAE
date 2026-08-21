"use client";

import { useParams } from "next/navigation";
import { VaccineEditForm } from "@/domains/vaccines/edit/vaccine-form";

export default function EditVaccinePage() {
  const params = useParams();
  const id = typeof params.id === "string" ? params.id : "";
  return <VaccineEditForm id={id} />;
}
