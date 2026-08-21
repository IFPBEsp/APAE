import { createBaseApi } from "@/lib/axios";
import { updateVaccineSchema } from "@/domains/vaccines/vaccines.schema";
import { NextResponse } from "next/server";
import { AxiosError } from "axios";

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
    return NextResponse.json(data);
  } catch (error) {
    if (error instanceof AxiosError) {
      return new NextResponse(
        JSON.stringify(error.response?.data || { message: error.message }),
        { status: error.response?.status || 500 }
      );
    }

    return new NextResponse(
      JSON.stringify({ message: "Erro ao buscar vacina." }),
      { status: 500 }
    );
  }
}

export async function PUT(request: Request, { params }: Params) {
  try {
    const { id } = await params;
    const body = await request.json();

    const validation = updateVaccineSchema.safeParse(body);
    if (!validation.success) {
      return new NextResponse(
        JSON.stringify({
          message: "Dados inválidos.",
          errors: validation.error.flatten().fieldErrors,
        }),
        { status: 400 }
      );
    }

    const api = await createBaseApi();
    const payload = validation.data;

    if (payload && payload.name && payload.name.length > 0) {
      payload.name = payload.name.charAt(0).toUpperCase() + payload.name.slice(1);
    }

    const { data } = await api.put(`/vaccines/${id}`, payload);
    return NextResponse.json(data);
  } catch (error) {
    if (error instanceof AxiosError) {
      return new NextResponse(
        JSON.stringify(error.response?.data || { message: error.message }),
        { status: error.response?.status || 500 }
      );
    }

    return new NextResponse(
      JSON.stringify({ message: "Erro ao atualizar vacina." }),
      { status: 500 }
    );
  }
}

export async function DELETE(request: Request, { params }: Params) {
  try {
    const { id } = await params;
    const api = await createBaseApi();
    await api.delete(`/vaccines/${id}`);
    return new Response(null, { status: 204 });
  } catch (error) {
    if (error instanceof AxiosError) {
      return new NextResponse(
        JSON.stringify(error.response?.data || { message: error.message }),
        { status: error.response?.status || 500 }
      );
    }

    return new NextResponse(
      JSON.stringify({ message: "Erro ao excluir vacina." }),
      { status: 500 }
    );
  }
}