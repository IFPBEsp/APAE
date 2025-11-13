import { createBaseApi } from "@/lib/axios";
import { updateTranstornoSchema } from "@/app/schemas/transtornosSchema";
import { NextResponse } from "next/server";

interface IParams {
  params: {
    id: string;
  };
}

export async function GET(request: Request, { params }: IParams) {
  try {
    const { id } = params;
    const api = await createBaseApi();
    const { data } = await api.get(`/transtornos/${id}`);

    return NextResponse.json(data);
  } catch (error: any) {
    return new NextResponse(
      JSON.stringify(error.response?.data || { message: error.message }),
      { status: error.response?.status || 500 }
    );
  }
}

export async function PUT(request: Request, { params }: IParams) {
  try {
    const { id } = params;
    const body = await request.json();

    const validation = updateTranstornoSchema.safeParse(body);
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
    const { data } = await api.put(`/transtornos/${id}`, validation.data);

    return NextResponse.json(data);
  } catch (error: any) {
    return new NextResponse(
      JSON.stringify(error.response?.data || { message: error.message }),
      { status: error.response?.status || 500 }
    );
  }
}

export async function DELETE(request: Request, { params }: IParams) {
  try {
    const { id } = params;
    const api = await createBaseApi();
    const { data } = await api.delete(`/transtornos/${id}`);

    return NextResponse.json(data);
  } catch (error: any) {
    return new NextResponse(
      JSON.stringify(error.response?.data || { message: error.message }),
      { status: error.response?.status || 500 }
    );
  }
}