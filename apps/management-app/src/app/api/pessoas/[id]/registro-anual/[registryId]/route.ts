// src/app/api/pessoas/[id]/registro-anual/[registryId]/route.ts

import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

export async function PUT(
  request: NextRequest,
  { params }: { params: Promise<{ id: string, registryId: string }> } 
) {
  const { id: patientId, registryId } = await params;
  const body = await request.json(); 

  if (!registryId) {
    return NextResponse.json({ message: "ID do registro é obrigatório" }, { status: 400 });
  }

  try {
    const api = await createBaseApi();
    
    const response = await api.put(
      `/patients/${patientId}/annual-registry/${registryId}`, 
      body
    );

    return NextResponse.json(response.data);

  } catch (error) {
    const err = error as AxiosError;
    console.error(`[API Route Error] PUT Registro Anual:`, err.message, err.response?.data);
    
    return NextResponse.json(
      { message: err.response?.data || "Erro ao atualizar registro no servidor" },
      { status: err.response?.status || 500 }
    );
  }
}