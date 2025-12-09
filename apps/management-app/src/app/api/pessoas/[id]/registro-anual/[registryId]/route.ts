export const dynamic = 'force-dynamic';

import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

interface RouteParams {
  params: Promise<{ id: string; registryId: string }>;
}

export async function GET(request: NextRequest, { params }: RouteParams) {
  const { id: patientId, registryId: year } = await params;

  try {
    const api = await createBaseApi();
    const { data } = await api.get(`/patients/${patientId}/annual-registry/${year}`);
    return NextResponse.json(data);
  } catch (error) {
    const err = error as AxiosError;
    if (err.response?.status === 404) {
      return NextResponse.json({ message: "Registro não encontrado" }, { status: 404 });
    }
    return NextResponse.json(
      { message: "Erro no servidor ao buscar registro" },
      { status: 500 }
    );
  }
}

export async function PUT(request: NextRequest, { params }: RouteParams) {
  const { id: patientId, registryId } = await params;
  const body = await request.json();

  if (!registryId) {
    return NextResponse.json(
      { message: "ID do registro é obrigatório" },
      { status: 400 }
    );
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
    console.error(
      `[API Route Error] PUT Registro Anual:`,
      err.message,
      err.response?.data
    );

    return NextResponse.json(
      { message: err.response?.data || "Erro ao atualizar registro no servidor" },
      { status: err.response?.status || 500 }
    );
  }
}