import { NextRequest, NextResponse } from "next/server";
import axios from "axios";

const BACKEND_URL = process.env.DOCUMENT_API_BASE_URL || "http://localhost:8081/api/documents";

export async function GET(
    req: NextRequest, { params }:
    { params: { patientId: string } }) {
        const token = req.headers.get("authorization")?.replace("Bearer ", "");

        if (!token) {
            return NextResponse.json({ error: "Token não fornecido" }, { status: 401 });
        }

        const { searchParams } = new URL(req.url);
        const year = searchParams.get("year");
        const category = searchParams.get("category");
        const type = searchParams.get("type");

        if (!year || !category) {
            return NextResponse.json({ error: "Os parâmetros 'year' e 'category' obrigatórios faltando" }, { status: 400 });
        }

        const endpoint = type
            ? `${BACKEND_URL}/${params.patientId}/type`
            : `${BACKEND_URL}/${params.patientId}`;

        try {
            const response = await axios.get(endpoint, {
            params: { year, category, ...(type && { type }) },
            headers: {
                Authorization: `Bearer ${token}`,
            },
            });
            
            return NextResponse.json(response.data);

        } catch (error: any) {
            console.error("Erro na API:", error?.response?.data || error);
            return NextResponse.json({ error: "Erro ao buscar documentos" }, { status: 500 });
        }
    }
