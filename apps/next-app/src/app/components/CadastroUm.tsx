import React from "react";

export default function CadastroUm({ onNext }: { onNext: () => void }) {
  return (
    <form className="bg-white p-10 rounded-2xl shadow-md max-w-4xl mx-auto font-sans">
      <h2 className="text-blue-900 font-bold text-xl mb-6">Cadastro de pessoas</h2>

      {/* Dados Pessoais Section */}
      <p className="text-blue-400 mb-4 select-none">Dados Pessoais</p>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        <input
          type="text"
          placeholder="Nome Completo*"
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="text"
          placeholder="CPF*"
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="text"
          placeholder="Contato*"
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="text"
          placeholder="Registro de Nascimento*"
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="text"
          placeholder="RG*"
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="date"
          placeholder="Data de Emissão*"
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="text"
          placeholder="Orgão Emissor*"
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="date"
          placeholder="Data de Nascimento*"
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
      </div>

      {/* Endereço Section */}
      <p className="text-blue-400 mb-4 select-none">Endereço</p>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <input
          type="text"
          placeholder="Rua*"
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700 col-span-1 md:col-span-2"
        />
        <input
          type="text"
          placeholder="Bairro*"
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="text"
          placeholder="CEP*"
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
        <input
          type="text"
          placeholder="Estado*"
          className="border border-blue-300 rounded-lg p-3 placeholder-gray-400 text-gray-700"
        />
      </div>

      <div className="flex justify-end mt-10">
        <button
          type="button"
          onClick={onNext}
          className=""
        >
          Próximo
        </button>
      </div>
    </form>
  );
}
