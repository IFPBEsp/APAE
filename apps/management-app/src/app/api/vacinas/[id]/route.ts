import { createBaseApi } from "@/lib/axios";
import { NextResponse } from "next/server";

interface Params {
    params: {
        id: string;
    };
}

export async function GET(request: Request, { params }: Params) {
    try {
        const { id } = params;
        const api = await createBaseApi();
        const { data } = await api.get(`/vaccines/${id}`);

        return NextResponse.json(data);
    } catch (error: any) {
        return new NextResponse(
            JSON.stringify(error.response?.data || { message: error.message }),
            { status: error.response?.status || 500 },
        );
    }
}

export async function PUT(request: Request, { params }: Params) {
    try {
        const { id } = params;
        const body = await request.json();
        const api = await createBaseApi();
        const { data } = await api.put(`/vaccines/${id}`, body);

        return NextResponse.json(data);
    } catch (error: any) {
        return new NextResponse(
            JSON.stringify(error.response?.data || { message: error.message }),
            { status: error.response?.status || 500 },
        );
    }
}

export async function DELETE(request: Request, { params }: Params) {
    try {
        const { id } = params;
        const api = await createBaseApi();
        const { data } = await api.delete(`/vaccines/${id}`);

        return NextResponse.json(data);
    } catch (error: any) {
        return new NextResponse(
            JSON.stringify(error.response?.data || { message: error.message }),
            { status: error.response?.status || 500 },
        );
    }
}
