import React, { useState, useEffect } from "react";

// --- Start: Mocked pessoaService content ---
// To resolve the import error, the necessary interfaces are included directly in this file.
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
// --- End: Mocked pessoaService content ---

// Define the props for the component
interface CadastroDoisProps {
  onNext: () => void;
  onBack: () => void;
}

// Define the shape of the form's state
interface FormData {
  vacinacoesRequests: VacinaRequest[];
  deficienciasRequests: TipoDeficienciaRequest[];
  atendimentosRequests: TipoAtendimentoRequest[];
  cadastrosAnuaisRequests: CadastroAnualRequest[];
}

export default function CadastroDois({ onNext, onBack }: CadastroDoisProps) {
  // Initialize the form state with default values
  const [formData, setFormData] = useState<FormData>({
    vacinacoesRequests: [{ nome: "", dataAplicacao: "" }],
    deficienciasRequests: [{ descricao: "" }],
    atendimentosRequests: [{ descricao: "" }],
    cadastrosAnuaisRequests: [{
      beneficioDePrestacaoContinuada: false,
      historicosAlergias: "",
      medicacoesContinuas: "",
      historicoDoencas: "",
      rendaFamiliar: 0,
    }],
  });

  // Load existing data from localStorage when the component mounts
  useEffect(() => {
    const savedData = localStorage.getItem("cadastroData");
    if (savedData) {
        const fullData = JSON.parse(savedData);
        setFormData({
            vacinacoesRequests: fullData.vacinacoesRequests && fullData.vacinacoesRequests.length > 0 ? fullData.vacinacoesRequests : [{ nome: "", dataAplicacao: "" }],
            deficienciasRequests: fullData.deficienciasRequests && fullData.deficienciasRequests.length > 0 ? fullData.deficienciasRequests : [{ descricao: "" }],
            atendimentosRequests: fullData.atendimentosRequests && fullData.atendimentosRequests.length > 0 ? fullData.atendimentosRequests : [{ descricao: "" }],
            cadastrosAnuaisRequests: fullData.cadastrosAnuaisRequests && fullData.cadastrosAnuaisRequests.length > 0 ? fullData.cadastrosAnuaisRequests : [{
              beneficioDePrestacaoContinuada: false,
              historicosAlergias: "",
              medicacoesContinuas: "",
              historicoDoencas: "",
              rendaFamiliar: 0,
            }],
        });
    }
  }, []);


  /**
   * Handles changes for all input fields in the dynamic arrays.
   * This function is now type-safe by handling each category specifically.
   */
  const handleArrayChange = (
    category: keyof FormData,
    index: number,
    field: string,
    value: string | boolean | number
  ) => {
      setFormData(prev => {
          const updatedArray = [...prev[category]];
          // Create a new object with the updated value, ensuring the type is correct
          const updatedItem = { ...updatedArray[index], [field]: value };
          updatedArray[index] = updatedItem;
          return { ...prev, [category]: updatedArray };
      });
  };

  /**
   * Adds a new empty item to a specified array in the form data.
   */
  const addItem = (category: keyof Omit<FormData, 'cadastrosAnuaisRequests'>) => {
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
    setFormData(prev => ({
        ...prev,
        [category]: [...prev[category], newItem],
    }));
  };

  /**
   * Handles form submission by saving data to localStorage and moving to the next step.
   */
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
    <form className="bg-white p-6 md:p-10 rounded-2xl shadow-lg max-w-4xl w-full mx-auto font-sans text-gray-700" onSubmit={handleSubmit}>
      <h2 className="text-blue-900 font-bold text-2xl mb-8 text-center">Cadastro de Assistido - Etapa 2</h2>

      {/* Vaccines Section */}
      <fieldset className="mb-8">
        <legend className="text-blue-500 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">Vacinas</legend>
        {formData.vacinacoesRequests.map((vacina, idx) => (
          <div key={idx} className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-4">
            <input type="text" placeholder="Nome da Vacina" value={vacina.nome} onChange={e => handleArrayChange('vacinacoesRequests', idx, 'nome', e.target.value)} className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700" />
            <input type="date" placeholder="Data de Aplicação" value={vacina.dataAplicacao} onChange={e => handleArrayChange('vacinacoesRequests', idx, 'dataAplicacao', e.target.value)} className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700" />
          </div>
        ))}
        <button type="button" onClick={() => addItem("vacinacoesRequests")} className="bg-blue-100 text-blue-800 font-semibold px-4 py-2 rounded-lg hover:bg-blue-200 transition-colors">Adicionar Vacina</button>
      </fieldset>
      
      {/* Deficiencies Section */}
      <fieldset className="mb-8">
        <legend className="text-blue-500 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">Deficiências</legend>
        {formData.deficienciasRequests.map((def, idx) => (
          <div key={idx} className="mb-4">
            <input type="text" placeholder="Tipo de Deficiência" value={def.descricao} onChange={e => handleArrayChange('deficienciasRequests', idx, 'descricao', e.target.value)} className="border border-blue-300 p-3 rounded-lg w-full focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700" />
          </div>
        ))}
        <button type="button" onClick={() => addItem("deficienciasRequests")} className="bg-blue-100 text-blue-800 font-semibold px-4 py-2 rounded-lg hover:bg-blue-200 transition-colors">Adicionar Deficiência</button>
      </fieldset>

      {/* Services Section */}
      <fieldset className="mb-8">
        <legend className="text-blue-500 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">Atendimentos</legend>
        {formData.atendimentosRequests.map((at, idx) => (
          <div key={idx} className="mb-4">
            <input type="text" placeholder="Tipo de Atendimento" value={at.descricao} onChange={e => handleArrayChange('atendimentosRequests', idx, 'descricao', e.target.value)} className="border border-blue-300 p-3 rounded-lg w-full focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700" />
          </div>
        ))}
        <button type="button" onClick={() => addItem("atendimentosRequests")} className="bg-blue-100 text-blue-800 font-semibold px-4 py-2 rounded-lg hover:bg-blue-200 transition-colors">Adicionar Atendimento</button>
      </fieldset>

      {/* Annual Registration Section */}
      <fieldset className="mb-8">
        <legend className="text-blue-500 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">Cadastro Anual</legend>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <input type="text" placeholder="Doenças que já teve" value={formData.cadastrosAnuaisRequests[0]?.historicoDoencas || ""} onChange={e => handleArrayChange('cadastrosAnuaisRequests', 0, 'historicoDoencas', e.target.value)} className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700" />
            <input type="text" placeholder="Tipo de medicação que toma" value={formData.cadastrosAnuaisRequests[0]?.medicacoesContinuas || ""} onChange={e => handleArrayChange('cadastrosAnuaisRequests', 0, 'medicacoesContinuas', e.target.value)} className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700" />
            <input type="text" placeholder="Tem alergias? Quais?" value={formData.cadastrosAnuaisRequests[0]?.historicosAlergias || ""} onChange={e => handleArrayChange('cadastrosAnuaisRequests', 0, 'historicosAlergias', e.target.value)} className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700" />
            <input type="number" placeholder="Renda Familiar" value={formData.cadastrosAnuaisRequests[0]?.rendaFamiliar || ""} onChange={e => handleArrayChange('cadastrosAnuaisRequests', 0, 'rendaFamiliar', Number(e.target.value))} className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400 text-gray-700" />
            <label className="flex items-center text-gray-700 md:col-span-2">
                <input type="checkbox" checked={formData.cadastrosAnuaisRequests[0]?.beneficioDePrestacaoContinuada || false} onChange={e => handleArrayChange('cadastrosAnuaisRequests', 0, 'beneficioDePrestacaoContinuada', e.target.checked)} className="mr-3 h-5 w-5 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
                Benefício de Prestação Continuada
            </label>
        </div>
      </fieldset>

      {/* Navigation Buttons */}
      <div className="flex justify-between items-center mt-10">
        <button type="button" onClick={onBack} className="bg-gray-300 text-gray-800 font-semibold py-3 px-8 rounded-lg hover:bg-gray-400 transition-colors">Voltar</button>
        <button type="submit" className="bg-blue-800 text-white font-semibold py-3 px-8 rounded-lg hover:bg-blue-900 transition-colors">Próximo</button>
      </div>
    </form>
  );
}
