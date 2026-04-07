import { NextResponse } from "next/server";

import { createBaseApi } from "@/lib/axios";
import { handleError } from "@/lib/handle-error";
import { updateserviceTypeSchema } from "@/schemas/service-type-schemas";

interface IParams {
  params: Promise<{ id: string }>;
}

export async function GET(_: Request, { params }: IParams) {
  try {
    const { id } = await params;
    const api = await createBaseApi();
    const { data } = await api.get(`/service-areas/${id}`);

    return NextResponse.json(data);
  } catch (error) {
    return handleError(error);
  }
}

export async function PUT(request: Request, { params }: IParams) {
  try {
    const { id } = await params;
    const body = await request.json();

    const validation = updateserviceTypeSchema.safeParse(body);
    if (!validation.success) {
      return NextResponse.json(
        { errors: validation.error.flatten() },
        { status: 400 },
      );
    }

    const api = await createBaseApi();
    const { data } = await api.put(`/service-areas/${id}`, validation.data);

    return NextResponse.json(data);
  } catch (error) {
    return handleError(error);
  }
}
