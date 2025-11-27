export const dynamic = 'force-dynamic';

import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextResponse } from "next/server";

export async function GET(
  { params }: { params: Promise<{ id: string, year: string}>}
) {
  const { id: patientId, year } = await params; 

  if (!patientId || !year) {
    return NextResponse.json({ message: "ID do paciente e Ano são obrigatórios" }, { status: 400 });
  }

  try {
    const api = await createBaseApi(); 
    
    const { data } = await api.get(`/patients/${patientId}/annual-registry/${year}`); 
    
    return NextResponse.json(data);

  } catch (error) {
    const err = error as AxiosError;
    
    if (err.response?.status === 404) {
      return NextResponse.json({ message: "Nenhum registro anual encontrado para este ano." }, { status: 404 });
    }

    console.error(`[API Route Error] /api/pessoas/${patientId}/annual-registry/${year}:`, err.message);
    
    return NextResponse.json(
      { message: err.response?.data || "Erro no servidor ao buscar registro anual" },
      { status: err.response?.status || 500 }
    );
  }
}