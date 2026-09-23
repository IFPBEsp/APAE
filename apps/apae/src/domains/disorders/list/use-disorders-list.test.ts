import { describe, it, expect, vi, beforeEach } from "vitest";
import { renderHook, waitFor } from "@testing-library/react";
import { useDisordersList } from "./use-disorders-list";
import { fetchDisordersApi } from "../disorders.api";

vi.mock("../disorders.api");
vi.mock("react-toastify", () => ({
  toast: { success: vi.fn(), error: vi.fn() },
}));

const disordersMock = [
  { id: "1", name: "TEA", hasPatient: false },
  { id: "2", name: "TDAH", hasPatient: true },
];

describe("useDisordersList", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("carrega a lista de transtornos", async () => {
    vi.mocked(fetchDisordersApi).mockResolvedValue(disordersMock);

    const { result } = renderHook(() => useDisordersList());

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
    });
    expect(result.current.disorders).toEqual(disordersMock);
    expect(fetchDisordersApi).toHaveBeenCalledTimes(1);
  });

  it("trata o caso de lista vazia", async () => {
    vi.mocked(fetchDisordersApi).mockResolvedValue([]);

    const { result } = renderHook(() => useDisordersList());

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
    });
    expect(result.current.disorders).toEqual([]);
  });
});