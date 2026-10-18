import { createBaseApi } from "@/lib/axios";
import { NextRequest, NextResponse } from "next/server";
import { AxiosError } from "axios";

export async function GET(
  request: NextRequest,
  context: { params: Promise<{ id: string }> },
) {
  const { id } = await context.params;

  try {
    const api = await createBaseApi();

    const response = await api.get(`/appointments/today/${id}`);
    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        {
          message: error.response?.data?.message || "Erro ao buscar agendamento",
        },
        { status: error.response?.status || 500 }
      );
    }

    console.error("Error proxying to backend:", error);
    return NextResponse.json(
      { error: "Internal server error" },
      { status: 500 },
    );
  }
}
