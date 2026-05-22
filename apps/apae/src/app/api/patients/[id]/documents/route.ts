import { createBaseApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";


export const dynamic = 'force-dynamic';


const categoryMap: { [key: string]: string } = { // ????
  personals: "personals",
  pessoais: "personals",
  medico: "medicals",
  medicos: "medicals",
  escolar: "schools",
  escolares: "schools",
  personal: "personals",
  medical: "medicals",
  school: "schools",
};


export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id: patientId } = await params;
  const searchParams = request.nextUrl.searchParams;
  const categoryRaw = searchParams.get("category");
  const year = searchParams.get("year");


  if (!patientId || !categoryRaw) {
    return NextResponse.json({ message: "ID e Categoria são obrigatórios" }, { status: 400 });
  }


  const endpointSuffix = categoryMap[categoryRaw.toLowerCase()];


  if (!endpointSuffix) {
    return NextResponse.json({ message: "Categoria inválida" }, { status: 400 });
  }


  try {
    const api = await createBaseApi();
    const config: any = {};
    if (year) {
      config.params = { year: parseInt(year) };
    }
    const { data } = await api.get(`/patients/${patientId}/documents/${endpointSuffix}`, config);
   
    return NextResponse.json(data);


  } catch (error) {
    const err = error as AxiosError;
    const status = err.response?.status || 500;
    const msg = err.response?.data ? JSON.stringify(err.response.data) : err.message;
   
    console.error(`[API GET] Erro ao buscar documentos (${endpointSuffix}):`, msg);
    if (status === 404) return NextResponse.json([]);


    return NextResponse.json(
      { message: "Erro ao buscar documentos." },
      { status }
    );
  }
}


export async function POST(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id: patientId } = await params;


  try {
    const incomingFormData = await request.formData();
       const api = await createBaseApi();


    const file = incomingFormData.get("file");
    const category = incomingFormData.get("category");
    const type = incomingFormData.get("type");
    const year = incomingFormData.get("year");


    if (!file || !category || !type) {
        return NextResponse.json({ message: "Dados incompletos" }, { status: 400 });
    }
    const backendFormData = new FormData();
    backendFormData.append("file", file);
    const response = await api.post(
        `/patients/${patientId}/documents`,
        backendFormData,
        {
            params: {
                category: category.toString(),
                type: type.toString(),
                year: year ? parseInt(year.toString()) : undefined
            },
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    );
    return NextResponse.json(response.data, { status: 201 });


  } catch (error) {
    const err = error as AxiosError;
    const status = err.response?.status || 500;
    const errorMsg = err.response?.data ? JSON.stringify(err.response.data) : err.message;

    console.error("[API POST] Erro Java:", errorMsg);


    return NextResponse.json(
      { message: "Erro no backend." },
      { status }
    );
  }
}
