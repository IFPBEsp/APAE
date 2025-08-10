import axios from "axios";

const API_BASE_URL = "http://localhost:8086/pessoas";

const TOKEN =
  "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIyQ1pDcTd4LTFnTm9FaGlsdUQ2ZERaRWltSFlVaVhzeDExVEtzQ205VURvIn0.eyJleHAiOjE3NTQ4NDkwODEsImlhdCI6MTc1NDg0ODc4MSwianRpIjoib25ydHJvOjYwM2UzOWQ5LTI0MmQtYjY5MS1jODM1LThmNGM0MDljNzcyNSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvb3JnLWFwYWUiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiYzA5M2NiODUtNTUwNS00ZmNjLWJmMTgtMTllY2JmMDIzNTE1IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiY2xpZW50LWFwYWUiLCJzaWQiOiI3NjkyOGUxMy0wNmM0LTQ5NzMtOWU0Mi04Y2RhNTY1ZTRkNzAiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLW9yZy1hcGFlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkpvaG4gRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiZGVtb19hZG1pbiIsImdpdmVuX25hbWUiOiJKb2huIiwiZmFtaWx5X25hbWUiOiJEb2UiLCJlbWFpbCI6InRlc3RlQHRlc3RlLmNvbSJ9.a1vcEL1km6PmvmhW9oOueUq3WKbdDX3_eHGaGLfJq17B6rubB_MoWLUeJ0JPo77XeVNM1IgXLFF8gIiqPAhv3_QgZFvUV6fXEjSFWNfXvpr05tEhsXdyixfBfOKt9C2kG4Cof2SHoJ2xXeWNUVRM6j_evy32EkoIwPghUGhMYakxc6fmPGotFnQwmo7LFPVMu1wk4P59vRLrNvfBWITRQ09p26QbsQkP8QMS0jT7rnXNaRVi1qKiXHveUA_UgSoBfKmJqoJZ--zTM0EgU_WxPaUXV0FT4_HDEWHt8kHRq-7ruCXz2PFnRVXX0PeyrmadrfE7EJn-S7QPwUkLaQRZig";

const api = axios.create({
  baseURL: API_BASE_URL,
});

api.interceptors.request.use(
  (config) => {
    if (TOKEN) {
      config.headers.Authorization = `Bearer ${TOKEN}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export interface ContatoRequest {
  enderecoAtivo: string;
  comprovanteResidencia: string;
  endereco: string;
  bairro: string;
  cidade: string;
  estado: string;
  cep: string;
  naturalidade: string;
  pessoaId?: string;
}

export interface VacinaRequest {
  nome: string;
  dataAplicacao: string;
  pessoaId?: string;
}

export interface TipoDeficienciaRequest {
  descricao: string;
  pessoaId?: string;
}

export interface TipoAtendimentoRequest {
  descricao: string;
  pessoaId?: string;
}

export interface PessoaResponsavelRequest {
  nome: string;
  ondeProcurar: string;
  vivo: boolean;
  profissao: string;
  rg: string;
  cpf: string;
  emergencia: string;
  tipoResponsavel: string;
  pessoaId?: string;
}

export interface CadastroAnualRequest {
  beneficioDePrestacaoContinuada: boolean;
  historicosAlergias: string;
  medicacoesContinuas: string;
  historicoDoencas: string;
  rendaFamiliar: number;
  pessoaId?: string;
  tipoAtendimentoId?: number;
}

export interface PessoaRequest {
  nomeCompleto: string;
  dataNascimento: string;
  numRegistroNasc: string;
  fls: string;
  livro: string;
  cartorio: string;
  cpf: string;
  rg: string;
  dataEmissaoRg: string;
  orgaoEmissorRg: string;
  cns: string;
  nis: string;
  dataCadastramento: string;
  contatoRequest: ContatoRequest;
  vacinacoesRequests: VacinaRequest[];
  deficienciasRequests: TipoDeficienciaRequest[];
  atendimentosRequests: TipoAtendimentoRequest[];
  responsaveisRequests: PessoaResponsavelRequest[];
  cadastrosAnuaisRequests: CadastroAnualRequest[];
}

export interface PessoaResponse extends PessoaRequest {
  id: string;
}

export async function criarPessoa(data: PessoaRequest) {
  const response = await api.post<PessoaResponse>("", data);
  return response.data;
}

export async function getPessoa(id: string) {
  const response = await api.get<PessoaResponse>(`/${id}`);
  return response.data;
}

export async function getTodasPessoas(page = 0, size = 10) {
  const response = await api.get(`/pessoas?page=${page}&size=${size}`);
  return response.data;
}

export async function atualizarPessoa(id: string, data: PessoaRequest) {
  const response = await api.put<PessoaResponse>(`/${id}`, data);
  return response.data;
}

export async function deletarPessoa(id: string) {
  await api.delete(`/${id}`);
}
