import { NextRequest, NextResponse } from "next/server";
import { createDocumentsAPI } from "@/lib/axios";

export async function GET(
  req: NextRequest,
  { params }: { params: Promise<{ patientId: string }> }
) {
  const { searchParams } = new URL(req.url);
  const year = searchParams.get("year");
  const category = searchParams.get("category")?.toUpperCase();
  const type = searchParams.get("type")?.toUpperCase();
  const { patientId } = await params;

  if (!year || !category) {
    return NextResponse.json(
      {
        error:
          "Os parâmetros 'year' e 'category' são obrigatórios e estão faltando",
      },
      { status: 400 }
    );
  }

  try {
    const api = await createDocumentsAPI();
    const response = await api.get(`/${patientId}/type`, {
      params: { year, category, ...(type && { type }) },
    });

    return NextResponse.json(response.data);
  } catch (error) {
    const axiosError = error as { response?: { data?: unknown } };
    console.error("Erro na API:", axiosError?.response?.data || error);
    return NextResponse.json(
      { error: "Erro ao buscar documentos" },
      { status: 500 }
    );
  }
}
