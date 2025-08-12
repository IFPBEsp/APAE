import React from "react";
import { PessoaRequest } from "../service/pessoaService";
import { Label } from "@radix-ui/react-label";
import { Input } from "@/components/ui/input";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import apae from "../images/apae.png";

interface Props {
  data: PessoaRequest;
  setData: React.Dispatch<React.SetStateAction<PessoaRequest>>;
  addFile: (key: string, file: File, category: string, type: string) => void;
  nextStep: () => void;
}

export default function CadastroUm({
  data,
  setData,
  addFile,
  nextStep,
}: Props) {
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setData((prev) => ({ ...prev, [name]: value }));
  };

  const handleContactChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setData((prev) => ({
      ...prev,
      contatoRequest: {
        ...prev.contatoRequest,
        [name]: value,
      },
    }));
  };

  return (
    <div className="flex min-h-screen w-full items-center justify-center p-4">
      <Card className="w-full min-h-screen max-w-6xl overflow-hidden shadow-2xl md:grid md:grid-cols-5 p-0">
        {/* Coluna da Imagem */}
        <div className="hidden md:col-span-2 md:block">
          <img
            src={apae.src}
            alt="Imagem de fundo do formulário"
            className="h-full w-full object-cover"
          />
        </div>

        {/* Conteúdo */}
        <div className="p-8 md:col-span-3">
          <CardHeader className="px-0 pt-0">
            <CardTitle>
              Passo 1: Dados Pessoais e Endereço
            </CardTitle>
            <CardDescription>
              Preencha as informações do assistido.
            </CardDescription>
          </CardHeader>

          <CardContent className="px-0 pb-0 space-y-8">
            {/* Seções lado a lado */}
            <div className="grid grid-cols-1 gap-8 md:grid-cols-2">
              {/* Dados Pessoais */}
              <div className="space-y-4">
                <h3 className="text-lg font-medium">Dados Pessoais</h3>

                <div className="grid w-full items-center gap-1.5">
                  <Label htmlFor="nomeCompleto">Nome Completo</Label>
                  <Input
                    type="text"
                    id="nomeCompleto"
                    name="nomeCompleto"
                    value={data.nomeCompleto}
                    onChange={handleChange}
                  />
                </div>

                <div className="grid w-full items-center gap-1.5">
                  <Label htmlFor="dataNascimento">Data de Nascimento</Label>
                  <Input
                    type="date"
                    id="dataNascimento"
                    name="dataNascimento"
                    value={data.dataNascimento}
                    onChange={handleChange}
                  />
                </div>

                <div className="grid w-full items-center gap-1.5">
                  <Label htmlFor="cpf">CPF</Label>
                  <Input
                    type="text"
                    id="cpf"
                    name="cpf"
                    value={data.cpf}
                    onChange={handleChange}
                  />
                </div>

                <div className="grid w-full items-center gap-1.5">
                  <Label htmlFor="numeroTelefone">Contato (Telefone)</Label>
                  <Input
                    type="tel"
                    id="numeroTelefone"
                    name="numeroTelefone"
                    value={data.contatoRequest.numeroTelefone}
                    onChange={handleContactChange}
                  />
                </div>

                <div className="grid w-full items-center gap-1.5">
                  <Label htmlFor="rg">RG</Label>
                  <Input
                    type="text"
                    id="rg"
                    name="rg"
                    value={data.rg}
                    onChange={handleChange}
                  />
                </div>

                <div className="grid w-full items-center gap-1.5">
                  <Label htmlFor="dataEmissaoRg">Data de Emissão do RG</Label>
                  <Input
                    type="date"
                    id="dataEmissaoRg"
                    name="dataEmissaoRg"
                    value={data.dataEmissaoRg}
                    onChange={handleChange}
                  />
                </div>

                <div className="grid w-full items-center gap-1.5">
                  <Label htmlFor="orgaoEmissorRg">Órgão Emissor do RG</Label>
                  <Input
                    type="text"
                    id="orgaoEmissorRg"
                    name="orgaoEmissorRg"
                    value={data.orgaoEmissorRg}
                    onChange={handleChange}
                  />
                </div>

                <div className="grid w-full items-center gap-1.5">
                  <Label htmlFor="numRegistroNasc">
                    Nº do Registro de Nascimento
                  </Label>
                  <Input
                    type="text"
                    id="numRegistroNasc"
                    name="numRegistroNasc"
                    value={data.numRegistroNasc}
                    onChange={handleChange}
                  />
                </div>
              </div>

              {/* Endereço */}
              <div className="space-y-4">
                <h3 className="text-lg font-medium">Endereço</h3>

                <div className="grid w-full items-center gap-1.5">
                  <Label htmlFor="cep">CEP</Label>
                  <Input
                    type="text"
                    id="cep"
                    name="cep"
                    value={data.contatoRequest.cep}
                    onChange={handleContactChange}
                  />
                </div>

                <div className="grid w-full items-center gap-1.5">
                  <Label htmlFor="bairro">Bairro</Label>
                  <Input
                    type="text"
                    id="bairro"
                    name="bairro"
                    value={data.contatoRequest.bairro}
                    onChange={handleContactChange}
                  />
                </div>

                <div className="grid w-full items-center gap-1.5">
                  <Label htmlFor="cidade">Cidade</Label>
                  <Input
                    type="text"
                    id="cidade"
                    name="cidade"
                    value={data.contatoRequest.cidade}
                    onChange={handleContactChange}
                  />
                </div>

                <div className="grid w-full items-center gap-1.5">
                  <Label htmlFor="estado">Estado</Label>
                  <Input
                    type="text"
                    id="estado"
                    name="estado"
                    value={data.contatoRequest.estado}
                    onChange={handleContactChange}
                  />
                </div>
              </div>
            </div>

            {/* Botão Navegação */}
            <div className="flex justify-end pt-6">
              <Button onClick={nextStep}>Próximo</Button>
            </div>
          </CardContent>
        </div>
      </Card>
    </div>
  );
}
