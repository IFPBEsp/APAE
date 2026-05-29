import { NextRequest, NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";

function removeLegacyAddressFromPayload(payload: Record<string, unknown>) {
  const compatiblePayload = { ...payload };
  delete compatiblePayload.address;
  return compatiblePayload;
}

export async function GET(
  req: NextRequest,
  context: { params: Promise<{ id: string }> },
) {
  try {
    const { id } = await context.params;

    const api = await createBaseApi();
    const response = await api.get(`/professionals/${id}`);

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      if (error.response?.status === 404) {
        return NextResponse.json(
          { message: "Profissional não encontrado" },
          { status: 404 },
        );
      }

      return NextResponse.json(
        {
          message:
            error.response?.data?.message || "Erro ao buscar profissional",
        },
        { status: error.response?.status || 500 },
      );
    }

    return NextResponse.json(
      { message: "Erro interno do servidor" },
      { status: 500 },
    );
  }
}

export async function PUT(
  req: NextRequest,
  context: { params: Promise<{ id: string }> },
) {
  try {
    const { id } = await context.params;

    const body = removeLegacyAddressFromPayload(await req.json());

    const api = await createBaseApi();
    const response = await api.put(`/professionals/${id}`, body);

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        {
          message:
            error.response?.data?.message || "Erro ao atualizar profissional",
        },
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
    await api.delete(`/professionals/${id}`);

    return NextResponse.json(
      { message: "Profissional excluído com sucesso" },
      { status: 200 },
    );
  } catch (error) {
    if (error instanceof AxiosError) {
      if (error.response?.status === 404) {
        return NextResponse.json(
          { message: "Profissional não encontrado" },
          { status: 404 },
        );
      }

      return NextResponse.json(
        {
          message:
            error.response?.data?.message || "Erro ao excluir profissional",
        },
        { status: error.response?.status || 500 },
      );
    }

    return NextResponse.json(
      { message: "Erro interno do servidor" },
      { status: 500 },
    );
  }
}
