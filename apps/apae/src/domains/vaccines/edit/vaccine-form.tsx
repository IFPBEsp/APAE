"use client";

import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ArrowLeft } from "lucide-react";
import { capitalizeFirst } from "@/lib/formats";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { updateVaccineSchema, type UpdateVaccineFormData } from "../vaccines.schema";
import { useVaccineEdit } from "./use-vaccine-edit";

export function VaccineEditForm({ id }: { id: string }) {
  const router = useRouter();
  const { updateVaccine, isSubmitting, isLoading, vaccine } = useVaccineEdit(id);

  const form = useForm<UpdateVaccineFormData>({
    resolver: zodResolver(updateVaccineSchema),
    mode: "onChange",
    defaultValues: { name: "" },
  });

  useEffect(() => {
    if (vaccine) {
      form.reset({ name: vaccine.name });
    }
  }, [vaccine, form]);

  const onSubmit = async (data: UpdateVaccineFormData) => {
    await updateVaccine({ id, name: data.name });
  };

  if (isLoading) {
    return (
      <div className="!bg-slate-100 min-h-screen flex items-center justify-center">
        <p className="text-[#003B93] font-semibold">Carregando dados da vacina...</p>
      </div>
    );
  }

  return (
    <div className="!bg-slate-100 min-h-screen">
      <main className="container mx-auto p-4 md:p-6">
        <div className="bg-white rounded-xl shadow-md border-2 p-6 mb-4">
          <Button variant="ghost" onClick={() => router.back()} className="mb-4 text-sm text-[#003B93] hover:bg-blue-50">
            <ArrowLeft className="h-4 w-4 mr-2" />
            Voltar
          </Button>

          <h1 className="text-2xl font-bold mb-6 text-[#003B93]">Editar Vacina</h1>

          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
              <FormField
                control={form.control}
                name="name"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="font-semibold text-[#003B93]">Nome da Vacina</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="Ex: Covid-19, Influenza, etc."
                        {...field}
                        onChange={(e) => field.onChange(capitalizeFirst(e.target.value))}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <div className="flex justify-end gap-2 pt-4">
                <Button type="button" variant="outline" onClick={() => router.back()}>Cancelar</Button>
                <Button type="submit" disabled={isSubmitting} className="!bg-[#0D4F97] !hover:bg-[#0b427d] text-white">
                  {isSubmitting ? "Salvando..." : "Salvar"}
                </Button>
              </div>
            </form>
          </Form>
        </div>
      </main>
    </div>
  );
}