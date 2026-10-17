"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "react-toastify";
import { bffFetchJson } from "@/lib/bff-client";
import {
  updateserviceTypeSchema,
  UpdateserviceTypeDTO,
} from "@/schemas/service-type-schemas";

export function useServiceTypeEdit(id: string | string[] | undefined) {
  const router = useRouter();

  const form = useForm<UpdateserviceTypeDTO>({
    resolver: zodResolver(updateserviceTypeSchema),
  });

  const { setValue, formState: { isSubmitting } } = form;

  useEffect(() => {
    if (!id) return;

    async function fetchServiceType() {
      try {
        const data = await bffFetchJson<{ area: string }>(
          `/apae-geral/api/service-types/${id}`
        );
        setValue("area", data.area);
      } catch (error) {
        const err = error as Error;
        toast.error(err.message);
        router.push("/service-types");
      }
    }

    fetchServiceType();
  }, [id, setValue, router]);

  async function onSubmit(data: UpdateserviceTypeDTO) {
    try {
      await bffFetchJson(`/apae-geral/api/service-types/${id}`, {
        method: "PUT",
        body: JSON.stringify(data),
      });
      toast.success("Tipo de atendimento atualizado com sucesso!");
      router.push("/service-types");
      router.refresh();
    } catch (error) {
      const err = error as Error;
      toast.error(err.message);
    }
  }

  return { form, isSubmitting, onSubmit };
}
