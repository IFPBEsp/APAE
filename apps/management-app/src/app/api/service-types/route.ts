import { NextResponse } from "next/server";

import { createBaseApi } from "@/lib/axios";
import { createServiceTypeSchema } from "@/domains/service-types/service-types.schema";

function normalizeServiceType(data: { area?: string; id: string | number; name?: string }) {
  return {
    id: data.id,
    name: data.name ?? data.area ?? "",
  };
}

export async function POST(request: Request) {
  try {
    const body = await request.json();
    const validation = createServiceTypeSchema.safeParse(body);

    if (!validation.success) {
      return new NextResponse(
        JSON.stringify({
          message: "Dados invalidos.",
          errors: validation.error.flatten().fieldErrors,
        }),
        { status: 400 },
      );
    }

    const api = await createBaseApi();
    const { data } = await api.post("/service-types", { name: validation.data.name });
    return NextResponse.json(normalizeServiceType(data), { status: 201 });
  } catch (error: any) {
    return new NextResponse(
      JSON.stringify(error.response?.data || { message: error.message }),
      { status: error.response?.status || 500 },
    );
  }
}

export async function GET() {
  try {
    const api = await createBaseApi();
    const { data } = await api.get("/service-types");
    return NextResponse.json(Array.isArray(data) ? data.map(normalizeServiceType) : []);
  } catch (error: any) {
    return new NextResponse(
      JSON.stringify(error.response?.data || { message: error.message }),
      { status: error.response?.status || 500 },
    );
  }
}
