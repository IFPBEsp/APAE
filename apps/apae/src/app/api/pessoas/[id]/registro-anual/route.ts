import { NextResponse } from "next/server";
import { cookies } from "next/headers";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8090/api";

export async function POST(
  request: Request,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params; 
    const body = await request.json();

    const cookieStore = await cookies();
    const tokenRaw = cookieStore.get("session")?.value || 
                     cookieStore.get("token")?.value || 
                     cookieStore.get("access_token")?.value;

    if (!tokenRaw) {
        return NextResponse.json({ message: "Não autenticado." }, { status: 401 });
    }

    let finalToken = tokenRaw;

    if (tokenRaw.trim().startsWith("{")) {
        try {
            const parsed = JSON.parse(tokenRaw);
            const extracted = parsed.accessToken || parsed.token || parsed.jwt || parsed.access_token;
            
            if (extracted) {
                finalToken = extracted;
            }
        } catch {
        }
    }

    finalToken = finalToken.replace(/^"|"$/g, '').replace(/^Bearer\s+/i, '').trim();

    const res = await fetch(`${API_BASE_URL}/patients/${id}/annual-registry`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${finalToken}`,
      },
      body: JSON.stringify(body),
    });

    if (!res.ok) {
      const errorText = await res.text();
      
      if (res.status === 409) {
          return NextResponse.json(
            { message: "Já existe um registro para este ano." },
            { status: 409 }
          );
      }

      console.error("Erro Backend Java:", errorText);
      return NextResponse.json(
        { message: "Erro ao criar registro no backend", details: errorText },
        { status: res.status }
      );
    }

    const data = await res.json();
    return NextResponse.json(data, { status: 201 });

  } catch (error: unknown) {
    const err = error as { message?: string };
    console.error("Erro interno rota Next.js:", error);
    return NextResponse.json(
      { message: "Erro interno de conexão.", details: err.message },
      { status: 500 }
    );
  }
}