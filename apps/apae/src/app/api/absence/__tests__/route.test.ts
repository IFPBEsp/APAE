import { describe, it, expect, vi, beforeEach } from "vitest";
import { AxiosError } from "axios";

// Mock next/server
vi.mock("next/server", () => ({
  NextResponse: {
    json: vi.fn((data: unknown, init?: ResponseInit) => ({
      data,
      status: init?.status ?? 200,
    })),
  },
}));

// Mock @/lib/axios
vi.mock("@/lib/axios", () => ({
  createBaseApi: vi.fn(),
}));

import { NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { GET, POST } from "../route";

const mockNextResponseJson = NextResponse.json as ReturnType<typeof vi.fn>;
const mockCreateBaseApi = createBaseApi as ReturnType<typeof vi.fn>;

function makeAxiosError(status: number, message?: string): AxiosError {
  const err = new AxiosError("request failed");
  err.response = {
    status,
    data: message ? { message } : {},
    headers: {},
    config: {} as never,
    statusText: String(status),
  };
  return err;
}

describe("GET /api/absence", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockNextResponseJson.mockImplementation((data: unknown, init?: ResponseInit) => ({
      data,
      status: init?.status ?? 200,
    }));
  });

  it("returns 200 with absences data on success", async () => {
    const absences = [{ id: "1", absenceDate: "2024-01-15" }];
    const mockApi = { get: vi.fn().mockResolvedValue({ data: absences }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/absence");
    await GET(req);

    expect(mockCreateBaseApi).toHaveBeenCalledOnce();
    expect(mockApi.get).toHaveBeenCalledWith("/absences");
    expect(mockNextResponseJson).toHaveBeenCalledWith(absences, { status: 200 });
  });

  it("returns AxiosError status and message when backend returns an error", async () => {
    const axiosErr = makeAxiosError(422, "Dados inválidos");
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/absence");
    await GET(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Dados inválidos" },
      { status: 422 }
    );
  });

  it("falls back to default message when AxiosError has no response message", async () => {
    const axiosErr = makeAxiosError(503);
    axiosErr.response!.data = {};
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/absence");
    await GET(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao buscar faltas" },
      { status: 503 }
    );
  });

  it("defaults to status 500 when AxiosError has no response", async () => {
    const axiosErr = new AxiosError("network error");
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/absence");
    await GET(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao buscar faltas" },
      { status: 500 }
    );
  });

  it("returns 500 with generic message for non-Axios errors", async () => {
    const mockApi = { get: vi.fn().mockRejectedValue(new Error("unexpected")) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/absence");
    await GET(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });

  it("returns empty array when backend returns empty data", async () => {
    const mockApi = { get: vi.fn().mockResolvedValue({ data: [] }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/absence");
    await GET(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith([], { status: 200 });
  });
});

describe("POST /api/absence", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockNextResponseJson.mockImplementation((data: unknown, init?: ResponseInit) => ({
      data,
      status: init?.status ?? 200,
    }));
  });

  it("returns 201 with created absence on success", async () => {
    const requestBody = { patientId: "abc", absenceDate: "2024-01-15" };
    const created = { id: "new-id", ...requestBody };
    const mockApi = { post: vi.fn().mockResolvedValue({ data: created }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/absence", {
      method: "POST",
      body: JSON.stringify(requestBody),
    });
    await POST(req);

    expect(mockApi.post).toHaveBeenCalledWith("/absences", requestBody);
    expect(mockNextResponseJson).toHaveBeenCalledWith(created, { status: 201 });
  });

  it("returns AxiosError status and message on backend error", async () => {
    const axiosErr = makeAxiosError(400, "Campo ausente");
    const mockApi = { post: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/absence", {
      method: "POST",
      body: JSON.stringify({ patientId: "abc" }),
    });
    await POST(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Campo ausente" },
      { status: 400 }
    );
  });

  it("falls back to default message when AxiosError has no response message", async () => {
    const axiosErr = makeAxiosError(500);
    axiosErr.response!.data = {};
    const mockApi = { post: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/absence", {
      method: "POST",
      body: JSON.stringify({}),
    });
    await POST(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao criar falta" },
      { status: 500 }
    );
  });

  it("defaults to status 500 when AxiosError has no response", async () => {
    const axiosErr = new AxiosError("network error");
    const mockApi = { post: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/absence", {
      method: "POST",
      body: JSON.stringify({}),
    });
    await POST(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao criar falta" },
      { status: 500 }
    );
  });

  it("returns 500 with generic message for non-Axios errors", async () => {
    const mockApi = { post: vi.fn().mockRejectedValue(new TypeError("oops")) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/absence", {
      method: "POST",
      body: JSON.stringify({}),
    });
    await POST(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });

  it("forwards the request body to the backend API unchanged", async () => {
    const requestBody = { patientId: "p1", absenceDate: "2024-06-01", justification: "sick" };
    const mockApi = { post: vi.fn().mockResolvedValue({ data: { id: "x" } }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/absence", {
      method: "POST",
      body: JSON.stringify(requestBody),
    });
    await POST(req);

    expect(mockApi.post).toHaveBeenCalledWith("/absences", requestBody);
  });
});