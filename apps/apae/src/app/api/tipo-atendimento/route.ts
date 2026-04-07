import { NextResponse } from "next/server";

import { createBaseApi } from "@/lib/axios";
import { createserviceTypeSchema } from "@/schemas/service-type-schemas";
import { handleError } from "@/lib/handle-error";

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
        { status: 400 },
      );
    }

    const api = await createBaseApi();
    const { data } = await api.post("/service-areas", validation.data);

    return NextResponse.json(data);
  } catch (error) {
    return handleError(error);
  }
}

export async function GET() {
  try {
    const api = await createBaseApi();
    const { data } = await api.get("/service-areas");

    return NextResponse.json(data);
  } catch (error) {
    return handleError(error);
  }
}
