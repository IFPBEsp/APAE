import { describe, it, expect, vi, beforeEach } from "vitest";
import { AxiosError } from "axios";

// Mock next/server before importing the route
vi.mock("next/server", () => ({
  NextResponse: {
    json: vi.fn((body: unknown, init?: { status?: number }) => ({
      body,
      status: init?.status ?? 200,
    })),
  },
}));

// Mock the axios lib module
vi.mock("@/lib/axios", () => ({
  createBaseApi: vi.fn(),
}));

import { GET, POST } from "@/app/api/absence/route";
import { NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";

const mockCreateBaseApi = vi.mocked(createBaseApi);
const mockNextResponseJson = vi.mocked(NextResponse.json);

function makeAxiosError(status: number, message?: string): AxiosError {
  const err = new AxiosError("error");
  err.response = {
    status,
    data: message ? { message } : {},
    statusText: "Error",
    headers: {},
    config: {} as never,
  };
  return err;
}

function makeRequest(body?: unknown): Request {
  return {
    json: vi.fn().mockResolvedValue(body ?? {}),
  } as unknown as Request;
}

beforeEach(() => {
  vi.clearAllMocks();
});

describe("GET /api/absence", () => {
  it("returns 200 with absences data on success", async () => {
    const mockAbsences = [{ id: "1", date: "2024-01-01" }];
    const mockApi = { get: vi.fn().mockResolvedValue({ data: mockAbsences }) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await GET(makeRequest());

    expect(mockCreateBaseApi).toHaveBeenCalledOnce();
    expect(mockApi.get).toHaveBeenCalledWith("/absences");
    expect(mockNextResponseJson).toHaveBeenCalledWith(mockAbsences, { status: 200 });
  });

  it("returns 500 with error message when AxiosError has no response", async () => {
    const axiosErr = new AxiosError("network error");
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await GET(makeRequest());

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao buscar faltas" },
      { status: 500 }
    );
  });

  it("returns upstream status and message from AxiosError response", async () => {
    const axiosErr = makeAxiosError(503, "Service unavailable");
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await GET(makeRequest());

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Service unavailable" },
      { status: 503 }
    );
  });

  it("returns 500 with generic message for non-Axios errors", async () => {
    const mockApi = { get: vi.fn().mockRejectedValue(new Error("unexpected")) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await GET(makeRequest());

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });

  it("uses fallback message when AxiosError response has no message field", async () => {
    const axiosErr = makeAxiosError(400);
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await GET(makeRequest());

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao buscar faltas" },
      { status: 400 }
    );
  });

  it("returns empty array when API returns empty list", async () => {
    const mockApi = { get: vi.fn().mockResolvedValue({ data: [] }) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await GET(makeRequest());

    expect(mockNextResponseJson).toHaveBeenCalledWith([], { status: 200 });
  });
});

describe("POST /api/absence", () => {
  it("returns 201 with created absence data on success", async () => {
    const requestBody = { patientId: "p1", date: "2024-01-15", reason: "sick" };
    const createdAbsence = { id: "a1", ...requestBody };
    const mockApi = { post: vi.fn().mockResolvedValue({ data: createdAbsence }) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await POST(makeRequest(requestBody));

    expect(mockApi.post).toHaveBeenCalledWith("/absences", requestBody);
    expect(mockNextResponseJson).toHaveBeenCalledWith(createdAbsence, { status: 201 });
  });

  it("returns 500 with error message when AxiosError has no response", async () => {
    const axiosErr = new AxiosError("network error");
    const mockApi = { post: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await POST(makeRequest({ patientId: "p1" }));

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao criar falta" },
      { status: 500 }
    );
  });

  it("returns upstream status and message from AxiosError response", async () => {
    const axiosErr = makeAxiosError(422, "Validation failed");
    const mockApi = { post: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await POST(makeRequest({ patientId: "p1" }));

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Validation failed" },
      { status: 422 }
    );
  });

  it("returns 500 with generic message for non-Axios errors", async () => {
    const mockApi = { post: vi.fn().mockRejectedValue(new TypeError("bad")) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await POST(makeRequest({ patientId: "p1" }));

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });

  it("uses fallback message when AxiosError response has no message field", async () => {
    const axiosErr = makeAxiosError(409);
    const mockApi = { post: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await POST(makeRequest({ patientId: "p1" }));

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao criar falta" },
      { status: 409 }
    );
  });

  it("passes request body to the backend API correctly", async () => {
    const body = { patientId: "p2", date: "2024-02-20", justification: "medical" };
    const mockApi = { post: vi.fn().mockResolvedValue({ data: { id: "new" } }) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await POST(makeRequest(body));

    expect(mockApi.post).toHaveBeenCalledWith("/absences", body);
  });
});