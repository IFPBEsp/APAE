import { NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";

export async function POST(req: Request) {
  try {
    const { email, nomeCompleto, cpf, senha } = await req.json();

    if (!email || !nomeCompleto || !cpf || !senha) {
      return NextResponse.json(
        { message: "Todos os campos são obrigatórios" },
        { status: 406 },
      );
    }

    const api = await createBaseApi();
    const response = await api.post("/auth/signup", {
      fullName: nomeCompleto,
      password: senha,
      email,
      cpf,
    });

    if (response.status !== 201) {
      return NextResponse.json(
        { message: response.data?.message || "Erro ao cadastrar usuário" },
        { status: response.status },
      );
    }

    return NextResponse.json(
      { message: "Cadastro bem-sucedido" },
      { status: 201 },
    );
  } catch (error) {
    console.error(error);

    if (error instanceof AxiosError && error.response) {
      return NextResponse.json(
        { message: error.response.data?.message || "Erro ao cadastrar" },
        { status: error.response.status },
      );
    }

    return NextResponse.json(
      {
        message: `Erro: ${
          error && (error as AxiosError).response?.data
            ? ((error as AxiosError).response?.data as { message?: string })
                .message
            : "Erro ao cadastrar usuário"
        }`,
      },
      {
        status: (error as AxiosError).response?.status,
      },
    );
  }
}
