// src/app/api/documents/medicos/route.ts

import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id } = await params;

  try {
    const api = await createBaseApi();
    const { data } = await api.get(`/patients/${id}/documents/medicals`);
    return NextResponse.json(data);
  } catch (error) {
    const err = error as AxiosError;
    return NextResponse.json([], { status: err.response?.status || 500 });
  }
}