import { NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";

export async function GET(req: Request) {
  try {
    const authHeader = req.headers.get("Authorization");
    const { searchParams } = new URL(req.url);

    const api = await createBaseApi();
    const response = await api.get("/absences", {
      headers: authHeader ? { Authorization: authHeader } : {},
      params: Object.fromEntries(searchParams.entries()), // repassa page, size, etc.
    });

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        { message: error.response?.data?.message || "Erro ao buscar faltas" },
        { status: error.response?.status || 500 }
      );
    }
    return NextResponse.json(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  }
}

export async function POST(req: Request) {
  try {
    const authHeader = req.headers.get("Authorization");
    const body = await req.json();

    const api = await createBaseApi();
    const response = await api.post("/absences", body, {
      headers: authHeader ? { Authorization: authHeader } : {},
    });

    return NextResponse.json(response.data, { status: 201 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        { message: error.response?.data?.message || "Erro ao criar falta" },
        { status: error.response?.status || 500 }
      );
    }
    return NextResponse.json(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  }
}