import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
  Form,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { formatCEP } from "@/lib/formats";
import { useForm } from "react-hook-form";
import { DialogProps } from "./dialog-types";

import { zodResolver } from "@hookform/resolvers/zod";
import { EditAddress } from "@/schemas/member-schemas";
import z from "zod";

export function EditAddressDialog({
  open,
  member,
  onOpenChange,
}: DialogProps<z.infer<typeof EditAddress>>) {
  const form = useForm<z.input<typeof EditAddress>>({
    resolver: zodResolver(EditAddress),
    mode: "onBlur",
    defaultValues: {
      street: member?.address?.street ?? "",
      district: member?.address?.neighborhood ?? "",
      city: member?.address?.city ?? "",
      state: member?.address?.state ?? "",
      cep: member?.address?.cep ?? "",
    },
  });

  const onSubmit = async (values: z.input<typeof EditAddress>) => {
    const response = await fetch(`/api/pessoas/${member.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        ...member,
        nationality: member.birthplace,

        address: {
          street: values.street ?? member.address.street,
          neighborhood: values.district ?? member.address.neighborhood,
          city: values.city ?? member.address.city,
          state: values.state ?? member.address.state,
          cep: values.cep ?? member.address.cep,
          number: values.street.replaceAll(/\D/g, "") ?? member.address.number,
        },
      }),
    });

    if (!response.ok) {
      throw new Error("Ocorreu um erro ao atualizar pessoa.");
    }

    onOpenChange(false);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[600px]">
        <DialogHeader>
          <DialogTitle className="text-[#0D4F97]">
            Editar Endereço Residencial
          </DialogTitle>
        </DialogHeader>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="street"
              render={({ field }) => (
                <FormItem className="md:col-span-2">
                  <FormLabel>Rua *</FormLabel>
                  <FormControl>
                    <Input placeholder="Adielson Assis Alves, 49" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="district"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Bairro *</FormLabel>
                  <FormControl>
                    <Input placeholder="Centro" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="city"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Cidade *</FormLabel>
                  <FormControl>
                    <Input placeholder="Esperança" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="state"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Estado *</FormLabel>
                  <FormControl>
                    <Input placeholder="Paraiba" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="cep"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>CEP *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="58135-000"
                      maxLength={9}
                      value={field.value}
                      onChange={(e) => {
                        const formated = formatCEP(e.target.value);
                        field.onChange(formated);
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <DialogFooter className="pt-6">
              <Button
                className="text-[#0D4F97]"
                variant="outline"
                onClick={() => onOpenChange(false)}
                type="button"
              >
                Cancelar
              </Button>
              <Button
                type="submit"
                className="text-white !bg-[#0D4F97] !hover:bg-[#0b427d]"
              >
                Salvar Alterações
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
}
