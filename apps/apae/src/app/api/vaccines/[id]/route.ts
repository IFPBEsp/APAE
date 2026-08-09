import { createBaseApi } from "@/lib/axios";

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
        return Response.json({ message: "Erro ao excluir vacina." }, { status: 500 });
    }
}