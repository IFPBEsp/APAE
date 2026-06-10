import { createBaseApi } from "@/lib/axios";
import { NextResponse } from "next/server";
import { AxiosError } from "axios";

export async function GET(request: Request) {
  try {
    const { searchParams } = new URL(request.url);
    const search = searchParams.get("search") || "";
    const page = parseInt(searchParams.get("page") || "1", 10);
    const limit = parseInt(searchParams.get("limit") || "10", 10);

    const api = await createBaseApi();
    const { data } = await api.get("/disorders");

    // Garantir que data é uma array
    if (!Array.isArray(data)) {
      throw new Error("Formato de resposta inválido do servidor.");
    }

    // Mapeia para o contrato solicitado (id e nome)
    const mapped = data.map((item: any) => ({
      id: item.id,
      nome: item.name || "",
    }));

    // Filtra pela busca (nome)
    const filtered = mapped.filter((item) =>
      item.nome.toLowerCase().includes(search.toLowerCase())
    );

    const total = filtered.length;
    const totalPages = Math.ceil(total / limit);

    // Paginação
    const startIndex = (page - 1) * limit;
    const paginatedData = filtered.slice(startIndex, startIndex + limit);

    return NextResponse.json({
      data: paginatedData,
      total,
      totalPages,
    });
  } catch (error) {
    if (error instanceof AxiosError) {
      return new NextResponse(
        JSON.stringify(error.response?.data || { message: error.message }),
        { status: error.response?.status || 500 }
      );
    }
    const message = error instanceof Error ? error.message : "Erro inesperado.";
    return new NextResponse(
      JSON.stringify({ message: `Erro ao buscar transtornos: ${message}` }),
      { status: 500 }
    );
  }
}
