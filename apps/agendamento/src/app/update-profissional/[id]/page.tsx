"use client";

import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useRouter } from "next/navigation";

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

export type FormData = {
  nomeCompleto: string;
  email: string;
  documentoProfissional: string;
  areaSaude: string;
  cpf: string;
  rg: string;
  estado: string;
  cidade: string;
  endereco: string;
  complemento?: string;
  telefone: string;
  cep: string;
};

export default function AtualizarProfissional() {
  const router = useRouter();
  const { profissional, loading: loadingProf, error: errorProf } =
    useGetByIdProfissional();
  const { updateProfissional, loading, error, success } =
    useUpdateProfissional();

  const form = useForm<FormData>({
    defaultValues: {
      nomeCompleto: "",
      email: "",
      documentoProfissional: "",
      areaSaude: "",
      cpf: "",
      rg: "",
      estado: "",
      cidade: "",
      endereco: "",
      complemento: "",
      telefone: "",
      cep: "",
    },
  });

  const [estados, setEstados] = useState<any[]>([]);
  const [cidades, setCidades] = useState<any[]>([]);

  // Buscar estados
  useEffect(() => {
    fetch(
      "https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome"
    )
      .then((res) => res.json())
      .then((data) => setEstados(data))
      .catch((err) => console.error("Erro ao carregar estados", err));
  }, []);

  // Buscar cidades quando o estado mudar
  useEffect(() => {
    const estadoSelecionado = form.getValues("estado");
    if (estadoSelecionado) {
      fetch(
        `https://servicodados.ibge.gov.br/api/v1/localidades/estados/${estadoSelecionado}/municipios`
      )
        .then((res) => res.json())
        .then((data) => setCidades(data))
        .catch((err) => console.error("Erro ao carregar cidades", err));
    }
  }, [form.watch("estado")]);

  // Preencher dados do profissional quando carregar
  useEffect(() => {
    if (profissional) {
      form.reset({
        nomeCompleto: profissional.nome ?? "",
        email: profissional.email ?? "",
        documentoProfissional: profissional.docProfissional ?? "",
        areaSaude: profissional.areaDaSaude ?? "",
        telefone: profissional.telefone ?? "",
      });
    }
  }, [profissional, form]);

  async function onSubmit(data: FormData) {
    if (!profissional?.id) {
      console.error("ID do profissional não encontrado");
      return;
    }

    const payload = { ...data };

    await updateProfissional(profissional.id, payload);
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
          {/* Nome completo */}
          <FormField
            control={form.control}
            name="nomeCompleto"
            rules={{ required: "Nome completo é obrigatório" }}
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

          {/* Email */}
          <FormField
            control={form.control}
            name="email"
            rules={{ required: "Email é obrigatório" }}
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

          {/* Documento Profissional + Área da saúde */}
          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="documentoProfissional"
              rules={{ required: "Documento profissional é obrigatório" }}
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

            <FormField
              control={form.control}
              name="areaSaude"
              rules={{ required: "Área da saúde é obrigatória" }}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Área da saúde</FormLabel>
                  <FormControl>
                    <Input placeholder="Digite uma área" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          {/* CPF + RG */}
          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="cpf"
              rules={{ required: "CPF é obrigatório" }}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>CPF</FormLabel>
                  <FormControl>
                    <Input placeholder="000.000.000-00" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="rg"
              rules={{ required: "RG é obrigatório" }}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>RG</FormLabel>
                  <FormControl>
                    <Input placeholder="00.000.000-0" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          {/* Estado + Cidade */}
          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="estado"
              rules={{ required: "Estado é obrigatório" }}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Estado</FormLabel>
                  <Select onValueChange={field.onChange} value={field.value}>
                    <FormControl>
                      <SelectTrigger className="w-full">
                        <SelectValue placeholder="Selecione um estado" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {estados.map((estado) => (
                        <SelectItem key={estado.id} value={estado.sigla}>
                          {estado.nome}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="cidade"
              rules={{ required: "Cidade é obrigatória" }}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Cidade</FormLabel>
                  <Select
                    onValueChange={field.onChange}
                    value={field.value}
                    disabled={!cidades.length}
                  >
                    <FormControl>
                      <SelectTrigger className="w-full">
                        <SelectValue placeholder="Selecione uma cidade" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {cidades.map((cidade) => (
                        <SelectItem key={cidade.id} value={cidade.nome}>
                          {cidade.nome}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          {/* Endereço */}
          <FormField
            control={form.control}
            name="endereco"
            rules={{ required: "Endereço é obrigatório" }}
            render={({ field }) => (
              <FormItem>
                <FormLabel>Endereço</FormLabel>
                <FormControl>
                  <Input placeholder="Rua Exemplo, 123" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          {/* Complemento (opcional) */}
          <FormField
            control={form.control}
            name="complemento"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Complemento</FormLabel>
                <FormControl>
                  <Input placeholder="Apartamento, bloco, sala..." {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          {/* Telefone + CEP */}
          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="telefone"
              rules={{ required: "Telefone é obrigatório" }}
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

            <FormField
              control={form.control}
              name="cep"
              rules={{ required: "CEP é obrigatório" }}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>CEP</FormLabel>
                  <FormControl>
                    <Input placeholder="00000-000" {...field} />
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
