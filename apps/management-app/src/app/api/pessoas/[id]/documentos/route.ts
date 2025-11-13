export const dynamic = 'force-dynamic';

import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

const categoryMap: { [key: string]: string } = {
  pessoal: "PERSONAL",
  pessoais: "PERSONAL",
  medico: "MEDICAL",
  medicos: "MEDICAL",
  escolar: "SCHOOL",
  escolares: "SCHOOL",
  apae: "APAE",
};

const typeMap: { [key: string]: string } = {
  LAUDO: "MEDICAL_REPORT",
  ENCAMINHAMENTO: "REFERRAL",
  CERTIDAO_DE_NASCIMENTO: "BIRTH_CERTIFICATE",
  CPF: "CPF",
  EXAME: "EXAMINATION",
  OUTRO: "OTHER",
  FOTO: "PHOTO",
  COMPROVANTE_DE_RESIDENCIA: "PROOF_OF_ADDRESS",
  RELATORIO: "PROGRESS_REPORT",
  RG: "RG",
};

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
    return NextResponse.json(
      { message: "Parâmetros inválidos: id, category e year são obrigatórios" },
      { status: 400 }
    );
  }

  const mappedCategory = categoryMap[category.toLowerCase()];
  const mappedType = type ? typeMap[type.toUpperCase()] : undefined;

  if (!mappedCategory) {
    return NextResponse.json(
      { message: "Categoria de documento inválida" },
      { status: 400 }
    );
  }

  try {
    const api = await createBaseApi();

    const { data } = await api.get(`/paciente/${patientId}/documentos`, {
      params: {
        category: mappedCategory,
        year: year,
        type: mappedType,
      },
    });
    
    return NextResponse.json(data);

  } catch (error) {
    const err = error as AxiosError;
    console.error(
      "[API Route Error] /api/pessoas/[id]/documentos:",
      err.message,
      err.response?.data
    );

    return NextResponse.json(
      { message: err.response?.data || "Erro ao buscar documentos no servidor" },
      { status: err.response?.status || 500 }
    );
  }
}