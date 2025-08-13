import { NextResponse } from "next/server";
import { AxiosError } from "axios";
import { createPersonApi } from "@/lib/axios";

export async function GET() {
  try {
    const api = await createPersonApi();

    const response = await api.get("", {
      params: { size: 10, page: 0 }
    });

    if (response.status !== 200) {
      return NextResponse.json(
        response.data.content.map((p: any) => ({
          id: p.id,
          nome: p.nomeCompleto,
          cpf: p.cpf,
          dataNascimento: p.dataNascimento
        })),
        { status: 200 }
      );
    }

    // route.ts - Modifique o mapeamento para garantir valores padrão
    return NextResponse.json(
      response.data.content.map((p: any) => ({
        id: p.id || '',
        nome: p.nomeCompleto || 'Nome não informado',
        cpf: p.cpf || 'Não informado',
        status: 'Ativo', // Defina um valor padrão ou mapeie do backend
        urlFoto: '',
        contato: {
          telefone: p.contatoResponse?.telefone || 'Não informado'
        },
        cidade: 'Não informada'
      })),
      { status: 200 }
    );

  } catch (error) {
    console.error("Erro no serviço da API:", error);

    if (error instanceof AxiosError && error.response) {
      return NextResponse.json(
        { message: error.response.data?.message || "Erro ao buscar pacientes" },
        { status: error.response.status }
      );
    }

    return NextResponse.json(
      { message: "Erro inesperado ao buscar pacientes" },
      { status: 500 }
    );
  }
}
