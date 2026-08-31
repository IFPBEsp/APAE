import React from "react";

interface InfoRowProps {
  label: string;
  value?: string | number | null;
  isCurrency?: boolean;
}

export const InfoRow: React.FC<InfoRowProps> = ({ label, value, isCurrency = false }) => {
  let displayValue: string | number = value || "Não informado";

  if (isCurrency && typeof value === "number") {
    displayValue = value.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
  } else if (!value) {
    displayValue = "Não informado";
  }

  return (
    <div className="mb-2">
      <span className="text-sm font-semibold text-gray-500">{label}</span>
      <p className="text-base text-black">{displayValue}</p>
    </div>
  );
};
