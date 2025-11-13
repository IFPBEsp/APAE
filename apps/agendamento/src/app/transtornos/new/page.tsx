"use client";

import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { createTranstornoSchema, CreateTranstornoDTO } from "@/app/schemas/transtornosSchema";
import { toast } from "react-toastify";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label"; 
import { ArrowLeft } from "lucide-react";

export default function NewTranstornoPage() {
  const router = useRouter();
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<CreateTranstornoDTO>({
    resolver: zodResolver(createTranstornoSchema),
  });

  const onSubmit = async (data: CreateTranstornoDTO) => {
    try {
      const response = await fetch("/api/transtornos", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });

      if (!response.ok) {
        throw new Error("Falha ao criar transtorno.");
      }

      toast.success("Transtorno criado com sucesso!");
      router.push("/transtornos");
      router.refresh();
    } catch (error: any) {
      toast.error(error.message);
    }
  };

  return (
    <div className="flex-1 !bg-slate-100 min-h-screen">
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
          
          <h1 className="text-2xl font-bold mb-6 text-[#003B93]">Novo Transtorno</h1>
          
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
              <Label htmlFor="name" className="font-semibold text-[#003B93]">
                Nome do Transtorno
              </Label>
              <Input
                id="name"
                {...register("name")}
                className="mt-1 block w-full"
                placeholder="Ex: TDAH"
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
                {isSubmitting ? "Salvando..." : "Salvar"}
              </Button>
            </div>
          </form>
        </div>
      </main>
    </div>
  );
}