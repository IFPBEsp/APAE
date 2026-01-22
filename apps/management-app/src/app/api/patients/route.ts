export const dynamic = 'force-dynamic';

import { NextResponse, NextRequest } from 'next/server';
import { createBaseApi } from '@/lib/axios';
import { AxiosError } from 'axios';

export async function GET(request: NextRequest) {
  try {
    const api = await createBaseApi();
    const { searchParams } = new URL(request.url);
    const paramsObj = Object.fromEntries(searchParams.entries());

    const response = await api.get('/patients', {
      params: paramsObj,
    });

    const data = response.data.content || response.data;

    return NextResponse.json(data);

  } catch (err) {
    const error = err as AxiosError;
    console.error('[ERRO API PATIENTS]:', error.response?.data || error.message);
    
    return NextResponse.json(
      { message: error.response?.data || 'Erro interno no servidor' },
      { status: error.response?.status || 500 }
    );
  }
}