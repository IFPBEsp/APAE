import { createBaseApi } from "@/lib/axios";
import { NextResponse } from "next/server";

export async function GET(
  req: Request,
  { params }: { params: { id: string } }
) {
  const api = await createBaseApi();

  const response = await api.get(
    '/professionals/${params.id}/documents'
  );

  return NextResponse.json(response.data);
}