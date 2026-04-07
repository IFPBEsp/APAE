import { NextResponse } from "next/server";
import { AxiosError } from "axios";

export function handleError(error: unknown) {
  if (error instanceof AxiosError) {
    return new NextResponse(
      JSON.stringify(error.response?.data || { message: error.message }),
      { status: error.response?.status || 500 },
    );
  }

  return new NextResponse(
    JSON.stringify({ message: "Erro interno do servidor." }),
    { status: 500 },
  );
}
