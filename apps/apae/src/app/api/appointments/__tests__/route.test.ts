import { describe, it, expect, vi, beforeEach } from "vitest";
import { AxiosError } from "axios";

vi.mock("next/server", () => ({
  NextResponse: {
    json: vi.fn((data: unknown, init?: ResponseInit) => ({
      data,
      status: init?.status ?? 200,
    })),
  },
}));

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

describe("GET /api/appointments", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockNextResponseJson.mockImplementation((data: unknown, init?: ResponseInit) => ({
      data,
      status: init?.status ?? 200,
    }));
  });

  it("returns 200 with appointments data on success", async () => {
    const appointments = { content: [{ id: "1" }], totalElements: 1 };
    const mockApi = { get: vi.fn().mockResolvedValue({ data: appointments }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments");
    await GET(req);

    expect(mockCreateBaseApi).toHaveBeenCalledOnce();
    expect(mockApi.get).toHaveBeenCalledWith("/appointments", { params: { date: null } });
    expect(mockNextResponseJson).toHaveBeenCalledWith(appointments, { status: 200 });
  });

  it("passes date query param to the backend when provided", async () => {
    const mockApi = { get: vi.fn().mockResolvedValue({ data: [] }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments?date=2024-06-01");
    await GET(req);

    expect(mockApi.get).toHaveBeenCalledWith("/appointments", { params: { date: "2024-06-01" } });
  });

  it("passes null date when not provided in query string", async () => {
    const mockApi = { get: vi.fn().mockResolvedValue({ data: [] }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments");
    await GET(req);

    expect(mockApi.get).toHaveBeenCalledWith("/appointments", { params: { date: null } });
  });

  it("returns AxiosError status and message on backend error", async () => {
    const axiosErr = makeAxiosError(403, "Acesso negado");
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments");
    await GET(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Acesso negado" },
      { status: 403 }
    );
  });

  it("falls back to default message when AxiosError response has no message", async () => {
    const axiosErr = makeAxiosError(500);
    axiosErr.response!.data = {};
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments");
    await GET(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao buscar agendamentos" },
      { status: 500 }
    );
  });

  it("defaults to status 500 when AxiosError has no response object", async () => {
    const axiosErr = new AxiosError("network error");
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments");
    await GET(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao buscar agendamentos" },
      { status: 500 }
    );
  });

  it("returns 500 with generic message for non-Axios errors", async () => {
    const mockApi = { get: vi.fn().mockRejectedValue(new Error("db error")) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments");
    await GET(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });
});

describe("POST /api/appointments", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockNextResponseJson.mockImplementation((data: unknown, init?: ResponseInit) => ({
      data,
      status: init?.status ?? 200,
    }));
  });

  it("returns 201 with created appointment on success", async () => {
    const requestBody = { patientId: "p1", professionalId: "pr1", date: "2024-06-01" };
    const created = { id: "appt-1", ...requestBody };
    const mockApi = { post: vi.fn().mockResolvedValue({ data: created }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments", {
      method: "POST",
      body: JSON.stringify(requestBody),
    });
    await POST(req);

    expect(mockApi.post).toHaveBeenCalledWith("/appointments", requestBody);
    expect(mockNextResponseJson).toHaveBeenCalledWith(created, { status: 201 });
  });

  it("returns AxiosError status and message on backend error", async () => {
    const axiosErr = makeAxiosError(409, "Conflito de horário");
    const mockApi = { post: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments", {
      method: "POST",
      body: JSON.stringify({}),
    });
    await POST(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Conflito de horário" },
      { status: 409 }
    );
  });

  it("falls back to default message when AxiosError has no response message", async () => {
    const axiosErr = makeAxiosError(500);
    axiosErr.response!.data = {};
    const mockApi = { post: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments", {
      method: "POST",
      body: JSON.stringify({}),
    });
    await POST(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao criar agendamento" },
      { status: 500 }
    );
  });

  it("defaults to status 500 when AxiosError has no response object", async () => {
    const axiosErr = new AxiosError("network error");
    const mockApi = { post: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments", {
      method: "POST",
      body: JSON.stringify({}),
    });
    await POST(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao criar agendamento" },
      { status: 500 }
    );
  });

  it("returns 500 with generic message for non-Axios errors", async () => {
    const mockApi = { post: vi.fn().mockRejectedValue(new RangeError("unexpected")) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments", {
      method: "POST",
      body: JSON.stringify({}),
    });
    await POST(req);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });

  it("forwards the full request body to the backend API", async () => {
    const requestBody = {
      patientId: "p1",
      professionalId: "pr2",
      date: "2024-07-10",
      frequencyDays: 7,
    };
    const mockApi = { post: vi.fn().mockResolvedValue({ data: { id: "new" } }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments", {
      method: "POST",
      body: JSON.stringify(requestBody),
    });
    await POST(req);

    expect(mockApi.post).toHaveBeenCalledWith("/appointments", requestBody);
  });
});