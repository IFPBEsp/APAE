import { NextRequest, NextResponse } from "next/server";
import { createDocumentsAPI } from "@/lib/axios";
import { AxiosError } from "axios";

const categoryMap: Record<string, string> = {
  PERSONAL: "personals",
  PESSOAL: "personals",
  PESSOAIS: "personals",
  MEDICAL: "medicals",
  MEDICO: "medicals",
  MEDICOS: "medicals",
  SCHOOL: "schools",
  ESCOLAR: "schools",
  ESCOLARES: "schools",
};

type DocumentSummary = {
  type?: string;
};

export async function GET(
  req: NextRequest,
  { params }: { params: Promise<{ patientId: string }> },
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
      { status: 400 },
    );
  }

  const endpointSuffix = categoryMap[category];

  if (!endpointSuffix) {
    return NextResponse.json(
      { error: "Categoria de documento inválida" },
      { status: 400 },
    );
  }

  const parsedYear = Number.parseInt(year, 10);

  if (Number.isNaN(parsedYear)) {
    return NextResponse.json(
      { error: "O parâmetro 'year' deve ser numérico" },
      { status: 400 },
    );
  }

  try {
    const api = await createDocumentsAPI();
    const response = await api.get(
      `/patients/${patientId}/documents/${endpointSuffix}`,
      {
        params: { year: parsedYear },
      },
    );

    const documents = Array.isArray(response.data)
      ? (response.data as DocumentSummary[])
      : [];

    const filteredDocuments = type
      ? documents.filter((document) => document.type?.toUpperCase() === type)
      : documents;

    return NextResponse.json(filteredDocuments);
  } catch (error) {
    const axiosError = error as AxiosError;
    if (axiosError.response?.status === 404) {
      return NextResponse.json([]);
    }

    console.error("Erro na API:", axiosError.response?.data || error);
    return NextResponse.json(
      { error: "Erro ao buscar documentos" },
      { status: 500 },
    );
  }
}
