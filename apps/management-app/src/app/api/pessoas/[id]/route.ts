export const dynamic = 'force-dynamic';

import { createBaseApi } from "@/lib/axios"; 
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

export async function GET(
  request: NextRequest,
  { params }: { params: { id: string } }
) {
  const { id: patientId } = params;

  if (!patientId) {
    return NextResponse.json({ message: "ID do paciente é obrigatório" }, { status: 400 });
  }

  try {
    const api = await createBaseApi();
    
    const { data } = await api.get(`/patients/${patientId}`); 
    
    return NextResponse.json(data);

  } catch (error) {
    const err = error as AxiosError;
    console.error(`[API Route Error] /api/pessoas/${patientId}:`, err.message);
    
    return NextResponse.json(
      { message: err.response?.data || "Erro no servidor ao buscar paciente" },
      { status: err.response?.status || 500 }
    );
  }
}