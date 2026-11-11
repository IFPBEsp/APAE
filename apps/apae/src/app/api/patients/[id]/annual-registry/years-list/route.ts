import { NextResponse } from "next/server";
import { cookies } from "next/headers";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8090/apae-geral/api";

export async function GET(
  request: Request,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params;
    const cookieStore = await cookies();
    let token = cookieStore.get("session")?.value || 
                cookieStore.get("token")?.value || 
                cookieStore.get("access_token")?.value;

    if (token && token.trim().startsWith("{")) {
        try {
            token = JSON.parse(token).accessToken || token;
        } catch {}
    }
    
    if (token) token = token.replace(/^"|"$/g, '').replace(/^Bearer\s+/i, '').trim();

    const res = await fetch(`${API_BASE_URL}/patients/${id}/annual-registry/years`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
      },
    });

    if (!res.ok) {
        if (res.status === 404) return NextResponse.json([], { status: 200 });
        
        const errorData = await res.json().catch(() => ({ message: "Erro ao buscar anos" }));
        return NextResponse.json(
          { message: errorData.message || "Erro ao buscar anos" },
          { status: res.status }
        );
    }

    const data = await res.json();
    return NextResponse.json(data, { status: 200 });

  } catch (error) {
    console.error("Error proxying to backend:", error);
    return NextResponse.json(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  }
}