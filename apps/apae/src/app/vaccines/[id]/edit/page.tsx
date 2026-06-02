"use client";

import { useRouter, useParams } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Loader2, ArrowLeft } from "lucide-react";
import { useVaccinesContext, Vaccine } from "@/hooks/use-vaccines";
import { z } from "zod";
import { capitalizeFirst } from "@/lib/formats";
import { UpdateVaccine } from "@/schemas/vaccine-schemas";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { useEffect, useState } from "react";

// testeee

export default function EditVaccinePage() {
  const { fetchVaccine, updateVaccine } = useVaccinesContext();
  const [vaccine, setVaccine] = useState<Vaccine | null>(null);
  const params = useParams();
  const id = typeof params.id === "string" ? params.id : "";
  const router = useRouter();

  const form = useForm<z.infer<typeof UpdateVaccine>>({
    resolver: zodResolver(UpdateVaccine),
    mode: "onChange",
    defaultValues: {
      name: "",
    },
    values: {
      name: vaccine?.name ?? "",
    },
  });

  const onSubmit = async (data: z.infer<typeof UpdateVaccine>) => {
    if (!vaccine) return;

    await updateVaccine({
      id: vaccine.id,
      name: data.name,
    });

    router.push("/vaccines");
  };

  useEffect(() => {
    if (!id) return;

    const loadVaccine = async () => {
      try {
        const loaded = await fetchVaccine({ id });
        setVaccine(loaded);
      } catch {
        router.push("/vaccines");
      }
    };

    loadVaccine();
  }, [fetchVaccine, id, router]);

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

          <h1 className="text-2xl font-bold mb-6 text-[#003B93]">
            Editar Vacina
          </h1>

          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
              <FormField
                control={form.control}
                name="name"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="font-semibold text-[#003B93]">
                      Nome da Vacina
                    </FormLabel>
                    <FormControl>
                      <Input
                        placeholder="Ex: Hepatite B"
                        {...field}
                        onChange={(e) => field.onChange(capitalizeFirst(e.target.value))}
                      />                   
                     </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <div className="flex justify-end gap-2 pt-4">
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => router.back()}
                >
                  Cancelar
                </Button>
                <Button
                  type="submit"
                  disabled={form.formState.isSubmitting}
                  className="!bg-[#0D4F97] !hover:bg-[#0b427d] text-white"
                >
                  {form.formState.isSubmitting ? "Atualizando..." : "Atualizar"}
                </Button>
              </div>
            </form>
          </Form>
        </div>
      </main>
    </div>
  );
}
