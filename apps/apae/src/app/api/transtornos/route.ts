import { AxiosError } from "axios";
import { NextResponse } from "next/server";

import { createBaseApi } from "@/lib/axios";
import { handleError } from "@/lib/handle-error";
import { createTranstornoSchema } from "@/schemas/transtornosSchema";

export async function POST(request: Request) {
  try {
    const body = await request.json();

    const validation = createTranstornoSchema.safeParse(body);
    if (!validation.success) {
      return new NextResponse(
        JSON.stringify({
          message: "Dados inválidos.",
          errors: validation.error.flatten().fieldErrors,
        }),
        { status: 400 },
      );
    }

    const api = await createBaseApi();
    const { data } = await api.post("/disorders", validation.data);

    return NextResponse.json(data, { status: 201 });
  } catch (error) {
    if (error instanceof AxiosError && error.response?.status === 409) {
      return NextResponse.json(
        { message: "Transtorno já existente, prosseguindo." },
        { status: 200 },
      );
    }

    return handleError(error);
  }
}

export async function GET() {
  try {
    const api = await createBaseApi();
    const { data } = await api.get("/disorders");

    return NextResponse.json(data);
  } catch (error) {
    return handleError(error);
  }
}
