import { describe, it, expect, vi, beforeEach } from "vitest";
import { AxiosError } from "axios";

// Mock next/server NextResponse
vi.mock("next/server", () => ({
  NextResponse: {
    json: vi.fn((data: unknown, init?: { status?: number }) => ({
      data,
      status: init?.status ?? 200,
    })),
  },
}));

// Mock @/lib/axios
const mockGet = vi.fn();
const mockPatch = vi.fn();
const mockPut = vi.fn();
const mockDelete = vi.fn();

vi.mock("@/lib/axios", () => ({
  createBaseApi: vi.fn(() =>
    Promise.resolve({
      get: mockGet,
      patch: mockPatch,
      put: mockPut,
      delete: mockDelete,
    })
  ),
}));

import { GET, PATCH, PUT, DELETE } from "@/app/api/appointments/[id]/route";
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

// Helper to build the params argument for routes using Promise-based params
function makeParams(id: string) {
  return { params: Promise.resolve({ id }) };
}

// Helper for sync params (PATCH/PUT/DELETE use non-Promise params)
function makeSyncParams(id: string) {
  return { params: { id } };
}

describe("GET /api/appointments/[id]", () => {
  beforeEach(() => vi.clearAllMocks());

  it("returns 200 with appointment data on success", async () => {
    const appointment = { id: "abc", date: "2024-01-10" };
    mockGet.mockResolvedValueOnce({ data: appointment });

    const req = new Request("http://localhost/api/appointments/abc");
    await GET(req, makeParams("abc"));

    expect(NextResponse.json).toHaveBeenCalledWith(appointment, { status: 200 });
  });

  it("calls api.get with correct endpoint", async () => {
    mockGet.mockResolvedValueOnce({ data: {} });

    const req = new Request("http://localhost/api/appointments/123");
    await GET(req, makeParams("123"));

    expect(mockGet).toHaveBeenCalledWith("/appointments/123");
  });

  it("returns 404 when AxiosError has status 404", async () => {
    const axiosError = makeAxiosError(404);
    mockGet.mockRejectedValueOnce(axiosError);

    const req = new Request("http://localhost/api/appointments/notfound");
    await GET(req, makeParams("notfound"));

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Agendamento não encontrado" },
      { status: 404 }
    );
  });

  it("returns the error status when AxiosError has non-404 status", async () => {
    const axiosError = makeAxiosError(503);
    mockGet.mockRejectedValueOnce(axiosError);

    const req = new Request("http://localhost/api/appointments/abc");
    await GET(req, makeParams("abc"));

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Erro ao buscar agendamento" },
      { status: 503 }
    );
  });

  it("returns 500 on non-Axios error", async () => {
    mockGet.mockRejectedValueOnce(new Error("network failure"));

    const req = new Request("http://localhost/api/appointments/abc");
    await GET(req, makeParams("abc"));

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });
});

describe("PATCH /api/appointments/[id]", () => {
  beforeEach(() => vi.clearAllMocks());

  it("returns 200 with updated data on success", async () => {
    const updated = { id: "abc", status: "cancelled" };
    mockPatch.mockResolvedValueOnce({ data: updated });

    const req = new Request("http://localhost/api/appointments/abc", {
      method: "PATCH",
      body: JSON.stringify({ status: "cancelled" }),
      headers: { "Content-Type": "application/json" },
    });
    await PATCH(req, makeSyncParams("abc"));

    expect(NextResponse.json).toHaveBeenCalledWith(updated, { status: 200 });
  });

  it("calls api.patch with the correct endpoint and body", async () => {
    const body = { status: "completed" };
    mockPatch.mockResolvedValueOnce({ data: {} });

    const req = new Request("http://localhost/api/appointments/42", {
      method: "PATCH",
      body: JSON.stringify(body),
      headers: { "Content-Type": "application/json" },
    });
    await PATCH(req, makeSyncParams("42"));

    expect(mockPatch).toHaveBeenCalledWith("/appointments/42", body);
  });

  it("returns 404 on AxiosError with status 404", async () => {
    const axiosError = makeAxiosError(404);
    mockPatch.mockRejectedValueOnce(axiosError);

    const req = new Request("http://localhost/api/appointments/missing", {
      method: "PATCH",
      body: JSON.stringify({}),
      headers: { "Content-Type": "application/json" },
    });
    await PATCH(req, makeSyncParams("missing"));

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Agendamento não encontrado" },
      { status: 404 }
    );
  });

  it("returns the Axios error status for other errors", async () => {
    const axiosError = makeAxiosError(422);
    mockPatch.mockRejectedValueOnce(axiosError);

    const req = new Request("http://localhost/api/appointments/abc", {
      method: "PATCH",
      body: JSON.stringify({}),
      headers: { "Content-Type": "application/json" },
    });
    await PATCH(req, makeSyncParams("abc"));

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Erro ao editar agendamento" },
      { status: 422 }
    );
  });

  it("returns 500 on non-Axios error", async () => {
    mockPatch.mockRejectedValueOnce(new RangeError("unexpected"));

    const req = new Request("http://localhost/api/appointments/abc", {
      method: "PATCH",
      body: JSON.stringify({}),
      headers: { "Content-Type": "application/json" },
    });
    await PATCH(req, makeSyncParams("abc"));

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });
});

describe("PUT /api/appointments/[id]", () => {
  beforeEach(() => vi.clearAllMocks());

  it("returns 200 with updated data on success", async () => {
    const updated = { id: "abc", date: "2024-05-01" };
    mockPut.mockResolvedValueOnce({ data: updated });

    const req = new Request("http://localhost/api/appointments/abc", {
      method: "PUT",
      body: JSON.stringify({ date: "2024-05-01" }),
      headers: { "Content-Type": "application/json" },
    });
    await PUT(req, makeSyncParams("abc"));

    expect(NextResponse.json).toHaveBeenCalledWith(updated, { status: 200 });
  });

  it("calls api.put with the correct endpoint and body", async () => {
    const body = { date: "2024-06-15", professionalId: "p1" };
    mockPut.mockResolvedValueOnce({ data: {} });

    const req = new Request("http://localhost/api/appointments/99", {
      method: "PUT",
      body: JSON.stringify(body),
      headers: { "Content-Type": "application/json" },
    });
    await PUT(req, makeSyncParams("99"));

    expect(mockPut).toHaveBeenCalledWith("/appointments/99", body);
  });

  it("returns Axios error message when response has message field", async () => {
    const axiosError = makeAxiosError(400, "Campo obrigatório ausente");
    mockPut.mockRejectedValueOnce(axiosError);

    const req = new Request("http://localhost/api/appointments/abc", {
      method: "PUT",
      body: JSON.stringify({}),
      headers: { "Content-Type": "application/json" },
    });
    await PUT(req, makeSyncParams("abc"));

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Campo obrigatório ausente" },
      { status: 400 }
    );
  });

  it("returns fallback message when AxiosError response has no message", async () => {
    const axiosError = makeAxiosError(503);
    mockPut.mockRejectedValueOnce(axiosError);

    const req = new Request("http://localhost/api/appointments/abc", {
      method: "PUT",
      body: JSON.stringify({}),
      headers: { "Content-Type": "application/json" },
    });
    await PUT(req, makeSyncParams("abc"));

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Erro ao atualizar agendamento" },
      { status: 503 }
    );
  });

  it("returns 500 on non-Axios error", async () => {
    mockPut.mockRejectedValueOnce(new Error("timeout"));

    const req = new Request("http://localhost/api/appointments/abc", {
      method: "PUT",
      body: JSON.stringify({}),
      headers: { "Content-Type": "application/json" },
    });
    await PUT(req, makeSyncParams("abc"));

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });
});

describe("DELETE /api/appointments/[id]", () => {
  beforeEach(() => vi.clearAllMocks());

  it("returns 200 with success message when deletion succeeds", async () => {
    mockDelete.mockResolvedValueOnce({});

    const req = new Request("http://localhost/api/appointments/abc", {
      method: "DELETE",
    });
    await DELETE(req, makeSyncParams("abc"));

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Agendamento excluído com sucesso" },
      { status: 200 }
    );
  });

  it("calls api.delete with the correct endpoint", async () => {
    mockDelete.mockResolvedValueOnce({});

    const req = new Request("http://localhost/api/appointments/xyz", {
      method: "DELETE",
    });
    await DELETE(req, makeSyncParams("xyz"));

    expect(mockDelete).toHaveBeenCalledWith("/appointments/xyz");
  });

  it("returns 404 when AxiosError has status 404", async () => {
    const axiosError = makeAxiosError(404);
    mockDelete.mockRejectedValueOnce(axiosError);

    const req = new Request("http://localhost/api/appointments/missing", {
      method: "DELETE",
    });
    await DELETE(req, makeSyncParams("missing"));

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Agendamento não encontrado" },
      { status: 404 }
    );
  });

  it("returns error status when AxiosError has non-404 status", async () => {
    const axiosError = makeAxiosError(500);
    mockDelete.mockRejectedValueOnce(axiosError);

    const req = new Request("http://localhost/api/appointments/abc", {
      method: "DELETE",
    });
    await DELETE(req, makeSyncParams("abc"));

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Erro ao excluir agendamento" },
      { status: 500 }
    );
  });

  it("returns 500 on non-Axios error", async () => {
    mockDelete.mockRejectedValueOnce(new TypeError("connection reset"));

    const req = new Request("http://localhost/api/appointments/abc", {
      method: "DELETE",
    });
    await DELETE(req, makeSyncParams("abc"));

    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });

  it("returns fallback 500 when AxiosError has no response status", async () => {
    // AxiosError without response (network-level error)
    const axiosError = new AxiosError("network error");
    mockDelete.mockRejectedValueOnce(axiosError);

    const req = new Request("http://localhost/api/appointments/abc", {
      method: "DELETE",
    });
    await DELETE(req, makeSyncParams("abc"));

    // No response.status → falls to || 500
    expect(NextResponse.json).toHaveBeenCalledWith(
      { message: "Erro ao excluir agendamento" },
      { status: 500 }
    );
  });
});