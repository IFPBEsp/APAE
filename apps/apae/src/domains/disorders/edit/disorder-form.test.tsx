import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { toast } from "react-toastify";
import { fetchDisorderApi, updateDisorderApi, fetchDisordersApi } from "../disorders.api";
import { DisordersProvider } from "@/hooks/use-disorders";
import EditDisorderPage from "@/app/disorders/[id]/edit/page";

vi.mock("../disorders.api");
vi.mock("react-toastify", () => ({
  toast: { success: vi.fn(), error: vi.fn() },
}));

const push = vi.fn();
const back = vi.fn();
vi.mock("next/navigation", () => ({
  useRouter: () => ({ push, back }),
  useParams: () => ({ id: "1" }),

}));

const disorderMock = { id: "1", name: "TDAH", hasPatient: false };

describe("EditDisorderPage", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(fetchDisordersApi).mockResolvedValue([]);
    vi.mocked(fetchDisorderApi).mockResolvedValue(disorderMock);
  });

  it("carrega o nome atual do transtorno", async () => {
    render(<EditDisorderPage />, { wrapper: DisordersProvider });

    expect(await screen.findByDisplayValue("TDAH")).toBeInTheDocument();
  });

  it("envio válido chama a atualização com o id e o novo nome", async () => {
    vi.mocked(updateDisorderApi).mockResolvedValue(undefined);

    render(<EditDisorderPage />, { wrapper: DisordersProvider });
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
    render(<EditDisorderPage />, { wrapper: DisordersProvider });
    await screen.findByDisplayValue("TDAH");

    fireEvent.input(screen.getByLabelText("Nome do Transtorno"), {
      target: { value: "" },
    });
    fireEvent.click(screen.getByRole("button", { name: "Atualizar" }));

    expect(await screen.findByText("O nome é obrigatório.")).toBeInTheDocument();
    expect(updateDisorderApi).not.toHaveBeenCalled();
  });
});
