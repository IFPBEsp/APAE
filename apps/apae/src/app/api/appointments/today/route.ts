import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextResponse } from "next/server";

export async function GET(req: Request) {
  try {
    const { searchParams } = new URL(req.url);
    const date = searchParams.get("date");
    const page = searchParams.get("page") || "0";
    const size = searchParams.get("size") || "100";

    const api = await createBaseApi();
    
    const response = await api.get("/appointments/today", {
      params: { date, page, size },
    });

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        { message: error.response?.data?.message || "Erro ao buscar agendamentos de hoje" },
        { status: error.response?.status || 500 }
      );
    }
    return NextResponse.json({ message: "Erro interno do servidor" }, { status: 500 });
  }
}