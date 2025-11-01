"use client";

import { PatientCardData } from "@/schemas/patientSchema"; 

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { SquarePen } from "lucide-react";
import Link from "next/link";

interface PatientCardProps {
  patient: PatientCardData;
}

export function PatientCard({ patient }: PatientCardProps) {
  return (
    <Card
      className="overflow-hidden relative rounded-lg shadow-md/30"
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
              <AvatarImage 
                src={patient.urlFoto ?? undefined} 
                alt={patient.nome}
              />
              <AvatarFallback>
                {patient.nome.charAt(0)}
              </AvatarFallback>
            </Avatar>

          </div>
          <div className="flex-1 flex flex-col h-full">
            <div>
              <h3 className="text-base font-bold text-[#235d9b]">
                {patient.nome}
              </h3>
              <div className="!text-[12px] text-[#235d9b] font-bold mt-1 space-y-0.5">
                <p>CPF: {patient.cpf}</p>
                <p>Contato: {patient.contato.telefone}</p>
                <p>Cidade: {patient.cidade}</p>
              </div>
            </div>

            <div className="mt-auto pt-2 flex justify-end">
              <Button className="w-[106px] h-[23px] rounded-[5px] !bg-[#0D4F97] !hover:bg-[#0b427d] !text-white !text-[12px]">
                <Link href={`/pessoa/${patient.id}`}>
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