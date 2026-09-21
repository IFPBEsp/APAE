"use client";

import { useParams } from "next/navigation";
import { DisorderEditForm } from "@/domains/disorders/edit/disorder-form";

export default function EditDisorderPage() {
  const params = useParams();
  const id = typeof params.id === "string" ? params.id : "";
  return <DisorderEditForm id={id} />;
}
