import { createBaseApi } from "@/lib/axios";
import { NextResponse } from "next/server";
import { AxiosError } from "axios";

export async function GET() {
    try {
        const api = await createBaseApi();
        const { data } = await api.get("/vaccines");
        return NextResponse.json(data);
    } catch (error) {
        const message = error instanceof Error ? error.message : "Erro desconhecido";
        console.error("Erro ao buscar vacinas:", message);
        return NextResponse.json({ message: "Erro ao buscar a lista de vacinas." }, { status: 500 }
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
            return new NextResponse(
                JSON.stringify(
                    error.response?.data || { message: error.message }
                ),
                { status: error.response?.status || 500 }
            );
        }

        return NextResponse.json(
            { message: "Erro ao criar vacina." },
            { status: 500 }
        );
    }
}