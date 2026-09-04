import { createVaccineSchema } from "@/domains/vaccines/vaccines.schema";
import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextResponse } from "next/server";

export async function GET() {
  try {
    const api = await createBaseApi();

    const { data } = await api.get("/vaccines");

    return NextResponse.json(data);
  } catch (error) {
    const message =
      error instanceof Error ? error.message : "Erro desconhecido";

    console.error("Erro ao buscar vacinas:", message);

    return NextResponse.json(
      { message: "Erro ao buscar a lista de vacinas." },
      { status: 500 },
    );
  }
}

export async function POST(request: Request) {
  try {
    const body = await request.json();

    const validation = createVaccineSchema.safeParse(body);

    if (!validation.success) {
      return NextResponse.json(
        {
          message: "Dados inválidos para criação da vacina.",
          errors: validation.error.flatten().fieldErrors,
        },
        { status: 400 },
      );
    }

    const api = await createBaseApi();

    const { data } = await api.post("/vaccines", validation.data);

    return NextResponse.json(data, { status: 201 });
  } catch (error) {
    if (error instanceof AxiosError) {
      const status = error.response?.status ?? 500;

      const message =
        error.response?.data?.message ?? "Erro ao criar vacina.";

      console.error("Erro ao criar vacina:", message);

      return NextResponse.json({ message }, { status });
    }

    const message =
      error instanceof Error ? error.message : "Erro desconhecido";

    console.error("Erro ao criar vacina:", message);

    return NextResponse.json(
      { message: "Erro ao criar vacina." },
      { status: 500 },
    );
  }
}