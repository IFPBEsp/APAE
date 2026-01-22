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
import { formatCEP, formatCPF, formatRG } from "@/lib/formats";
import { useFieldArray, useForm } from "react-hook-form";
import { DoubleCheckboxFormField } from "../../cadastro/form";
import { DialogProps } from "./dialog-types";

import { zodResolver } from "@hookform/resolvers/zod";
import { EditGuardians } from "@/schemas/member-schemas";
import z from "zod";

export function EditGuardiansDialog({
  open,
  member,
  onOpenChange,
}: DialogProps<z.infer<typeof EditGuardians>>) {
  const form = useForm<z.input<typeof EditGuardians>>({
    resolver: zodResolver(EditGuardians),
    mode: "onBlur",
    defaultValues: {
      guardian: {
        address: {
          cep: member?.guardian?.address?.cep ?? "",
          city: member?.guardian?.address?.city ?? "",
          district: member?.guardian?.address?.neighborhood ?? "",
          state: member?.guardian?.address?.state ?? "",
          street:
            member?.guardian?.address?.street &&
            member?.guardian?.address?.number
              ? `${member.guardian.address.street}, ${member.guardian.address.number}`
              : "",
        },
        contact: member?.guardian?.contact ?? "",
        kinship: member?.guardian?.kinship ?? "",
        name: member?.guardian?.name ?? "",
      },
      kinships:
        member?.parents?.map((parent: any) => ({
          rg: parent.rg ?? "",
          cpf: parent.cpf ?? "",
          alive: parent.isAlive ?? "",
          name: parent.name ?? "",
          occupation: parent.profession ?? "",
          type: parent.kinship ?? "",
        })) ?? [],
    },
  });

  const { fields, append, remove } = useFieldArray({
    control: form.control,
    name: "kinships",
  });

  const onSubmit = async (values: z.output<typeof EditGuardians>) => {
    const response = await fetch(`/api/pessoas/${member.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        ...member,
        nationality: member.birthplace,

        guardian: {
          address: {
            cep: values.guardian.address.cep ?? member.guardian.address.cep,
            city: values.guardian.address.city ?? member.guardian.address.city,
            neighborhood:
              values.guardian.address.district ??
              member.guardian.address.neighborhood,
            state:
              values.guardian.address.state ?? member.guardian.address.state,
            street:
              values.guardian.address.street?.replaceAll(/, \d+/g, "") ??
              member.guardian.address.street,
            number:
              values.guardian.address.street?.replaceAll(/\D/g, "") ??
              member.guardian.address.number,
          },
          contact: values.guardian.contact ?? member.guardian.contact,
          kinship: values.guardian.kinship ?? member.guardian.kinship,
          name: values.guardian.name ?? member.guardian.name,
        },
        parents: values.kinships.map((parent) => ({
          rg: parent.rg,
          cpf: parent.cpf,
          isAlive: parent.alive,
          name: parent.name,
          profession: parent.occupation,
          kinship: parent.type,
        })),
      }),
    });

    if (!response.ok) {
      throw new Error("Ocorreu um erro ao atualizar pessoa.");
    }

    onOpenChange(false);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[600px]  max-h-[80vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="text-[#0D4F97]">
            Editar Dados dos Responsáveis
          </DialogTitle>
        </DialogHeader>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="guardian.name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nome Completo do Responsável *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Digite o nome completo do responsável"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="guardian.contact"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Contato de Emergência *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Número de telefone, email e etc."
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="guardian.address.street"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Rua do Responsável *</FormLabel>
                  <FormControl>
                    <Input placeholder="Adielson Assis Alves, 49" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="guardian.address.cep"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>CEP do Responsável *</FormLabel>
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

            <FormField
              control={form.control}
              name="guardian.address.state"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Estado do Responsável *</FormLabel>
                  <FormControl>
                    <Input placeholder="Paraiba" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="guardian.address.city"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Cidade do Responsável *</FormLabel>
                  <FormControl>
                    <Input placeholder="Esperança" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="guardian.address.district"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Bairro do Responsável *</FormLabel>
                  <FormControl>
                    <Input placeholder="Centro" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="guardian.kinship"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Parentesco do Responsável *</FormLabel>
                  <FormControl>
                    <Input placeholder="Irmã" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {fields.map((item, index) => (
              <div
                key={item.id}
                className="mt-6 rounded-md border p-4 space-y-4"
              >
                <h3 className="font-semibold text-sm text-[#0D4F97]">
                  Parente {index + 1}
                </h3>

                <FormField
                  control={form.control}
                  name={`kinships.${index}.name`}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Nome Completo do Parente *</FormLabel>
                      <FormControl>
                        <Input
                          placeholder="Digite o nome completo do parente"
                          {...field}
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name={`kinships.${index}.rg`}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>RG do parente *</FormLabel>
                      <FormControl>
                        <Input
                          placeholder="1.234.567"
                          maxLength={9}
                          value={field.value}
                          onChange={(e) =>
                            field.onChange(formatRG(e.target.value))
                          }
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name={`kinships.${index}.occupation`}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Profissão do Parente? *</FormLabel>
                      <FormControl>
                        <Input placeholder="Profissão" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name={`kinships.${index}.cpf`}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>CPF do parente *</FormLabel>
                      <FormControl>
                        <Input
                          placeholder="000.000.000-00"
                          maxLength={14}
                          value={field.value}
                          onChange={(e) =>
                            field.onChange(formatCPF(e.target.value))
                          }
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name={`kinships.${index}.type`}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Parentesco *</FormLabel>
                      <FormControl>
                        <Input placeholder="Digite o parentesco" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormItem>
                  <FormControl>
                    <Button
                      className="bg-transparent mt-5.5"
                      onClick={() => remove(index)}
                      type="button"
                      variant="outline"
                    >
                      Remover parente
                    </Button>
                  </FormControl>
                  <FormMessage />
                </FormItem>

                <div className="md:col-span-2">
                  <DoubleCheckboxFormField
                    control={form.control}
                    name={`kinships.${index}.alive`}
                    labels={{
                      main: "Vivo? *",
                      true: "Sim",
                      false: "Não",
                    }}
                  />
                </div>
              </div>
            ))}

            <button
              type="button"
              onClick={() =>
                append({
                  name: "",
                  rg: "",
                  cpf: "",
                  alive: true,
                  occupation: "",
                  type: "",
                })
              }
              className="rounded px-3 py-2 border"
            >
              Adicionar Parente
            </button>

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
