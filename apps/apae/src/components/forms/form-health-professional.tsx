"use client";

import { useState } from "react";
import { useFormContext } from "react-hook-form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";

type State = { id: number; nome: string; sigla: string };
type City = { id: number; nome: string };

export type FormValues = {
  nomeCompleto: string;
  email: string;
  documentoProfissional: string;
  areaSaude: string;
  cpf: string;
  rg: string;
  state: string;
  cidade: string;
  endereco: string;
  complemento?: string;
  telefone: string;
  cep: string;
};

type Props = {
  states: State[];
  cidades: City[];
  loading?: boolean;
  error?: string | null;
  success?: boolean;
  onCancel: () => void;
  onSubmit: (values: FormValues) => void;
  submitLabel: string;
};

export default function FormHealthProfessional({
  states,
  cidades,
  loading,
  error,
  success,
  onCancel,
  onSubmit,
  submitLabel,
}: Props) {
  const form = useFormContext<FormValues>();

  const [areasSaude, setAreasSaude] = useState<string[]>(["Fisioterapia", "Nutrição", "Psicologia", "Psiquiatria"]);
  const [novaArea, setNovaArea] = useState("");
  const [dialogOpen, setDialogOpen] = useState(false);
  const [areaSearch, setAreaSearch] = useState("");
  const [areaSelectOpen, setAreaSelectOpen] = useState(false);
  const [stateSelectOpen, setStateSelectOpen] = useState(false);
  const [cidadeSelectOpen, setCidadeSelectOpen] = useState(false);

  const filteredAreas = areasSaude.filter((area) => area.toLowerCase().includes(areaSearch.toLowerCase()));

  const handleAddArea = () => {
    if (novaArea && !areasSaude.includes(novaArea)) {
      setAreasSaude([...areasSaude, novaArea]);
      form.setValue("areaSaude", novaArea);
      setNovaArea("");
      setDialogOpen(false);
    }
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6 max-w-2xl">
        <FormField control={form.control} name="nomeCompleto" render={({ field }) => (
          <FormItem>
            <FormLabel>Nome completo</FormLabel>
            <FormControl><Input placeholder="Ex: Maria da Silva" {...field} /></FormControl>
            <FormMessage />
          </FormItem>
        )} />

        <FormField control={form.control} name="email" render={({ field }) => (
          <FormItem>
            <FormLabel>Email</FormLabel>
            <FormControl><Input type="email" placeholder="profissional@exemplo.com" {...field} /></FormControl>
            <FormMessage />
          </FormItem>
        )} />

        <div className="grid grid-cols-2 gap-4">
          <FormField control={form.control} name="documentoProfissional" render={({ field }) => (
            <FormItem>
              <FormLabel>Documento profissional</FormLabel>
              <FormControl><Input placeholder="Ex: CRM/SP 123456" {...field} /></FormControl>
              <FormMessage />
            </FormItem>
          )} />

          <FormField control={form.control} name="areaSaude" render={({ field }) => (
            <FormItem>
              <FormLabel>Área da Saúde</FormLabel>
              <FormControl>
                <Select onValueChange={field.onChange} value={field.value} open={areaSelectOpen} onOpenChange={setAreaSelectOpen}>
                  <SelectTrigger className="w-full"><SelectValue placeholder="Selecione a Área" /></SelectTrigger>
                  <SelectContent className="p-0">
                    <div className="sticky top-0 z-10 bg-white p-3 border-b">
                      <Input placeholder="Encontre a área..." value={areaSearch} onChange={(e) => setAreaSearch(e.target.value)} className="h-8" />
                    </div>
                    <div className="max-h-60 overflow-auto">
                      {filteredAreas.length > 0 ? filteredAreas.map((area) => (
                        <SelectItem key={area} value={area} className="pl-3 py-2">{area}</SelectItem>
                      )) : (
                        <div className="py-3 px-3 text-sm text-gray-500 text-center">Nenhuma área encontrada</div>
                      )}
                    </div>
                    <div className="border-t mt-1" />
                    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
                      <DialogTrigger asChild>
                        <button type="button" className="w-full text-left p-3 text-blue-600 hover:bg-blue-50 text-sm font-medium">+ Adicionar nova área</button>
                      </DialogTrigger>
                      <DialogContent>
                        <DialogHeader>
                          <DialogTitle>Adicione uma nova área da saúde</DialogTitle>
                        </DialogHeader>
                        <div className="space-y-4 py-4">
                          <FormItem>
                            <FormLabel>Título</FormLabel>
                            <FormControl>
                              <Input placeholder="Fisioterapia" value={novaArea} onChange={(e) => setNovaArea(e.target.value)} />
                            </FormControl>
                          </FormItem>
                          <Button onClick={handleAddArea} className="w-full bg-blue-800 hover:bg-blue-900">Criar</Button>
                        </div>
                      </DialogContent>
                    </Dialog>
                  </SelectContent>
                </Select>
              </FormControl>
              <FormMessage />
            </FormItem>
          )} />
        </div>

        <div className="grid grid-cols-2 gap-4">
          <FormField control={form.control} name="cpf" render={({ field }) => (
            <FormItem>
              <FormLabel>CPF</FormLabel>
              <FormControl><Input placeholder="000.000.000-00" {...field} /></FormControl>
              <FormMessage />
            </FormItem>
          )} />
          <FormField control={form.control} name="rg" render={({ field }) => (
            <FormItem>
              <FormLabel>RG</FormLabel>
              <FormControl><Input placeholder="00.000.000-0" {...field} /></FormControl>
              <FormMessage />
            </FormItem>
          )} />
        </div>

        <div className="grid grid-cols-2 gap-4">
          <FormField control={form.control} name="state" render={({ field }) => (
            <FormItem>
              <FormLabel>Estado</FormLabel>
              <FormControl>
                <Select onValueChange={field.onChange} value={field.value} open={stateSelectOpen} onOpenChange={setStateSelectOpen}>
                  <SelectTrigger><SelectValue placeholder="Selecione um estado" /></SelectTrigger>
                  <SelectContent>
                    {states.map((state) => (
                      <SelectItem key={state.id} value={state.sigla}>{state.nome}</SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </FormControl>
              <FormMessage />
            </FormItem>
          )} />

          <FormField control={form.control} name="cidade" render={({ field }) => (
            <FormItem>
              <FormLabel>Cidade</FormLabel>
              <FormControl>
                <Select onValueChange={field.onChange} value={field.value} disabled={!cidades.length} open={cidadeSelectOpen} onOpenChange={setCidadeSelectOpen}>
                  <SelectTrigger><SelectValue placeholder="Selecione uma cidade" /></SelectTrigger>
                  <SelectContent>
                    {cidades.map((cidade) => (
                      <SelectItem key={cidade.id} value={cidade.nome}>{cidade.nome}</SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </FormControl>
              <FormMessage />
            </FormItem>
          )} />
        </div>

        <FormField control={form.control} name="endereco" render={({ field }) => (
          <FormItem>
            <FormLabel>Endereço</FormLabel>
            <FormControl><Input placeholder="Rua Exemplo, 123" {...field} /></FormControl>
            <FormMessage />
          </FormItem>
        )} />
        <FormField control={form.control} name="complemento" render={({ field }) => (
          <FormItem>
            <FormLabel>Complemento</FormLabel>
            <FormControl><Input placeholder="Apartamento, bloco, sala..." {...field} /></FormControl>
            <FormMessage />
          </FormItem>
        )} />

        <div className="grid grid-cols-2 gap-4">
          <FormField control={form.control} name="telefone" render={({ field }) => (
            <FormItem>
              <FormLabel>Telefone</FormLabel>
              <FormControl><Input placeholder="(11) 98765-4321" {...field} /></FormControl>
              <FormMessage />
            </FormItem>
          )} />
          <FormField control={form.control} name="cep" render={({ field }) => (
            <FormItem>
              <FormLabel>CEP</FormLabel>
              <FormControl><Input placeholder="00000-000" {...field} /></FormControl>
              <FormMessage />
            </FormItem>
          )} />
        </div>

        {loading && <p className="text-blue-500">Salvando...</p>}
        {error && <p className="text-red-500">Erro: {error}</p>}
        {success && <p className="text-green-600">Operação realizada com sucesso!</p>}

        <div className="flex justify-end gap-4">
          <Button type="button" variant="outline" onClick={onCancel}>Cancelar</Button>
          <Button type="submit" className="bg-blue-800 hover:bg-blue-900" disabled={loading}>{submitLabel}</Button>
        </div>
      </form>
    </Form>
  );
}
