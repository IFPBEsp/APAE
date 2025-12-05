// src/app/api/pessoas/[id]/registro-anual/[year]/route.ts

export const dynamic = 'force-dynamic';
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextResponse } from "next/server";

export async function GET(
  request: Request,
  { params }: { params: Promise<{ id: string, year: string }> }
) {
  const { id: patientId, year } = await params;
  try {
    const api = await createBaseApi();
    const { data } = await api.get(`/patients/${patientId}/annual-registry/${year}`);
    return NextResponse.json(data);
  } catch (error) {
    const err = error as AxiosError;
    if (err.response?.status === 404) {
      return NextResponse.json({ message: "Registro não encontrado" }, { status: 404 });
    }
    return NextResponse.json({ message: "Erro no servidor" }, { status: 500 });
  }
}