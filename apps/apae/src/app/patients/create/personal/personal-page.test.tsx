import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import MembersRegisterPersonalPage from "./page";
import { MembersRegisterProvider } from "@/domains/patients/hooks/use-members-register-context";

const { pathnameMock } = vi.hoisted(() => ({ pathnameMock: vi.fn() }));

vi.mock("next/navigation", () => ({
  useRouter: () => ({ push: vi.fn(), back: vi.fn() }),
  usePathname: pathnameMock,
  useParams: () => ({}),
}));

const MATRICULA_32_DIGITOS = "12345678901234567890123456789012";
const CERTIDAO_MENOS_32_DIGITOS = "12345";
const DICA_TEXTO = "Certidão sem matrícula: informe cartório, livro e folha";

function renderPage() {
  return render(
    <MembersRegisterProvider>
      <MembersRegisterPersonalPage />
    </MembersRegisterProvider>,
  );
}

describe("MembersRegisterPersonalPage - Cartório, Livro e Folha (#1132)", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    pathnameMock.mockReturnValue("/patients/create/personal");
  });

  it("permite anexar a cópia opcional do CPF e mostra o nome do arquivo", () => {
    const { container } = renderPage();

    expect(
      screen.getByRole("button", {
        name: "Selecionar Cópia do CPF",
      }),
    ).toBeInTheDocument();

    const fileInput = container.querySelector<HTMLInputElement>(
      'input[id="cpfFile-upload"]',
    );
    const file = new File(["cópia"], "cpf.pdf", {
      type: "application/pdf",
    });

    fireEvent.change(fileInput!, { target: { files: [file] } });

    expect(screen.getByText("Arquivo: cpf.pdf")).toBeInTheDocument();
  });

  it("permite anexar a cópia opcional da certidão e mostra o nome do arquivo", () => {
    const { container } = renderPage();

    expect(
      screen.getByRole("button", {
        name: "Selecionar Cópia da Certidão de Nascimento",
      }),
    ).toBeInTheDocument();

    const fileInput = container.querySelector<HTMLInputElement>(
      'input[id="birth.file-upload"]',
    );
    const file = new File(["cópia"], "certidao.pdf", {
      type: "application/pdf",
    });

    fireEvent.change(fileInput!, { target: { files: [file] } });

    expect(screen.getByText("Arquivo: certidao.pdf")).toBeInTheDocument();
  });

  it("esconde a seleção de arquivos durante a edição do paciente", () => {
    pathnameMock.mockReturnValue("/patients/123/edit/personal");

    renderPage();

    expect(
      screen.queryByRole("button", {
        name: "Selecionar Cópia do CPF",
      }),
    ).not.toBeInTheDocument();

    expect(
      screen.queryByRole("button", {
        name: "Selecionar Cópia da Certidão de Nascimento",
      }),
    ).not.toBeInTheDocument();
  });

  it("não exibe os campos de cartório, livro e folha quando o campo da certidão está vazio", () => {
    renderPage();

    expect(screen.queryByLabelText(/cartório/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/livro/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/folha/i)).not.toBeInTheDocument();
    expect(screen.queryByText(DICA_TEXTO)).not.toBeInTheDocument();
  });

  it("exibe os campos e a dica quando a certidão possui entre 1 e 31 dígitos (< 32)", () => {
    renderPage();

    const certidaoInput = screen.getByLabelText(/certidão de nascimento/i);
    fireEvent.change(certidaoInput, { target: { value: CERTIDAO_MENOS_32_DIGITOS } });

    expect(screen.getByLabelText(/cartório/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/livro/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/folha/i)).toBeInTheDocument();
    expect(screen.getByText(DICA_TEXTO)).toBeInTheDocument();
  });

  it("esconde os campos e a dica quando a certidão atinge 32 dígitos", () => {
    renderPage();

    const certidaoInput = screen.getByLabelText(/certidão de nascimento/i);

    // Primeiro digita certidão curta (< 32)
    fireEvent.change(certidaoInput, { target: { value: CERTIDAO_MENOS_32_DIGITOS } });
    expect(screen.getByLabelText(/cartório/i)).toBeInTheDocument();
    expect(screen.getByText(DICA_TEXTO)).toBeInTheDocument();

    // Em seguida digita certidão com 32 dígitos
    fireEvent.change(certidaoInput, { target: { value: MATRICULA_32_DIGITOS } });

    expect(screen.queryByLabelText(/cartório/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/livro/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/folha/i)).not.toBeInTheDocument();
    expect(screen.queryByText(DICA_TEXTO)).not.toBeInTheDocument();
  });

  it("não limpa os valores do formulário quando os campos são escondidos e reexibidos", () => {
    renderPage();

    const certidaoInput = screen.getByLabelText(/certidão de nascimento/i);

    // Exibe os campos
    fireEvent.change(certidaoInput, { target: { value: CERTIDAO_MENOS_32_DIGITOS } });

    const cartorioInput = screen.getByLabelText(/cartório/i) as HTMLInputElement;
    const livroInput = screen.getByLabelText(/livro/i) as HTMLInputElement;
    const folhaInput = screen.getByLabelText(/folha/i) as HTMLInputElement;

    fireEvent.change(cartorioInput, { target: { value: "1º Ofício de Registro" } });
    fireEvent.change(livroInput, { target: { value: "Livro A-12" } });
    fireEvent.change(folhaInput, { target: { value: "045" } });

    // Esconde ao mudar para 32 dígitos
    fireEvent.change(certidaoInput, { target: { value: MATRICULA_32_DIGITOS } });
    expect(screen.queryByLabelText(/cartório/i)).not.toBeInTheDocument();

    // Volta para menos de 32 dígitos
    fireEvent.change(certidaoInput, { target: { value: "9876" } });

    // Os campos reaparecem com os valores anteriores preservados
    expect((screen.getByLabelText(/cartório/i) as HTMLInputElement).value).toBe(
      "1º Ofício de Registro",
    );
    expect((screen.getByLabelText(/livro/i) as HTMLInputElement).value).toBe(
      "Livro A-12",
    );
    expect((screen.getByLabelText(/folha/i) as HTMLInputElement).value).toBe(
      "045",
    );
  });

  it("permite texto livre no livro (letras) e na folha (zeros à esquerda)", () => {
    renderPage();

    const certidaoInput = screen.getByLabelText(/certidão de nascimento/i);
    fireEvent.change(certidaoInput, { target: { value: "123" } });

    const livroInput = screen.getByLabelText(/livro/i) as HTMLInputElement;
    const folhaInput = screen.getByLabelText(/folha/i) as HTMLInputElement;

    fireEvent.change(livroInput, { target: { value: "Livro B/2020-X" } });
    fireEvent.change(folhaInput, { target: { value: "0012" } });

    expect(livroInput.value).toBe("Livro B/2020-X");
    expect(folhaInput.value).toBe("0012");
  });
});
