import { NextResponse } from "next/server";
import { AxiosError } from "axios";
import { createPersonApi } from "@/lib/axios";

export async function GET() {
  try {
    const api = await createPersonApi();
    const response = await api.get("", {
      params: { size: 100, page: 0 },
    });

    if (!response.data || !Array.isArray(response.data.content)) {
      return NextResponse.json([], { status: 200 });
    }

    const formattedData = response.data.content.map((p: any) => {
      const primeiroContato =
        p.contatoResponse && p.contatoResponse.length > 0
          ? p.contatoResponse[0]
          : null;

      return {
        id: p.id || "",
        nome: p.nomeCompleto || "Nome não informado",
        cpf: p.cpf || "Não informado",
        status: p.status || "Ativo",
        urlFoto: p.urlFoto || "",
        contato: {
          telefone: primeiroContato?.telefone || "Não informado",
        },
        cidade: primeiroContato?.cidade || "Não informada",
      };
    });

    return NextResponse.json(formattedData, { status: 200 });
  } catch (error) {
    console.error("Erro na API Route (/api/pessoas):", error);

    if (error instanceof AxiosError && error.response) {
      return NextResponse.json(
        { message: error.response.data?.message || "Erro ao buscar pessoas" },
        { status: error.response.status }
      );
    }

    return NextResponse.json(
      { message: "Erro inesperado no servidor" },
      { status: 500 }
    );
  }
}

export async function POST(req: Request) {
  try {
    const data = await req.json();

    const api = await createPersonApi();
    const response = await api.post("", data);

    if (response.status !== 201) {
      return NextResponse.json(
        { message: response.data?.message || "Erro ao cadastrar pessoa" },
        { status: response.status }
      );
    }

    return NextResponse.json(
      { message: "Cadastro bem-sucedido" },
      { status: 201 }
    );
  } catch (error) {
    console.error(error);

    if (error instanceof AxiosError && error.response) {
      return NextResponse.json(
        { message: error.response.data?.message || "Erro ao cadastrar" },
        { status: error.response.status }
      );
    }

    return NextResponse.json(
      {
        message: `Erro: ${
          error && (error as AxiosError).response?.data
            ? ((error as AxiosError).response?.data as { message?: string })
                .message
            : "Erro ao cadastrar pessoa"
        }`,
      },
      {
        status: (error as AxiosError).response?.status,
      }
    );
  }
}
