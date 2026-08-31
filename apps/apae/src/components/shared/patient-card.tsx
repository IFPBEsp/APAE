"use client";

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { SquarePen } from "lucide-react";
import { cn } from "@/lib/utils";
import Link from "next/link";
import { PatientCardData } from "@/schemas/patientSchema";

interface PatientCardProps {
  patient: PatientCardData;
}

interface PatientStatusData {
  isDeleted?: boolean;
  isStudent?: boolean;
}

const getStatus = (patient: PatientStatusData) => {
  if (patient.isDeleted) return "Inativo";
  if (patient.isStudent) return "Aluno";
  return "Paciente";
};

const statusTextStyles: { [key: string]: string } = {
  Patient: "text-[#468f71]",
  Student: "text-[#003B93]",
  Inactive: "text-[#871d1e]",
  "In Line": "text-[#9f9e9e]",
};

const statusBorderStyles: { [key: string]: string } = {
  Patient: "border-2 border-[#5db993]",
  Student: "border-2 border-[#0D4F97]",
  Inactive: "border-2 border-[#ac3637]",
  "In Line": "border border-[#9f9e9e]",
};

export function PatientCard({ patient }: PatientCardProps) {
  const patientStatus = getStatus(patient);

  return (
    <Card
      className={cn(
        "overflow-hidden relative rounded-lg shadow-md/30 h-full flex flex-col",
        statusBorderStyles[patientStatus],
      )}
    >
      <Button
        size="icon"
        className="absolute top-2 right-2 h-8 w-8 z-10 !bg-transparent hover:!bg-transparent"
        asChild
      >
        <Link href={`/patients/${patient.id}/edit/personal`}>
          <SquarePen className="h-5 w-5 text-[#145095]" />
          <span className="sr-only">Editar</span>
        </Link>
      </Button>

      <CardContent className="p-4 flex-1">
        {/* 3. CHANGED: 'items-center' to 'items-start' and added 'h-full'. 
                    This aligns the avatar and texts to the top, and allows the button to go to the bottom. */}
        <div className="flex items-start gap-4 h-full">
          <div className="flex flex-col items-center gap-2 flex-shrink-0">
            <Avatar className="h-20 w-20 border">
              <AvatarImage
                src={patient.photoUrl ?? undefined}
                alt={patient.fullName ?? "Foto do paciente"}
              />
              <AvatarFallback>{patient.fullName?.charAt(0) ?? "P"}</AvatarFallback>
            </Avatar>
            <p className={cn("font-semibold text-sm", statusTextStyles[patientStatus])}>
              {patientStatus}
            </p>
          </div>

          {/* 4. ADDED: 'min-w-0' to prevent long texts from breaking the card */}
          <div className="flex-1 flex flex-col h-full min-w-0">
            <div>
              {/* 5. ADDED: 'line-clamp-2 break-all min-h-[3rem]' 
                                Limits to 2 lines, breaks long words and fixes title height, 
                                aligning the CPF of all cards. */}
              <h3 className="text-base font-bold text-[#235d9b] line-clamp-2 break-all min-h-[3rem]">
                {patient.fullName ?? "Nome não informado"}
              </h3>
              <div className="!text-[12px] text-[#235d9b] font-bold mt-1 space-y-0.5">
                {/* 6. ADDED: 'truncate' to prevent bottom info from wrapping to a 2nd line */}
                <p className="truncate">CPF: {patient.cpf ?? "Não informado"}</p>
                <p className="truncate">Contato: {patient.contact ?? "Não informado"}</p>
                <p className="truncate">Cidade: {patient.address?.city ?? "Não informado"}</p>
              </div>
            </div>

            {/* mt-auto here will now work perfectly, pushing the button to the bottom! */}
            <div className="mt-auto pt-2 flex justify-end">
              <Button
                asChild
                className="w-[106px] h-[23px] rounded-[5px] !bg-[#0D4F97] !hover:bg-[#0b427d] !text-white !text-[12px]"
              >
                <Link href={`/patients/${patient.id}`}>Ver mais</Link>
              </Button>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
