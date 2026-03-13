import { NextResponse, NextRequest } from 'next/server';
import { createBaseApi } from '@/lib/axios';
import { AxiosError } from 'axios';

export async function GET(
  request: NextRequest, 
  { params }: { params: Promise<{ slug: string }> }
) {
  const { slug } = await params;
  const allowedFilters = ['transtornos', 'anos', 'cidades', 'tipos-atendimento'];
  
  if (!allowedFilters.includes(slug)) {
    return NextResponse.json({ message: 'Filtro inválido' }, { status: 400 });
  }

  try {
    const api = await createBaseApi();
    const response = await api.get(`/patients/filtros/${slug}`);
    return NextResponse.json(response.data);

  } catch (err) {
    const error = err as AxiosError;
    return NextResponse.json(
      { message: error.response?.data || 'Erro nos filtros' },
      { status: error.response?.status || 500 }
    );
  }
}