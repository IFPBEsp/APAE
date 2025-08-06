"use client";

import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { patients } from "@/lib/mock-data";
import { PatientCard } from "./patient-card"; 

export function PatientsAndStudentsScreen() {
  return (
    <div className="bg-slate-100 min-h-screen">
      
      <main className="container mx-auto p-4 md:p-6">
        <section className="relative md:bg-white md:rounded-xl md:shadow-md md:border-2 md:border-[#003B93] md:p-6">
          <div className="hidden md:flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-[#003B93]">
              Pacientes e Alunos
            </h2>
            <Button asChild className="!bg-[#0D4F97] !hover:bg-[#0b427d] text-white">
                <Link href="/cadastro-de-pessoas">
                  Adicionar
                </Link>
            </Button>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {patients.map((patient) => (
              <PatientCard key={patient.id} patient={patient} />
            ))}
          </div>
        </section>
      </main>

      <Button
        asChild
        className="fixed bottom-6 right-6 h-[53px] w-[53px] rounded-full shadow-lg md:hidden bg-[#0D4F97] hover:bg-[#0b427d]"
      >
        <Link href="/cadastro-de-pessoas">
          <Plus className="h-7 w-7" />
          <span className="sr-only">Adicionar Pessoa</span>
        </Link>
      </Button>
    </div>
  );
}