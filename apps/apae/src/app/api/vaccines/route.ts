import { createBaseApi } from "@/lib/axios";
import { NextResponse } from "next/server";
import { isAxiosError } from "axios";

export async function GET() {
    try {
        const api = await createBaseApi();
        const { data } = await api.get("/vaccines");
        return NextResponse.json(data);
    } catch (error) {
        if (isAxiosError(error) && error.response) {
            return NextResponse.json(
                error.response.data ?? { message: "Erro ao buscar a lista de vacinas." },
                { status: error.response.status }
            );
        }
        console.error("Erro ao buscar vacinas:", error);
        return NextResponse.json({ message: "Erro ao buscar a lista de vacinas." }, { status: 500 });
    }
}

export async function POST(request: Request) {
    try {
        const body = await request.json();
        const api = await createBaseApi();
        const { data } = await api.post("/vaccines", body);
        return Response.json(data, { status: 201 });
    } catch (error) {
        if (isAxiosError(error) && error.response) {
            return Response.json(
                error.response.data ?? { message: "Erro ao criar vacina." },
                { status: error.response.status }
            );
        }
        console.error("Erro ao criar vacina:", error);
        return Response.json({ message: "Erro ao criar vacina." }, { status: 500 });
    }
}