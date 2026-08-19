import { createBaseApi } from "@/lib/axios";
import { NextResponse } from "next/server";

type Params = {
  params: Promise<{ id: string }> | { id: string };
};

export async function GET(request: Request, { params }: Params) {
    try {
        const { id } = await params;
        const api = await createBaseApi();
        const { data } = await api.get(`/vaccines/${id}`);
        return NextResponse.json(data);
    } catch (error) {
        const message = error instanceof Error ? error.message : "Erro desconhecido";
        console.error("Erro ao buscar vacina por id:", message);
        return NextResponse.json(
            { message: "Erro ao buscar detalhes da vacina." },
            { status: 500 }
        );
    }
}

export async function PUT(request: Request, { params }: Params) {
    try {
        const { id } = await params;
        const body = await request.json();
        const api = await createBaseApi();
        const { data } = await api.put(`/vaccines/${id}`, body);
        return NextResponse.json(data);
    } catch (error) {
        const message = error instanceof Error ? error.message : "Erro desconhecido";
        console.error("Erro ao atualizar vacina:", message);
        return NextResponse.json(
            { message: "Erro ao atualizar vacina." },
            { status: 500 }
        );
    }
}

export async function DELETE(request: Request, { params }: Params) {
    try {
        const { id } = await params;
        const api = await createBaseApi();
        await api.delete(`/vaccines/${id}`);
        return new NextResponse(null, { status: 204 });
    } catch (error) {
        const message = error instanceof Error ? error.message : "Erro desconhecido";
        console.error("Erro ao excluir vacina:", message);
        return NextResponse.json(
            { message: "Erro ao excluir vacina." },
            { status: 500 }
        );
    }
}