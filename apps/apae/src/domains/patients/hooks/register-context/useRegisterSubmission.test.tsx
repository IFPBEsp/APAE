import { act, renderHook } from "@testing-library/react";
import { beforeEach, describe, expect, it, vi } from "vitest";
import { initialState } from "./reducer";
import type { MembersRegisterState } from "./types";
import { useRegisterSubmission } from "./useRegisterSubmission";
import { bffFetch } from "@/lib/bff-client";

vi.mock("@/lib/bff-client", () => ({
  bffFetch: vi.fn(),
}));

describe("useRegisterSubmission birth certificate upload", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
    vi.mocked(bffFetch).mockResolvedValue({
      ok: false,
      status: 400,
      json: async () => ({}),
    } as Response);
  });

  it("sends an attached birth certificate with the expected multipart field", async () => {
    const file = new File(["cópia"], "certidao.pdf", {
      type: "application/pdf",
    });
    const state: MembersRegisterState = {
      ...initialState,
      personal: {
        ...initialState.personal,
        birth: { ...initialState.personal.birth, file },
      },
    };
    const { result } = renderHook(() =>
      useRegisterSubmission(state, "patient-draft"),
    );

    await act(async () => {
      await result.current();
    });

    const requestBody = vi.mocked(bffFetch).mock.calls[0][1]?.body as FormData;
    expect(requestBody.get("birth_certificate")).toBe(file);
  });

  it("does not add the optional field when no file was selected", async () => {
    const { result } = renderHook(() =>
      useRegisterSubmission(initialState, "patient-draft"),
    );

    await act(async () => {
      await result.current();
    });

    const requestBody = vi.mocked(bffFetch).mock.calls[0][1]?.body as FormData;
    expect(requestBody.has("birth_certificate")).toBe(false);
  });
});
