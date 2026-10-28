import { describe, it, expect, vi, beforeEach } from "vitest";
import { NextRequest } from "next/server";
import { AxiosError, AxiosHeaders } from "axios";
import { GET } from "./route";
import { createDocumentsAPI } from "@/lib/axios";

vi.mock("@/lib/axios", () => ({
  createDocumentsAPI: vi.fn(),
}));

const PATIENT_ID = "40000000-0000-4000-8000-000000000001";

function buildDocumentsRequest(query = "") {
  const url = `http://localhost:3000/api/documents/${PATIENT_ID}${query ? `?${query}` : ""}`;
  return new NextRequest(url, { method: "GET" });
}

function buildContext(patientId = PATIENT_ID) {
  return { params: Promise.resolve({ patientId }) };
}

describe("GET /api/documents/[patientId]", () => {
  const getMock = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(createDocumentsAPI).mockResolvedValue({
      get: getMock,
    } as unknown as Awaited<ReturnType<typeof createDocumentsAPI>>);
  });

  it("retorna 400 quando year ou category estão ausentes", async () => {
    const missingYearRes = await GET(
      buildDocumentsRequest("category=PERSONAL"),
      buildContext(),
    );
    expect(missingYearRes.status).toBe(400);
    await expect(missingYearRes.json()).resolves.toEqual({
      error: "Os parâmetros 'year' e 'category' são obrigatórios e estão faltando",
    });

    const missingCategoryRes = await GET(
      buildDocumentsRequest("year=2026"),
      buildContext(),
    );
    expect(missingCategoryRes.status).toBe(400);
    await expect(missingCategoryRes.json()).resolves.toEqual({
      error: "Os parâmetros 'year' e 'category' são obrigatórios e estão faltando",
    });

    expect(createDocumentsAPI).not.toHaveBeenCalled();
  });

  it("retorna 400 quando a categoria é inválida", async () => {
    const res = await GET(
      buildDocumentsRequest("year=2026&category=FINANCEIRO"),
      buildContext(),
    );

    expect(res.status).toBe(400);
    await expect(res.json()).resolves.toEqual({
      error: "Categoria de documento inválida",
    });
    expect(createDocumentsAPI).not.toHaveBeenCalled();
  });

  it("retorna 400 quando year não é numérico", async () => {
    const res = await GET(
      buildDocumentsRequest("year=abc&category=PESSOAL"),
      buildContext(),
    );

    expect(res.status).toBe(400);
    await expect(res.json()).resolves.toEqual({
      error: "O parâmetro 'year' deve ser numérico",
    });
    expect(createDocumentsAPI).not.toHaveBeenCalled();
  });

  it("retorna 200 traduzindo a categoria via categoryMap e filtrando os documentos por type", async () => {
    const apiDocuments = [
      { id: "doc-1", name: "RG.pdf", type: "RG" },
      { id: "doc-2", name: "CPF.pdf", type: "CPF" },
      { id: "doc-3", name: "RG-verso.pdf", type: "rg" },
    ];
    getMock.mockResolvedValue({ data: apiDocuments });

    const res = await GET(
      buildDocumentsRequest("year=2026&category=pessoais&type=rg"),
      buildContext(),
    );
    const data = await res.json();

    expect(res.status).toBe(200);
    expect(getMock).toHaveBeenCalledWith(
      `/patients/${PATIENT_ID}/documents/personals`,
      { params: { year: 2026 } },
    );
    expect(data).toEqual([
      { id: "doc-1", name: "RG.pdf", type: "RG" },
      { id: "doc-3", name: "RG-verso.pdf", type: "rg" },
    ]);
  });

  it("retorna 200 com lista vazia ([]) quando a API retorna 404", async () => {
    const axiosError = new AxiosError(
      "Not Found",
      "ERR_BAD_REQUEST",
      undefined,
      undefined,
      {
        status: 404,
        statusText: "Not Found",
        headers: {},
        config: { headers: new AxiosHeaders() },
        data: { message: "Nenhum documento encontrado" },
      },
    );
    getMock.mockRejectedValue(axiosError);

    const res = await GET(
      buildDocumentsRequest("year=2026&category=MEDICAL"),
      buildContext(),
    );
    const data = await res.json();

    expect(res.status).toBe(200);
    expect(getMock).toHaveBeenCalledWith(
      `/patients/${PATIENT_ID}/documents/medicals`,
      { params: { year: 2026 } },
    );
    expect(data).toEqual([]);
  });
});
