import { createBaseApi } from "@/lib/axios";

import { NextResponse } from "next/server";
import { isAxiosError } from "axios";

type Params = {
  params: Promise<{
    id: string;
  }>;
};

export async function GET(
  request: Request,
  { params }: Params,
) {
  try {
    const { id } = await params;
    const api = await createBaseApi();
    const { data } = await api.get(`/vaccines/${id}`);

    return NextResponse.json(data);
  } catch (error) {
    if (isAxiosError(error) && error.response) {
      return NextResponse.json(
        error.response.data ?? { message: "Erro ao buscar a vacina." },
        { status: error.response.status },
      );
    }

    const message =
      error instanceof Error ? error.message : "Erro desconhecido";

    console.error("Erro ao buscar vacina:", message);

    return NextResponse.json(
      { message: "Erro ao buscar a vacina." },
      { status: 500 },
    );
  }
}

export async function PUT(
  request: Request,
  { params }: Params,
) {
  try {
    const { id } = await params;
    const body = await request.json();

    const api = await createBaseApi();
    const { data } = await api.put(`/vaccines/${id}`, body);

    return NextResponse.json(data);
  } catch (error) {
    if (isAxiosError(error) && error.response) {
      return NextResponse.json(
        error.response.data ?? { message: "Erro ao atualizar a vacina." },
        { status: error.response.status },
      );
    }

    const message =
      error instanceof Error ? error.message : "Erro desconhecido";

    console.error("Erro ao atualizar vacina:", message);

    return NextResponse.json(
      { message: "Erro ao atualizar a vacina." },
      { status: 500 },
    );
  }
}

export async function DELETE(
  request: Request,
  { params }: Params,
) {
  try {
    const { id } = await params;
    const api = await createBaseApi();
    const { data } = await api.delete(`/vaccines/${id}`);

    return NextResponse.json(data);
  } catch (error) {
    if (isAxiosError(error) && error.response) {
      return NextResponse.json(
        error.response.data ?? { message: "Erro ao excluir a vacina." },
        { status: error.response.status },
      );
    }

    const message =
      error instanceof Error ? error.message : "Erro desconhecido";

    console.error("Erro ao excluir vacina:", message);

    return NextResponse.json(
      { message: "Erro ao excluir a vacina." },
      { status: 500 },
    );
  }
}