"use client";

import { useRouter, useParams } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { updateTranstornoSchema, UpdateTranstornoDTO } from "@/schemas/transtornosSchema";
import { toast } from "react-toastify";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useEffect } from "react";
import { Loader2, ArrowLeft } from "lucide-react";

export default function EditTranstornoPage() {
  const router = useRouter();
  const params = useParams();
  const { id } = params;

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors, isSubmitting },
  } = useForm<UpdateTranstornoDTO>({
    resolver: zodResolver(updateTranstornoSchema),
  });

  useEffect(() => {
    if (id) {
      const fetchTranstorno = async () => {
        try {
          const response = await fetch(`/api/transtornos/${id}`);
          if (!response.ok) throw new Error("Transtorno não encontrado.");
          const data = await response.json();
          setValue("name", data.name);
        } catch (error) {
          toast.error(error instanceof Error ? error.message : "Erro ao carregar transtorno");
          router.push("/disorders");
        }
      };
      fetchTranstorno();
    }
  }, [id, setValue, router]);

  const onSubmit = async (data: UpdateTranstornoDTO) => {
    try {
      const response = await fetch(`/api/transtornos/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });
      if (!response.ok) {
        throw new Error("Falha ao atualizar transtorno.");
      }
      toast.success("Transtorno atualizado com sucesso!");
      router.push("/disorders");
      router.refresh();
    } catch (error) {
      toast.error(error instanceof Error ? error.message : "Erro ao atualizar transtorno");
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
          
          <h1 className="text-2xl font-bold mb-6 text-[#003B93]">Editar Transtorno</h1>
          
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
              <Label htmlFor="name" className="font-semibold text-[#003B93]">
                Nome do Transtorno
              </Label>
              <Input
                id="name"
                {...register("name")}
                className="mt-1 block w-full"
              />
              {errors.name && (
                <p className="mt-1 text-sm text-red-600">{errors.name.message}</p>
              )}
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