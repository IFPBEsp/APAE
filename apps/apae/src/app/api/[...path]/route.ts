import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";

type RouteContext = {
  params: Promise<{ path: string[] }>;
};

const RESPONSE_HEADERS = [
  "cache-control",
  "content-disposition",
  "content-type",
  "etag",
  "last-modified",
  "location",
] as const;

async function proxyToBackend(request: NextRequest, context: RouteContext) {
  try {
    const { path } = await context.params;
    const requestUrl = new URL(request.url);
    const api = await createBaseApi();
    const contentType = request.headers.get("content-type");
    const headers = contentType ? { "Content-Type": contentType } : undefined;
    const hasBody = !["GET", "HEAD"].includes(request.method);
    const data = hasBody ? await request.arrayBuffer() : undefined;
    const upstreamPath = `/${path.map(encodeURIComponent).join("/")}${requestUrl.search}`;

    const response = await api.request<ArrayBuffer>({
      url: upstreamPath,
      method: request.method,
      data,
      headers,
      responseType: "arraybuffer",
      validateStatus: () => true,
      maxBodyLength: Infinity,
    });

    const responseHeaders = new Headers();
    for (const header of RESPONSE_HEADERS) {
      const value = response.headers[header];
      if (typeof value === "string") responseHeaders.set(header, value);
    }

    const responseBody = response.status === 204
      ? null
      : new Uint8Array(response.data);

    return new NextResponse(responseBody, {
      status: response.status,
      headers: responseHeaders,
    });
  } catch (error) {
    if (error instanceof AxiosError) {
      console.error("Authenticated API proxy failed:", error.message);
    } else {
      console.error("Authenticated API proxy failed:", error);
    }

    return NextResponse.json(
      { message: "Não foi possível comunicar com a API." },
      { status: 502 },
    );
  }
}

export const GET = proxyToBackend;
export const HEAD = proxyToBackend;
export const POST = proxyToBackend;
export const PUT = proxyToBackend;
export const PATCH = proxyToBackend;
export const DELETE = proxyToBackend;
