"use client";

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { SquarePen } from "lucide-react";
import { cn } from "@/lib/utils";
import Link from "next/link";
import type {
  PatientCardData,
  PatientStatus,
} from "@/domains/patients/types/patient";

interface PatientCardProps {
  patient: PatientCardData;
  status?: PatientStatus;
}

type PatientType = "Paciente" | "Aluno";

const getType = (patient: PatientCardData): PatientType =>
  patient.isStudent ? "Aluno" : "Paciente";

const typeTextStyles: Record<PatientType, string> = {
  Paciente: "text-[#468f71]",
  Aluno: "text-[#003B93]",
};

const typeBorderStyles: Record<PatientType, string> = {
  Paciente: "border-2 border-[#5db993]",
  Aluno: "border-2 border-[#0D4F97]",
};

const statusLabels: Record<PatientStatus, string> = {
  active: "Ativo",
  inactive: "Inativo",
};

const statusTextStyles: Record<PatientStatus, string> = {
  active: "text-[#468f71]",
  inactive: "text-[#871d1e]",
};

const inactiveBorderStyle = "border-2 border-[#ac3637]";

export function PatientCard({ patient, status }: PatientCardProps) {
  const patientType = getType(patient);

  const effectiveStatus: PatientStatus | undefined = patient.isDeleted
    ? "inactive"
    : status;

  return (
    <Card
      className={cn(
        "overflow-hidden relative rounded-lg shadow-md/30 h-full flex flex-col",
        effectiveStatus === "inactive"
          ? inactiveBorderStyle
          : typeBorderStyles[patientType],
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
        <div className="flex items-start gap-4 h-full">
          <div className="flex flex-col items-center gap-1 flex-shrink-0">
            <Avatar className="h-20 w-20 border">
              <AvatarImage
                src={patient.photoUrl ?? undefined}
                alt={patient.fullName ?? "Foto do paciente"}
              />
              <AvatarFallback>
                {patient.fullName?.charAt(0) ?? "P"}
              </AvatarFallback>
            </Avatar>
            <p
              className={cn(
                "font-semibold text-sm",
                typeTextStyles[patientType],
              )}
            >
              {patientType}
            </p>
            {effectiveStatus && (
              <p
                className={cn(
                  "font-semibold text-xs",
                  statusTextStyles[effectiveStatus],
                )}
              >
                {statusLabels[effectiveStatus]}
              </p>
            )}
          </div>

          <div className="flex-1 flex flex-col h-full min-w-0">
            <div>
              <h3 className="text-base font-bold text-[#235d9b] line-clamp-2 break-all min-h-[3rem]">
                {patient.fullName ?? "Nome não informado"}
              </h3>
              <div className="!text-[12px] text-[#235d9b] font-bold mt-1 space-y-0.5">
                <p className="truncate">
                  CPF: {patient.cpf ?? "Não informado"}
                </p>
                <p className="truncate">
                  Contato: {patient.contact ?? "Não informado"}
                </p>
                <p className="truncate">
                  Cidade: {patient.address?.city ?? "Não informado"}
                </p>
              </div>
            </div>

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
