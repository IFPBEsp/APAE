import { createBaseApi } from "@/lib/axios";

import { NextResponse } from "next/server";
import { isAxiosError } from "axios";

export async function GET() {
  try {
    const api = await createBaseApi();
    const { data } = await api.get("/vaccines");

    return NextResponse.json(data);
  } catch (error) {
    if (isAxiosError(error) && error.response) {
      return NextResponse.json(
        error.response.data ?? { message: "Erro ao buscar a lista de vacinas." },
        { status: error.response.status },
      );
    }

    const message =
      error instanceof Error ? error.message : "Erro desconhecido";

    console.error("Erro ao buscar vacinas:", message);

    return NextResponse.json(
      { message: "Erro ao buscar a lista de vacinas." },
      { status: 500 },
    );
  }
}

export async function POST(request: Request) {
  try {
    const body = await request.json();

    const api = await createBaseApi();
    const { data } = await api.post("/vaccines", body);

    return NextResponse.json(data);
  } catch (error) {
    if (isAxiosError(error) && error.response) {
      return NextResponse.json(
        error.response.data ?? { message: "Erro ao criar a vacina." },
        { status: error.response.status },
      );
    }

    const message =
      error instanceof Error ? error.message : "Erro desconhecido";

    console.error("Erro ao criar vacina:", message);

    return NextResponse.json(
      { message: "Erro ao criar a vacina." },
      { status: 500 },
    );
  }
}