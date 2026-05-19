"use client";

import { useMembersRegisterContext, MembersRegisterStep } from "@/hooks/use-members-register-context";
import { CheckCircle2, Circle } from "lucide-react";
import { Nunito } from "next/font/google";
import { usePathname } from "next/navigation";

const nunito = Nunito({ subsets: ["latin"], weight: ["400", "600", "700", "900"] });

export function SidebarSteps() {
  const { state: { step } } = useMembersRegisterContext();
  const pathname = usePathname();
  const isEditing = pathname.includes("/edit"); 

  const stepsList = [
    { id: MembersRegisterStep.PERSONAL, label: "Dados Pessoais" },
    { id: MembersRegisterStep.KINSHIPS, label: "Parentesco" },
    { id: MembersRegisterStep.GUARDIAN, label: "Responsável" },
    { id: MembersRegisterStep.ADDRESS, label: "Endereço" },
    ...(!isEditing ? [{ id: MembersRegisterStep.ADDITIONALS, label: "Saúde & Social" }] : []),
    { id: MembersRegisterStep.PROFILE, label: "Perfil & Documentos" },
  ];

  const currentStepIndex = stepsList.findIndex((s) => s.id === step);

  return (
    <div className={`relative flex flex-col w-full h-full justify-center text-left text-white px-8 md:px-16 ${nunito.className}`}>
      <h1 className="text-3xl md:text-4xl font-bold mb-4">{isEditing ? "EDIÇÃO" : "BEM-VINDO"}</h1>
      <p className="text-base md:text-lg font-semibold mb-10 text-blue-100">
        {isEditing ? "Acompanhe o progresso da edição:" : "Acompanhe o progresso do seu cadastro:"}
      </p>

      <div className="flex flex-col gap-6">
        {stepsList.map((s, index) => {
          const isCompleted = index < currentStepIndex;
          const isCurrent = index === currentStepIndex;

          return (
            <div key={s.id} className={`flex items-center gap-4 transition-all duration-300 ${isCurrent ? "opacity-100 scale-105" : "opacity-60"}`}>
              {/* O Ícone da Bolinha */}
              <div className="flex-shrink-0">
                {isCompleted ? (
                  <CheckCircle2 className="w-8 h-8 text-green-400" />
                ) : isCurrent ? (
                  <div className="w-8 h-8 rounded-full bg-white flex items-center justify-center text-[#0D4F97] font-bold shadow-lg">
                    {index + 1}
                  </div>
                ) : (
                  <Circle className="w-8 h-8 text-blue-300" />
                )}
              </div>
              {/* O Texto do Passo */}
              <span className={`text-base md:text-lg font-bold ${isCurrent ? "text-white" : "text-blue-200"}`}>
                {s.label}
              </span>
            </div>
          );
        })}
      </div>
    </div>
  );
}