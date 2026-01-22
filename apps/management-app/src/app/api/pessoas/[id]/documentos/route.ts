import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";
import { headers } from "next/headers";

export const dynamic = 'force-dynamic';

const categoryMap: { [key: string]: string } = {
  pessoal: "personals",
  pessoais: "personals",
  medico: "medicals",
  medicos: "medicals",
  escolar: "schools",
  escolares: "schools",
  personal: "personals",
  medical: "medicals",
  school: "schools",
};

export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id: patientId } = await params;
  const searchParams = request.nextUrl.searchParams;
  const categoryRaw = searchParams.get("category");

  if (!patientId || !categoryRaw) {
    return NextResponse.json({ message: "ID e Categoria são obrigatórios" }, { status: 400 });
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
    const status = err.response?.status || 500;
    const msg = err.response?.data ? JSON.stringify(err.response.data) : err.message;
    
    console.error(`[API GET] Erro ao buscar documentos (${endpointSuffix}):`, msg);
    if (status === 404) return NextResponse.json([]);

    return NextResponse.json(
      { message: "Erro ao buscar documentos." },
      { status }
    );
  }
}

export async function POST(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id: patientId } = await params;

  try {
    const incomingFormData = await request.formData();
    const file = incomingFormData.get("file");
    const category = incomingFormData.get("category");
    const type = incomingFormData.get("type");

    if (!file || !category || !type) {
        return NextResponse.json({ message: "Dados incompletos" }, { status: 400 });
    }

    const baseUrl = process.env.NEXT_PUBLIC_API || "http://localhost:8090/api"; 

    const queryParams = new URLSearchParams({
        category: category.toString(),
        type: type.toString()
    });

    const targetUrl = `${baseUrl}/patients/${patientId}/documents?${queryParams.toString()}`;

    const backendFormData = new FormData();
    backendFormData.append("file", file);

    const headersList = await headers();
    const authToken = headersList.get("Authorization");

    const response = await fetch(targetUrl, {
        method: "POST",
        headers: {
            ...(authToken && { "Authorization": authToken }),
        },
        body: backendFormData,
    });

    if (!response.ok) {
        const errorText = await response.text();
        console.error("[API POST] Erro Java:", errorText);
        return NextResponse.json({ message: "Erro no backend." }, { status: response.status });
    }

    return NextResponse.json({ success: true }, { status: 201 });

  } catch (error: any) {
    console.error("[API POST] Erro Next.js:", error);
    return NextResponse.json({ message: "Erro interno no upload." }, { status: 500 });
  }
}