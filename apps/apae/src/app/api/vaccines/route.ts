import { createBaseApi } from "@/lib/axios";
import { createVaccineSchema } from "@/domains/vaccines/vaccines.schema";
import { NextResponse } from "next/server";
import { AxiosError } from "axios";

function handleApiError(error: unknown, defaultMessage: string) {
  if (error instanceof AxiosError) {
    return NextResponse.json(
      error.response?.data || { message: error.message },
      { status: error.response?.status || 500 }
    );
  }

  return NextResponse.json({ message: defaultMessage }, { status: 500 });
}

export async function GET() {
  try {
    const api = await createBaseApi();
    const { data } = await api.get("/vaccines");
    
    return NextResponse.json(data);
  } catch (error) {
    return handleApiError(error, "Erro ao buscar a lista de vacinas.");
  }
}

export async function POST(request: Request) {
  try {
    const body = await request.json();
    const validation = createVaccineSchema.safeParse(body);

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
    const { data } = await api.post("/vaccines", payload);
    
    return NextResponse.json(data, { status: 201 });
  } catch (error) {
    return handleApiError(error, "Erro inesperado ao criar vacina.");
  }
}