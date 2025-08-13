"use client";

import { useEffect } from "react";
import { useForm } from "react-hook-form";

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

import { useGetByIdProfissional } from "@/hooks/profissional/use-get-by-id-profissional";
import { useUpdateProfissional } from "@/hooks/profissional/use-update-profissional";
import { useRouter } from "next/navigation";

export type FormData = {
  nome: string;
  email: string;
  docProfissional: string;
  areaDaSaude: string;
  telefone: string;
};

export default function AtualizarProfissional() {
  const router = useRouter();
  const {
    profissional,
    loading: loadingProf,
    error: errorProf,
  } = useGetByIdProfissional();
  const { updateProfissional, loading, error, success } =
    useUpdateProfissional();

  const form = useForm<FormData>({
    defaultValues: {
      nome: "",
      email: "",
      docProfissional: "",
      areaDaSaude: "",
      telefone: "",
    },
  });

  useEffect(() => {
    if (profissional) {
      form.reset({
        nome: profissional.nome,
        email: profissional.email,
        docProfissional: profissional.docProfissional,
        areaDaSaude: profissional.areaDaSaude,
        telefone: profissional.telefone,
      });
    }
  }, [profissional, form]);

  async function onSubmit(data: FormData) {
    if (!profissional?.id) {
      console.error("ID do profissional não encontrado");
      return;
    }
    await updateProfissional(profissional.id, data);
  }

  function onCancel() {
    router.push("/visualization-professional");
  }

  if (loadingProf) return <p>Carregando dados do profissional...</p>;
  if (errorProf) return <p className="text-red-500">Erro: {errorProf}</p>;

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">Atualizar Profissional</h1>

      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="space-y-6 max-w-2xl"
        >
          <FormField
            control={form.control}
            name="nome"
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

          <FormField
            control={form.control}
            name="docProfissional"
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

          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="areaDaSaude"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Área da saúde</FormLabel>
                  <Select onValueChange={field.onChange} value={field.value}>
                    <FormControl>
                      <SelectTrigger className="w-full">
                        <SelectValue placeholder="Selecione uma opção" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      <SelectItem value="Medicina">Medicina</SelectItem>
                      <SelectItem value="Enfermagem">Enfermagem</SelectItem>
                      <SelectItem value="Fisioterapia">Fisioterapia</SelectItem>
                      <SelectItem value="Psicologia">Psicologia</SelectItem>
                      <SelectItem value="Nutrição">Nutrição</SelectItem>
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="telefone"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Telefone</FormLabel>
                  <FormControl>
                    <Input placeholder="(11) 98765-4321" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          {loading && <p className="text-blue-500">Salvando...</p>}
          {error && <p className="text-red-500">Erro: {error}</p>}
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
              disabled={loading}
            >
              Salvar
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
}
