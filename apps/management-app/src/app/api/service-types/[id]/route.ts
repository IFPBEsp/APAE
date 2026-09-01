import { NextResponse } from "next/server";

import { updateServiceTypeSchema } from "@/domains/service-types/service-types.schema";
import { createBaseApi } from "@/lib/axios";

interface RouteParams {
  params: Promise<{ id: string }>;
}

export async function GET(request: Request, { params }: RouteParams) {
  try {
    const { id } = await params;
    const api = await createBaseApi();
    const { data } = await api.get(`/service-types/${id}`);
    return NextResponse.json(data);
  } catch (error: any) {
    return new NextResponse(
      JSON.stringify(error.response?.data || { message: error.message }),
      { status: error.response?.status || 500 },
    );
  }
}

export async function PUT(request: Request, { params }: RouteParams) {
  try {
    const { id } = await params;
    const body = await request.json();
    const validation = updateServiceTypeSchema.safeParse(body);

    if (!validation.success) {
      return NextResponse.json({ errors: validation.error.flatten() }, { status: 400 });
    }

    const api = await createBaseApi();
    const { data } = await api.put(`/service-types/${id}`, validation.data);
    return NextResponse.json(data);
  } catch (error: any) {
    return new NextResponse(
      JSON.stringify(error.response?.data || { message: error.message }),
      { status: error.response?.status || 500 },
    );
  }
}

export async function DELETE(request: Request, { params }: RouteParams) {
  try {
    const { id } = await params;
    const api = await createBaseApi();
    await api.delete(`/service-types/${id}`);
    return new NextResponse(null, { status: 204 });
  } catch (error: any) {
    return new NextResponse(
      JSON.stringify(error.response?.data || { message: error.message }),
      { status: error.response?.status || 500 },
    );
  }
}
