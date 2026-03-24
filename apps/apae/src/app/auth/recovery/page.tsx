"use client";

import { Send } from "lucide-react";
import React, { useState } from "react";

type RecoverPasswordModalProps = {
  isOpen: boolean;
  onClose: () => void;
};

const RecoverPasswordModal: React.FC<RecoverPasswordModalProps> = ({
  isOpen,
  onClose,
}) => {
  const [email, setEmail] = useState("");
  const [code, setCode] = useState("");

  const onSubmit = () => {
    // Lógica para enviar o código de recuperação e validar o código inserido
    // Você pode adicionar chamadas à API aqui para lidar com a recuperação de senha
    alert(`Email: ${email}\nCódigo: ${code}`);
  }

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/40 z-50">
      <div className="w-full max-w-md bg-white rounded-2xl shadow-lg p-6 relative">
        
        <div className="flex flex-col items-center mb-4">
          <img
            src="apps/apae/src/assets/APAE-logo.svg"
            alt="APAE"
            className="w-16 h-16 mb-2"
          />
          <h1 className="text-xl font-bold text-gray-800">APAE</h1>
          <h2 className="text-lg font-semibold text-blue-700 mt-1">
            Recuperar Senha
          </h2>
        </div>

        <div className="mb-4">
          <label className="text-sm font-medium text-gray-700">
            Usuário
          </label>
          <div className="flex mt-1">
            <input
              type="email"
              placeholder="Digite seu email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="flex-1 border rounded-l-md px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500"
            />
            <button
              className="bg-blue-700 text-white px-4 flex items-center justify-center rounded-r-md hover:bg-blue-800 cursor-pointer"
              title="Enviar código"
              onClick={() => {}}
            >
              <Send size={18} />
            </button>
          </div>
          <p className="text-xs text-gray-500 mt-1">
            Um código de verificação será enviado para o seu email.
          </p>
        </div>

        <div className="mb-6">
          <label className="text-sm font-medium text-gray-700">
            Código de verificação
          </label>
          <input
            type="text"
            placeholder="Digite o código"
            value={code}
            onChange={(e) => setCode(e.target.value)}
            className="w-full mt-1 border rounded-md px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <button 
          className="w-full bg-blue-700 text-white py-2 rounded-md hover:bg-blue-800 transition cursor-pointer"
          onClick={() => {onSubmit}}>
          
          Enviar
        </button>

        <div className="text-center mt-3">
          <button
            onClick={onClose}
            className="text-orange-500 text-sm hover:underline cursor-pointer transition-colors duration-200"
          >
            Voltar para tela de login.
          </button>
        </div>
      </div>
    </div>
  );
};

export default function RecoveryPage() {
  return (
    <RecoverPasswordModal
      isOpen={true}
      onClose={() => (window.location.href = "/auth/login")}
    />
  );
}