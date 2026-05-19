import { NextRequest, NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";

export async function GET(
  req: NextRequest,
  context: { params: Promise<{ id: string }> },
) {
  try {
    const { id } = await context.params;
    const api = await createBaseApi();

    const response = await api.get(`/consultation-histories/${id}`);

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      if (error.response?.status === 404) {
        return NextResponse.json(
          { message: "Histórico de consulta não encontrado" },
          { status: 404 },
        );
      }

      return NextResponse.json(
        { message: "Erro ao buscar histórico de consulta" },
        { status: error.response?.status || 500 },
      );
    }

    return NextResponse.json(
      { message: "Erro interno do servidor" },
      { status: 500 },
    );
  }
}

export async function DELETE(
  req: NextRequest,
  context: { params: Promise<{ id: string }> },
) {
  try {
    const { id } = await context.params;
    const api = await createBaseApi();

    await api.delete(`/consultation-histories/${id}`);

    return NextResponse.json(
      { message: "Histórico de consulta excluído com sucesso" },
      { status: 200 },
    );
  } catch (error) {
    if (error instanceof AxiosError) {
      if (error.response?.status === 404) {
        return NextResponse.json(
          { message: "Histórico de consulta não encontrado" },
          { status: 404 },
        );
      }

      return NextResponse.json(
        { message: "Erro ao excluir histórico de consulta" },
        { status: error.response?.status || 500 },
      );
    }

    return NextResponse.json(
      { message: "Erro interno do servidor" },
      { status: 500 },
    );
  }
}
