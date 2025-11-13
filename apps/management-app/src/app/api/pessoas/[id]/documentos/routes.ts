import { createBaseApi } from "@/lib/axios"; 
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

export async function GET(
  request: NextRequest,
  { params }: { params: { id: string } }
) {
  const { id: patientId } = params;
  
  const searchParams = request.nextUrl.searchParams;
  const category = searchParams.get("category");
  const year = searchParams.get("year");
  const type = searchParams.get("type");

  if (!patientId || !category || !year) {
    return NextResponse.json({ message: "Parâmetros inválidos" }, { status: 400 });
  }

  try {
    const api = await createBaseApi();

    const { data } = await api.get(`/patients/${patientId}/documentos`, {
      params: { 
        category: category.toUpperCase(),
        year: year,
        type: type ? type.toUpperCase() : undefined,
       },
    });

    return NextResponse.json(data);

  } catch (error) {
    const err = error as AxiosError;
    
    console.error("[API Route Error] /api/pessoas/[id]/documentos:", err.message);

    return NextResponse.json(
      { message: err.response?.data || "Erro ao buscar documentos no servidor" },
      { status: err.response?.status || 500 }
    );
  }
}