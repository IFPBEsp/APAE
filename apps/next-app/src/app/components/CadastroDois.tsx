import React from "react";

export default function CadastroDois({ onNext, onBack }: { onNext: () => void; onBack: () => void }) {
  return (
    <form className="bg-white p-10 rounded-2xl shadow-md max-w-4xl mx-auto font-sans">
      <h2 className="text-blue-900 font-bold text-xl mb-6">Informações Adicionais</h2>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        <input
          className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700"
          placeholder="Doenças que já teve*"
        />
        <input
          className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700"
          placeholder="Vacinas Tomadas*"
        />
        <input
          className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700"
          placeholder="Tipo de medicação que toma"
        />
        <input
          className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700"
          placeholder="Tem alergias? Quais?*"
        />
        <input
          className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700"
          placeholder="Tipo de Deficiência*"
        />
        <input
          className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700"
          placeholder="Tipo de Atendimento*"
        />
      </div>

      <div className="mb-6">
        <button
          type="button"
          className="bg-blue-800 text-white font-semibold px-6 py-3 rounded-lg hover:bg-blue-900 transition-colors duration-200"
        >
          Upload do Laudo
        </button>
      </div>
      <div className="mb-8">
        <button
          type="button"
          className="bg-blue-800 text-white font-semibold px-6 py-3 rounded-lg hover:bg-blue-900 transition-colors duration-200"
        >
          Encaminhamento
        </button>
      </div>

      <div className="flex justify-end">
        <button
          type="button"
          onClick={onBack}
          className="mx-4 bg-yellow-400 hover:bg-yellow-500 text-white font-semibold py-3 px-8 rounded-lg transition duration-200"
        >
          Voltar
        </button>
        <button
          type="button"
          onClick={onNext}
          className="bg-yellow-400 hover:bg-yellow-500 text-white font-semibold py-3 px-8 rounded-lg transition duration-200"
        >
          Próximo
        </button>
      </div>
    </form>
  );
}
