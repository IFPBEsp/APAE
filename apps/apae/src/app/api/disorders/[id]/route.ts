import { createBaseApi } from "@/lib/axios";
import { updateDisorderSchema } from "@/schemas/transtornosSchema";
import { NextResponse } from "next/server";
import { AxiosError } from "axios";

interface IParams {
  params: Promise<{ id: string }>;
}

export async function GET(request: Request, { params }: IParams) {
  try {
    const { id } = await params;
    const api = await createBaseApi();
    const { data } = await api.get(`/disorders/${id}`);
    return NextResponse.json(data);
  } catch (error) {
    if (error instanceof AxiosError) {
      return new NextResponse(JSON.stringify(error.response?.data || { message: error.message }), { status: error.response?.status || 500 });
    }
    return new NextResponse(JSON.stringify({ message: "Erro ao buscar dados." }), { status: 500 });
  }
}

export async function PUT(request: Request, { params }: IParams) {
  try {
    const { id } = await params;
    const body = await request.json();
    const validation = updateDisorderSchema.safeParse(body);
    if (!validation.success) return NextResponse.json({ errors: validation.error.flatten() }, { status: 400 });

    const api = await createBaseApi();
    const { data } = await api.put(`/disorders/${id}`, validation.data);
    return NextResponse.json(data);
  } catch (error) {
    if (error instanceof AxiosError) {
      return new NextResponse(JSON.stringify(error.response?.data || { message: error.message }), { status: error.response?.status || 500 });
    }
    return new NextResponse(JSON.stringify({ message: "Erro ao atualizar dados." }), { status: 500 });
  }
}

export async function DELETE(request: Request, { params }: IParams) {
  try {
    const { id } = await params;
    const api = await createBaseApi();
    const { data } = await api.delete(`/disorders/${id}`);
    return NextResponse.json(data);
  } catch (error) {
    if (error instanceof AxiosError) {
      return new NextResponse(JSON.stringify(error.response?.data || { message: error.message }), { status: error.response?.status || 500 });
    }
    return new NextResponse(JSON.stringify({ message: "Erro ao excluir dados." }), { status: 500 });
  }
}