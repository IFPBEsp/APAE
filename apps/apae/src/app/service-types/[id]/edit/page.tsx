"use client";

import { useParams } from "next/navigation";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Loader2, ArrowLeft } from "lucide-react";
import { useServiceTypeEdit } from "@/hooks/service-types/use-service-type-edit";

export default function EditServiceTypePage() {
  const router = useRouter();
  const params = useParams();
  const { id } = params;

  const { form, isSubmitting, onSubmit } = useServiceTypeEdit(id);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = form;

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
              <Input
                id="area"
                {...register("area")}
                className="mt-1 block w-full"
              />
              {errors.area && (
                <p className="mt-1 text-sm text-red-600">{errors.area.message}</p>
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