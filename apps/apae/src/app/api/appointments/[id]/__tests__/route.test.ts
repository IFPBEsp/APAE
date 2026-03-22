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
import { GET, PUT, DELETE } from "../route";

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

describe("GET /api/appointments/[id]", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockNextResponseJson.mockImplementation((data: unknown, init?: ResponseInit) => ({
      data,
      status: init?.status ?? 200,
    }));
  });

  it("returns 200 with appointment data on success", async () => {
    const appointment = { id: "appt-1", patientId: "p1" };
    const mockApi = { get: vi.fn().mockResolvedValue({ data: appointment }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1");
    const params = Promise.resolve({ id: "appt-1" });
    await GET(req, { params });

    expect(mockCreateBaseApi).toHaveBeenCalledOnce();
    expect(mockApi.get).toHaveBeenCalledWith("/appointments/appt-1");
    expect(mockNextResponseJson).toHaveBeenCalledWith(appointment, { status: 200 });
  });

  it("returns 404 when appointment is not found", async () => {
    const axiosErr = makeAxiosError(404);
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/non-existent");
    const params = Promise.resolve({ id: "non-existent" });
    await GET(req, { params });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Agendamento não encontrado" },
      { status: 404 }
    );
  });

  it("returns AxiosError status for non-404 backend errors", async () => {
    const axiosErr = makeAxiosError(500);
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1");
    const params = Promise.resolve({ id: "appt-1" });
    await GET(req, { params });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao buscar agendamento" },
      { status: 500 }
    );
  });

  it("defaults to 500 when AxiosError has no response object", async () => {
    const axiosErr = new AxiosError("network timeout");
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1");
    const params = Promise.resolve({ id: "appt-1" });
    await GET(req, { params });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao buscar agendamento" },
      { status: 500 }
    );
  });

  it("returns 500 with generic message for non-Axios errors", async () => {
    const mockApi = { get: vi.fn().mockRejectedValue(new Error("unexpected")) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1");
    const params = Promise.resolve({ id: "appt-1" });
    await GET(req, { params });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });

  it("uses the correct id from params when fetching", async () => {
    const mockApi = { get: vi.fn().mockResolvedValue({ data: {} }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/unique-id-123");
    const params = Promise.resolve({ id: "unique-id-123" });
    await GET(req, { params });

    expect(mockApi.get).toHaveBeenCalledWith("/appointments/unique-id-123");
  });
});

describe("PUT /api/appointments/[id]", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockNextResponseJson.mockImplementation((data: unknown, init?: ResponseInit) => ({
      data,
      status: init?.status ?? 200,
    }));
  });

  it("returns 200 with updated appointment on success", async () => {
    const updateBody = { date: "2024-08-01", frequencyDays: 14 };
    const updated = { id: "appt-1", ...updateBody };
    const mockApi = { put: vi.fn().mockResolvedValue({ data: updated }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1", {
      method: "PUT",
      body: JSON.stringify(updateBody),
    });
    await PUT(req, { params: { id: "appt-1" } });

    expect(mockApi.put).toHaveBeenCalledWith("/appointments/appt-1", updateBody);
    expect(mockNextResponseJson).toHaveBeenCalledWith(updated, { status: 200 });
  });

  it("returns AxiosError status and message on backend error", async () => {
    const axiosErr = makeAxiosError(400, "Dados inválidos para atualização");
    const mockApi = { put: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1", {
      method: "PUT",
      body: JSON.stringify({}),
    });
    await PUT(req, { params: { id: "appt-1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Dados inválidos para atualização" },
      { status: 400 }
    );
  });

  it("falls back to default message when AxiosError has no response message", async () => {
    const axiosErr = makeAxiosError(500);
    axiosErr.response!.data = {};
    const mockApi = { put: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1", {
      method: "PUT",
      body: JSON.stringify({}),
    });
    await PUT(req, { params: { id: "appt-1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao atualizar agendamento" },
      { status: 500 }
    );
  });

  it("defaults to 500 when AxiosError has no response object", async () => {
    const axiosErr = new AxiosError("network error");
    const mockApi = { put: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1", {
      method: "PUT",
      body: JSON.stringify({}),
    });
    await PUT(req, { params: { id: "appt-1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao atualizar agendamento" },
      { status: 500 }
    );
  });

  it("returns 500 with generic message for non-Axios errors", async () => {
    const mockApi = { put: vi.fn().mockRejectedValue(new Error("unexpected")) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1", {
      method: "PUT",
      body: JSON.stringify({}),
    });
    await PUT(req, { params: { id: "appt-1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });
});

describe("DELETE /api/appointments/[id]", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockNextResponseJson.mockImplementation((data: unknown, init?: ResponseInit) => ({
      data,
      status: init?.status ?? 200,
    }));
  });

  it("returns 200 with success message on successful deletion", async () => {
    const mockApi = { delete: vi.fn().mockResolvedValue({}) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1", { method: "DELETE" });
    await DELETE(req, { params: { id: "appt-1" } });

    expect(mockApi.delete).toHaveBeenCalledWith("/appointments/appt-1");
    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Agendamento excluído com sucesso" },
      { status: 200 }
    );
  });

  it("returns 404 when appointment is not found on delete", async () => {
    const axiosErr = makeAxiosError(404);
    const mockApi = { delete: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/ghost-id", { method: "DELETE" });
    await DELETE(req, { params: { id: "ghost-id" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Agendamento não encontrado" },
      { status: 404 }
    );
  });

  it("returns AxiosError status for non-404 backend errors on delete", async () => {
    const axiosErr = makeAxiosError(403);
    const mockApi = { delete: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1", { method: "DELETE" });
    await DELETE(req, { params: { id: "appt-1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao excluir agendamento" },
      { status: 403 }
    );
  });

  it("defaults to 500 when AxiosError has no response object on delete", async () => {
    const axiosErr = new AxiosError("network error");
    const mockApi = { delete: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1", { method: "DELETE" });
    await DELETE(req, { params: { id: "appt-1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao excluir agendamento" },
      { status: 500 }
    );
  });

  it("returns 500 with generic message for non-Axios errors on delete", async () => {
    const mockApi = { delete: vi.fn().mockRejectedValue(new Error("unexpected")) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1", { method: "DELETE" });
    await DELETE(req, { params: { id: "appt-1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });

  it("uses the correct id from params when deleting", async () => {
    const mockApi = { delete: vi.fn().mockResolvedValue({}) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/target-id", { method: "DELETE" });
    await DELETE(req, { params: { id: "target-id" } });

    expect(mockApi.delete).toHaveBeenCalledWith("/appointments/target-id");
  });
});