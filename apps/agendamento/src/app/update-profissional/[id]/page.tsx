"use client";

import React, { useEffect } from "react";
import { useForm, FormProvider } from "react-hook-form";
import { useRouter } from "next/navigation";
import FormHealthProfessional, { FormValues } from "@/components/forms/form-health-professional";
import { useGetByIdProfissional } from "@/hooks/profissional/use-get-by-id-profissional";
import { useUpdateProfissional } from "@/hooks/profissional/use-update-profissional";
import { useEstadosECidades } from "@/hooks/profissional/use-estados-cidades";

export default function AtualizarProfissional() {
  const router = useRouter();
  const { profissional, loading: loadingProf, error: errorProf } = useGetByIdProfissional();
  const { updateProfissional, loading, error, success } = useUpdateProfissional();

  const form = useForm<FormValues>({
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

  const { estados, cidades } = useEstadosECidades(form);

  useEffect(() => {
    if (profissional && estados.length) {
      const estadoSelecionado = estados.find((e) => e.sigla === profissional.estadoSigla);

      form.reset({
        nomeCompleto: profissional.nome ?? "",
        email: profissional.email ?? "",
        documentoProfissional: profissional.docProfissional ?? "",
        areaSaude: profissional.areaDaSaude ?? "",
        cpf: profissional.cpf ?? "",
        rg: profissional.rg ?? "",
        estado: estadoSelecionado?.sigla ?? "",
        cidade: profissional.cidade ?? "",
        endereco: profissional.endereco ?? "",
        complemento: profissional.complemento ?? "",
        telefone: profissional.telefone ?? "",
        cep: profissional.cep ?? "",
      });
    }
  }, [profissional, estados, form]);

  const onSubmit = async (values: FormValues) => {
    if (!profissional?.id) return;

    const estadoSelecionado = estados.find((e) => e.sigla === values.estado);

    const payload = {
      nome: values.nomeCompleto,
      email: values.email,
      docProfissional: values.documentoProfissional,
      areaDaSaude: values.areaSaude,
      cpf: values.cpf,
      rg: values.rg,
      estado: estadoSelecionado?.sigla,
      cidade: values.cidade,
      endereco: values.endereco,
      complemento: values.complemento,
      telefone: values.telefone,
      cep: values.cep,
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
