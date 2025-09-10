import { NextRequest, NextResponse } from "next/server";
import axios from "axios";
import { createDocumentsAPI } from "@/lib/axios";

// const BACKEND_URL = process.env.DOCUMENT_API_BASE_URL || "http://localhost:8081/api/documents";

export async function GET(
    req: NextRequest, { params }:
        { params: { patientId: string } }) {
    const { searchParams } = new URL(req.url);
    const year = searchParams.get("year");
    const category = searchParams.get("category");
    const type = searchParams.get("type");

    if (!year || !category) {
        return NextResponse.json({ error: "Os parâmetros 'year' e 'category' são obrigatórios e estão faltando" }, { status: 400 });
    }

    try {
        const api = await createDocumentsAPI()
        const response = await api.get(`/paciente/${params.patientId}`,{
            params: { year, category, ...(type && { type }) },
        });

        return NextResponse.json(response.data);

    } catch (error: any) {
        console.error("Erro na API:", error?.response?.data || error);
        return NextResponse.json({ error: "Erro ao buscar documentos" }, { status: 500 });
    }
}
