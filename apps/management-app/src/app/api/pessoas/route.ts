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

    const formattedData = response.data.content.map((p: any) => ({
      id: p.id || '',
      nome: p.nomeCompleto || 'Nome não informado',
      cpf: p.cpf || 'Não informado',
      status: p.status || 'Ativo',
      urlFoto: p.urlFoto || '',
      contato: {
        telefone: p.contatoResponse?.telefone || 'Não informado',
      },
      cidade: p.enderecoResponse?.cidade || 'Não informada',
    }));

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