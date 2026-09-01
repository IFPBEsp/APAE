import { createBaseApi } from "@/lib/axios";
import { isAxiosError } from "axios";

type Params = {
    params: Promise<{
        id: string;
    }>;
};

export async function GET(request: Request, { params }: Params) {
    try {
        const { id } = await params;
        const api = await createBaseApi();
        const { data } = await api.get(`/vaccines/${id}`);
        return Response.json(data);
    } catch (error) {
        if (isAxiosError(error) && error.response) {
            return Response.json(
                error.response.data ?? { message: "Erro ao buscar vacina." },
                { status: error.response.status }
            );
        }
        console.error("Erro ao buscar vacina:", error);
        return Response.json({ message: "Erro ao buscar vacina." }, { status: 500 });
    }
}

export async function PUT(request: Request, { params }: Params) {
    try {
        const { id } = await params;
        const body = await request.json();
        const api = await createBaseApi();
        const { data } = await api.put(`/vaccines/${id}`, body);
        return Response.json(data);
    } catch (error) {
        if (isAxiosError(error) && error.response) {
            return Response.json(
                error.response.data ?? { message: "Erro ao atualizar vacina." },
                { status: error.response.status }
            );
        }
        console.error("Erro ao atualizar vacina:", error);
        return Response.json({ message: "Erro ao atualizar vacina." }, { status: 500 });
    }
}

export async function DELETE(request: Request, { params }: Params) {
    try {
        const { id } = await params;
        const api = await createBaseApi();
        await api.delete(`/vaccines/${id}`);
        return new Response(null, { status: 204 });
    } catch (error) {
        if (isAxiosError(error) && error.response) {
            return Response.json(
                error.response.data ?? { message: "Erro ao excluir vacina." },
                { status: error.response.status }
            );
        }
        console.error("Erro ao excluir vacina:", error);
        return Response.json({ message: "Erro ao excluir vacina." }, { status: 500 });
    }
}