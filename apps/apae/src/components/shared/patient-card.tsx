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
    Inativo: "text-[#871d1e]",
    "Em Fila": "text-[#9f9e9e]",
};

const statusBorderStyles: { [key: string]: string } = {
    Paciente: "border-2 border-[#5db993]",
    Aluno: "border-2 border-[#0D4F97]",
    Inativo: "border-2 border-[#ac3637]",
    "Em Fila": "border border-[#9f9e9e]",
};

export function PatientCard({ patient }: PatientCardProps) {
    const patientStatus = getStatus(patient);

    return (
         <Card
            className={cn(
                "overflow-hidden relative rounded-lg shadow-md/30 h-full flex flex-col",
                statusBorderStyles[patientStatus]
            )}
        >
            <Button
                size="icon"
                className="absolute top-2 right-2 h-8 w-8 z-10 !bg-transparent hover:!bg-transparent"
                asChild
            >
                <Link href={`/person/${patient.id}/edit/personal`}>
                    <SquarePen className="h-5 w-5 text-[#145095]" />
                    <span className="sr-only">Editar</span>
                </Link>
            </Button>

            <CardContent className="p-4 flex-1">
                {/* 3. ALTERADO: 'items-center' para 'items-start' e adicionado 'h-full'. 
                    Isso alinha o avatar e os textos pelo topo, e permite que o botão vá pro fundo. */}
                <div className="flex items-start gap-4 h-full">
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

                    {/* 4. ADICIONADO: 'min-w-0' para impedir que textos longos (como silvawww...) quebrem o card */}
                    <div className="flex-1 flex flex-col h-full min-w-0">
                        <div>
                            {/* 5. ADICIONADO: 'line-clamp-2 break-all min-h-[3rem]' 
                                Limita a 2 linhas, quebra a palavra longa e fixa a altura do título, 
                                alinhando o CPF de todos os cards. */}
                            <h3 className="text-base font-bold text-[#235d9b] line-clamp-2 break-all min-h-[3rem]">
                                {patient.fullName ?? "Nome não informado"}
                            </h3>
                            <div className="!text-[12px] text-[#235d9b] font-bold mt-1 space-y-0.5">
                                {/* 6. ADICIONADO: 'truncate' para evitar que as infos de baixo gerem uma 2ª linha */}
                                <p className="truncate">CPF: {patient.cpf ?? "Não informado"}</p>
                                <p className="truncate">Contato: {patient.contact ?? "Não informado"}</p>
                                <p className="truncate">Cidade: {patient.address?.city ?? "Não informado"}</p>
                            </div>
                        </div>

                        {/* Seu mt-auto aqui agora vai funcionar perfeitamente, empurrando o botão para a base! */}
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