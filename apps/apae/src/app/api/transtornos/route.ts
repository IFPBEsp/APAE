import { createBaseApi } from "@/lib/axios";
import { createTranstornoSchema } from "@/schemas/transtornosSchema";
import { NextResponse } from "next/server";

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

    return NextResponse.json(data);
  } catch (error: any) {
    return new NextResponse(
      JSON.stringify(error.response?.data || { message: error.message }),
      { status: error.response?.status || 500 }
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