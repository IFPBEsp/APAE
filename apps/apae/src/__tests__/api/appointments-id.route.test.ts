import { describe, it, expect, vi, beforeEach } from "vitest";
import { AxiosError } from "axios";

vi.mock("next/server", () => ({
  NextResponse: {
    json: vi.fn((body: unknown, init?: { status?: number }) => ({
      body,
      status: init?.status ?? 200,
    })),
  },
}));

vi.mock("@/lib/axios", () => ({
  createBaseApi: vi.fn(),
}));

import { GET, PATCH, PUT, DELETE } from "@/app/api/appointments/[id]/route";
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

function makeParams(id: string) {
  return { params: Promise.resolve({ id }) };
}

function makeSyncParams(id: string) {
  return { params: { id } };
}

function makeRequest(body?: unknown): Request {
  return {
    json: vi.fn().mockResolvedValue(body ?? {}),
  } as unknown as Request;
}

beforeEach(() => {
  vi.clearAllMocks();
});

// ─── GET ──────────────────────────────────────────────────────────────────────

describe("GET /api/appointments/[id]", () => {
  it("returns 200 with appointment data on success", async () => {
    const appointment = { id: "abc", date: "2024-03-01" };
    const mockApi = { get: vi.fn().mockResolvedValue({ data: appointment }) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await GET(makeRequest(), makeParams("abc") as never);

    expect(mockApi.get).toHaveBeenCalledWith("/appointments/abc");
    expect(mockNextResponseJson).toHaveBeenCalledWith(appointment, { status: 200 });
  });

  it("returns 404 when appointment is not found", async () => {
    const axiosErr = makeAxiosError(404);
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await GET(makeRequest(), makeParams("missing-id") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Agendamento não encontrado" },
      { status: 404 }
    );
  });

  it("returns upstream status for non-404 AxiosError", async () => {
    const axiosErr = makeAxiosError(503);
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await GET(makeRequest(), makeParams("id1") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao buscar agendamento" },
      { status: 503 }
    );
  });

  it("returns 500 when AxiosError has no response", async () => {
    const axiosErr = new AxiosError("network");
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await GET(makeRequest(), makeParams("id1") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao buscar agendamento" },
      { status: 500 }
    );
  });

  it("returns 500 with generic message for non-Axios errors", async () => {
    const mockApi = { get: vi.fn().mockRejectedValue(new Error("unexpected")) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await GET(makeRequest(), makeParams("id1") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });
});

// ─── PATCH ────────────────────────────────────────────────────────────────────

describe("PATCH /api/appointments/[id]", () => {
  it("returns 200 with updated appointment on success", async () => {
    const body = { status: "cancelled" };
    const updated = { id: "abc", status: "cancelled" };
    const mockApi = { patch: vi.fn().mockResolvedValue({ data: updated }) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await PATCH(makeRequest(body), makeSyncParams("abc") as never);

    expect(mockApi.patch).toHaveBeenCalledWith("/appointments/abc", body);
    expect(mockNextResponseJson).toHaveBeenCalledWith(updated, { status: 200 });
  });

  it("returns 404 when appointment not found during patch", async () => {
    const axiosErr = makeAxiosError(404);
    const mockApi = { patch: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await PATCH(makeRequest({ status: "x" }), makeSyncParams("missing") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Agendamento não encontrado" },
      { status: 404 }
    );
  });

  it("returns upstream status for non-404 AxiosError", async () => {
    const axiosErr = makeAxiosError(400);
    const mockApi = { patch: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await PATCH(makeRequest({}), makeSyncParams("id1") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao editar agendamento" },
      { status: 400 }
    );
  });

  it("falls back to 500 when AxiosError has no response status", async () => {
    const axiosErr = new AxiosError("network");
    const mockApi = { patch: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await PATCH(makeRequest({}), makeSyncParams("id1") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao editar agendamento" },
      { status: 500 }
    );
  });

  it("returns 500 for non-Axios errors during patch", async () => {
    const mockApi = { patch: vi.fn().mockRejectedValue(new Error("boom")) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await PATCH(makeRequest({}), makeSyncParams("id1") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });
});

// ─── PUT ──────────────────────────────────────────────────────────────────────

describe("PUT /api/appointments/[id]", () => {
  it("returns 200 with updated appointment on success", async () => {
    const body = { date: "2024-05-01", professional: "Dr. X" };
    const updated = { id: "abc", ...body };
    const mockApi = { put: vi.fn().mockResolvedValue({ data: updated }) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await PUT(makeRequest(body), makeSyncParams("abc") as never);

    expect(mockApi.put).toHaveBeenCalledWith("/appointments/abc", body);
    expect(mockNextResponseJson).toHaveBeenCalledWith(updated, { status: 200 });
  });

  it("returns upstream status and message from AxiosError response", async () => {
    const axiosErr = makeAxiosError(422, "Invalid date range");
    const mockApi = { put: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await PUT(makeRequest({}), makeSyncParams("id1") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Invalid date range" },
      { status: 422 }
    );
  });

  it("uses fallback message when AxiosError response has no message", async () => {
    const axiosErr = makeAxiosError(500);
    const mockApi = { put: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await PUT(makeRequest({}), makeSyncParams("id1") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao atualizar agendamento" },
      { status: 500 }
    );
  });

  it("returns 500 for non-Axios errors during put", async () => {
    const mockApi = { put: vi.fn().mockRejectedValue(new RangeError("bad")) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await PUT(makeRequest({}), makeSyncParams("id1") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });

  it("falls back to 500 when AxiosError has no response", async () => {
    const axiosErr = new AxiosError("timeout");
    const mockApi = { put: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await PUT(makeRequest({}), makeSyncParams("id1") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao atualizar agendamento" },
      { status: 500 }
    );
  });
});

// ─── DELETE ───────────────────────────────────────────────────────────────────

describe("DELETE /api/appointments/[id]", () => {
  it("returns 200 with success message on successful deletion", async () => {
    const mockApi = { delete: vi.fn().mockResolvedValue({}) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await DELETE(makeRequest(), makeSyncParams("abc") as never);

    expect(mockApi.delete).toHaveBeenCalledWith("/appointments/abc");
    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Agendamento excluído com sucesso" },
      { status: 200 }
    );
  });

  it("returns 404 when appointment not found during delete", async () => {
    const axiosErr = makeAxiosError(404);
    const mockApi = { delete: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await DELETE(makeRequest(), makeSyncParams("missing") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Agendamento não encontrado" },
      { status: 404 }
    );
  });

  it("returns upstream status for non-404 AxiosError during delete", async () => {
    const axiosErr = makeAxiosError(403);
    const mockApi = { delete: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await DELETE(makeRequest(), makeSyncParams("id1") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao excluir agendamento" },
      { status: 403 }
    );
  });

  it("falls back to 500 when AxiosError has no response status", async () => {
    const axiosErr = new AxiosError("connection reset");
    const mockApi = { delete: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await DELETE(makeRequest(), makeSyncParams("id1") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao excluir agendamento" },
      { status: 500 }
    );
  });

  it("returns 500 for non-Axios errors during delete", async () => {
    const mockApi = { delete: vi.fn().mockRejectedValue(new Error("unexpected")) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await DELETE(makeRequest(), makeSyncParams("id1") as never);

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });

  it("calls delete with the correct appointment id", async () => {
    const mockApi = { delete: vi.fn().mockResolvedValue({}) };
    mockCreateBaseApi.mockResolvedValue(mockApi as never);

    await DELETE(makeRequest(), makeSyncParams("specific-uuid-123") as never);

    expect(mockApi.delete).toHaveBeenCalledWith("/appointments/specific-uuid-123");
  });
});