import { createBaseApi } from "@/lib/axios";
import { NextResponse } from "next/server";
import { AxiosError } from "axios";
import { createVaccineSchema } from "@/domains/vaccines/vaccines.schema";

export async function POST(request: Request) {
  try {
    const body = await request.json();

    const validation = createVaccineSchema.safeParse(body);
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
    const { data } = await api.post("/vaccines", validation.data);
    return NextResponse.json(data, { status: 201 });

  } catch (error) {
    if (error instanceof AxiosError) {
      return new NextResponse(
        JSON.stringify(error.response?.data || { message: error.message }),
        { status: error.response?.status || 500 }
      );
    }
    return new NextResponse(JSON.stringify({ message: "Erro inesperado ao criar vacina." }), { status: 500 });
  }
}

export async function GET() {
  try {
    const api = await createBaseApi();
    const { data } = await api.get("/vaccines");
    return NextResponse.json(data);
  } catch (error) {
    if (error instanceof AxiosError) {
      return new NextResponse(
        JSON.stringify(error.response?.data || { message: error.message }),
        { status: error.response?.status || 500 }
      );
    }
    return new NextResponse(JSON.stringify({ message: "Erro ao buscar a lista de vacinas." }), { status: 500 });
  }
}
