import { createBaseApi } from '@/lib/axios';
import { setSessionCookie } from '@/lib/cookies';
import { AxiosError } from 'axios';
import { NextResponse } from 'next/server';

function decodeJwtPayload(token: string): Record<string, unknown> | null {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload);
  } catch {
    return null;
  }
}

const DISABLE_AUTH = false;
export async function POST(req: Request) {
  if (DISABLE_AUTH) {
    const payload = {
      accessToken: 'fake-token-dev',
    };

    await setSessionCookie(payload);

    return NextResponse.json(
      { message: 'Login ignorado (DEV MODE)' },
      { status: 200 }
    );
  }
  try {
    const { username, password } = await req.json();

    if (!username || !password) {
      return NextResponse.json(
        { message: 'Todos os campos são obrigatórios' },
        { status: 406 }
      );
    }
    const api = await createBaseApi();
    const response = await api.post('/auth/signin', { username, password });

    if (response.status !== 200) {
      return NextResponse.json(
        { message: response.data?.message || 'Erro ao fazer login' },
        { status: response.status }
      );
    }

    const { token } = response.data;

    const payload = {
      accessToken: token,
    };

    const jwtPayload = decodeJwtPayload(token);
    const expiresAtSec = jwtPayload?.exp && typeof jwtPayload.exp === 'number'
      ? jwtPayload.exp
      : undefined;

    await setSessionCookie(payload, expiresAtSec);

    return NextResponse.json(
      { message: 'Login bem-sucedido' },
      { status: 200 }
    );
  } catch (error) {
    if (error instanceof AxiosError) {
      if (error.response?.status === 401) {
        return NextResponse.json(
          { message: 'Credenciais inválidas' },
          { status: 401 }
        );
      }
      return NextResponse.json(
        {
          message:
            error.response?.data?.message || 'Erro no servidor de autenticação',
        },
        { status: error.response?.status || 500 }
      );
    }

    return NextResponse.json(
      { message: 'Erro interno ao processar o login' },
      { status: 500 }
    );
  }
}
