"use client";

import { useEffect } from "react";
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
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Checkbox } from "@/components/ui/checkbox";

import { useGetByIdProfissional } from "@/hooks/profissional/use-get-by-id-profissional";
import { useUpdateProfissional } from "@/hooks/profissional/use-update-profissional";

const diasDaSemana = [
  { id: "segunda", label: "Segunda" },
  { id: "terca", label: "Terça" },
  { id: "quarta", label: "Quarta" },
  { id: "quinta", label: "Quinta" },
  { id: "sexta", label: "Sexta" },
];

const turnos = [
  { id: "manha", label: "Manhã" },
  { id: "tarde", label: "Tarde" },
];

export type FormData = {
  nome: string;
  email: string;
  docProfissional: string;
  areaDaSaude: string;
  telefone: string;
  disponibilidade: {
    [key: string]: {
      [key: string]: boolean;
    };
  };
};

export default function AtualizarProfissional() {
  const router = useRouter();
  const { profissional, loading: loadingProf, error: errorProf } =
    useGetByIdProfissional();
  const { updateProfissional, loading, error, success } =
    useUpdateProfissional();

  const form = useForm<FormData>({
    defaultValues: {
      nome: "",
      email: "",
      docProfissional: "",
      areaDaSaude: "",
      telefone: "",
      disponibilidade: {
        manha: {
          segunda: false,
          terca: false,
          quarta: false,
          quinta: false,
          sexta: false,
        },
        tarde: {
          segunda: false,
          terca: false,
          quarta: false,
          quinta: false,
          sexta: false,
        },
      },
    },
  });

  useEffect(() => {
    if (profissional) {
      const disponibilidadeState = {
        manha: {
          segunda: false,
          terca: false,
          quarta: false,
          quinta: false,
          sexta: false,
        },
        tarde: {
          segunda: false,
          terca: false,
          quarta: false,
          quinta: false,
          sexta: false,
        },
      };

      if (profissional.disponibilidade && profissional.disponibilidade.length > 0) {
        profissional.disponibilidade.forEach((item) => {
          const dia = item.dia.toLowerCase();
          const turno = item.turno.toLowerCase();
          if (
            disponibilidadeState[turno] &&
            disponibilidadeState[turno][dia] !== undefined
          ) {
            disponibilidadeState[turno][dia] = true;
          }
        });
      }

      form.reset({
        nome: profissional.nome,
        email: profissional.email,
        docProfissional: profissional.docProfissional,
        areaDaSaude: profissional.areaDaSaude,
        telefone: profissional.telefone,
        disponibilidade: disponibilidadeState,
      });
    }
  }, [profissional, form]);

  async function onSubmit(data: FormData) {
    if (!profissional?.id) {
      console.error("ID do profissional não encontrado");
      return;
    }

    const disponibilidadePayload = [];
    const { disponibilidade } = data;
    for (const turno in disponibilidade) {
      for (const dia in disponibilidade[turno]) {
        if (disponibilidade[turno][dia]) {
          disponibilidadePayload.push({
            dia: dia.toUpperCase(),
            turno: turno.toUpperCase(),
          });
        }
      }
    }

    const payload = {
      nome: data.nome,
      email: data.email,
      docProfissional: data.docProfissional,
      areaDaSaude: data.areaDaSaude,
      telefone: data.telefone,
      disponibilidade: disponibilidadePayload,
    };

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
          className="space-y-8 max-w-4xl"
        >
          <FormField
            control={form.control}
            name="nome"
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

          <FormField
            control={form.control}
            name="docProfissional"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Documento profissional</FormLabel>
                <FormControl>
                  <Input {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <FormField
              control={form.control}
              name="areaDaSaude"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Área da saúde</FormLabel>
                  <FormControl>
                    <Select onValueChange={field.onChange} value={field.value}>
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

            <FormField
              control={form.control}
              name="telefone"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Telefone</FormLabel>
                  <FormControl>
                    <Input {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
          
          <div className="space-y-4">
            <FormLabel>Disponibilidade</FormLabel>
            <FormDescription>
              Marque os dias e turnos em que o profissional está disponível.
            </FormDescription>
            <div className="border rounded-md">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead className="w-[100px]">Turno</TableHead>
                    {diasDaSemana.map((dia) => (
                      <TableHead key={dia.id} className="text-center">
                        {dia.label}
                      </TableHead>
                    ))}
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {turnos.map((turno) => (
                    <TableRow key={turno.id}>
                      <TableCell className="font-medium">
                        {turno.label}
                      </TableCell>
                      {diasDaSemana.map((dia) => (
                        <TableCell key={dia.id} className="text-center">
                          <FormField
                            control={form.control}
                            name={`disponibilidade.${turno.id}.${dia.id}`}
                            render={({ field }) => (
                              <FormItem className="flex items-center justify-center">
                                <FormControl>
                                  <Checkbox
                                    checked={field.value}
                                    onCheckedChange={field.onChange}
                                  />
                                </FormControl>
                              </FormItem>
                            )}
                          />
                        </TableCell>
                      ))}
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          </div>

          {loading && <p className="text-blue-500">Salvando...</p>}
          {error && <p className="text-red-500">Erro: {error}</p>}
          {success && (
            <p className="text-green-600">
              Profissional atualizado com sucesso!
            </p>
          )}

          <div className="flex justify-end gap-4 pt-4">
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
