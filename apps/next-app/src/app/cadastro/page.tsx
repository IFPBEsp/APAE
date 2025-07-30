"use client";

import React, { useState } from "react";
import CadastroUm from "../components/CadastroUm";
import CadastroDois from "../components/CadastroDois";
import CadastroTres from "../components/CadastroTres";

export default function CadastroPage() {
  const [etapa, setEtapa] = useState(1);

  const proximaEtapa = () => setEtapa((prev) => prev + 1);
  const etapaAnterior = () => setEtapa((prev) => prev - 1);

  return (
    <div className="min-h-screen bg-white p-6"> {/* <- tela inteira branca */}
      {etapa === 1 && <CadastroUm onNext={proximaEtapa} />}
      {etapa === 2 && <CadastroDois onNext={proximaEtapa} onBack={etapaAnterior} />}
      {etapa === 3 && <CadastroTres onBack={etapaAnterior} />}
    </div>
  );
}
