import { createBaseApi } from "@/lib/axios";
import { createTranstornoSchema } from "@/schemas/transtornosSchema";
import { NextResponse } from "next/server";
import { AxiosError } from "axios";

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
        { status: 400 }
      );
    }

    const api = await createBaseApi();

    const { data } = await api.post("/disorders", validation.data);

    return NextResponse.json(data, { status: 201 });

  } catch (error: any) {
    const err = error as AxiosError;
    
    if (err.response?.status === 409) {
        return NextResponse.json(
            { message: "Transtorno já existente, prosseguindo." }, 
            { status: 200 }
        );
    }

    return new NextResponse(
      JSON.stringify(err.response?.data || { message: err.message }),
      { status: err.response?.status || 500 }
    );
  }
}

export async function GET() {
  try {
    const api = await createBaseApi();
    const { data } = await api.get("/disorders");
    return NextResponse.json(data);
  } catch (error: any) {
    return new NextResponse(
      JSON.stringify(error.response?.data || { message: error.message }),
      { status: error.response?.status || 500 }
    );
  }
}