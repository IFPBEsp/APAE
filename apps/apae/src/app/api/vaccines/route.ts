import { createBaseApi } from "@/lib/axios";
import { NextResponse } from "next/server";
import { AxiosError } from "axios";

export async function GET() {
    try {
        const api = await createBaseApi();
        const { data } = await api.get("/vaccines");
        return NextResponse.json(data);
    } catch {
        return NextResponse.json(
            { message: "Erro ao buscar a lista de vacinas." },
            { status: 500 },
        );
    }
}

export async function POST(request: Request) {
    try {
        const body = await request.json();
        const api = await createBaseApi();
        const { data } = await api.post("/vaccines", body);
        return NextResponse.json(data, { status: 201 });
    } catch (error) {
        if (error instanceof AxiosError) {
            return NextResponse.json(
                error.response?.data || { message: error.message },
                { status: error.response?.status || 500 },
            );
        }
        return NextResponse.json(
            { message: "Erro inesperado ao criar vacina." },
            { status: 500 },
        );
    }
}