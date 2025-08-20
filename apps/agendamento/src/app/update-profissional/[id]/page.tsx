"use client";

import { useForm, FormProvider } from "react-hook-form";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

import FormHealthProfessional from "@/components/forms/form-health-professional";
import { useGetByIdProfissional } from "@/hooks/profissional/use-get-by-id-profissional";
import { useUpdateProfissional } from "@/hooks/profissional/use-update-profissional";

export default function AtualizarProfissional() {
  const router = useRouter();
  const { profissional, loading: loadingProf, error: errorProf } =
    useGetByIdProfissional();
  const { updateProfissional, loading, error, success } =
    useUpdateProfissional();

  const form = useForm({
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

  useEffect(() => {
    fetch("https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome")
      .then((res) => res.json())
      .then((data) => setEstados(data));
  }, []);

  useEffect(() => {
    const estadoSelecionado = form.getValues("estado");
    if (estadoSelecionado) {
      fetch(`https://servicodados.ibge.gov.br/api/v1/localidades/estados/${estadoSelecionado}/municipios`)
        .then((res) => res.json())
        .then((data) => setCidades(data));
    }
  }, [form.watch("estado")]);

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

  async function onSubmit(values: any) {
    if (!profissional?.id) return;
    await updateProfissional(profissional.id, values);
  }

  function onCancel() {
    router.push("/visualization-professional");
  }

  if (loadingProf) return <p>Carregando dados...</p>;
  if (errorProf) return <p className="text-red-500">Erro: {errorProf}</p>;

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">Atualizar Profissional</h1>
      <FormProvider {...form}>
        <FormHealthProfessional
          estados={estados}
          cidades={cidades}
          loading={loading}
          error={error}
          success={success}
          onCancel={onCancel}
          onSubmit={onSubmit}
          submitLabel="Salvar"
        />
      </FormProvider>
    </div>
  );
}
