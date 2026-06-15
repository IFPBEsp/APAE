"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "react-toastify";

import { getServiceType, updateServiceType } from "../service-types.api";
import { updateServiceTypeSchema } from "../service-types.schema";
import type { UpdateServiceTypeDTO } from "../service-types.types";

export function useEditServiceType(id: string | undefined) {
  const router = useRouter();
  const form = useForm<UpdateServiceTypeDTO>({
    resolver: zodResolver(updateServiceTypeSchema),
  });

  useEffect(() => {
    async function loadServiceType(serviceTypeId: string) {
      try {
        const data = await getServiceType(serviceTypeId);
        form.setValue("name", data.name);
      } catch (error) {
        const message = error instanceof Error ? error.message : "Tipo de atendimento nao encontrado.";
        toast.error(message);
        router.push("/service-types");
      }
    }

    if (id) {
      loadServiceType(id);
    }
  }, [form, id, router]);

  async function onSubmit(data: UpdateServiceTypeDTO) {
    if (!id) {
      return;
    }

    try {
      await updateServiceType(id, data);
      toast.success("Tipo de atendimento atualizado com sucesso!");
      router.push("/service-types");
      router.refresh();
    } catch (error) {
      const message = error instanceof Error ? error.message : "Falha ao atualizar o tipo de atendimento.";
      toast.error(message);
    }
  }

  return {
    form,
    onSubmit,
    router,
  };
}
