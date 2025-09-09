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

    console.log(response.data.content.contatoResponse)

    const formattedData = response.data.content.map((p: any) => {
  // Pega o primeiro contato do array, ou null se o array for vazio/nulo
  const primeiroContato = p.contatoResponse && p.contatoResponse.length > 0
    ? p.contatoResponse[0]
    : null;

  return {
    id: p.id || '',
    nome: p.nomeCompleto || 'Nome não informado',
    cpf: p.cpf || 'Não informado',
    status: p.status || 'Ativo',
    urlFoto: p.urlFoto || '',
    contato: {
      telefone: primeiroContato?.cep || 'Não informado',
    },
    cidade: primeiroContato?.cidade || 'Não informada',
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