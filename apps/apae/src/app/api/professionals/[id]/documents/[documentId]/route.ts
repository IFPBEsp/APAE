import { NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";

export async function DELETE(
  req: Request,
  context: { params: Promise<{ id: string; documentId: string }> },
) {
  try {
    const api = await createBaseApi();
    const { id, documentId } = await context.params;
    await api.delete(`/professionals/${id}/documents/${documentId}`);

    return NextResponse.json({ success: true }, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        {
          message: error.response?.data?.message || "Erro ao remover documento",
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
