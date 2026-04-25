import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

export const dynamic = "force-dynamic";

export async function PATCH(
  request: NextRequest,
  { params }: { params: Promise<{ id: string; documentId: string }> },
) {
  const { id: patientId, documentId } = await params;

  try {
    const incomingFormData = await request.formData();
    const file = incomingFormData.get("file");

    if (!file) {
      return NextResponse.json(
        { message: "Arquivo obrigatório" },
        { status: 400 },
      );
    }

    const backendFormData = new FormData();
    backendFormData.append("file", file);

    const api = await createBaseApi();
    const response = await api.patch(
      `/patients/${patientId}/documents/${documentId}`,
      backendFormData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      },
    );

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        {
          message:
            error.response?.data?.message || "Erro ao substituir documento",
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
