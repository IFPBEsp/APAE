import axios from "axios";
import { NextRequest, NextResponse } from "next/server";

const BACKEND_URL = "http://localhost:8086/pessoas";

export async function GET(
    req: NextRequest,
    { params }: { params: { id: string } }
) {
    try {
        const response = await axios.get(`${BACKEND_URL}/${params.id}`);
        return NextResponse.json(response.data);
    } catch (error: any) {
        console.error("Erro ao buscar pessoa: ", error?.response?.data || error);
        return NextResponse.json({ error: "Erro ao buscar pessoa" }, { status: 500 })
    }
}