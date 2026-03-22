import { describe, it, expect, vi, beforeEach } from "vitest";
import { AxiosError } from "axios";

vi.mock("next/server", () => ({
  NextRequest: class MockNextRequest extends Request {},
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

import { NextRequest, NextResponse } from "next/server";
import { createBaseApi } from "@/lib/axios";
import { GET } from "../route";

const mockNextResponseJson = NextResponse.json as ReturnType<typeof vi.fn>;
const mockCreateBaseApi = createBaseApi as ReturnType<typeof vi.fn>;

describe("GET /api/appointments/today/[id]", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockNextResponseJson.mockImplementation((data: unknown, init?: ResponseInit) => ({
      data,
      status: init?.status ?? 200,
    }));
  });

  it("returns 200 with today's appointment data on success", async () => {
    const todayAppointment = { id: "appt-1", date: "2024-06-01", patientId: "p1" };
    const mockApi = { get: vi.fn().mockResolvedValue({ data: todayAppointment }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new NextRequest("http://localhost/api/appointments/today/p1");
    await GET(req, { params: { id: "p1" } });

    expect(mockCreateBaseApi).toHaveBeenCalledOnce();
    expect(mockApi.get).toHaveBeenCalledWith("/appointments/today/p1");
    expect(mockNextResponseJson).toHaveBeenCalledWith(todayAppointment, { status: 200 });
  });

  it("uses the correct patient id from params in the backend request", async () => {
    const mockApi = { get: vi.fn().mockResolvedValue({ data: {} }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new NextRequest("http://localhost/api/appointments/today/patient-xyz");
    await GET(req, { params: { id: "patient-xyz" } });

    expect(mockApi.get).toHaveBeenCalledWith("/appointments/today/patient-xyz");
  });

  it("returns 500 with error message when backend call fails with AxiosError", async () => {
    const axiosErr = new AxiosError("connection refused");
    const mockApi = { get: vi.fn().mockRejectedValue(axiosErr) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new NextRequest("http://localhost/api/appointments/today/p1");
    await GET(req, { params: { id: "p1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { error: "Internal server error" },
      { status: 500 }
    );
  });

  it("returns 500 with error message for non-Axios errors", async () => {
    const mockApi = { get: vi.fn().mockRejectedValue(new Error("unexpected failure")) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new NextRequest("http://localhost/api/appointments/today/p1");
    await GET(req, { params: { id: "p1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { error: "Internal server error" },
      { status: 500 }
    );
  });

  it("returns null data when backend returns null", async () => {
    const mockApi = { get: vi.fn().mockResolvedValue({ data: null }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new NextRequest("http://localhost/api/appointments/today/p1");
    await GET(req, { params: { id: "p1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(null, { status: 200 });
  });

  it("returns an array of appointments when backend returns multiple entries", async () => {
    const appointments = [
      { id: "a1", time: "09:00" },
      { id: "a2", time: "11:00" },
    ];
    const mockApi = { get: vi.fn().mockResolvedValue({ data: appointments }) };
    mockCreateBaseApi.mockResolvedValue(mockApi);

    const req = new NextRequest("http://localhost/api/appointments/today/p1");
    await GET(req, { params: { id: "p1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(appointments, { status: 200 });
  });

  it("returns 500 when createBaseApi itself throws", async () => {
    mockCreateBaseApi.mockRejectedValue(new Error("api init failed"));

    const req = new NextRequest("http://localhost/api/appointments/today/p1");
    await GET(req, { params: { id: "p1" } });

    expect(mockNextResponseJson).toHaveBeenCalledWith(
      { error: "Internal server error" },
      { status: 500 }
    );
  });
});