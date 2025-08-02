import React, { useState, useEffect } from "react";

// 1. IMPORT the real service function and interfaces.
// This assumes your file is in the correct location relative to `pessoaService.ts`.
import { 
  criarPessoa,
  PessoaRequest, 
  PessoaResponsavelRequest 
} from "../service/pessoaService";


// Define the props for the component
interface CadastroTresProps {
  onBack: () => void;
}

// Define the shape of the form's state
interface FormData {
  responsaveisRequests: PessoaResponsavelRequest[];
}

// The Modal component remains the same.
const Modal = ({ message, onClose, isError }: { message: string, onClose: () => void, isError?: boolean }) => (
    <div className="fixed inset-0 bg-gray-900 bg-opacity-75 flex justify-center items-center z-50">
        <div className="bg-white p-8 rounded-lg shadow-2xl max-w-sm w-full text-center">
            <h3 className={`text-2xl font-bold mb-4 ${isError ? 'text-red-600' : 'text-blue-800'}`}>
                {isError ? 'Erro' : 'Sucesso'}
            </h3>
            <p className="text-gray-700 mb-6">{message}</p>
            <button
                onClick={onClose}
                className={`w-full py-2 px-4 rounded-lg text-white font-semibold transition-colors ${isError ? 'bg-red-500 hover:bg-red-600' : 'bg-blue-800 hover:bg-blue-900'}`}
            >
                Fechar
            </button>
        </div>
    </div>
);


export default function CadastroTres({ onBack }: CadastroTresProps) {
  // The component's state and logic remain the same.
  const [formData, setFormData] = useState<FormData>({
    responsaveisRequests: [{
      nome: "",
      ondeProcurar: "",
      vivo: true,
      profissao: "",
      rg: "",
      cpf: "",
      emergencia: "",
      tipoResponsavel: "",
    }],
  });
  const [isLoading, setIsLoading] = useState(false);
  const [modalInfo, setModalInfo] = useState<{ message: string, isError?: boolean } | null>(null);

  useEffect(() => {
    const savedData = localStorage.getItem("cadastroData");
    if (savedData) {
        const fullData = JSON.parse(savedData);
        if (fullData.responsaveisRequests && fullData.responsaveisRequests.length > 0) {
            setFormData({
                responsaveisRequests: fullData.responsaveisRequests
            });
        }
    }
  }, []);

  const handleChange = (index: number, field: keyof PessoaResponsavelRequest, value: string | boolean) => {
    setFormData(prev => {
        const updatedResponsaveis = [...prev.responsaveisRequests];
        const responsavel = { ...updatedResponsaveis[index], [field]: value };
        updatedResponsaveis[index] = responsavel;
        return { ...prev, responsaveisRequests: updatedResponsaveis };
    });
  };

  const addResponsavel = () => {
    setFormData(prev => ({
      ...prev,
      responsaveisRequests: [
        ...prev.responsaveisRequests,
        {
          nome: "",
          ondeProcurar: "",
          vivo: true,
          profissao: "",
          rg: "",
          cpf: "",
          emergencia: "",
          tipoResponsavel: "",
        },
      ],
    }));
  };

  // This submission handler now calls the REAL `criarPessoa` function from your service.
  const handleFinalSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      const storedData = JSON.parse(localStorage.getItem("cadastroData") || "{}");
      
      const finalData: PessoaRequest = {
        ...storedData,
        responsaveisRequests: formData.responsaveisRequests,
        vacinacoesRequests: storedData.vacinacoesRequests || [],
        deficienciasRequests: storedData.deficienciasRequests || [],
        atendimentosRequests: storedData.atendimentosRequests || [],
        cadastrosAnuaisRequests: storedData.cadastrosAnuaisRequests || [],
      };

      // This is now a REAL API call.
      const response = await criarPessoa(finalData);
      console.log("Pessoa criada com sucesso:", response);
      
      localStorage.removeItem("cadastroData");
      localStorage.removeItem("cadastroStep1");
      
      setModalInfo({ message: "Cadastro finalizado com sucesso!" });

    } catch (error) {
      console.error("Erro ao criar pessoa:", error);
      let errorMessage = "Não foi possível finalizar o cadastro. Verifique os dados e tente novamente.";
      // You can add more specific error handling here if the backend sends detailed messages
      // if (axios.isAxiosError(error) && error.response) {
      //   errorMessage = error.response.data.message || errorMessage;
      // }
      setModalInfo({ message: errorMessage, isError: true });
    } finally {
      setIsLoading(false);
    }
  };

  // The JSX for the form remains the same.
  return (
    <>
      {modalInfo && <Modal message={modalInfo.message} isError={modalInfo.isError} onClose={() => setModalInfo(null)} />}
      <form className="bg-white p-6 md:p-10 rounded-2xl shadow-lg max-w-4xl w-full mx-auto font-sans text-gray-700" onSubmit={handleFinalSubmit}>
        <h2 className="text-blue-900 font-bold text-2xl mb-8 text-center">Cadastro de Assistido - Etapa Final</h2>

        <fieldset className="mb-8">
            <legend className="text-blue-500 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">Responsáveis</legend>
            {formData.responsaveisRequests.map((resp, idx) => (
                <div key={idx} className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6 p-4 border border-gray-200 rounded-lg">
                    <input type="text" placeholder="Nome do Responsável" value={resp.nome || ""} onChange={e => handleChange(idx, 'nome', e.target.value)} required className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400" />
                    <input type="text" placeholder="CPF" value={resp.cpf || ""} onChange={e => handleChange(idx, 'cpf', e.target.value)} required className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400" />
                    <input type="text" placeholder="RG" value={resp.rg || ""} onChange={e => handleChange(idx, 'rg', e.target.value)} required className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400" />
                    <input type="text" placeholder="Profissão" value={resp.profissao || ""} onChange={e => handleChange(idx, 'profissao', e.target.value)} className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400" />
                    <input type="text" placeholder="Contato de Emergência" value={resp.emergencia || ""} onChange={e => handleChange(idx, 'emergencia', e.target.value)} required className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400" />
                    <input type="text" placeholder="Tipo de Responsável (Ex: Pai, Mãe)" value={resp.tipoResponsavel || ""} onChange={e => handleChange(idx, 'tipoResponsavel', e.target.value)} required className="border border-blue-300 p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400" />
                    <input type="text" placeholder="Onde procurar (endereço, etc.)" value={resp.ondeProcurar || ""} onChange={e => handleChange(idx, 'ondeProcurar', e.target.value)} className="border border-blue-300 p-3 rounded-lg md:col-span-2 focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400" />
                    <label className="flex items-center text-gray-700">
                        <input type="checkbox" checked={resp.vivo} onChange={e => handleChange(idx, 'vivo', e.target.checked)} className="mr-3 h-5 w-5 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
                        Vivo
                    </label>
                </div>
            ))}
            <button type="button" onClick={addResponsavel} className="bg-blue-100 text-blue-800 font-semibold px-4 py-2 rounded-lg hover:bg-blue-200 transition-colors">Adicionar Responsável</button>
        </fieldset>

        <div className="flex justify-between items-center mt-10">
          <button type="button" onClick={onBack} disabled={isLoading} className="bg-gray-300 text-gray-800 font-semibold py-3 px-8 rounded-lg hover:bg-gray-400 transition-colors disabled:opacity-50">Voltar</button>
          <button type="submit" disabled={isLoading} className="bg-green-600 text-white font-semibold py-3 px-8 rounded-lg hover:bg-green-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
            {isLoading ? 'Salvando...' : 'Salvar Cadastro'}
          </button>
        </div>
      </form>
    </>
  );
}