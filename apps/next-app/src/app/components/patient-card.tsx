"use client";

import { Patient, PatientStatus } from "@/lib/mock-data";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { SquarePen } from "lucide-react";
import { cn } from "@/lib/utils";
import Link from "next/link";

interface PatientCardProps {
  patient: Patient;
}

const statusTextStyles: Record<PatientStatus, string> = {
  Ativo: "text-[#468f71]",
  Inativo: "text-[#871d1e]",
  "Em Fila": "text-[#9f9e9e]",
};

export function PatientCard({ patient }: PatientCardProps) {
  return (
    <Card
      className={cn(
        "overflow-hidden relative rounded-lg shadow",
        {
          "border-2 border-[#871d1e]": patient.status === "Inativo",
          "border-2 border-[#1E7F5C]": patient.status === "Ativo",
          "border border-[#9f9e9e]": patient.status !== "Inativo" && patient.status !== "Ativo",
        }
      )}
    >
      <Button
        size="icon"
        className="absolute top-2 right-2 h-8 w-8 z-10 !bg-transparent hover:!bg-transparent"
      >
        <Link href="/">
          <SquarePen className="h-5 w-5 text-[#145095]" />
          <span className="sr-only">Editar</span>
        </Link>
      </Button>

      <CardContent className="p-4">
        <div className="flex items-center gap-4">
          <div className="flex flex-col items-center gap-2 flex-shrink-0">
            <Avatar className="h-20 w-20 border">
              <AvatarImage src={patient.avatarUrl} alt={patient.name} />
              <AvatarFallback>{patient.name.charAt(0)}</AvatarFallback>
            </Avatar>

            <p className={cn("font-semibold text-sm", statusTextStyles[patient.status])}>
              {patient.status}
            </p>
          </div>

          <div className="flex-1 flex flex-col h-full">
            <div>
              <h3 className="text-base font-bold text-[#235d9b]">{patient.name}</h3>
              <div className="!text-[12px] text-[#235d9b] font-bold mt-1 space-y-0.5">
                <p>CPF: {patient.cpf}</p>
                <p>Contato: {patient.contact}</p>
                <p>Cidade: {patient.city}</p>
              </div>
            </div>

            <div className="mt-auto pt-2 flex justify-end">
              <Button className="w-[106px] h-[23px] rounded-[5px] !bg-[#0D4F97] !hover:bg-[#0b427d] !text-white !text-[12px]">
                <Link href="/">
                  Ver mais
                </Link>
              </Button>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}