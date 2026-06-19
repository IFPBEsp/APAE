import { createBaseApi } from "@/lib/axios";
import { createserviceTypeSchema } from "@/schemas/service-type-schemas";
import { NextResponse } from "next/server";
import { AxiosError } from "axios";

export async function POST(request: Request) {
  try {
    const body = await request.json();

    const validation = createserviceTypeSchema.safeParse(body);
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

    const { data } = await api.post("/service-types", validation.data);

    return NextResponse.json(data);
  } catch (error) {
    if (error instanceof AxiosError) {
      return new NextResponse(
        JSON.stringify(error.response?.data || { message: error.message }),
        { status: error.response?.status || 500 }
      );
    }

    return new NextResponse(
      JSON.stringify({ message: "Ocorreu um erro inesperado." }), { status: 500 });
  }
}

export async function GET() {
  try {
    const api = await createBaseApi();
    const { data } = await api.get("/service-types");
    return NextResponse.json(data);
  } catch (error) {
    if (error instanceof AxiosError) {
      return new NextResponse(
        JSON.stringify(error.response?.data || { message: error.message }),
        { status: error.response?.status || 500 }
      );
    }

    return new NextResponse(JSON.stringify({ message: "Erro ao buscar dados." }), { status: 500 });
  }
}
