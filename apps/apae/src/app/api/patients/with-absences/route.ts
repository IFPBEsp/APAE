export const dynamic = "force-dynamic";

import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

export async function GET(request: NextRequest) {
  try {
    const api = await createBaseApi();
    const { searchParams } = new URL(request.url);
    const paramsObj = Object.fromEntries(searchParams.entries());

    const response = await api.get("/patients/with-absences", {
      params: paramsObj,
    });

    return NextResponse.json(response.data);
  } catch (err) {
    const error = err as AxiosError;
    console.error(
      "[ERRO API PATIENT WITH ABSENCES]:",
      error.response?.data || error.message,
    );

    return NextResponse.json(
      { message: error.response?.data || "Erro interno no servidor" },
      { status: error.response?.status || 500 },
    );
  }
}
