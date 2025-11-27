import { NextResponse } from 'next/server';
import { createBaseApi } from '@/lib/axios';
import { AxiosError } from 'axios';

export async function GET(request: Request) {
  const url = new URL(request.url);
  const pathParts = url.pathname.split('/');
  const slug = pathParts[pathParts.length - 1];


  const allowedFilters = ['transtornos', 'anos', 'cidades'];
  if (!allowedFilters.includes(slug)) {
    return NextResponse.json({ message: 'Filtro inválido' }, { status: 400 });
  }

  try {
    const api = await createBaseApi();
    const response = await api.get(`/patients/filtros/${slug}`);

    return NextResponse.json(response.data);

  } catch (err) {
    const error = err as AxiosError;
    console.error(`[ERRO NO PROXY /api/patients/filtros/${slug}]:`, error.response?.data || error.message);
    
    return NextResponse.json(
      { message: error.response?.data || 'Erro interno no servidor Next.js' },
      { status: error.response?.status || 500 }
    );
  }
}