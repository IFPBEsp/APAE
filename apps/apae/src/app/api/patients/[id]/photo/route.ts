export const dynamic = "force-dynamic";

import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

export async function PUT(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id } = await params;

  try {
    const formData = await request.formData();
    const api = await createBaseApi();
    const response = await api.putForm(`/patients/${id}/photo`, formData);

    return NextResponse.json(response.data);
  } catch (error) {
    const err = error as AxiosError;
    console.error(`[ERRO PUT FOTO PESSOA/${id}]:`, err.response?.data || err.message);

    return NextResponse.json(
      { message: err.response?.data || "Erro ao atualizar foto do paciente" },
      { status: err.response?.status || 500 }
    );
  }
}
