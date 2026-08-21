import { createBaseApi } from "@/lib/axios";
import { NextResponse } from "next/server";
import { AxiosError } from "axios";

interface IParams {
  params: Promise<{ id: string }>;
}

export async function GET(request: Request, { params }: IParams) {
  try {
    const { id } = await params;

    const api = await createBaseApi();

    const { data } = await api.get(`/vaccines/${id}`);

    return NextResponse.json(data);
  } catch (error) {
    if (error instanceof AxiosError) {
      return new NextResponse(
        JSON.stringify(
          error.response?.data || { message: error.message }
        ),
        { status: error.response?.status || 500 }
      );
    }

    return new NextResponse(
      JSON.stringify({ message: "Erro ao buscar vacina." }),
      { status: 500 }
    );
  }
}

export async function PUT(request: Request, { params }: IParams) {
  try {
    const { id } = await params;

    const body = await request.json();

    const api = await createBaseApi();

    const { data } = await api.put(`/vaccines/${id}`, body);

    return NextResponse.json(data, { status: 200 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return new NextResponse(
        JSON.stringify(
          error.response?.data || { message: error.message }
        ),
        { status: error.response?.status || 500 }
      );
    }

    return new NextResponse(
      JSON.stringify({ message: "Erro ao atualizar vacina." }),
      { status: 500 }
    );
  }
}

export async function DELETE(request: Request, { params }: IParams) {
  try {
    const { id } = await params;

    const api = await createBaseApi();

    await api.delete(`/vaccines/${id}`);

    return new NextResponse(null, { status: 204 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return new NextResponse(
        JSON.stringify(
          error.response?.data || { message: error.message }
        ),
        { status: error.response?.status || 500 }
      );
    }

    return new NextResponse(
      JSON.stringify({ message: "Erro ao excluir vacina." }),
      { status: 500 }
    );
  }
}