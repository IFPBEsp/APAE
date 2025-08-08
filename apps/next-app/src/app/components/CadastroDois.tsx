import React, { useState, useEffect } from "react";
import apae from "../images/apae.png";

export interface VacinaRequest {
  nome: string;
  dataAplicacao: string;
  pessoaId?: string;
}

export interface TipoDeficienciaRequest {
  descricao: string;
  pessoaId?: string;
}

export interface TipoAtendimentoRequest {
  descricao: string;
  pessoaId?: string;
}

export interface CadastroAnualRequest {
  beneficioDePrestacaoContinuada: boolean;
  historicosAlergias: string;
  medicacoesContinuas: string;
  historicoDoencas: string;
  rendaFamiliar: number;
  pessoaId?: string;
  tipoAtendimentoId?: number;
}
interface CadastroDoisProps {
  onNext: () => void;
  onBack: () => void;
}
interface FormData {
  vacinacoesRequests: VacinaRequest[];
  deficienciasRequests: TipoDeficienciaRequest[];
  atendimentosRequests: TipoAtendimentoRequest[];
  cadastrosAnuaisRequests: CadastroAnualRequest[];
}
export default function CadastroDois({ onNext, onBack }: CadastroDoisProps) {
  const [formData, setFormData] = useState<FormData>({
    vacinacoesRequests: [{ nome: "", dataAplicacao: "" }],
    deficienciasRequests: [{ descricao: "" }],
    atendimentosRequests: [{ descricao: "" }],
    cadastrosAnuaisRequests: [
      {
        beneficioDePrestacaoContinuada: false,
        historicosAlergias: "",
        medicacoesContinuas: "",
        historicoDoencas: "",
        rendaFamiliar: 0,
      },
    ],
  });

  useEffect(() => {
    const savedData = localStorage.getItem("cadastroData");
    if (savedData) {
      const fullData = JSON.parse(savedData);
      setFormData({
        vacinacoesRequests:
          fullData.vacinacoesRequests && fullData.vacinacoesRequests.length > 0
            ? fullData.vacinacoesRequests
            : [{ nome: "", dataAplicacao: "" }],
        deficienciasRequests:
          fullData.deficienciasRequests &&
          fullData.deficienciasRequests.length > 0
            ? fullData.deficienciasRequests
            : [{ descricao: "" }],
        atendimentosRequests:
          fullData.atendimentosRequests &&
          fullData.atendimentosRequests.length > 0
            ? fullData.atendimentosRequests
            : [{ descricao: "" }],
        cadastrosAnuaisRequests:
          fullData.cadastrosAnuaisRequests &&
          fullData.cadastrosAnuaisRequests.length > 0
            ? fullData.cadastrosAnuaisRequests
            : [
                {
                  beneficioDePrestacaoContinuada: false,
                  historicosAlergias: "",
                  medicacoesContinuas: "",
                  historicoDoencas: "",
                  rendaFamiliar: 0,
                },
              ],
      });
    }
  }, []);
  const handleArrayChange = (
    category: keyof FormData,
    index: number,
    field: string,
    value: string | boolean | number
  ) => {
    setFormData((prev) => {
      const updatedArray = [...prev[category]];
      // Create a new object with the updated value, ensuring the type is correct
      const updatedItem = { ...updatedArray[index], [field]: value };
      updatedArray[index] = updatedItem;
      return { ...prev, [category]: updatedArray };
    });
  };
  const addItem = (
    category: keyof Omit<FormData, "cadastrosAnuaisRequests">
  ) => {
    let newItem: any;
    switch (category) {
      case "vacinacoesRequests":
        newItem = { nome: "", dataAplicacao: "" };
        break;
      case "deficienciasRequests":
      case "atendimentosRequests":
        newItem = { descricao: "" };
        break;
      default:
        return;
    }
    setFormData((prev) => ({
      ...prev,
      [category]: [...prev[category], newItem],
    }));
  };
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const step1Data = JSON.parse(localStorage.getItem("cadastroStep1") || "{}");
    // Combine and save all data so far
    localStorage.setItem(
      "cadastroData",
      JSON.stringify({
        ...step1Data,
        ...formData,
      })
    );
    onNext();
  };

  return (
    <div className="min-h-screen flex w-full items-center justify-center p-6">
      <div className="flex rounded-xl shadow-lg w-full bg-white overflow-hidden">
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
            <h2 className="text-blue-900 font-bold text-2xl mb-8 text-center">
              Cadastro de Assistido - Etapa 2
            </h2>

            {/* Vaccines Section */}
            <fieldset className="mb-8">
              <legend className="text-blue-500 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">
                Vacinas
              </legend>
              {formData.vacinacoesRequests.map((vacina, idx) => (
                <div
                  key={idx}
                  className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-4 p-4 border border-gray-200 rounded-lg"
                >
                  <div className="flex flex-col">
                    <label className="text-black font-medium mb-1">
                      Nome da Vacina
                    </label>
                    <input
                      type="text"
                      placeholder="Nome da Vacina"
                      value={vacina.nome}
                      onChange={(e) =>
                        handleArrayChange(
                          "vacinacoesRequests",
                          idx,
                          "nome",
                          e.target.value
                        )
                      }
                      className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700"
                    />
                  </div>
                  <div className="flex flex-col">
                    <label className="text-black font-medium mb-1">
                      Data de Aplicação
                    </label>
                    <input
                      type="date"
                      placeholder="Data de Aplicação"
                      value={vacina.dataAplicacao}
                      onChange={(e) =>
                        handleArrayChange(
                          "vacinacoesRequests",
                          idx,
                          "dataAplicacao",
                          e.target.value
                        )
                      }
                      className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700"
                    />
                  </div>
                </div>
              ))}
              <button
                type="button"
                onClick={() => addItem("vacinacoesRequests")}
                className="bg-blue-100 text-blue-800 font-semibold px-4 py-2 rounded-lg hover:bg-blue-200 transition-colors"
              >
                Adicionar Vacina
              </button>
            </fieldset>

            {/* Deficiencies Section */}
            <fieldset className="mb-8">
              <legend className="text-blue-500 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">
                Deficiências
              </legend>
              {formData.deficienciasRequests.map((def, idx) => (
                <div
                  key={idx}
                  className="mb-4 p-4 border border-gray-200 rounded-lg"
                >
                  <label className="text-black font-medium mb-1">
                    Tipo de Deficiência
                  </label>
                  <input
                    type="text"
                    placeholder="Tipo de Deficiência"
                    value={def.descricao}
                    onChange={(e) =>
                      handleArrayChange(
                        "deficienciasRequests",
                        idx,
                        "descricao",
                        e.target.value
                      )
                    }
                    className="border border-blue-300 p-3 rounded-lg w-full focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700"
                  />
                </div>
              ))}
              <button
                type="button"
                onClick={() => addItem("deficienciasRequests")}
                className="bg-blue-100 text-blue-800 font-semibold px-4 py-2 rounded-lg hover:bg-blue-200 transition-colors"
              >
                Adicionar Deficiência
              </button>
            </fieldset>

            {/* Services Section */}
            <fieldset className="mb-8">
              <legend className="text-blue-500 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">
                Atendimentos
              </legend>
              {formData.atendimentosRequests.map((at, idx) => (
                <div
                  key={idx}
                  className="mb-4 p-4 border border-gray-200 rounded-lg"
                >
                  <label className="text-black font-medium mb-1">
                    Tipo de Atendimento
                  </label>
                  <input
                    type="text"
                    placeholder="Tipo de Atendimento"
                    value={at.descricao}
                    onChange={(e) =>
                      handleArrayChange(
                        "atendimentosRequests",
                        idx,
                        "descricao",
                        e.target.value
                      )
                    }
                    className="border border-blue-300 p-3 rounded-lg w-full focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700"
                  />
                </div>
              ))}
              <button
                type="button"
                onClick={() => addItem("atendimentosRequests")}
                className="bg-blue-100 text-blue-800 font-semibold px-4 py-2 rounded-lg hover:bg-blue-200 transition-colors"
              >
                Adicionar Atendimento
              </button>
            </fieldset>

            {/* Annual Registration Section */}
            <fieldset className="mb-8">
              <legend className="text-blue-500 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">
                Cadastro Anual
              </legend>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="flex flex-col">
                  <label className="text-black font-medium mb-1">
                    Doenças que já teve
                  </label>
                  <input
                    type="text"
                    placeholder="Doenças que já teve"
                    value={
                      formData.cadastrosAnuaisRequests[0]?.historicoDoencas ||
                      ""
                    }
                    onChange={(e) =>
                      handleArrayChange(
                        "cadastrosAnuaisRequests",
                        0,
                        "historicoDoencas",
                        e.target.value
                      )
                    }
                    className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700"
                  />
                </div>
                <div className="flex flex-col">
                  <label className="text-black font-medium mb-1">
                    Tipo de medicação que toma
                  </label>
                  <input
                    type="text"
                    placeholder="Tipo de medicação que toma"
                    value={
                      formData.cadastrosAnuaisRequests[0]
                        ?.medicacoesContinuas || ""
                    }
                    onChange={(e) =>
                      handleArrayChange(
                        "cadastrosAnuaisRequests",
                        0,
                        "medicacoesContinuas",
                        e.target.value
                      )
                    }
                    className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700"
                  />
                </div>
                <div className="flex flex-col">
                  <label className="text-black font-medium mb-1">
                    Tem alergias? Quais?
                  </label>
                  <input
                    type="text"
                    placeholder="Tem alergias? Quais?"
                    value={
                      formData.cadastrosAnuaisRequests[0]?.historicosAlergias ||
                      ""
                    }
                    onChange={(e) =>
                      handleArrayChange(
                        "cadastrosAnuaisRequests",
                        0,
                        "historicosAlergias",
                        e.target.value
                      )
                    }
                    className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700"
                  />
                </div>
                <div className="flex flex-col">
                  <label className="text-black font-medium mb-1">
                    Renda Familiar
                  </label>
                  <input
                    type="number"
                    placeholder="Renda Familiar"
                    value={
                      formData.cadastrosAnuaisRequests[0]?.rendaFamiliar || ""
                    }
                    onChange={(e) =>
                      handleArrayChange(
                        "cadastrosAnuaisRequests",
                        0,
                        "rendaFamiliar",
                        Number(e.target.value)
                      )
                    }
                    className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700"
                  />
                </div>
                <label className="flex items-center text-gray-700 md:col-span-2 mt-2">
                  <input
                    type="checkbox"
                    checked={
                      formData.cadastrosAnuaisRequests[0]
                        ?.beneficioDePrestacaoContinuada || false
                    }
                    onChange={(e) =>
                      handleArrayChange(
                        "cadastrosAnuaisRequests",
                        0,
                        "beneficioDePrestacaoContinuada",
                        e.target.checked
                      )
                    }
                    className="mr-3 h-5 w-5 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                  />
                  Benefício de Prestação Continuada
                </label>
              </div>
            </fieldset>

            {/* Navigation Buttons */}
            <div className="flex justify-between items-center mt-10">
              <button
                type="button"
                onClick={onBack}
                className="bg-gray-300 text-gray-800 font-semibold py-3 px-8 rounded-lg hover:bg-gray-400 transition-colors"
              >
                Voltar
              </button>
              <button
                type="submit"
                className="bg-blue-800 text-white font-semibold py-3 px-8 rounded-lg hover:bg-blue-900 transition-colors"
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
