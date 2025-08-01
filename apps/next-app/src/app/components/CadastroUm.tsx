import React, { useState } from "react";
import { criarPessoa, PessoaRequest } from "../service/pessoaService";

export default function CadastroUm({ onNext }: { onNext: () => void }) {
  const [formData, setFormData] = useState<PessoaRequest>({
    nome: "",
    cpf: "",
    rg: "",
    contato: "",
    registroNascimento: "",
    orgaoEmissor: "",
    dataNascimento: "",
    dataEmissao: "",
    rua: "",
    cep: "",
    bairro: "",
    estado: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async () => {
    try {
      const pessoaCriada = await criarPessoa(formData);
      console.log("Pessoa criada com sucesso:", pessoaCriada);
      onNext(); // avançar para a próxima etapa
    } catch (error) {
      console.error("Erro ao criar pessoa:", error);
    }
  };

  return (
    <form
      className="bg-white p-10 rounded-2xl shadow-md max-w-4xl mx-auto font-sans"
      onSubmit={(e) => {
        e.preventDefault();
        handleSubmit();
      }}
    >
      <h2 className="text-blue-900 font-bold text-xl mb-6">Cadastro de pessoas</h2>

      {/* Dados Pessoais Section */}
      <p className="text-blue-400 mb-4 select-none">Dados Pessoais</p>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        <input
          type="text"
          name="nome"
          placeholder="Nome Completo*"
          value={formData.nome}
          onChange={handleChange}
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="text"
          name="cpf"
          placeholder="CPF*"
          value={formData.cpf}
          onChange={handleChange}
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="text"
          name="contato"
          placeholder="Contato*"
          value={formData.contato}
          onChange={handleChange}
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="text"
          name="registroNascimento"
          placeholder="Registro de Nascimento*"
          value={formData.registroNascimento}
          onChange={handleChange}
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="text"
          name="rg"
          placeholder="RG*"
          value={formData.rg}
          onChange={handleChange}
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="date"
          name="dataEmissao"
          placeholder="Data de Emissão*"
          value={formData.dataEmissao}
          onChange={handleChange}
          className="border border-blue-300 rounded-lg p-3 text-gray-700"
        />
        <input
          type="text"
          name="orgaoEmissor"
          placeholder="Orgão Emissor*"
          value={formData.orgaoEmissor}
          onChange={handleChange}
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="date"
          name="dataNascimento"
          placeholder="Data de Nascimento*"
          value={formData.dataNascimento}
          onChange={handleChange}
          className="border border-blue-300 rounded-lg p-3 text-gray-700"
        />
      </div>

      {/* Endereço Section */}
      <p className="text-blue-400 mb-4 select-none">Endereço</p>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <input
          type="text"
          name="rua"
          placeholder="Rua*"
          value={formData.rua}
          onChange={handleChange}
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700 col-span-1 md:col-span-2"
        />
        <input
          type="text"
          name="bairro"
          placeholder="Bairro*"
          value={formData.bairro}
          onChange={handleChange}
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="text"
          name="cep"
          placeholder="CEP*"
          value={formData.cep}
          onChange={handleChange}
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="text"
          name="estado"
          placeholder="Estado*"
          value={formData.estado}
          onChange={handleChange}
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
      </div>

      <div className="flex justify-end mt-10">
        <button
          type="submit"
          className="bg-yellow-400 hover:bg-yellow-500 text-white font-semibold py-3 px-8 rounded-lg transition-colors duration-200"
        >
          Próximo
        </button>
      </div>
    </form>
  );
}
