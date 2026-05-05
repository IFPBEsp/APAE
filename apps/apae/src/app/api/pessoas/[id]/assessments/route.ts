import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextResponse } from "next/server";


export async function GET(request: Request, { params }: { params: Promise<{ id: string }> }) {
    try {
        const resolvedParams = await params;
        const id = resolvedParams.id;

        if (!id || id === "undefined") {
            return NextResponse.json({ message: "ID inválido" }, { status: 400 });
        }

        const api = await createBaseApi();
        const { data } = await api.get(`/patients/${id}/assessments`);
        return NextResponse.json(data);
    } catch (error) {
        console.error("Erro na rota API Assessments:", error);
        if (error instanceof AxiosError) {
            return new NextResponse(JSON.stringify(error.response?.data || { message: error.message }), { status: error.response?.status || 500 });
        }
    }

    return NextResponse.json(
        { message: "Erro ao buscar avaliações." },
        { status: 500 }
    );
}