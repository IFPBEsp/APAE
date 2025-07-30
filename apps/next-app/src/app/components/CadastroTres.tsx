import React from "react";

export default function CadastroTres({ onBack }: { onBack: () => void }) {
  return (
    <form className="bg-white p-10 rounded-2xl shadow-md max-w-4xl mx-auto font-sans">
      <h2 className="text-blue-900 font-bold text-xl mb-6">Finalização</h2>

      <div className="grid grid-cols-1 gap-6 mb-8">
        <input
          type="date"
          className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700"
          placeholder="Data de Cadastro*"
        />

        <div>
          <label className="block mb-2 text-gray-700 font-medium">Adicione uma foto*</label>
          <input
            type="file"
            className="block w-full text-gray-700 file:border file:border-blue-300 file:p-2 file:rounded-lg file:text-blue-700"
          />
        </div>

        <div>
          <h3 className="text-blue-600 font-semibold mb-2">Informação Importante</h3>
          <label className="block text-gray-700 mb-1">
            <input type="checkbox" className="mr-2" />
            Aluno
          </label>
          <label className="block text-gray-700 mb-1">
            <input type="checkbox" className="mr-2" />
            Paciente
          </label>
          <label className="block text-gray-700">
            <input type="checkbox" className="mr-2" />
            Ambos
          </label>
        </div>
      </div>

      <div className="flex">
        <div className="flex justify-end gap-4">
          <button
            type="button"
            className="bg-gray-300 hover:bg-gray-400 text-gray-800 font-semibold py-3 px-6 rounded-lg transition duration-200"
          >
            Cancelar
          </button>
        </div>
        
        <div className="flex justify-end gap-4 ml-auto">
          <button
            type="button"
            onClick={onBack}
            className="bg-yellow-400 hover:bg-yellow-500 text-white font-semibold py-3 px-6 rounded-lg transition duration-200"
          >
            Voltar
          </button>
          <button
            type="submit"
            className="bg-yellow-400 hover:bg-yellow-500 text-white font-semibold py-3 px-6 rounded-lg transition duration-200"
          >
            Salvar
          </button>
        </div>
      </div>

    </form>
  );
}
