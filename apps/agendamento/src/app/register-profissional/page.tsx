"use client";

import { useForm, FormProvider } from "react-hook-form";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

import FormHealthProfessional from "@/components/forms/form-health-professional";
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

  function onCancel() {
    router.push("/visualization-professional");
  }

  async function onSubmit(values: any) {
    const payload = {
      nome: values.nomeCompleto,
      email: values.email,
      docProfissional: values.documentoProfissional,
      areaDaSaude: values.areaSaude,
      cpf: values.cpf,
      rg: values.rg,
      estado: values.estado,
      cidade: values.cidade,
      endereco: values.endereco,
      complemento: values.complemento,
      telefone: values.telefone,
      cep: values.cep,
    };
    await create(payload);
  }

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">Cadastrar Profissional</h1>
      <FormProvider {...form}>
        <FormHealthProfessional
          estados={estados}
          cidades={cidades}
          loading={loading}
          error={error}
          success={success}
          onCancel={onCancel}
          onSubmit={onSubmit}
          submitLabel="Cadastrar"
        />
      </FormProvider>
    </div>
  );
}
