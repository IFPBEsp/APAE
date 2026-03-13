import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextResponse } from "next/server";

export async function PATCH(
  request: Request,
  { params }: { params: { id: string } }
) {
  try {
    const { id } = await params;

    const api = await createBaseApi();
    const response = await api.patch(`/appointments/${id}/rule`, {
      method: "PATCH",
      body: await request.text(),
    });

    return NextResponse.json(response.data, { status: 200 });
  } catch (error: any) {
      if (error instanceof AxiosError) {
        if (error.response?.status === 404) {
          return NextResponse.json(
            { message: "Agendamento não encontrado" },
            { status: 404 }
          );
        }
        return NextResponse.json(
          { message: "Erro ao editar agendamento" },
          { status: error.response?.status || 500 }
        );
      }
      return NextResponse.json(
        { message: "Erro interno do servidor" },
        { status: 500 }
      );
  }
}