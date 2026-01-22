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
import { formatPhone } from "@/lib/formats";
import { useForm } from "react-hook-form";
import { DoubleCheckboxFormField } from "../../cadastro/form";
import { DialogProps } from "./dialog-types";

import { zodResolver } from "@hookform/resolvers/zod";
import { EditPersonal } from "@/schemas/member-schemas";
import z from "zod";

export function EditPersonalDialog({
  open,
  member,
  onOpenChange,
}: DialogProps<z.infer<typeof EditPersonal>>) {
  const form = useForm<z.input<typeof EditPersonal>>({
    resolver: zodResolver(EditPersonal),
    mode: "onBlur",
    defaultValues: {
      name: member?.fullName ?? "",
      birth: {
        date: new Date(member?.birthDate),
        place: member?.birthplace ?? "",
      },
      phone: member?.contact ?? "",
      allergies: member?.allergies ?? "",
      student: member?.isStudent ?? false,
      registrationDate: new Date(member?.registrationDate),
    },
  });

  const onSubmit = async (values: z.output<typeof EditPersonal>) => {
    const response = await fetch(`/api/pessoas/${member.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        ...member,
        nationality: member.birthplace,

        fullName: values.name,
        birthDate: values.birth.date.toISOString().split("T")[0],
        birthplace: values.birth.place,
        contact: values.phone,
        allergies: values.allergies,
        isStudent: values.student,
        registrationDate: values.registrationDate.toISOString().split("T")[0],
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
            Editar Dados Pessoais
          </DialogTitle>
        </DialogHeader>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem className="md:col-span-2">
                  <FormLabel>Nome Completo *</FormLabel>
                  <FormControl>
                    <Input placeholder="Digite o nome completo" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="birth.date"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Data de Nascimento *</FormLabel>
                  <FormControl>
                    <Input
                      type="date"
                      {...field}
                      value={
                        field.value instanceof Date &&
                        !Number.isNaN(field.value.getTime())
                          ? field.value.toISOString().split("T")[0]
                          : ""
                      }
                      onChange={(e) => field.onChange(new Date(e.target.value))}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="birth.place"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Naturalidade *</FormLabel>
                  <FormControl>
                    <Input placeholder="Brasil" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="phone"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Contato *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="(00) 00000-0000"
                      type="tel"
                      maxLength={15}
                      value={field.value}
                      onChange={(e) => {
                        const formatted = formatPhone(e.target.value);
                        field.onChange(formatted);
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="allergies"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Alergias *</FormLabel>
                  <FormControl>
                    <Input placeholder="Alergia a abacaxi" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <DoubleCheckboxFormField
              control={form.control}
              name="student"
              labels={{
                main: "Estudante? *",
                true: "Sim",
                false: "Não",
              }}
            />

            <FormField
              control={form.control}
              name="registrationDate"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Data de Cadastro *</FormLabel>
                  <FormControl>
                    <Input
                      type="date"
                      {...field}
                      value={
                        field.value instanceof Date &&
                        !Number.isNaN(field.value.getTime())
                          ? field.value.toISOString().split("T")[0]
                          : ""
                      }
                      onChange={(e) => field.onChange(new Date(e.target.value))}
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
