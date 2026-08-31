import { createBaseApi } from "@/lib/axios";
import { NextRequest, NextResponse } from "next/server";
import { AxiosError } from "axios";

export async function GET(req: NextRequest, context: { params: Promise<{ id: string }> }) {
  try {
    const api = await createBaseApi();
    const { id } = await context.params;
    const response = await api.get(`/professionals/${id}/documents`);

    return NextResponse.json(response.data);
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        {
          message: error.response?.data?.message || "Erro ao buscar documentos",
        },
        { status: error.response?.status || 500 },
      );
    }

    return NextResponse.json({ message: "Erro interno do servidor" }, { status: 500 });
  }
}

export async function PATCH(req: NextRequest, context: { params: Promise<{ id: string }> }) {
  try {
    const api = await createBaseApi();
    const formData = await req.formData();
    const { id } = await context.params;

    const response = await api.patch(`/professionals/${id}/documents`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        {
          message: error.response?.data?.message || "Erro ao enviar documentos",
        },
        { status: error.response?.status || 500 },
      );
    }

    return NextResponse.json({ message: "Erro interno do servidor" }, { status: 500 });
  }
}
