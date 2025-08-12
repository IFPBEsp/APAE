"use client";

import { useForm } from "react-hook-form";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage
} from "@/components/ui/form";

export default function CadastroProfissional() {
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

  function onSubmit(values: any) {
    console.log("Dados salvos:", values);
  }

  return (
    <div className="flex min-h-screen font-sans">
      <div className="flex-1 p-5">
        <header className="mb-8">
          <h1 className="text-2xl font-bold text-black">Cadastrar Profissional</h1>
        </header>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="max-w-full mx-auto space-y-4">
            <FormField
              control={form.control}
              name="nomeCompleto"
              rules={{ required: "Digite o nome completo" }}
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
              rules={{
                required: "Informe o email",
                pattern: { value: /^\S+@\S+$/i, message: "Email inválido" },
              }}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Email</FormLabel>
                  <FormControl>
                    <Input type="email" placeholder="Ex: profissional@exemplo.com" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <div className="flex gap-5 max-md:flex-col">
              <FormField
                control={form.control}
                name="documentoProfissional"
                rules={{ required: "Informe o documento profissional" }}
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>Documento profissional</FormLabel>
                    <FormControl>
                      <Input placeholder="Ex: CRM/SP 123456" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="areaSaude"
                rules={{ required: "Selecione uma área" }}
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>Área da saúde</FormLabel>
                    <FormControl>
                      <Select
                        value={field.value}
                        onValueChange={field.onChange}
                      >
                        <SelectTrigger>
                          <SelectValue placeholder="Selecione uma opção" />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="Medicina">Medicina</SelectItem>
                          <SelectItem value="Enfermagem">Enfermagem</SelectItem>
                          <SelectItem value="Fisioterapia">Fisioterapia</SelectItem>
                          <SelectItem value="Psicologia">Psicologia</SelectItem>
                          <SelectItem value="Nutrição">Nutrição</SelectItem>
                        </SelectContent>
                      </Select>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <div className="flex gap-5 max-md:flex-col">
              <FormField
                control={form.control}
                name="cpf"
                rules={{
                  required: "Informe o CPF",
                  minLength: { value: 11, message: "CPF inválido" },
                }}
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>CPF</FormLabel>
                    <FormControl>
                      <Input placeholder="Ex: 123.456.789-00" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="telefone"
                rules={{
                  required: "Informe o telefone",
                  minLength: { value: 8, message: "Telefone inválido" },
                }}
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>Telefone</FormLabel>
                    <FormControl>
                      <Input placeholder="Ex: (11) 98765-4321" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
            
            <div className="flex justify-end gap-3 pt-4">
              <Button
                type="button"
                variant="outline"
                className="border-[#B2B9DE] text-[#B2B9DE] hover:bg-[#B2B9DE] hover:text-white"
              >
                Cancelar
              </Button>
              <Button
                type="submit"
                className="bg-black text-white hover:bg-neutral-800"
              >
                Salvar
              </Button>
            </div>
          </form>
        </Form>
      </div>
    </div>
  );
}
