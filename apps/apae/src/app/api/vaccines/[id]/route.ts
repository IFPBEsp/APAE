import { createBaseApi } from "@/lib/axios";
import { updateVaccineSchema } from "@/domains/vaccines/vaccines.schema";
import { NextResponse } from "next/server";
import { AxiosError } from "axios";

type Params = {
  params: Promise<{
    id: string;
  }>;
};

function handleApiError(error: unknown, defaultMessage: string) {
  if (error instanceof AxiosError) {
    return NextResponse.json(
      error.response?.data || { message: error.message },
      { status: error.response?.status || 500 }
    );
  }

  return NextResponse.json({ message: defaultMessage }, { status: 500 });
}

export async function GET(request: Request, { params }: Params) {
  try {
    const { id } = await params;
    const api = await createBaseApi();
    const { data } = await api.get(`/vaccines/${id}`);
    
    return NextResponse.json(data);
  } catch (error) {
    return handleApiError(error, "Erro ao buscar vacina.");
  }
}

export async function PUT(request: Request, { params }: Params) {
  try {
    const { id } = await params;
    const body = await request.json();

    const validation = updateVaccineSchema.safeParse(body);
    
    if (!validation.success) {
      return NextResponse.json(
        {
          message: "Dados inválidos.",
          errors: validation.error.flatten().fieldErrors,
        },
        { status: 400 }
      );
    }

    const payload = validation.data;

    if (payload?.name) {
      payload.name = payload.name.charAt(0).toUpperCase() + payload.name.slice(1);
    }

    const api = await createBaseApi();
    const { data } = await api.put(`/vaccines/${id}`, payload);
    
    return NextResponse.json(data);
  } catch (error) {
    return handleApiError(error, "Erro ao atualizar vacina.");
  }
}

export async function DELETE(request: Request, { params }: Params) {
  try {
    const { id } = await params;
    const api = await createBaseApi();
    await api.delete(`/vaccines/${id}`);
    
    return new Response(null, { status: 204 });
  } catch (error) {
    return handleApiError(error, "Erro ao excluir vacina.");
  }
}