"use client";

import { useState } from "react";
import { criarCadastroAnual, criarContato, criarPessoa, criarPessoaResponsavel, criarTipoAtendimento, criarTipoDeficiencia, criarVacina, PessoaRequest } from "../service/pessoaService";
import { createBucket, uploadDocument } from "../service/documentoService";
import CadastroUm from "../components/CadastroUm";
import CadastroDois from "../components/CadastroDois";
import CadastroTres from "../components/CadastroTres";
import CadastroQuatro from "../components/CadastroQuatro";

interface DocumentUploadInfo {
  file: File;
  category: string;
  type: string;
}

export default function CadastroPage() {
  const [step, setStep] = useState(1);
  const [isLoading, setIsLoading] = useState(false);
  const [filesToUpload, setFilesToUpload] = useState<{ [key: string]: DocumentUploadInfo }>({});

  const [pessoaData, setPessoaData] = useState<PessoaRequest>({
    nomeCompleto: "",
    dataNascimento: "",
    numRegistroNasc: "",
    fls: "",
    livro: "",
    cartorio: "",
    cpf: "",
    rg: "",
    dataEmissaoRg: "",
    orgaoEmissorRg: "",
    cns: "",
    nis: "",
    dataCadastramento: new Date().toISOString().split("T")[0],
    numeroTelefone: "",
    contatoRequest: {
      enderecoAtivo: "S",
      comprovanteResidencia: "",
      endereco: "",
      bairro: "",
      cidade: "",
      estado: "",
      cep: "",
      naturalidade: "",
    },
    vacinacoesRequests: [],
    deficienciasRequests: [],
    atendimentosRequests: [],
    responsaveisRequests: [],
    cadastrosAnuaisRequests: [],
  });

  const nextStep = () => setStep((prev) => prev + 1);
  const prevStep = () => setStep((prev) => prev - 1);

  const addFile = (key: string, file: File, category: string, type: string) => {
    setFilesToUpload((prev) => ({
      ...prev,
      [key]: { file, category, type },
    }));
  };

  const handleSubmit = async () => {
    setIsLoading(true);
    try {
      // Criar pessoa
      const createdPessoa = await criarPessoa(pessoaData);
      const patientId = createdPessoa.id;
      console.log(`Pessoa criada com ID: ${patientId}`);

      // Criar contato
      if (pessoaData.contatoRequest) {
        await criarContato({ ...pessoaData.contatoRequest, pessoaId: patientId });
        console.log("Contato criado");
      }

      // Criar vacinas
      for (const vacina of pessoaData.vacinacoesRequests) {
        await criarVacina({ ...vacina, pessoaId: patientId });
      }
      console.log("Vacinas criadas");

      // Criar deficiências
      for (const deficiencia of pessoaData.deficienciasRequests) {
        await criarTipoDeficiencia({ ...deficiencia, pessoaId: patientId });
      }
      console.log("Deficiências criadas");

      // Criar atendimentos
      for (const atendimento of pessoaData.atendimentosRequests) {
        await criarTipoAtendimento({ ...atendimento, pessoaId: patientId });
      }
      console.log("Atendimentos criados");

      // Criar responsáveis
      for (const responsavel of pessoaData.responsaveisRequests) {
        await criarPessoaResponsavel({ ...responsavel, pessoaId: patientId });
      }
      console.log("Responsáveis criados");

      // Criar cadastros anuais
      for (const cadastroAnual of pessoaData.cadastrosAnuaisRequests) {
        await criarCadastroAnual({ ...cadastroAnual, pessoaId: patientId });
      }
      console.log("Cadastros anuais criados");

      // Criar bucket
      await createBucket(patientId);
      console.log(`Bucket criado para o paciente ${patientId}`);

      // Fazer upload de arquivos
      for (const key in filesToUpload) {
        const { file, category, type } = filesToUpload[key];
        console.log(category);
        console.log(type);
        await uploadDocument({
          patientId,
          year: new Date().getFullYear(),
          documentCategory: category,
          documentType: type,
          file,
        });
        console.log(`Arquivo ${key} enviado com sucesso.`);
      }

      alert("Cadastro realizado com sucesso! ✅");
      setStep(1);
    } catch (error) {
      console.error("Falha no processo de cadastro:", error);
      alert("❌ Erro ao realizar o cadastro. Verifique o console para mais detalhes.");
    } finally {
      setIsLoading(false);
    }
  };


  const renderStep = () => {
    if (isLoading) return <div>Enviando...</div>;

    switch (step) {
      case 1:
        return (
          <CadastroUm
            data={pessoaData}
            setData={setPessoaData}
            addFile={addFile}
            nextStep={nextStep}
          />
        );
      case 2:
        return (
          <CadastroDois
            data={pessoaData}
            setData={setPessoaData}
            addFile={addFile}
            nextStep={nextStep}
            prevStep={prevStep}
          />
        );
      case 3:
        return (
          <CadastroTres
            data={pessoaData}
            setData={setPessoaData}
            nextStep={nextStep}
            prevStep={prevStep}
          />
        );
      case 4:
        return (
          <CadastroQuatro
            data={pessoaData}
            setData={setPessoaData}
            addFile={addFile}
            prevStep={prevStep}
            handleSubmit={handleSubmit}
          />
        );
      default:
        return <div>Passo inválido</div>;
    }
  };

  return (
    <div style={{ fontFamily: "sans-serif", padding: "20px" }}>
      <h1>Formulário de Cadastro em Múltiplas Etapas</h1>
      {renderStep()}
    </div>
  );
}
