"use client";

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
import { useRouter } from "next/navigation";

import { useCreateProfissional } from "@/hooks/profissional/use-create-profissional";

export default function CadastroProfissional() {
  const { create, loading, error, success } = useCreateProfissional();
  const router = useRouter();

  const form = useForm({
    defaultValues: {
      nomeCompleto: "",
      email: "",
      documentoProfissional: "",
      areaSaude: "",
      cpf: "",
      telefone: "",
    },
  });

  function onCancel() {
    router.push("/visualization-professional");
  }

  async function onSubmit(values: any) {
    try {
      const payload = {
        nome: values.nomeCompleto,
        email: values.email,
        docProfissional: values.documentoProfissional,
        areaDaSaude: values.areaSaude,
        telefone: values.telefone,
      };

      await create(payload);
    } catch (e) {
      console.error("Erro ao criar profissional", e);
    }
  }
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">Cadastrar Profissional</h1>

      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="space-y-6 max-w-2xl"
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

          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="areaSaude"
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
            <p className="text-green-600">Profissional criado com sucesso!</p>
          )}

          <div className="flex justify-end gap-4">
            <Button type="button" variant="outline" onClick={onCancel}>
              Cancelar
            </Button>
            <Button
              type="submit"
              className="bg-blue-800 hover:bg-blue-900"
              disabled={loading}
              style={{ background: '#0D4F97' }}
            >
              Cadastrar
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
}
