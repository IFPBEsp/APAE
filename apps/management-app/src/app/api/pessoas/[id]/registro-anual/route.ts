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
    
    // 1. Pega o valor bruto do cookie
    let tokenRaw = cookieStore.get("session")?.value || 
                   cookieStore.get("token")?.value || 
                   cookieStore.get("access_token")?.value;

    if (!tokenRaw) {
        return NextResponse.json({ message: "Não autenticado." }, { status: 401 });
    }

    let finalToken = tokenRaw;

    // 2. --- NOVA LÓGICA: Tenta extrair o token de dentro do JSON ---
    if (tokenRaw.trim().startsWith("{")) {
        try {
            const parsed = JSON.parse(tokenRaw);
            // Tenta achar o token em propriedades comuns
            const extracted = parsed.accessToken || parsed.token || parsed.jwt || parsed.access_token;
            
            if (extracted) {
                console.log("📦 [Auth] Token extraído do JSON com sucesso.");
                finalToken = extracted;
            }
        } catch (e) {
            console.warn("⚠️ [Auth] Parecia JSON mas falhou ao parsear. Usando bruto.");
        }
    }

    // 3. Limpeza final (remove aspas extras e prefixo Bearer se houver)
    finalToken = finalToken.replace(/^"|"$/g, '').replace(/^Bearer\s+/i, '').trim();

    const backendUrl = `${API_BASE_URL}/patients/${id}/annual-registry`;
    
    console.log(`🔵 [POST] URL: ${backendUrl}`);
    // Log para confirmar que agora está enviando o token limpo (sem chaves {})
    console.log(`🔑 [Auth] Token final: ${finalToken.substring(0, 15)}...`);

    const res = await fetch(backendUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${finalToken}`,
      },
      body: JSON.stringify(body),
    });

    if (!res.ok) {
      const errorText = await res.text();
      console.error(`🔴 [Erro Java ${res.status}]: ${errorText}`);
      
      if (res.status === 409) {
          return NextResponse.json(
            { message: "Já existe um registro para este ano." },
            { status: 409 }
          );
      }

      return NextResponse.json(
        { message: "Erro do servidor backend", details: errorText },
        { status: res.status }
      );
    }

    const data = await res.json();
    console.log("🟢 [Sucesso] Registro criado!");
    return NextResponse.json(data, { status: 201 });

  } catch (error: any) {
    console.error("🔴 [Erro Next.js]:", error.message);
    return NextResponse.json(
      { message: "Erro interno de conexão.", details: error.message },
      { status: 500 }
    );
  }
}