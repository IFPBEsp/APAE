"use client";

import React, { useState, useEffect } from "react";
import CadastroUm from "../components/CadastroUm";
import CadastroDois, { FullFormData } from "../components/CadastroDois";
import CadastroTres from "../components/CadastroTres";

export default function CadastroPage() {

  const [etapa, setEtapa] = useState(1);
  const [token, setToken] = useState("");
  const [hasToken, setHasToken] = useState(false);
  const [formData, setFormData] = useState<FullFormData>({
    vacinacoesRequests: [{ nome: "", dataAplicacao: "" }],
    deficienciasRequests: [{ descricao: "", file: undefined }],
    atendimentosRequests: [{ descricao: "", file: undefined }],
    cadastrosAnuaisRequests: [{
      beneficioDePrestacaoContinuada: false,
      historicosAlergias: "",
      medicacoesContinuas: "",
      historicoDoencas: "",
      rendaFamiliar: 0
    }],
    responsaveisRequests: [
      {
        nome: "",
        ondeProcurar: "",
        vivo: true,
        profissao: "",
        rg: "",
        cpf: "",
        emergencia: "",
        tipoResponsavel: "",
      }
    ]
  });



  useEffect(() => {
    const storedToken = localStorage.getItem("authToken");
    if (storedToken) {
      setToken(storedToken);
      setHasToken(true);
    }
  }, []);

  const handleTokenChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setToken(e.target.value);
  };

  const handleTokenSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (token.trim()) {
      localStorage.setItem("authToken", token.trim());
      setHasToken(true);
    }
  };

  const proximaEtapa = () => setEtapa((prev) => prev + 1);
  const etapaAnterior = () => setEtapa((prev) => prev - 1);

  return (
    <div className="min-h-screen bg-white p-6 flex flex-col items-center justify-center">
      {!hasToken ? (
        <form
          onSubmit={handleTokenSubmit}
          className="bg-gray-100 p-8 rounded-lg shadow-md max-w-sm w-full"
        >
          <h2 className="text-xl font-bold mb-4 text-center text-blue-900">
            Informe seu Token de Acesso
          </h2>
          <input
            type="text"
            name="token"
            placeholder="Token de acesso"
            value={token}
            onChange={handleTokenChange}
            className="text-black w-full mb-4 p-3 border rounded-lg"
            autoComplete="off"
          />
          <button
            type="submit"
            className="w-full bg-blue-800 text-white font-semibold py-3 rounded-lg hover:bg-blue-900"
          >
            Acessar
          </button>
        </form>
      ) : (
        <>
          {etapa === 1 && <CadastroUm onNext={proximaEtapa} />}
          {etapa === 2 && <CadastroDois onNext={proximaEtapa} onBack={etapaAnterior} formData={formData} setFormData={setFormData} />}
          {etapa === 3 && <CadastroTres onBack={etapaAnterior} formData={formData} />}
        </>
      )}
    </div>
  );
}