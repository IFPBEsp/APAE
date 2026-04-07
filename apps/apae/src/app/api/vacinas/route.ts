import { AxiosError } from "axios";
import { NextResponse } from "next/server";

import { createBaseApi } from "@/lib/axios";
import { handleError } from "@/lib/handle-error";

export async function POST(request: Request) {
  try {
    const body = await request.json();
    const api = await createBaseApi();
    const { data } = await api.post("/vaccines", body);

    return NextResponse.json(data, { status: 201 });
  } catch (error) {
    if (error instanceof AxiosError && error.response?.status === 409) {
      return NextResponse.json(
        { message: "Vacina já existe" },
        { status: 200 },
      );
    }

    return handleError(error);
  }
}

export async function GET() {
  try {
    const api = await createBaseApi();
    const { data } = await api.get("/vaccines");

    return NextResponse.json(data);
  } catch (error) {
    console.error("Erro ao buscar vacinas:", error);

    return NextResponse.json([], { status: 200 });
  }
}
