"use client";

import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ArrowLeft, Loader2 } from "lucide-react";
import { capitalizeFirst } from "@/lib/formats";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { updateDisorderSchema, type UpdateDisorderFormData } from "../disorders.schema";
import { useDisorderEdit } from "./use-disorder-edit";

interface DisorderEditFormProps {
  id: string;
}

export function DisorderEditForm({ id }: DisorderEditFormProps) {
  const router = useRouter();
  const { disorder, updateDisorder, isSubmitting } = useDisorderEdit(id);

  const form = useForm<UpdateDisorderFormData>({
    resolver: zodResolver(updateDisorderSchema),
    mode: "onChange",
    defaultValues: { name: "" },
  });

  useEffect(() => {
    if (disorder) form.reset({ name: disorder.name });
  }, [disorder, form]);

  const onSubmit = async (data: UpdateDisorderFormData) => {
    await updateDisorder({ id, name: data.name });
  };

  if (!disorder) {
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

          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
              <FormField
                control={form.control}
                name="name"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="font-semibold text-[#003B93]">
                      Nome do Transtorno
                    </FormLabel>
                    <FormControl>
                      <Input
                        placeholder="Ex: TDAH"
                        {...field}
                        onChange={(e) => field.onChange(capitalizeFirst(e.target.value))}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
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
          </Form>
        </div>
      </main>
    </div>
  );
}
