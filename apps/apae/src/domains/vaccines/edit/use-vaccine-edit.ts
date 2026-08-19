import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { updateVaccineSchema, UpdateVaccineFormData } from "../vaccines.schema";
import { fetchVaccineApi, updateVaccineApi } from "../vaccines.api";
import { toast } from "react-toastify";

export function useVaccineEdit() {
  const router = useRouter();
  const { id } = useParams<{ id: string }>();
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const form = useForm<UpdateVaccineFormData>({
    resolver: zodResolver(updateVaccineSchema),
    defaultValues: {
      name: "",
    },
  });

  useEffect(() => {
    async function loadVaccine() {
      try {
        const vaccine = await fetchVaccineApi(id);
        form.setValue("name", vaccine.name);
      } catch (error) {
        toast.error("Erro ao carregar dados da vacina.");
        router.push("/vaccines");
      } finally {
        setIsLoading(false);
      }
    }
    loadVaccine();
  }, [id, form, router]);

  async function onSubmit(data: UpdateVaccineFormData) {
    setIsSubmitting(true);
    try {
      await updateVaccineApi({ id, name: data.name });
      toast.success("Vacina atualizada com sucesso!");
      router.push("/vaccines");
    } catch (error) {
      toast.error("Ocorreu um erro ao atualizar a vacina.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return {
    form,
    onSubmit: form.handleSubmit(onSubmit),
    isLoading,
    isSubmitting,
    router,
  };
}