import { NextResponse } from "next/server";
import { createAuthAPI } from "@/lib/axios";
import { AxiosError } from "axios";
import { setSessionCookie } from "@/lib/cookies";

export async function login(req: Request) {
  const { email, password } = await req.json();

  if (!email || !password) {
    return NextResponse.json(
      { message: "Todos os campos são obrigatórios" },
      { status: 406 }
    );
  }

  try {
    const api = await createAuthAPI();
    const response = await api.post("/signin", { email, password });

    if (!response.status || response.status !== 200) {
      return NextResponse.json(
        { message: response.data?.message || "Erro ao fazer login do usuário" },
        { status: response.status }
      );
    }

    const payload = {
      accessToken: response.data.access_token,
      expiresIn: 60 * 1000,
    };

    setSessionCookie(payload);

    return NextResponse.json(
      {
        message: "Login bem-sucedido",
      },
      { status: 200 }
    );
  } catch (error) {
    console.error("Erro ao fazer login: ", error);
    if (error instanceof Error && error.message.includes("401")) {
      return NextResponse.json(
        { message: "Credenciais inválidas" },
        { status: 401 }
      );
    }

    return NextResponse.json(
      {
        message: `Erro: ${
          error && (error as AxiosError).response?.data
            ? ((error as AxiosError).response?.data as { message?: string })
                .message
            : "Erro ao fazer o login do usuário"
        }`,
      },
      {
        status: (error as AxiosError).response?.status,
      }
    );
  }
}
