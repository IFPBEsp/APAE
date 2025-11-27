export const dynamic = 'force-dynamic';

import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

const categoryMap: { [key: string]: string } = {
  pessoal: "personals",
  pessoais: "personals",
  medico: "medicals",
  medicos: "medicals",
  escolar: "schools",
  escolares: "schools",
};

export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id: patientId } = await params;
  const searchParams = request.nextUrl.searchParams;
  const categoryRaw = searchParams.get("category");

  if (!patientId || !categoryRaw) {
    return NextResponse.json({ message: "Dados incompletos" }, { status: 400 });
  }

  const endpointSuffix = categoryMap[categoryRaw.toLowerCase()];

  if (!endpointSuffix) {
    return NextResponse.json({ message: "Categoria inválida" }, { status: 400 });
  }

  try {
    const api = await createBaseApi();
    const { data } = await api.get(`/patients/${patientId}/documents/${endpointSuffix}`);
    
    return NextResponse.json(data);

  } catch (error) {
    const err = error as AxiosError;
    return NextResponse.json(
      { message: err.response?.data || "Erro ao buscar documentos" },
      { status: err.response?.status || 500 }
    );
  }
}