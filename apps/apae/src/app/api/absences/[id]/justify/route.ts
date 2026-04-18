import { NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";

export async function PATCH(
  req: Request,
  { params }: { params: { id: string } }
) {
  try {
    const body = await req.json();
    
    const api = await createBaseApi();
    const response = await api.patch(`/absences/${params.id}/justify`, body);

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        { message: error.response?.data?.message || "Erro ao justificar falta" },
        { status: error.response?.status || 500 }
      );
    }
    return NextResponse.json({ message: "Erro interno do servidor" }, { status: 500 });
  }
}