import React from "react";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import z from "zod";
import { capitalizeFirst } from "@/lib/formats";
import { useVaccinesContext } from "@/hooks/use-vaccines";
import { CreateVaccine } from "@/schemas/vaccine-schemas";

export type DialogProps = Readonly<{
  open: boolean;
  onOpenChange: (value: boolean) => void;
  onSuccess?: (name: string) => void;
}>;

export function CreateVaccineDialog({ open, onOpenChange, onSuccess }: DialogProps) {
  const { createVaccine } = useVaccinesContext();
  const form = useForm<z.infer<typeof CreateVaccine>>({
    resolver: zodResolver(CreateVaccine),
    defaultValues: { name: "" },
  });

  const onSubmit = async (data: z.infer<typeof CreateVaccine>) => {
    await createVaccine(data);
    onOpenChange(false);
    onSuccess?.(data.name);
    form.reset();
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Nova Vacina</DialogTitle>
        </DialogHeader>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nome da Vacina</FormLabel>
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
            <DialogFooter>
              <Button variant="outline" type="button" onClick={() => onOpenChange(false)}>
                Cancelar
              </Button>
              <Button type="submit" disabled={form.formState.isSubmitting}>
                Salvar
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
}
