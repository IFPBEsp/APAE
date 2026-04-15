"use client";

import Image from "@/assets/background_image.jpg";
import { MembersRegisterProvider } from "@/hooks/use-members-register-context";
import { PageOrchestrator } from "./orchestrator";
import { Nunito } from "next/font/google";
import { VaccinesProvider } from "@/hooks/use-vaccines";
import { useMembersRegisterContext, MembersRegisterStep } from "@/hooks/use-members-register-context";
import { CheckCircle2, Circle } from "lucide-react";
import { DisordersProvider } from "@/hooks/use-disorders";

const nunito = Nunito({
  subsets: ["latin"],
  weight: ["400", "600", "700", "900"],
});

function SidebarSteps() {
  const { state: { step } } = useMembersRegisterContext();

  const stepsList = [
    { id: MembersRegisterStep.PERSONAL, label: "Dados Pessoais" },
    { id: MembersRegisterStep.KINSHIPS, label: "Parentesco" },
    { id: MembersRegisterStep.ADDRESS, label: "Endereço" },
    { id: MembersRegisterStep.ADDITIONALS, label: "Saúde & Social" },
    { id: MembersRegisterStep.GUARDIAN, label: "Responsável" },
    { id: MembersRegisterStep.PROFILE, label: "Perfil & Documentos" },
  ];

  const currentStepIndex = stepsList.findIndex((s) => s.id === step);

  return (
    <div className={`relative flex flex-col w-full h-full justify-center text-left text-white px-12 ${nunito.className}`}>
      <h1 className="text-4xl font-bold mb-4">BEM-VINDO</h1>
      <p className="text-lg font-semibold mb-12 text-blue-100">Progresso do cadastro:</p>

      <div className="flex flex-col gap-6">
        {stepsList.map((s, index) => {
          const isCompleted = index < currentStepIndex;
          const isCurrent = index === currentStepIndex;

          return (
            <div key={s.id} className={`flex items-center gap-4 transition-all ${isCurrent ? "opacity-100 scale-105" : "opacity-60"}`}>
              {isCompleted ? (
                <CheckCircle2 className="w-8 h-8 text-green-400" />
              ) : isCurrent ? (
                <div className="w-8 h-8 rounded-full bg-white flex items-center justify-center text-[#0D4F97] font-bold shadow-lg">{index + 1}</div>
              ) : (
                <Circle className="w-8 h-8 text-blue-300" />
              )}
              <span className={`text-lg font-bold ${isCurrent ? "text-white" : "text-blue-200"}`}>{s.label}</span>
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default function MembersRegisterLayout({
  children,
}: {
  readonly children: React.ReactNode;
}) {
  return (
    <MembersRegisterProvider>
      <VaccinesProvider>
        <DisordersProvider>
          <PageOrchestrator>
            <div className="h-screen rounded-lg mx-10 relative grid grid-cols-1 md:grid-cols-[1fr_2fr] antialiased overflow-hidden">
              <div
                className="absolute inset-0 bg-cover bg-center bg-no-repeat grayscale-90"
                style={{
                  backgroundImage: `url(${Image.src})`,
                  backgroundAttachment: "fixed",
                }}
              />

              <div
                className="absolute inset-0"
                style={{
                  background:
                    "linear-gradient(180deg, rgba(13, 79, 151, 0.7) 54.32%, rgba(255, 255, 255, 0.6) 110.28%)",
                }}
              />
               <div className="hidden md:flex relative z-10 w-full h-full bg-[#0D4F97]/90 backdrop-blur-sm">
                <SidebarSteps />
              </div>
              <div className="relative flex flex-col w-full h-full p-8 bg-muted overflow-y-auto">
                <h1 className="text-2xl font-bold text-blue-900 mb-4">
                  Cadastro de pessoas
                </h1>
                {children}
              </div>
            </div>
          </PageOrchestrator>
        </DisordersProvider>
      </VaccinesProvider>
    </MembersRegisterProvider>
  );
}
