import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextResponse } from "next/server";

export async function POST(req: Request) {
  try {
    const body = await req.json();

    const api = await createBaseApi();

    await api.post("/auth/password-recovery/reset", body);

    return NextResponse.json(
      { message: "Senha redefinida com sucesso." },
      { status: 200 }
    );
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        {
          message:
            error.response?.data?.message ||
            "Erro ao redefinir senha",
        },
        { status: error.response?.status || 500 }
      );
    }

    return NextResponse.json(
      { message: "Erro interno" },
      { status: 500 }
    );
  }
}