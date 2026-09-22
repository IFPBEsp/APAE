import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { toast } from "react-toastify";
import { DisorderCreateForm } from "./disorder-form";
import { createDisorderApi } from "../disorders.api";

vi.mock("../disorders.api");
vi.mock("react-toastify", () => ({
  toast: { success: vi.fn(), error: vi.fn() },
}));

const push = vi.fn();
const back = vi.fn();
vi.mock("next/navigation", () => ({
  useRouter: () => ({ push, back }),
}));

describe("DisorderCreateForm", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("envio válido chama a criação com o nome informado", async () => {
    vi.mocked(createDisorderApi).mockResolvedValue(undefined);

    render(<DisorderCreateForm />);

    fireEvent.input(screen.getByLabelText("Nome do Transtorno"), {
      target: { value: "TEA" },
    });
    fireEvent.click(screen.getByRole("button", { name: "Salvar" }));

    await waitFor(() => {
      expect(createDisorderApi).toHaveBeenCalledWith({ name: "TEA" });
    });
    expect(toast.success).toHaveBeenCalled();
    expect(push).toHaveBeenCalledWith("/disorders");
  });

  it("envio com nome vazio exibe a mensagem de validação e não chama a API", async () => {
    render(<DisorderCreateForm />);

    fireEvent.click(screen.getByRole("button", { name: "Salvar" }));

    expect(await screen.findByText("O nome é obrigatório.")).toBeInTheDocument();
    expect(createDisorderApi).not.toHaveBeenCalled();
  });
});