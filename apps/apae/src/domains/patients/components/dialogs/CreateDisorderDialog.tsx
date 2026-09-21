import React from "react";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import z from "zod";
import { useDisordersContext } from "@/hooks/use-disorders";
import { CreateDisorder } from "@/schemas/disorder-schemas";

export type DialogProps = Readonly<{
  open: boolean;
  onOpenChange: (value: boolean) => void;
  onSuccess?: (name: string) => void;
}>;

export function CreateDisorderDialog({ open, onOpenChange, onSuccess }: DialogProps) {
  const { createDisorder } = useDisordersContext();
  const form = useForm<z.infer<typeof CreateDisorder>>({
    resolver: zodResolver(CreateDisorder),
    defaultValues: { name: "" },
  });

  const onSubmit = async (data: z.infer<typeof CreateDisorder>) => {
    await createDisorder(data);
    onOpenChange(false);
    onSuccess?.(data.name);
    form.reset();
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Novo Transtorno</DialogTitle>
        </DialogHeader>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nome do Transtorno</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: TDAH" {...field} />
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
