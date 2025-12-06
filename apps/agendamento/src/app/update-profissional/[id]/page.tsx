"use client";

import { useForm, Controller, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { InputMask } from "@react-input/mask";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { useRouter } from "next/navigation";

import { useGetByIdProfissional } from "@/hooks/profissional/use-get-by-id-profissional";
import { useUpdateProfissional } from "@/hooks/profissional/use-update-profissional";
import { updateProfessionalSchema } from "@/schemas/profissional.schema";
import { STATES } from "@/lib/states";
import { JSX, useEffect } from "react";
import HealthAreaSelect from "@/components/shared/HealthAreaSelect";
import Disponibilidade from "@/components/forms/DisponibilidadeForm";

type UpdateFormValues = z.infer<typeof updateProfessionalSchema>;

export default function AtualizarProfissional(): JSX.Element {
  const router = useRouter();
  const { profissional, loading: loadingProf, error: errorProf,} = useGetByIdProfissional();
  const { updateProfissional, loading, error, success } = useUpdateProfissional();

  const defaultValues: Partial<UpdateFormValues> = {
    nomeCompleto: "",
    email: "",
    documentoProfissional: "",
    areaAtendimento: "",
    telefone: "",
    rg: "",
    estado: "",
    cidade: "",
    bairro: "",
    rua: "",
    numero: "",
    complemento: "",
    cep: "",
    disponibilidade: []
  };

  const form = useForm<UpdateFormValues>({
    resolver: zodResolver(updateProfessionalSchema),
    defaultValues,
  });

  useEffect(() => {
    if (!profissional) return;

    form.reset({
      nomeCompleto: profissional.name,
      email: profissional.email,
      documentoProfissional: profissional.professionalDocument,
      areaAtendimento: profissional.serviceArea.area,
      telefone: profissional.phoneNumber,
      rg: profissional.identityDocument,
      estado: profissional.address.state,
      cidade: profissional.address.city,
      bairro: profissional.address.neighborhood,
      rua: profissional.address.street,
      numero: profissional.address.number,
      complemento: profissional.address.complement,
      cep: profissional.address.cep,
    });
  }, [profissional, form]);

  const onSubmit: SubmitHandler<UpdateFormValues> = async (values) => {
    if (!profissional?.id) return;

    const availabilities = values.disponibilidade
      .filter((d) => d?.checked)
      .map((d) => ({
        day: d?.dia,
        shift: d?.turno,
      }));

    const payload = {
      name: values.nomeCompleto.trim(),
      email: values.email.trim(),
      professionalDocument: values.documentoProfissional.trim(),
      serviceArea: values.areaAtendimento,
      phoneNumber: values.telefone,
      identityDocument: values.rg.trim(),
      address: {
        state: values.estado,
        city: values.cidade.trim(),
        neighborhood: values.bairro.trim(),
        street: values.rua.trim(),
        number: values.numero?.trim(),
        complement: values.complemento?.trim(),
        cep: values.cep,
      },
      availabilities,
    };

    await updateProfissional(profissional.id, payload);
  };

  await updateProfissional(profissional.id, payload);
};

  const onCancel = () => {
    router.push("/visualization-professional");
  };

  if (loadingProf) return <p>Carregando dados...</p>;
  if (errorProf) return <p className="text-red-500">Erro: {errorProf}</p>;

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">Atualizar Profissional</h1>
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="space-y-6 max-w-2xl w-full mx-auto"
        >
          <FormField
            control={form.control}
            name="nomeCompleto"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Nome completo</FormLabel>
                <FormControl>
                  <Input {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Email</FormLabel>
                <FormControl>
                  <Input type="email" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="documentoProfissional"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Documento profissional</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: CRM/SP 123456" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <Controller
              control={form.control}
              name="areaAtendimento"
              render={({ field, fieldState }) => (
                <FormItem>
                  <FormLabel>Área de atendimento *</FormLabel>
                  <FormControl>
                    <HealthAreaSelect
                      value={field.value}
                      onChange={field.onChange}
                    />
                  </FormControl>
                  <FormMessage>{fieldState.error?.message}</FormMessage>
                </FormItem>
              )}
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="rg"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>RG</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: 1234567" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <Controller
              control={form.control}
              name="telefone"
              render={({ field, fieldState }) => (
                <FormItem>
                  <FormLabel>Telefone</FormLabel>
                  <FormControl>
                    <InputMask
                      mask="(__) _____-____"
                      replacement={{ _: /\d/ }}
                      value={field.value ?? ""}
                      onChange={(e) => field.onChange(e.target.value)}
                      onBlur={field.onBlur}
                      placeholder="(xx) xxxxx-xxxx"
                      className="w-full rounded-md border px-3 py-2"
                    />
                  </FormControl>
                  <FormMessage>{fieldState.error?.message}</FormMessage>
                </FormItem>
              )}
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <Controller
              control={form.control}
              name="estado"
              render={({ field, fieldState }) => (
                <FormItem>
                  <FormLabel>Estado</FormLabel>
                  <FormControl>
                    <Select onValueChange={field.onChange} value={field.value}>
                      <SelectTrigger
                        className={`w-full ${
                          fieldState.invalid
                            ? "border-red-500"
                            : "border-gray-300"
                        }`}
                      >
                        <SelectValue placeholder="Selecione um estado" />
                      </SelectTrigger>
                      <SelectContent>
                        {STATES.map((s) => (
                          <SelectItem key={s} value={s}>
                            {s}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </FormControl>
                  <FormMessage>{fieldState.error?.message}</FormMessage>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="cidade"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Cidade</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: João Pessoa" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <FormField
            control={form.control}
            name="rua"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Endereço</FormLabel>
                <FormControl>
                  <Input placeholder="Ex: Rua das Flores" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <FormField
              control={form.control}
              name="bairro"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Bairro</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: Centro" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <Controller
              control={form.control}
              name="cep"
              render={({ field, fieldState }) => (
                <FormItem>
                  <FormLabel>CEP</FormLabel>
                  <FormControl>
                    <InputMask
                      mask="_____-___"
                      replacement={{ _: /\d/ }}
                      value={field.value ?? ""}
                      onChange={(e) => field.onChange(e.target.value)}
                      onBlur={field.onBlur}
                      placeholder="12345-678"
                      className="w-full rounded-md border px-3 py-2"
                    />
                  </FormControl>
                  <FormMessage>{fieldState.error?.message}</FormMessage>
                </FormItem>
              )}
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="numero"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Número</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: 123" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="complemento"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Complemento</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: Apt 101" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <Disponibilidade control={form.control} watch={form.watch} />

          {loading && <p className="text-blue-500">Salvando...</p>}
          {error && <p className="text-red-500">{error}</p>}
          {success && (
            <p className="text-green-600">
              Profissional atualizado com sucesso!
            </p>
          )}
          <div className="flex justify-end gap-4">
            <Button type="button" variant="outline" onClick={onCancel}>
              Cancelar
            </Button>
            <Button
              type="submit"
              className="bg-blue-800 hover:bg-blue-900"
              disabled={form.formState.isSubmitting || loading}
            >
              Salvar
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
}
