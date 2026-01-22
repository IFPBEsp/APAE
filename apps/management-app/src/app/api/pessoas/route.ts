export const dynamic = "force-dynamic";

import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

export async function GET(request: NextRequest) {
  const searchParams = request.nextUrl.searchParams;

  const page = searchParams.get("page") || "0";
  const size = searchParams.get("size") || "20";
  const sort = searchParams.get("sort") || "fullName,asc";

  try {
    const api = await createBaseApi();

    const { data } = await api.get(`/patients`, {
      params: { page, size, sort },
    });

    if (data && data.content) {
      return NextResponse.json(data.content);
    }

    return NextResponse.json(data);
  } catch (error) {
    const err = error as AxiosError;
    console.error(`[API Route Error] /api/pessoas:`, err.message);

    return NextResponse.json(
      { message: err.response?.data || "Erro no servidor ao listar pacientes" },
      { status: err.response?.status || 500 },
    );
  }
}

export async function POST(req: Request) {
  try {
    const data = await req.formData();
    const api = await createBaseApi();

    const response = await api.postForm("/patients", data);

    if (response.status !== 201) {
      return NextResponse.json(
        { message: response.data?.message || "Erro ao cadastrar pessoa" },
        { status: response.status },
      );
    }

    return NextResponse.json(
      { message: "Cadastro bem-sucedido" },
      { status: 201 },
    );
  } catch (error) {
    console.error(error);

    console.log(error.response);

    if (error instanceof AxiosError && error.response) {
      return NextResponse.json(
        { message: error.response.data?.message || "Erro ao cadastrar" },
        { status: error.response.status },
      );
    }

    return NextResponse.json(
      {
        message: `Erro: ${
          error && (error as AxiosError).response?.data
            ? ((error as AxiosError).response?.data as { message?: string })
                .message
            : "Erro ao cadastrar pessoa"
        }`,
      },
      {
        status: (error as AxiosError).response?.status,
      },
    );
  }
}
