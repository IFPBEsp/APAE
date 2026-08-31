"use client";

import { useRouter, useParams } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { updateserviceTypeSchema, UpdateserviceTypeDTO } from "@/schemas/service-type-schemas";
import { toast } from "react-toastify";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useEffect } from "react";
import { Loader2, ArrowLeft } from "lucide-react";

export default function EditServiceTypePage() {
  const router = useRouter();
  const params = useParams();
  const { id } = params;

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors, isSubmitting },
  } = useForm<UpdateserviceTypeDTO>({
    resolver: zodResolver(updateserviceTypeSchema),
  });

  useEffect(() => {
    if (id) {
      const fetchserviceType = async () => {
        try {
          const response = await fetch(`/api/service-types/${id}`);
          if (!response.ok) throw new Error("Tipo de atendimento não encontrado.");
          const data = await response.json();
          setValue("area", data.area);
        } catch (error: any) {
          toast.error(error.message);
          router.push("/tipo-atendimento");
        }
      };
      fetchserviceType();
    }
  }, [id, setValue, router]);

  const onSubmit = async (data: UpdateserviceTypeDTO) => {
    try {
      const response = await fetch(`/api/service-types/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });
      if (!response.ok) {
        throw new Error("Falha ao atualizar o tipo de atendimento.");
      }
      toast.success("Tipo de atendimento atualizado com sucesso!");
      router.push("/tipo-atendimento");
      router.refresh();
    } catch (error: any) {
      toast.error(error.message);
    }
  };

  if (!id) {
    return (
      <div className="flex justify-center items-center p-10">
        <Loader2 className="h-8 w-8 animate-spin text-gray-500" />
      </div>
    );
  }

  return (
    <div className="!bg-slate-100 min-h-screen">
      <main className="container mx-auto p-4 md:p-6">
        <div className="bg-white rounded-xl shadow-md border-2 p-6 mb-4">
          <Button
            variant="ghost"
            onClick={() => router.back()}
            className="mb-4 text-sm text-[#003B93] hover:bg-blue-50"
          >
            <ArrowLeft className="h-4 w-4 mr-2" />
            Voltar
          </Button>

          <h1 className="text-2xl font-bold mb-6 text-[#003B93]">Editar tipo de atendimento</h1>

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
              <Label htmlFor="area" className="font-semibold text-[#003B93]">
                Nome do tipo de atendimento
              </Label>
              <Input id="area" {...register("area")} className="mt-1 block w-full" />
              {errors.area && <p className="mt-1 text-sm text-red-600">{errors.area.message}</p>}
            </div>

            <div className="flex justify-end gap-2 pt-4">
              <Button type="button" variant="outline" onClick={() => router.back()}>
                Cancelar
              </Button>
              <Button
                type="submit"
                disabled={isSubmitting}
                className="!bg-[#0D4F97] !hover:bg-[#0b427d] text-white"
              >
                {isSubmitting ? "Atualizando..." : "Atualizar"}
              </Button>
            </div>
          </form>
        </div>
      </main>
    </div>
  );
}
