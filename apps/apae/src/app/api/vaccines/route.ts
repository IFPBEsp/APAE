import { createBaseApi } from "@/lib/axios";
import { NextRequest, NextResponse } from "next/server";
import { AxiosError } from "axios";

export async function POST(request: Request) {
    try {
        const body = await request.json();
        const api = await createBaseApi();
        const { data } = await api.post("/vaccines", body);
        
        return NextResponse.json(data, { status: 201 });
    } catch (error) {
        if(error instanceof AxiosError) {
            if (error.response?.status === 409) {
             return new NextResponse(JSON.stringify({ message: "Vacina já existe" }), { status: 200 });
            }
            return new NextResponse(
                JSON.stringify(error.response?.data || { message: error.message }), { status: error.response?.status || 500 }
            );
        }
        
        return new NextResponse(JSON.stringify({ message: "Erro inesperado ao criar vacina" }), { status: 500 });
    }
}

export async function GET(request: NextRequest) {
    try {
        const api = await createBaseApi();
        const { searchParams } = new URL(request.url);
        const params: Record<string, string> = {};

        const name = searchParams.get("name");
        const page = searchParams.get("page");
        const size = searchParams.get("size");

        if (name !== null) params.name = name;
        if (page !== null) params.page = page;
        if (size !== null) params.size = size;

        const { data } = await api.get("/vaccines", { params });
        return NextResponse.json(data);
    } catch (error) {
        if (error instanceof AxiosError) {
            return NextResponse.json(
                {
                    message: error.response?.data?.message || "Erro ao buscar vacinas",
                },
                { status: error.response?.status || 500 }
            );
        }
        const message = error instanceof Error ? error.message : "Erro desconhecido";
        console.error("Erro ao buscar vacinas:", message);
        return NextResponse.json({ message: "Erro ao buscar a lista de vacinas." }, { status: 500 }
        );
    }
}