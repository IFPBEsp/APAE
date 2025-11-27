import { NextResponse } from 'next/server';
import { createBaseApi } from '@/lib/axios';
import { AxiosError } from 'axios';

export async function GET(request: Request) {
  try {
    const api = await createBaseApi();
    const { searchParams } = new URL(request.url);

    const response = await api.get('/patients', {
      params: searchParams,
    });

    return NextResponse.json(response.data);

  } catch (err) {
    const error = err as AxiosError;
    console.error('[ERRO NO PROXY /api/patients]:', error.response?.data || error.message);
    
    return NextResponse.json(
      { message: error.response?.data || 'Erro interno no servidor Next.js' },
      { status: error.response?.status || 500 }
    );
  }
}

