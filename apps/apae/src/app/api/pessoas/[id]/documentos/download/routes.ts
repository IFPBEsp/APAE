import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id: patientId } = await params;
  const documentName = request.nextUrl.searchParams.get("name");

  if (!patientId || !documentName) {
    return NextResponse.json({ message: "Parâmetros inválidos" }, { status: 400 });
  }

  try {
    const api = await createBaseApi();
    const response = await api.get(
      `/patients/${patientId}/documents/download`,
      {
        params: { documentName },
        responseType: "stream", 
      }
    );

    const contentDisposition = response.headers["content-disposition"];
    let filename = "download.bin";
    if (contentDisposition) {
      const match = /filename="([^"]+)"/.exec(contentDisposition);
      if (match && match[1]) filename = match[1];
    }

    const headers = new Headers();
    headers.set("Content-Type", "application/octet-stream");
    headers.set("Content-Disposition", `attachment; filename="${filename}"`);

    return new NextResponse(response.data, { headers });

  } catch (error) {
    const err = error as AxiosError;
    return NextResponse.json(
      { message: err.response?.data || "Erro ao baixar documento" },
      { status: err.response?.status || 500 }
    );
  }
}