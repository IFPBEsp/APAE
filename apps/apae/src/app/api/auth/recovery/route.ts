import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextResponse } from "next/server";

export async function POST(req: Request) {
  try {
    const { email } = await req.json();

    if (!email) {
      return NextResponse.json(
        { message: "E-mail obrigatório" },
        { status: 400 }
      );
    }

    const api = await createBaseApi();

    await api.post("/auth/password-recovery/request", {
      email,
    });

    return NextResponse.json(
      {
        message:
          "Se o e-mail existir, um link de recuperação foi enviado.",
      },
      { status: 200 }
    );
  } catch (error) {
    if (error instanceof AxiosError) {
      return NextResponse.json(
        {
          message:
            error.response?.data?.message ||
            "Erro ao solicitar recuperação",
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