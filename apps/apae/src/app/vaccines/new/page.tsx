"use client";

import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ArrowLeft } from "lucide-react";
import { useVaccinesContext } from "@/hooks/use-vaccines";
import { CreateVaccine } from "@/schemas/vaccine-schemas";
import { z } from "zod";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form";

export default function NewVaccinePage() {
    const router = useRouter();
    const { createVaccine } = useVaccinesContext();
    const form = useForm<z.infer<typeof CreateVaccine>>({
        resolver: zodResolver(CreateVaccine),
        mode: "onChange",
        defaultValues: {
            name: "",
        },
    });

    const onSubmit = async (data: z.infer<typeof CreateVaccine>) => {
        await createVaccine(data);
        router.push("/vaccines");
    };

    return (
        <div className="!bg-white min-h-screen">
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
                        Nova Vacina
                    </h1>

                    <Form {...form}>
                        <form
                            onSubmit={form.handleSubmit(onSubmit)}
                            className="space-y-4"
                        >
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
                                    {form.formState.isSubmitting
                                        ? "Salvando..."
                                        : "Salvar"}
                                </Button>
                            </div>
                        </form>
                    </Form>
                </div>
            </main>
        </div>
    );
}
