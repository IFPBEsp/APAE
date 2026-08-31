export const dynamic = "force-dynamic";

import { NextResponse, NextRequest } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";

export async function GET(request: NextRequest) {
  try {
    const api = await createBaseApi();

    const { searchParams } = new URL(request.url);
    const minAbsences = searchParams.get("minAbsences");

    const response = await api.get("/dashboard/overview", {
      params: {
        minAbsences,
      },
    });

    return NextResponse.json(response.data);
  } catch (err) {
    const error = err as AxiosError;

    console.error("[ERRO API DASHBOARD OVERVIEW]:", error.response?.data || error.message);

    return NextResponse.json(
      { message: error.response?.data || "Erro interno no servidor" },
      { status: error.response?.status || 500 },
    );
  }
}
