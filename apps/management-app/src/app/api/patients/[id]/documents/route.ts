import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

import { createBaseApi } from "@/lib/axios";

const categoryRouteMap: Record<string, string> = {
  medical: "medicals",
  medicals: "medicals",
  medicos: "medicals",
  personal: "personals",
  personals: "personals",
  pessoais: "personals",
  school: "schools",
  schools: "schools",
  escolares: "schools",
};

function normalizeCategory(category: string | null) {
  if (!category) {
    return null;
  }

  return categoryRouteMap[category.toLowerCase()] || null;
}

export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> },
) {
  const { id } = await params;
  const category = normalizeCategory(request.nextUrl.searchParams.get("category"));
  const year = request.nextUrl.searchParams.get("year");
  const type = request.nextUrl.searchParams.get("type");

  if (!category) {
    return NextResponse.json({ message: "Categoria de documento invalida." }, { status: 400 });
  }

  try {
    const api = await createBaseApi();
    const searchParams = new URLSearchParams();

    if (year) {
      searchParams.set("year", year);
    }

    const suffix = searchParams.size > 0 ? `?${searchParams.toString()}` : "";
    const response = await api.get(`/patients/${id}/documents/${category}${suffix}`);
    const documents = Array.isArray(response.data) ? response.data : [];
    const filteredDocuments = type
      ? documents.filter((document: { type?: string }) => document.type === type)
      : documents;

    return NextResponse.json(filteredDocuments);
  } catch (error) {
    const err = error as AxiosError;
    return NextResponse.json(
      { message: err.response?.data || "Erro ao buscar documentos" },
      { status: err.response?.status || 500 },
    );
  }
}

export async function POST(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> },
) {
  const { id } = await params;

  try {
    const incomingFormData = await request.formData();
    const file = incomingFormData.get("file");
    const category = incomingFormData.get("category");
    const type = incomingFormData.get("type");
    const year = incomingFormData.get("year");

    if (!(file instanceof File) || typeof category !== "string" || typeof type !== "string") {
      return NextResponse.json({ message: "Dados de upload invalidos." }, { status: 400 });
    }

    const formData = new FormData();
    formData.append("file", file);
    formData.append("category", category);
    formData.append("type", type);

    if (typeof year === "string" && year.trim()) {
      formData.append("year", year);
    }

    const api = await createBaseApi();
    const response = await api.post(`/patients/${id}/documents`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });

    return NextResponse.json(response.data, { status: response.status });
  } catch (error) {
    const err = error as AxiosError;
    return NextResponse.json(
      { message: err.response?.data || "Erro ao enviar documento" },
      { status: err.response?.status || 500 },
    );
  }
}
