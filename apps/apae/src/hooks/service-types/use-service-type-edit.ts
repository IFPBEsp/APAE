import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "react-toastify";
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
        const response = await fetch(`/apae-geral/api/service-types/${id}`);
        if (!response.ok) throw new Error("Tipo de atendimento não encontrado.");
        const data = await response.json();
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
      const response = await fetch(`/apae-geral/api/service-types/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });
      if (!response.ok) {
        throw new Error("Falha ao atualizar o tipo de atendimento.");
      }
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
