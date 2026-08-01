import { createBaseApi } from "@/lib/axios";
import { NextResponse } from "next/server";

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
