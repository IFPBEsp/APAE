import { NextResponse } from "next/server";

import { createBaseApi } from "@/lib/axios";
import { handleError } from "@/lib/handle-error";

interface IParams {
  params: Promise<{ id: string }>;
}

export async function GET(_: Request, { params }: IParams) {
  try {
    const { id } = await params;
    const api = await createBaseApi();
    const { data } = await api.get(`/vaccines/${id}`);

    return NextResponse.json(data);
  } catch (error) {
    return handleError(error);
  }
}

export async function PUT(request: Request, { params }: IParams) {
  try {
    const { id } = await params;
    const body = await request.json();

    const api = await createBaseApi();
    const { data } = await api.put(`/vaccines/${id}`, body);

    return NextResponse.json(data);
  } catch (error) {
    return handleError(error);
  }
}

export async function DELETE(_: Request, { params }: IParams) {
  try {
    const { id } = await params;
    const api = await createBaseApi();
    const { data } = await api.delete(`/vaccines/${id}`);

    return NextResponse.json(data);
  } catch (error) {
    return handleError(error);
  }
}
