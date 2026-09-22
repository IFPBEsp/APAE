import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { toast } from "react-toastify";
import { DisorderEditForm } from "./disorder-form";
import { fetchDisorderApi, updateDisorderApi } from "../disorders.api";

vi.mock("../disorders.api");
vi.mock("react-toastify", () => ({
  toast: { success: vi.fn(), error: vi.fn() },
}));

const push = vi.fn();
const back = vi.fn();
vi.mock("next/navigation", () => ({
  useRouter: () => ({ push, back }),
}));

const disorderMock = { id: "1", name: "TDAH", hasPatient: false };

describe("DisorderEditForm", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(fetchDisorderApi).mockResolvedValue(disorderMock);
  });

  it("carrega o nome atual do transtorno", async () => {
    render(<DisorderEditForm id="1" />);

    expect(await screen.findByDisplayValue("TDAH")).toBeInTheDocument();
  });

  it("envio válido chama a atualização com o id e o novo nome", async () => {
    vi.mocked(updateDisorderApi).mockResolvedValue(undefined);

    render(<DisorderEditForm id="1" />);
    await screen.findByDisplayValue("TDAH");

    fireEvent.input(screen.getByLabelText("Nome do Transtorno"), {
      target: { value: "TEA" },
    });
    fireEvent.click(screen.getByRole("button", { name: "Atualizar" }));

    await waitFor(() => {
      expect(updateDisorderApi).toHaveBeenCalledWith({ id: "1", name: "TEA" });
    });
    expect(toast.success).toHaveBeenCalled();
    expect(push).toHaveBeenCalledWith("/disorders");
  });

  it("envio com nome vazio exibe a mensagem de validação e não chama a API", async () => {
    render(<DisorderEditForm id="1" />);
    await screen.findByDisplayValue("TDAH");

    fireEvent.input(screen.getByLabelText("Nome do Transtorno"), {
      target: { value: "" },
    });
    fireEvent.click(screen.getByRole("button", { name: "Atualizar" }));

    expect(await screen.findByText("O nome é obrigatório.")).toBeInTheDocument();
    expect(updateDisorderApi).not.toHaveBeenCalled();
  });
});