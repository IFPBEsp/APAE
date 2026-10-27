import { describe, it, expect, vi, beforeEach } from "vitest";
import { AxiosError, AxiosHeaders } from "axios";
import { POST } from "./route";
import { createBaseApi } from "@/lib/axios";
import { setSessionCookie } from "@/lib/cookies";

vi.mock("@/lib/axios", () => ({
  createBaseApi: vi.fn(),
}));

vi.mock("@/lib/cookies", () => ({
  setSessionCookie: vi.fn(),
}));

function buildLoginRequest(body: Record<string, unknown>) {
  return new Request("http://localhost:3000/api/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
}

describe("POST /api/auth/login", () => {
  const postMock = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(createBaseApi).mockResolvedValue({
      post: postMock,
    } as unknown as Awaited<ReturnType<typeof createBaseApi>>);
  });

  it("mantém DISABLE_AUTH desativado (false) exigindo credenciais reais e sem usar fake-token-dev", async () => {
    const req = buildLoginRequest({});

    const res = await POST(req);
    const data = await res.json();

    expect(res.status).toBe(406);
    expect(data.message).not.toBe("Login ignorado (DEV MODE)");
    expect(setSessionCookie).not.toHaveBeenCalledWith({
      accessToken: "fake-token-dev",
    });
  });

  it("retorna 406 quando usuário ou senha estão ausentes", async () => {
    const missingPasswordRes = await POST(
      buildLoginRequest({ username: "admin@teste.local" }),
    );
    expect(missingPasswordRes.status).toBe(406);
    await expect(missingPasswordRes.json()).resolves.toEqual({
      message: "Todos os campos são obrigatórios",
    });

    const missingUsernameRes = await POST(
      buildLoginRequest({ password: "123456" }),
    );
    expect(missingUsernameRes.status).toBe(406);
    await expect(missingUsernameRes.json()).resolves.toEqual({
      message: "Todos os campos são obrigatórios",
    });

    expect(createBaseApi).not.toHaveBeenCalled();
    expect(setSessionCookie).not.toHaveBeenCalled();
  });

  it("retorna 200 e define o cookie de sessão quando o login tem sucesso", async () => {
    const exp = 1893456000;
    const header = btoa(JSON.stringify({ alg: "HS256", typ: "JWT" }));
    const payload = btoa(JSON.stringify({ sub: "admin@teste.local", exp }));
    const fakeJwt = `${header}.${payload}.signature`;

    postMock.mockResolvedValue({
      status: 200,
      data: { token: fakeJwt },
    });

    const req = buildLoginRequest({
      username: "admin@teste.local",
      password: "123456",
    });

    const res = await POST(req);
    const data = await res.json();

    expect(res.status).toBe(200);
    expect(data).toEqual({ message: "Login bem-sucedido" });
    expect(postMock).toHaveBeenCalledWith("/auth/signin", {
      username: "admin@teste.local",
      password: "123456",
    });
    expect(setSessionCookie).toHaveBeenCalledWith(
      { accessToken: fakeJwt },
      exp,
    );
  });

  it("retorna 401 com mensagem 'Credenciais inválidas' quando a API retorna 401", async () => {
    const axiosError = new AxiosError(
      "Unauthorized",
      "ERR_BAD_REQUEST",
      undefined,
      undefined,
      {
        status: 401,
        statusText: "Unauthorized",
        headers: {},
        config: { headers: new AxiosHeaders() },
        data: { message: "Bad credentials" },
      },
    );
    postMock.mockRejectedValue(axiosError);

    const req = buildLoginRequest({
      username: "admin@teste.local",
      password: "wrong-password",
    });

    const res = await POST(req);
    const data = await res.json();

    expect(res.status).toBe(401);
    expect(data).toEqual({ message: "Credenciais inválidas" });
    expect(setSessionCookie).not.toHaveBeenCalled();
  });

  it("repassa o status e a mensagem da API em caso de erro genérico", async () => {
    const axiosError = new AxiosError(
      "Service Unavailable",
      "ERR_BAD_RESPONSE",
      undefined,
      undefined,
      {
        status: 503,
        statusText: "Service Unavailable",
        headers: {},
        config: { headers: new AxiosHeaders() },
        data: { message: "Serviço temporariamente indisponível" },
      },
    );
    postMock.mockRejectedValue(axiosError);

    const req = buildLoginRequest({
      username: "admin@teste.local",
      password: "123456",
    });

    const res = await POST(req);
    const data = await res.json();

    expect(res.status).toBe(503);
    expect(data).toEqual({
      message: "Serviço temporariamente indisponível",
    });
    expect(setSessionCookie).not.toHaveBeenCalled();
  });
});
