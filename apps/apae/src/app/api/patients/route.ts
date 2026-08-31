export const dynamic = "force-dynamic";

import { NextResponse, NextRequest } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";

export async function GET(request: NextRequest) {
  try {
    const api = await createBaseApi();

    const { searchParams } = new URL(request.url);
    const paramsObj = Object.fromEntries(searchParams.entries());

    const page = searchParams.get("page") || "0";
    const size = searchParams.get("size") || "20";
    const sort = searchParams.get("sort") || "fullName,asc";

    const { data } = await api.get(`/patients`, {
      params: { ...paramsObj, page, size, sort },
    });

    return NextResponse.json(data);
  } catch (err) {
    const error = err as AxiosError;
    console.error("[ERRO API PATIENTS]:", error.response?.data || error.message);

    return NextResponse.json(
      { message: error.response?.data || "Erro interno no servidor" },
      { status: error.response?.status || 500 },
    );
  }
}

export async function POST(req: Request) {
  try {
    const data = await req.formData();
    const api = await createBaseApi();

    const response = await api.postForm("/patients", data);

    if (response.status !== 201 && response.status !== 200) {
      return NextResponse.json(response.data, { status: response.status });
    }

    return NextResponse.json({ message: "Cadastro bem-sucedido" }, { status: 201 });
  } catch (error) {
    console.error("[API Route POST Error]:", error);

    if (error instanceof AxiosError && error.response) {
      return NextResponse.json(error.response.data, {
        status: error.response.status,
      });
    }

    return NextResponse.json({ message: "Erro inesperado ao cadastrar pessoa" }, { status: 500 });
  }
}
