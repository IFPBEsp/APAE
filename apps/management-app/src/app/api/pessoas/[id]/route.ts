export const dynamic = "force-dynamic";

import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }, // Await params (Next 15)
) {
  const { id } = await params;

  try {
    const api = await createBaseApi();
    const response = await api.get(`/patients/${id}`);

    return NextResponse.json(response.data);
  } catch (error) {
    const err = error as AxiosError;
    console.error(`[ERRO API PESSOA/${id}]:`, err.message);

    return NextResponse.json(
      { message: err.response?.data || "Erro ao buscar paciente" },
      { status: err.response?.status || 500 },
    );
  }
}

export async function PUT(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }, // Await params (Next 15)
) {
  const { id } = await params;

  try {
    const data = await request.json();
    const api = await createBaseApi();
    const response = await api.post(`/patients/${id}/`, data);

    return NextResponse.json(response.data);
  } catch (error) {
    const err = error as AxiosError;
    console.error(`[ERRO API PESSOA/${id}]:`, err.message);

    return NextResponse.json(
      { message: err.response?.data || "Erro ao buscar paciente" },
      { status: err.response?.status || 500 },
    );
  }
}
