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

const getStatus = (patient: any) => {
    if (patient.isDeleted) return "Inativo";
    if (patient.isStudent) return "Aluno";
    return "Paciente";
};

const statusTextStyles: { [key: string]: string } = {
    Paciente: "text-[#468f71]",
    Aluno: "text-[#003B93]",
    Inactive: "text-[#871d1e]",
    "Em Fila": "text-[#9f9e9e]",
};

const statusBorderStyles: { [key: string]: string } = {
    Paciente: "border-2 border-[#5db993]",
    Aluno: "border-2 border-[#0D4F97]",
    Inactive: "border-2 border-[#ac3637]",
    "Em Fila": "border border-[#9f9e9e]",
};

export function PatientCard({ patient }: PatientCardProps) {
    const patientStatus = getStatus(patient);

    return (
        <Card
            className={cn(
                "overflow-hidden relative rounded-lg shadow-md/30",
                statusBorderStyles[patientStatus]
            )}
        >
            <CardContent className="p-4">
                <div className="flex items-center gap-4">
                    <div className="flex flex-col items-center gap-2 flex-shrink-0">
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
                                statusTextStyles[patientStatus]
                            )}
                        >
                            {patientStatus}
                        </p>
                    </div>

                    <div className="flex-1 flex flex-col h-full">
                        <div>
                            <h3 className="text-base font-bold text-[#235d9b]">
                                {patient.fullName ?? "Nome não informado"}
                            </h3>
                            <div className="!text-[12px] text-[#235d9b] font-bold mt-1 space-y-0.5">
                                <p>CPF: {patient.cpf ?? "Não informado"}</p>
                                <p>Contato: {patient.contact ?? "Não informado"}</p>
                                <p>Cidade: {patient.address?.city ?? "Não informado"}</p>
                            </div>
                        </div>

                        <div className="mt-auto pt-2 flex justify-end">
                            <Button
                                asChild
                                className="w-[106px] h-[23px] rounded-[5px] !bg-[#0D4F97] !hover:bg-[#0b427d] !text-white !text-[12px]"
                            >
                                <Link href={`/person/${patient.id}`}>Ver mais</Link>
                            </Button>
                        </div>
                    </div>
                </div>
            </CardContent>
        </Card>
    );
}