import { createBaseApi } from "@/lib/axios";
import { NextRequest, NextResponse } from "next/server";

export async function GET(
  request: NextRequest,
  context: { params: Promise<{ id: string }> },
) {
  const { id } = await context.params;

  try {
    const api = await createBaseApi();

    const response = await api.get(`/appointments/today/${id}`);
    return NextResponse.json(response.data, { status: 200 });
  } catch (error) {
    console.error("Error proxying to backend:", error);
    return NextResponse.json(
      { error: "Internal server error" },
      { status: 500 },
    );
  }
}
