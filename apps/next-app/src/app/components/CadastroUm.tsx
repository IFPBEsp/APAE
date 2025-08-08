import React, { useState, useEffect } from "react";
import { PessoaRequest, ContatoRequest } from "../service/pessoaService";
import apae from "../images/apae.png";

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
    <div className="min-h-screen flex w-full items-center justify-center p-4 sm:p-6 font-sans">
      <div className="flex rounded-2xl shadow-2xl w-full bg-white overflow-hidden">
        <div className="relative bg-blue-900">
          <img
            src={apae.src}
            alt="APAE"
            className="absolute h-full w-full object-cover z-0"
          />
          <div className="relative z-10 flex flex-col justify-center items-center h-full px-8 text-center text-white">
            <h2 className="font-extrabold text-3xl md:text-4xl mb-4 drop-shadow-lg tracking-wide uppercase">
              Bem-vindo à APAE
            </h2>
            <p className="text-lg md:text-2xl max-w-md font-semibold mb-6 drop-shadow-md">
              É um prazer receber você!
              <br />
              Preencha seus dados ao lado para fazer parte da nossa associação e
              transformar vidas conosco.
            </p>
            <div className="mt-8 w-12 h-12" />
          </div>
        </div>

        <div className="flex items-center justify-center w-full">
          <form className="w-full p-10 space-y-8" onSubmit={handleSubmit}>
            <h2 className="text-blue-900 font-bold text-2xl mb-6 text-center">
              Cadastro de Assistido - Etapa 1
            </h2>

            <fieldset>
              <legend className="text-blue-600 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">
                Dados Pessoais
              </legend>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-4">
                <div className="flex flex-col md:col-span-2">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="nomeCompleto"
                  >
                    Nome Completo
                  </label>
                  <input
                    id="nomeCompleto"
                    type="text"
                    name="nomeCompleto"
                    placeholder="Nome Completo do Assistido"
                    value={formData.nomeCompleto || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                    required
                  />
                </div>
                <div className="flex flex-col">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="dataNascimento"
                  >
                    Data de Nascimento
                  </label>
                  <input
                    id="dataNascimento"
                    type="date"
                    name="dataNascimento"
                    value={formData.dataNascimento || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                    required
                  />
                </div>
                <div className="flex flex-col">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="naturalidade"
                  >
                    Naturalidade
                  </label>
                  <input
                    id="naturalidade"
                    type="text"
                    name="contato.naturalidade"
                    placeholder="Cidade de Nascimento"
                    value={formData.contatoRequest?.naturalidade || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
                <div className="flex flex-col">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="cpf"
                  >
                    CPF
                  </label>
                  <input
                    id="cpf"
                    type="text"
                    name="cpf"
                    placeholder="000.000.000-00"
                    value={formData.cpf || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                    required
                  />
                </div>
                <div className="flex flex-col">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="rg"
                  >
                    RG
                  </label>
                  <input
                    id="rg"
                    type="text"
                    name="rg"
                    placeholder="Número do RG"
                    value={formData.rg || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
                <div className="flex flex-col">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="dataEmissaoRg"
                  >
                    Data de Emissão do RG
                  </label>
                  <input
                    id="dataEmissaoRg"
                    type="date"
                    name="dataEmissaoRg"
                    value={formData.dataEmissaoRg || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
                <div className="flex flex-col">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="orgaoEmissorRg"
                  >
                    Órgão Emissor do RG
                  </label>
                  <input
                    id="orgaoEmissorRg"
                    type="text"
                    name="orgaoEmissorRg"
                    placeholder="Ex: SSP/SP"
                    value={formData.orgaoEmissorRg || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
                <div className="flex flex-col">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="cns"
                  >
                    CNS
                  </label>
                  <input
                    id="cns"
                    type="text"
                    name="cns"
                    placeholder="Cartão Nacional de Saúde"
                    value={formData.cns || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
                <div className="flex flex-col">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="nis"
                  >
                    NIS
                  </label>
                  <input
                    id="nis"
                    type="text"
                    name="nis"
                    placeholder="Número de Ident. Social"
                    value={formData.nis || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
              </div>
            </fieldset>

            <fieldset>
              <legend className="text-blue-600 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">
                Certidão de Nascimento
              </legend>
              <div className="grid grid-cols-1 md:grid-cols-4 gap-x-6 gap-y-4">
                <div className="flex flex-col md:col-span-2">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="numRegistroNasc"
                  >
                    Nº do Registro
                  </label>
                  <input
                    id="numRegistroNasc"
                    type="text"
                    name="numRegistroNasc"
                    placeholder="Número do Registro"
                    value={formData.numRegistroNasc || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
                <div className="flex flex-col">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="fls"
                  >
                    Folha
                  </label>
                  <input
                    id="fls"
                    type="text"
                    name="fls"
                    placeholder="Fls"
                    value={formData.fls || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
                <div className="flex flex-col">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="livro"
                  >
                    Livro
                  </label>
                  <input
                    id="livro"
                    type="text"
                    name="livro"
                    placeholder="Livro"
                    value={formData.livro || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
                <div className="flex flex-col md:col-span-4">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="cartorio"
                  >
                    Cartório
                  </label>
                  <input
                    id="cartorio"
                    type="text"
                    name="cartorio"
                    placeholder="Nome do Cartório"
                    value={formData.cartorio || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
              </div>
            </fieldset>

            <fieldset>
              <legend className="text-blue-600 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">
                Endereço
              </legend>
              <div className="grid grid-cols-1 md:grid-cols-4 gap-x-6 gap-y-4">
                <div className="flex flex-col md:col-span-3">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="endereco"
                  >
                    Endereço
                  </label>
                  <input
                    id="endereco"
                    type="text"
                    name="contato.endereco"
                    placeholder="Rua, Avenida, etc."
                    value={formData.contatoRequest?.endereco || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
                <div className="flex flex-col md:col-span-1">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="cep"
                  >
                    CEP
                  </label>
                  <input
                    id="cep"
                    type="text"
                    name="contato.cep"
                    placeholder="00000-000"
                    value={formData.contatoRequest?.cep || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
                <div className="flex flex-col md:col-span-2">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="bairro"
                  >
                    Bairro
                  </label>
                  <input
                    id="bairro"
                    type="text"
                    name="contato.bairro"
                    placeholder="Bairro"
                    value={formData.contatoRequest?.bairro || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
                <div className="flex flex-col">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="cidade"
                  >
                    Cidade
                  </label>
                  <input
                    id="cidade"
                    type="text"
                    name="contato.cidade"
                    placeholder="Cidade"
                    value={formData.contatoRequest?.cidade || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
                <div className="flex flex-col">
                  <label
                    className="text-gray-700 font-medium mb-1"
                    htmlFor="estado"
                  >
                    Estado
                  </label>
                  <input
                    id="estado"
                    type="text"
                    name="contato.estado"
                    placeholder="Estado"
                    value={formData.contatoRequest?.estado || ""}
                    onChange={handleChange}
                    className="border border-gray-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-700"
                  />
                </div>
              </div>
            </fieldset>

            <div className="flex justify-end items-center pt-4">
              <button
                type="submit"
                className="bg-blue-800 text-white font-semibold py-3 px-8 rounded-lg hover:bg-blue-900 transition-colors shadow-md hover:shadow-lg focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-700"
              >
                Próximo
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
