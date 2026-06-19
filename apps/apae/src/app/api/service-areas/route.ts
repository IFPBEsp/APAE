import { NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";

export async function GET() {
  try {
    const api = await createBaseApi();
    const response = await api.get("/service-types");

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        {
          message:
            error.response?.data?.message || "Erro ao buscar tipos de atendimento",
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
    const body = await req.json();

    const api = await createBaseApi();
    const response = await api.post("/service-types", body);

    return NextResponse.json(response.data, { status: 201 });
  } catch (error) {
    if (error instanceof AxiosError) {
      const data = error.response?.data;
      return NextResponse.json(
        {
          message:
            data?.message || "Erro ao criar tipo de atendimento"
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
