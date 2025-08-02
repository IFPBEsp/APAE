import React, { useState, useEffect } from "react";
import { PessoaRequest, ContatoRequest } from "../service/pessoaService"; // Assuming your service file is in this path

// The props for this component, indicating it's a step in a larger form.
interface CadastroUmProps {
  onNext: () => void;
}

export default function CadastroUm({ onNext }: CadastroUmProps) {
  // State to hold the form data. It's typed as a partial PessoaRequest
  // because we are only filling out a portion of the complete data in this step.
  const [formData, setFormData] = useState<Partial<PessoaRequest>>({
    // Initial values for the form fields
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
    // Set the registration date to today by default
    dataCadastramento: new Date().toISOString().split('T')[0],
    // Initialize the nested contact object
    contatoRequest: {
      enderecoAtivo: "S", // Default value
      comprovanteResidencia: "",
      endereco: "",
      bairro: "",
      cidade: "",
      estado: "",
      cep: "",
      naturalidade: "",
    } as ContatoRequest,
  });

  // Load data from localStorage when the component mounts
  useEffect(() => {
    const savedData = localStorage.getItem('cadastroStep1');
    if (savedData) {
      setFormData(JSON.parse(savedData));
    }
  }, []);


  /**
   * Handles changes for all input fields.
   * It has special logic to update the nested 'contatoRequest' object
   * if the input name starts with 'contato.'.
   */
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    // Check if the field belongs to the nested contact object
    if (name.startsWith("contato.")) {
      const contactField = name.split(".")[1]; // e.g., "endereco" from "contato.endereco"
      setFormData((prev) => ({
        ...prev,
        contatoRequest: {
          // Safely spread the previous contact request, providing a fallback
          ...(prev.contatoRequest || {}),
          [contactField]: value,
        } as ContatoRequest, // Assert the final object shape to satisfy TypeScript
      }));
    } else {
      // Handle top-level fields
      setFormData((prev) => ({
        ...prev,
        [name]: value,
      }));
    }
  };

  /**
   * Handles the form submission.
   * It prevents the default browser action, saves the current form data
   * to localStorage, and then calls the onNext function to proceed to the next step.
   */
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Store the data from this step in localStorage to be retrieved later
    localStorage.setItem('cadastroStep1', JSON.stringify(formData));
    onNext(); // Navigate to the next form step
  };

  return (
    <form
      className="bg-white p-6 md:p-10 rounded-2xl shadow-lg max-w-4xl mx-auto font-sans"
      onSubmit={handleSubmit}
    >
      <h2 className="text-blue-900 font-bold text-2xl mb-8 text-center">
        Cadastro de Assistido - Etapa 1
      </h2>

      {/* Section for Personal Data */}
      <fieldset className="mb-8">
        <legend className="text-blue-500 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">Dados Pessoais</legend>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <input type="text" name="nomeCompleto" placeholder="Nome Completo*" value={formData.nomeCompleto || ""} onChange={handleChange} required className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          
          {/* Labeled Date of Birth Input */}
          <div>
            <label htmlFor="dataNascimento" className="block text-sm font-medium text-gray-600 mb-1">Data de Nascimento*</label>
            <input id="dataNascimento" type="date" name="dataNascimento" value={formData.dataNascimento || ""} onChange={handleChange} required className="w-full border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>

          <input type="text" name="cpf" placeholder="CPF*" value={formData.cpf || ""} onChange={handleChange} required className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          <input type="text" name="rg" placeholder="RG*" value={formData.rg || ""} onChange={handleChange} required className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          
          {/* Labeled RG Issue Date Input */}
          <div>
            <label htmlFor="dataEmissaoRg" className="block text-sm font-medium text-gray-600 mb-1">Data de Emissão do RG*</label>
            <input id="dataEmissaoRg" type="date" name="dataEmissaoRg" value={formData.dataEmissaoRg || ""} onChange={handleChange} required className="w-full border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>

          <input type="text" name="orgaoEmissorRg" placeholder="Órgão Emissor do RG*" value={formData.orgaoEmissorRg || ""} onChange={handleChange} required className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
      </fieldset>

      {/* Section for Birth Certificate */}
      <fieldset className="mb-8">
        <legend className="text-blue-500 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">Registro de Nascimento</legend>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <input type="text" name="numRegistroNasc" placeholder="Número do Registro*" value={formData.numRegistroNasc || ""} onChange={handleChange} required className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          <input type="text" name="fls" placeholder="Folha*" value={formData.fls || ""} onChange={handleChange} required className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          <input type="text" name="livro" placeholder="Livro*" value={formData.livro || ""} onChange={handleChange} required className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          <input type="text" name="cartorio" placeholder="Cartório*" value={formData.cartorio || ""} onChange={handleChange} required className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
      </fieldset>

      {/* Section for Other Documents */}
      <fieldset className="mb-8">
        <legend className="text-blue-500 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">Outros Documentos</legend>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <input type="text" name="cns" placeholder="CNS (Cartão Nacional de Saúde)" value={formData.cns || ""} onChange={handleChange} className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          <input type="text" name="nis" placeholder="NIS (Número de Identificação Social)" value={formData.nis || ""} onChange={handleChange} className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
      </fieldset>

      {/* Section for Address */}
      <fieldset className="mb-8">
        <legend className="text-blue-500 font-semibold mb-4 text-lg w-full border-b border-blue-200 pb-2">Endereço</legend>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Note the 'name' attribute is 'contato.endereco' to match the handleChange logic */}
          <input type="text" name="contato.endereco" placeholder="Endereço*" value={formData.contatoRequest?.endereco || ""} onChange={handleChange} required className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 md:col-span-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          <input type="text" name="contato.bairro" placeholder="Bairro*" value={formData.contatoRequest?.bairro || ""} onChange={handleChange} required className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          <input type="text" name="contato.cidade" placeholder="Cidade*" value={formData.contatoRequest?.cidade || ""} onChange={handleChange} required className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          <input type="text" name="contato.estado" placeholder="Estado*" value={formData.contatoRequest?.estado || ""} onChange={handleChange} required className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          <input type="text" name="contato.cep" placeholder="CEP*" value={formData.contatoRequest?.cep || ""} onChange={handleChange} required className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          <input type="text" name="contato.naturalidade" placeholder="Naturalidade*" value={formData.contatoRequest?.naturalidade || ""} onChange={handleChange} required className="border border-blue-300 p-3 rounded-lg placeholder-gray-400 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
      </fieldset>
      
      <div className="flex justify-end mt-8">
        <button type="submit" className="bg-blue-800 text-white font-semibold px-8 py-3 rounded-lg hover:bg-blue-900 transition-colors duration-300 shadow-md focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
          Próxima Etapa
        </button>
      </div>
    </form>
  );
}