import { NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";

export async function GET(req: Request) {
  try {
    const { searchParams } = new URL(req.url);
    const ativo = searchParams.get("ativo");
    const query = ativo !== null ? `?ativo=${ativo}` : "";

    const api = await createBaseApi();
    const response = await api.get(`/professionals${query}`);

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        {
          message:
            error.response?.data?.message || "Erro ao buscar profissionais",
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

export async function POST(req: Request) {
  try {
    const body = await req.formData();

    const api = await createBaseApi();
    const response = await api.post("/professionals", body, {
      headers: { "Content-Type": undefined },
    });

    return NextResponse.json(response.data, { status: 201 });
  } catch (error) {
    if (error instanceof AxiosError) {
      const data = error.response?.data;
      return NextResponse.json(
        {
          message:
            data?.message || "Erro ao criar profissional"
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
