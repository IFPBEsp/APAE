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
import { cadastroSchema } from "@/schemas/profissional.schema";
import { HEALTH_AREAS } from "@/lib/health-areas";
import { STATES } from "@/lib/states";
import { JSX, useEffect } from "react";

type UpdateFormValues = z.infer<typeof cadastroSchema>;

export default function AtualizarProfissional(): JSX.Element {
  const router = useRouter();
  const {
    profissional,
    loading: loadingProf,
    error: errorProf,
  } = useGetByIdProfissional();
  const { updateProfissional, loading, error, success } =
    useUpdateProfissional();

  const defaultValues: UpdateFormValues = {
    nomeCompleto: "",
    email: "",
    documentoProfissional: "",
    areaSaude: "",
    telefone: "",
    rg: "",
    estado: "",
    cidade: "",
    bairro: "",
    rua: "",
    numero: "",
    complemento: "",
    cep: "",
  };

  const form = useForm<UpdateFormValues>({
    resolver: zodResolver(cadastroSchema),
    defaultValues,
  });

  useEffect(() => {
    if (!profissional) return;

    form.reset({
      nomeCompleto: profissional.nome,
      email: profissional.email,
      documentoProfissional: profissional.docProfissional,
      areaSaude: profissional.areaDaSaude,
      telefone: profissional.telefone,
      rg: profissional.rg,
      estado: profissional.endereco.estado,
      cidade: profissional.endereco.cidade,
      bairro: profissional.endereco.bairro,
      rua: profissional.endereco.rua,
      numero: profissional.endereco.numero,
      complemento: profissional.endereco.complemento,
      cep: profissional.endereco.cep,
    });
  }, [profissional, form]);

  const onSubmit: SubmitHandler<UpdateFormValues> = async (values) => {
    if (!profissional?.id) return;

    const payload = {
      nome: values.nomeCompleto.trim(),
      email: values.email.trim(),
      docProfissional: values.documentoProfissional.trim(),
      areaDaSaude: values.areaSaude,
      telefone: values.telefone,
      rg: values.rg.trim(),
      endereco: {
        estado: values.estado,
        cidade: values.cidade.trim(),
        bairro: values.bairro.trim(),
        rua: values.rua.trim(),
        numero: values.numero?.trim(),
        complemento: values.complemento?.trim(),
        cep: values.cep,
      },
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
                  <Input placeholder="Ex: Maria da Silva" {...field} />
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
                  <Input
                    type="email"
                    placeholder="profissional@exemplo.com"
                    {...field}
                  />
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
              name="areaSaude"
              render={({ field, fieldState }) => (
                <FormItem>
                  <FormLabel>Área da saúde</FormLabel>
                  <FormControl>
                    <Select onValueChange={field.onChange} value={field.value}>
                      <SelectTrigger
                        className={`w-full ${
                          fieldState.invalid
                            ? "border-red-500"
                            : "border-gray-300"
                        }`}
                      >
                        <SelectValue placeholder="Selecione uma opção" />
                      </SelectTrigger>
                      <SelectContent>
                        {HEALTH_AREAS.map((a) => (
                          <SelectItem key={a} value={a}>
                            {a}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
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

          <div className="grid grid-cols-2 gap-4">
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

          {loading && <p className="text-blue-500">Salvando...</p>}
          {error && <p className="text-red-500">{error}</p>}
          {success && (
            <p className="text-green-600">Profissional atualizado com sucesso!</p>
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
