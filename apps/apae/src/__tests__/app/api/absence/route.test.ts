import { describe, it, expect, vi, beforeEach } from "vitest";
import { AxiosError } from "axios";

// Mock next/server NextResponse
vi.mock("next/server", () => {
  return {
    NextResponse: {
      json: vi.fn((data: unknown, init?: { status?: number }) => ({
        data,
        status: init?.status ?? 200,
      })),
    },
  };
});

// Mock @/lib/axios
const mockGet = vi.fn();
const mockPost = vi.fn();

vi.mock("@/lib/axios", () => ({
  createBaseApi: vi.fn(() =>
    Promise.resolve({
      get: mockGet,
      post: mockPost,
    })
  ),
}));

import { GET, POST } from "@/app/api/absence/route";
import { NextResponse } from "next/server";

function makeAxiosError(status: number, message?: string): AxiosError {
  const error = new AxiosError("request failed");
  error.response = {
    status,
    data: message ? { message } : undefined,
    statusText: "",
    headers: {},
    config: {} as never,
  };
  return error;
}

describe("GET /api/absence", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("returns 200 with absences data on success", async () => {
    const absencesData = [{ id: "1", absenceDate: "2024-01-10" }];
    mockGet.mockResolvedValueOnce({ data: absencesData });

    const req = new Request("http://localhost/api/absence");
    await GET(req);

    expect(NextResponse.json).toHaveBeenCalledWith(absencesData, {
      status: 200,
    });
  });

  it("returns 500 with Axios error message when API returns 500", async () => {
    const axiosError = makeAxiosError(500, "Internal Server Error");
    mockGet.mockRejectedValueOnce(axiosError);

    const req = new Request("http://localhost/api/absence");
    await GET(req);

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Internal Server Error" },
      { status: 500 }
    );
  });

  it("returns fallback message when AxiosError has no response message", async () => {
    const axiosError = makeAxiosError(503);
    mockGet.mockRejectedValueOnce(axiosError);

    const req = new Request("http://localhost/api/absence");
    await GET(req);

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Erro ao buscar faltas" },
      { status: 503 }
    );
  });

  it("returns 500 when a non-Axios error is thrown", async () => {
    mockGet.mockRejectedValueOnce(new Error("Unexpected error"));

    const req = new Request("http://localhost/api/absence");
    await GET(req);

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });

  it("calls api.get with correct endpoint /absences", async () => {
    mockGet.mockResolvedValueOnce({ data: [] });

    const req = new Request("http://localhost/api/absence");
    await GET(req);

    expect(mockGet).toHaveBeenCalledWith("/absences");
  });
});

describe("POST /api/absence", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("returns 201 with created absence data on success", async () => {
    const newAbsence = { patientId: "123", absenceDate: "2024-01-15" };
    const createdAbsence = { id: "abc", ...newAbsence };
    mockPost.mockResolvedValueOnce({ data: createdAbsence });

    const req = new Request("http://localhost/api/absence", {
      method: "POST",
      body: JSON.stringify(newAbsence),
      headers: { "Content-Type": "application/json" },
    });

    await POST(req);

    expect(NextResponse.json).toHaveBeenCalledWith(createdAbsence, {
      status: 201,
    });
  });

  it("calls api.post with correct endpoint and body", async () => {
    const body = { patientId: "42", absenceDate: "2024-03-20" };
    mockPost.mockResolvedValueOnce({ data: { id: "1", ...body } });

    const req = new Request("http://localhost/api/absence", {
      method: "POST",
      body: JSON.stringify(body),
      headers: { "Content-Type": "application/json" },
    });

    await POST(req);

    expect(mockPost).toHaveBeenCalledWith("/absences", body);
  });

  it("returns Axios error message and status on AxiosError", async () => {
    const axiosError = makeAxiosError(400, "Dados inválidos");
    mockPost.mockRejectedValueOnce(axiosError);

    const req = new Request("http://localhost/api/absence", {
      method: "POST",
      body: JSON.stringify({ patientId: "x" }),
      headers: { "Content-Type": "application/json" },
    });

    await POST(req);

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Dados inválidos" },
      { status: 400 }
    );
  });

  it("returns fallback 'Erro ao criar falta' message when AxiosError has no response message", async () => {
    const axiosError = makeAxiosError(422);
    mockPost.mockRejectedValueOnce(axiosError);

    const req = new Request("http://localhost/api/absence", {
      method: "POST",
      body: JSON.stringify({}),
      headers: { "Content-Type": "application/json" },
    });

    await POST(req);

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Erro ao criar falta" },
      { status: 422 }
    );
  });

  it("returns 500 with generic message on non-Axios error", async () => {
    mockPost.mockRejectedValueOnce(new TypeError("Something unexpected"));

    const req = new Request("http://localhost/api/absence", {
      method: "POST",
      body: JSON.stringify({}),
      headers: { "Content-Type": "application/json" },
    });

    await POST(req);

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });
});