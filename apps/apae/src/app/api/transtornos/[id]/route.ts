import { createBaseApi } from "@/lib/axios";
import { updateTranstornoSchema } from "@/schemas/transtornosSchema";
import { NextResponse } from "next/server";

interface IParams {
  params: Promise<{ id: string }>;
}

export async function GET(request: Request, { params }: IParams) {
  try {
    const { id } = await params;
    const api = await createBaseApi();
    const { data } = await api.get(`/disorders/${id}`);
    return NextResponse.json(data);
  } catch (error: unknown) {
    const err = error as { response?: { data?: unknown }; message?: string };
    return new NextResponse(JSON.stringify(err.response?.data || { message: err.message }), { status: 500 });
  }
}

export async function PUT(request: Request, { params }: IParams) {
  try {
    const { id } = await params;
    const body = await request.json();
    const validation = updateTranstornoSchema.safeParse(body);
    if (!validation.success) return NextResponse.json({ errors: validation.error.flatten() }, { status: 400 });

    const api = await createBaseApi();
    const { data } = await api.put(`/disorders/${id}`, validation.data);
    return NextResponse.json(data);
  } catch (error: unknown) {
    const err = error as { response?: { data?: unknown }; message?: string };
    return new NextResponse(JSON.stringify(err.response?.data || { message: err.message }), { status: 500 });
  }
}

export async function DELETE(request: Request, { params }: IParams) {
  try {
    const { id } = await params;
    const api = await createBaseApi();
    const { data } = await api.delete(`/disorders/${id}`);
    return NextResponse.json(data);
  } catch (error: unknown) {
    const err = error as { response?: { data?: unknown; status?: number }; message?: string };
    const errorMessage = err.response?.data || "Erro ao excluir transtorno";
    const status = err.response?.status || 500;
    return new NextResponse(JSON.stringify(errorMessage), { status });
  }
}