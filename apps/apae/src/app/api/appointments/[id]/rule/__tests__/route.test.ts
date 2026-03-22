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
import { PATCH } from "../route";

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

describe("PATCH /api/appointments/[id]/rule", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockNextResponseJson.mockImplementation((data: unknown, init?: ResponseInit) => ({
      data,
      status: init?.status ?? 200,
    }));
  });

  it("returns 200 with updated rule data on success", async () => {
    const ruleData = { frequencyDays: 7 };
    const mockApi = { patch: vi.fn().mockResolvedValue({ data: ruleData }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1/rule", {
      method: "PATCH",
      body: JSON.stringify({ frequencyDays: 7 }),
    });
    await PATCH(req, { params: { id: "appt-1" } });

    expect(mockCreateBaseApi).toHaveBeenCalledOnce();
    expect(mockApi.patch).toHaveBeenCalledWith("/appointments/appt-1/rule", expect.any(Object));
    expect(mockNextResponseJson).toHaveBeenCalledWith(ruleData, { status: 200 });
  });

  it("calls backend with the correct appointment id", async () => {
    const mockApi = { patch: vi.fn().mockResolvedValue({ data: {} }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/specific-id/rule", {
      method: "PATCH",
      body: "some body text",
    });
    await PATCH(req, { params: { id: "specific-id" } });

    expect(mockApi.patch).toHaveBeenCalledWith(
      "/appointments/specific-id/rule",
      expect.any(Object)
    );
  });

  it("passes text body from request to the backend", async () => {
    const bodyText = '{"frequencyDays":14}';
    const mockApi = { patch: vi.fn().mockResolvedValue({ data: {} }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1/rule", {
      method: "PATCH",
      body: bodyText,
    });
    await PATCH(req, { params: { id: "appt-1" } });

    expect(mockApi.patch).toHaveBeenCalledWith("/appointments/appt-1/rule", {
      method: "PATCH",
      body: bodyText,
    });
  });

  it("returns 404 when appointment is not found", async () => {
    const axiosErr = makeAxiosError(404);
    const mockApi = { patch: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/ghost-id/rule", {
      method: "PATCH",
      body: "",
    });
    await PATCH(req, { params: { id: "ghost-id" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Agendamento não encontrado" },
      { status: 404 }
    );
  });

  it("returns AxiosError status for non-404 backend errors", async () => {
    const axiosErr = makeAxiosError(422);
    const mockApi = { patch: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1/rule", {
      method: "PATCH",
      body: "",
    });
    await PATCH(req, { params: { id: "appt-1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao editar agendamento" },
      { status: 422 }
    );
  });

  it("defaults to 500 when AxiosError has no response object", async () => {
    const axiosErr = new AxiosError("network error");
    const mockApi = { patch: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1/rule", {
      method: "PATCH",
      body: "",
    });
    await PATCH(req, { params: { id: "appt-1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro ao editar agendamento" },
      { status: 500 }
    );
  });

  it("returns 500 with generic message for non-Axios errors", async () => {
    const mockApi = { patch: vi.fn().mockRejectedValue(new Error("unexpected")) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1/rule", {
      method: "PATCH",
      body: "",
    });
    await PATCH(req, { params: { id: "appt-1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { message: "Erro interno do servidor" },
      { status: 500 }
    );
  });

  it("returns empty object response when backend returns empty data", async () => {
    const mockApi = { patch: vi.fn().mockResolvedValue({ data: {} }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new Request("http://localhost/api/appointments/appt-1/rule", {
      method: "PATCH",
      body: "",
    });
    await PATCH(req, { params: { id: "appt-1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith({}, { status: 200 });
  });
});