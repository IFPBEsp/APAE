// app/api/consultation-histories/route.ts
import { NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";

export async function POST(req: Request) {
  try {
    const body = await req.json();
    const api = await createBaseApi();

    const response = await api.post("/consultation-histories", body);

    return NextResponse.json(response.data, { status: 201 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        {
          message:
            error.response?.data?.message ||
            "Erro ao registrar histórico de consulta",
        },
        { status: error.response?.status || 500 }
      );
    }

    return NextResponse.json(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  }
}

export async function GET(req: Request) {
  try {
    const { searchParams } = new URL(req.url);
    const agendamentoId = searchParams.get("agendamentoId");
    const date = searchParams.get("date");

    const api = await createBaseApi();

    const url = "/consultation-histories";
    const params: Record<string, string> = {};

    if (agendamentoId) params.agendamentoId = agendamentoId;
    if (date) params.date = date;

    const response = await api.get(url, { params });

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      if (error.response?.status === 404) {
        return NextResponse.json({ content: [] }, { status: 200 });
      }

      return NextResponse.json(
        { message: "Erro ao buscar histórico de consultas" },
        { status: error.response?.status || 500 }
      );
    }

    return NextResponse.json(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  }
}