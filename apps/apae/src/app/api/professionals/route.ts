import { NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";

async function removeLegacyAddressFromPayload(body: FormData) {
  const professional = body.get("professional");
  if (!(professional instanceof Blob)) return;

  const text = await professional.text();
  const payload = JSON.parse(text) as Record<string, unknown>;
  const compatiblePayload = { ...payload };
  delete compatiblePayload.address;

  body.set(
    "professional",
    new Blob([JSON.stringify(compatiblePayload)], { type: "application/json" }),
  );
}

export async function GET(req: Request) {
  try {
    const { searchParams } = new URL(req.url);
    const ativo = searchParams.get("ativo");
    const query = ativo !== null ? `?ativo=${ativo}` : "";

    const api = await createBaseApi();
    const response = await api.get(`/professionals${query}`);

    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        {
          message:
            error.response?.data?.message || "Erro ao buscar profissionais",
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

export async function POST(req: Request) {
  try {
    const body = await req.formData();
    await removeLegacyAddressFromPayload(body);

    const api = await createBaseApi();
    const response = await api.post("/professionals", body, {
      headers: { "Content-Type": undefined },
    });

    return NextResponse.json(response.data, { status: 201 });
  } catch (error) {
    if (error instanceof AxiosError) {
      const data = error.response?.data;
      return NextResponse.json(
        {
          message:
            data?.message || "Erro ao criar profissional"
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
