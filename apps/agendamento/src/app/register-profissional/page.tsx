"use client"; 

import { useForm, FormProvider } from "react-hook-form";
import { useRouter } from "next/navigation";
import FormHealthProfessional from "@/components/forms/form-health-professional";
import { useEstadosECidades } from "@/hooks/profissional/use-estados-cidades";
import { useCreateProfissional } from "@/hooks/profissional/use-create-profissional";

export default function CadastroProfissional() {
  const router = useRouter();
  const { create, loading, error, success } = useCreateProfissional();

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

  const { estados, cidades } = useEstadosECidades(form);

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
