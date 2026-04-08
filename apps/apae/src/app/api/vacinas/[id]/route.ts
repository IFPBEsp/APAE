import { createBaseApi } from "@/lib/axios";
import { NextResponse } from "next/server";
import { AxiosError } from "axios";

interface Params {
    params: Promise<{ id: string }>;
}

export async function GET(request: Request, { params }: Params) {
    try {
        const { id } = await params;
        const api = await createBaseApi();
        const { data } = await api.get(`/vaccines/${id}`);
        return NextResponse.json(data);
    } catch (error) {
        const err = error as AxiosError;
        return new NextResponse(JSON.stringify(err.response?.data), { status: 500 });
    }
}

export async function PUT(request: Request, { params }: Params) {
    try {
        const { id } = await params;
        const body = await request.json();
        const api = await createBaseApi();
        const { data } = await api.put(`/vaccines/${id}`, body);
        return NextResponse.json(data);
    } catch (error) {
        const err = error as AxiosError;
        return new NextResponse(JSON.stringify(err.response?.data), { status: 500 });
    }
}

export async function DELETE(request: Request, { params }: Params) {
    try {
        const { id } = await params;
        const api = await createBaseApi();
        const { data } = await api.delete(`/vaccines/${id}`);
        return NextResponse.json(data);
    } catch (error) {
        const err = error as AxiosError;
        return new NextResponse(JSON.stringify(err.response?.data), { status: 500 });
    }
}