import { createBaseApi } from "@/lib/axios";
import { NextResponse } from "next/server";
import { AxiosError } from "axios";

export async function POST(request: Request) {
    try {
        const body = await request.json();
        const api = await createBaseApi();
        const { data } = await api.post("/vaccines", body);
        
        return NextResponse.json(data, { status: 201 });
    } catch (error: unknown) {
        const err = error as AxiosError;
        if (err.response?.status === 409) {
             return NextResponse.json({ message: "Vacina já existe" }, { status: 200 });
        }
        return new NextResponse(
            JSON.stringify(err.response?.data || { message: err.message }),
            { status: err.response?.status || 500 },
        );
    }
}

export async function GET() {
    try {
        const api = await createBaseApi();
        const { data } = await api.get("/vaccines");
        return NextResponse.json(data);
    } catch (error: unknown) {
        const err = error as { message?: string };
        console.error("Erro ao buscar vacinas:", err.message);
        return NextResponse.json([], { status: 200 });
    }
}