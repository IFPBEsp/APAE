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
import { useCreateServiceArea } from "@/hooks/service-area/use-create-service-area";
import { CreateCare } from "@/schemas/care-schemas";

export type DialogProps = Readonly<{
  open: boolean;
  onOpenChange: (value: boolean) => void;
  onSuccess?: (name: string) => void;
  onConfirm?: () => Promise<void>;
}>;

export function CreateCareDialog({ open, onOpenChange, onSuccess, onConfirm }: DialogProps) {
  const { create } = useCreateServiceArea();
  const form = useForm<z.infer<typeof CreateCare>>({
    resolver: zodResolver(CreateCare),
    defaultValues: { name: "" },
  });

  const onSubmit = async (data: z.infer<typeof CreateCare>) => {
    await create(data.name);
    onOpenChange(false);
    await onConfirm?.();
    onSuccess?.(data.name);
    form.reset();
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Novo Atendimento</DialogTitle>
        </DialogHeader>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nome do Atendimento</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: Oftamologista" {...field} />
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
