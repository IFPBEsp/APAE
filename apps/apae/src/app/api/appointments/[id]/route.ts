import { NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";

export async function GET(
  req: Request,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params;

    const api = await createBaseApi();
    const response = await api.get(`/appointments/${id}`);

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      if (error.response?.status === 404) {
        return NextResponse.json(
          { message: "Agendamento não encontrado" },
          { status: 404 }
        );
      }
      return NextResponse.json(
        { message: "Erro ao buscar agendamento" },
        { status: error.response?.status || 500 }
      );
    }
    return NextResponse.json(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  }
}

export async function PUT(
  req: Request,
  { params }: { params: { id: string } }
) {
  try {
    const { id } = params;
    const body = await req.json();

    const api = await createBaseApi();
    const response = await api.put(`/appointments/${id}`, body);

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        {
          message:
            error.response?.data?.message ||
            "Erro ao atualizar agendamento",
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

export async function DELETE(
  req: Request,
  { params }: { params: { id: string } }
) {
  try {
    const { id } = params;
    const api = await createBaseApi();
    await api.delete(`/appointments/${id}`);

    return NextResponse.json(
      { message: "Agendamento excluído com sucesso" },
      { status: 200 }
    );
  } catch (error) {
    if (error instanceof AxiosError) {
      if (error.response?.status === 404) {
        return NextResponse.json(
          { message: "Agendamento não encontrado" },
          { status: 404 }
        );
      }
      return NextResponse.json(
        { message: "Erro ao excluir agendamento" },
        { status: error.response?.status || 500 }
      );
    }
    return NextResponse.json(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  }
}