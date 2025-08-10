import React, { useState, useEffect } from "react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { ArrowRight } from "lucide-react";
import apae from "../images/apae.png";

export interface ContatoRequest {
  enderecoAtivo: "S" | "N";
  comprovanteResidencia: string;
  endereco: string;
  bairro: string;
  cidade: string;
  estado: string;
  cep: string;
  naturalidade: string;
}

export interface PessoaRequest {
  nomeCompleto: string;
  dataNascimento: string;
  numRegistroNasc: string;
  fls: string;
  livro: string;
  cartorio: string;
  cpf: string;
  rg: string;
  dataEmissaoRg: string;
  orgaoEmissorRg: string;
  cns: string;
  nis: string;
  dataCadastramento: string;
  contatoRequest?: ContatoRequest;
}

interface CadastroUmProps {
  onNext: () => void;
}

export default function CadastroUm({ onNext }: CadastroUmProps) {
  const [formData, setFormData] = useState<Partial<PessoaRequest>>({
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
    contatoRequest: {
      enderecoAtivo: "S",
      comprovanteResidencia: "",
      endereco: "",
      bairro: "",
      cidade: "",
      estado: "",
      cep: "",
      naturalidade: "",
    } as ContatoRequest,
  });

  useEffect(() => {
    const savedData = localStorage.getItem("cadastroStep1");
    if (savedData) {
      setFormData(JSON.parse(savedData));
    }
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    if (name.startsWith("contato.")) {
      const contactField = name.split(".")[1];
      setFormData((prev) => ({
        ...prev,
        contatoRequest: {
          ...(prev.contatoRequest || {}),
          [contactField]: value,
        } as ContatoRequest,
      }));
    } else {
      setFormData((prev) => ({
        ...prev,
        [name]: value,
      }));
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    localStorage.setItem("cadastroStep1", JSON.stringify(formData));
    onNext();
  };

  return (
    <div className="min-h-screen w-full lg:grid lg:grid-cols-12 font-sans overflow-hidden">
      <div className="hidden lg:col-span-5 lg:relative lg:flex">
        <img
          src={apae.src}
          alt="Group of people"
          className="absolute inset-0 h-full w-full object-cover"
        />
        <div className="relative z-10 flex w-full flex-col items-center justify-center bg-gradient-to-t from-white/30 to-transparent text-white space-y-6 text-center">
          <h1 className="text-5xl font-bold tracking-tight">BEM-VINDO</h1>
          <p className="text-xl max-w-sm">
            Informe seus dados ao lado para poder fazer parte da nossa
            associação.
          </p>
        </div>
        <div className="absolute z-20 top-1/2 -right-7 -translate-y-1/2">
          <button className="h-14 w-14 bg-blue-700 rounded-full flex items-center justify-center text-white hover:bg-blue-800 transition-colors">
            <ArrowRight className="h-6 w-6" />
          </button>
        </div>
      </div>

      <div className="lg:col-span-7 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-16 bg-gray-50">
        <div className="w-full max-w-3xl space-y-8">
          <h2 className="text-2xl font-bold text-gray-800">
            Cadastro de pessoas
          </h2>
          <form onSubmit={handleSubmit} className="space-y-8">
            <div>
              <h3 className="text-lg font-semibold text-blue-600 mb-4">
                Dados Pessoais
              </h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-6">
                <div className="space-y-2">
                  <Label htmlFor="nomeCompleto">Nome Completo*</Label>
                  <Input
                    id="nomeCompleto"
                    name="nomeCompleto"
                    value={formData.nomeCompleto || ""}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="cpf">CPF*</Label>
                  <Input
                    id="cpf"
                    name="cpf"
                    value={formData.cpf || ""}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="dataNascimento">Data de Nascimento*</Label>
                  <Input
                    id="dataNascimento"
                    name="dataNascimento"
                    type="date"
                    value={formData.dataNascimento || ""}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="naturalidade">Naturalidade</Label>
                  <Input
                    id="naturalidade"
                    name="contato.naturalidade"
                    value={formData.contatoRequest?.naturalidade || ""}
                    onChange={handleChange}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="rg">RG*</Label>
                  <Input
                    id="rg"
                    name="rg"
                    value={formData.rg || ""}
                    onChange={handleChange}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="dataEmissaoRg">Data de Emissão*</Label>
                  <Input
                    id="dataEmissaoRg"
                    name="dataEmissaoRg"
                    type="date"
                    value={formData.dataEmissaoRg || ""}
                    onChange={handleChange}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="orgaoEmissorRg">Órgão Emissor*</Label>
                  <Input
                    id="orgaoEmissorRg"
                    name="orgaoEmissorRg"
                    value={formData.orgaoEmissorRg || ""}
                    onChange={handleChange}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="cns">CNS</Label>
                  <Input
                    id="cns"
                    name="cns"
                    value={formData.cns || ""}
                    onChange={handleChange}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="nis">NIS</Label>
                  <Input
                    id="nis"
                    name="nis"
                    value={formData.nis || ""}
                    onChange={handleChange}
                  />
                </div>
              </div>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-blue-600 mb-4">
                Certidão de Nascimento
              </h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-6">
                <div className="space-y-2">
                  <Label htmlFor="numRegistroNasc">
                    Registro de Nascimento*
                  </Label>
                  <Input
                    id="numRegistroNasc"
                    name="numRegistroNasc"
                    value={formData.numRegistroNasc || ""}
                    onChange={handleChange}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="fls">Folha</Label>
                  <Input
                    id="fls"
                    name="fls"
                    value={formData.fls || ""}
                    onChange={handleChange}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="livro">Livro</Label>
                  <Input
                    id="livro"
                    name="livro"
                    value={formData.livro || ""}
                    onChange={handleChange}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="cartorio">Cartório</Label>
                  <Input
                    id="cartorio"
                    name="cartorio"
                    value={formData.cartorio || ""}
                    onChange={handleChange}
                  />
                </div>
              </div>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-blue-600 mb-4">
                Endereço
              </h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-6">
                <div className="space-y-2">
                  <Label htmlFor="endereco">Rua*</Label>
                  <Input
                    id="endereco"
                    name="contato.endereco"
                    value={formData.contatoRequest?.endereco || ""}
                    onChange={handleChange}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="cep">CEP*</Label>
                  <Input
                    id="cep"
                    name="contato.cep"
                    value={formData.contatoRequest?.cep || ""}
                    onChange={handleChange}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="bairro">Bairro*</Label>
                  <Input
                    id="bairro"
                    name="contato.bairro"
                    value={formData.contatoRequest?.bairro || ""}
                    onChange={handleChange}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="estado">Estado*</Label>
                  <Input
                    id="estado"
                    name="contato.estado"
                    value={formData.contatoRequest?.estado || ""}
                    onChange={handleChange}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="cidade">Cidade</Label>
                  <Input
                    id="cidade"
                    name="contato.cidade"
                    value={formData.contatoRequest?.cidade || ""}
                    onChange={handleChange}
                  />
                </div>
              </div>
            </div>

            <div className="flex justify-end pt-6">
              <Button
                type="submit"
                className="bg-yellow-400 text-black font-bold hover:bg-yellow-500 px-8 py-6 text-base"
              >
                Próximo
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
