import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { createVaccineSchema, CreateVaccineFormData } from "../vaccines.schema";
import { createVaccineApi } from "../vaccines.api";
import { toast } from "react-toastify"; // Biblioteca oficial do seu projeto!
import { useState } from "react";

export function useVaccineCreate() {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);

  const form = useForm<CreateVaccineFormData>({
    resolver: zodResolver(createVaccineSchema),
    defaultValues: {
      name: "",
    },
  });

  async function onSubmit(data: CreateVaccineFormData) {
    setIsLoading(true);
    try {
      await createVaccineApi(data);
      toast.success("Vacina cadastrada com sucesso!");
      router.push("/vaccines");
    } catch (error) {
      toast.error("Ocorreu um erro ao cadastrar a vacina.");
    } finally {
      setIsLoading(false);
    }
  }

  return {
    form,
    onSubmit: form.handleSubmit(onSubmit),
    isLoading,
    router,
  };
}