import { NextRequest, NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";

export async function PUT(req: NextRequest, context: { params: Promise<{ id: string }> }) {
  try {
    const api = await createBaseApi();
    const { id } = await context.params;
    const response = await api.put(`/professionals/${id}/activate`);

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        {
          message: error.response?.data?.message || "Erro ao ativar profissional",
        },
        { status: error.response?.status || 500 },
      );
    }

    return NextResponse.json({ message: "Erro interno do servidor" }, { status: 500 });
  }
}
