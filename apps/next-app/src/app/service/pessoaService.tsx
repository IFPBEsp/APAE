// src/service/pessoaService.ts
import axios from "axios";

// Create a custom Axios instance
const API_BASE_URL = "http://localhost:8086/pessoas";

// --- For Testing: Hardcode your token here ---
// Replace "YOUR_TOKEN_HERE" with the actual token you used in Postman.
const TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIyQ1pDcTd4LTFnTm9FaGlsdUQ2ZERaRWltSFlVaVhzeDExVEtzQ205VURvIn0.eyJleHAiOjE3NTQxNjY2NDIsImlhdCI6MTc1NDE2NjM0MiwianRpIjoib25ydHJvOjQxOTAyMzQ5LTNmMzgtYTFhMi05NzQzLTIzZTQ0Y2MxOWZiNyIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvb3JnLWFwYWUiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiYzA5M2NiODUtNTUwNS00ZmNjLWJmMTgtMTllY2JmMDIzNTE1IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiY2xpZW50LWFwYWUiLCJzaWQiOiJiYzE1YzJhYS1kMTQ0LTQyOGEtYmVhMS1lZmQxMmM1ZTRlMTkiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLW9yZy1hcGFlIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkpvaG4gRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiZGVtb19hZG1pbiIsImdpdmVuX25hbWUiOiJKb2huIiwiZmFtaWx5X25hbWUiOiJEb2UiLCJlbWFpbCI6InRlc3RlQHRlc3RlLmNvbSJ9.NbJIMKtljR53cq5KdwZ5xl6PJ9B5YmZUyuQ8DAJVStxy2MV1j70AGPHutxXfMbQqaNaPfFjGq_hKpLR0kwD0_jbrUpDcDAfgNAnd5R5W8g0uk1hIWXdS8xjqJvWdladRb6u4o8JN3QHvDWLzez6JBkbnUiGY_Y-TAahY2ZlfnibduE7EwLvAd-dI3rWZeA0R--O6AHrM6nSqhdLlsO3GxKjqf3ueFJXgCmLyZ5SbI-b2JzAHGUInBBPzdefvEeZ_j0YnooV-zG7HRXy8_qQndhXEvbCEkO6vAVb6ho8cgmFcw4af-0jUNHER865Cbh4jrfiVIcFZfZiYyPqIPPhtEQ";

// Create a custom Axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
});

// Add a request interceptor to attach the auth token to every request
api.interceptors.request.use(
  (config) => {
    // If a token exists, add it to the Authorization header
    if (TOKEN) {
      config.headers.Authorization = `Bearer ${TOKEN}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Your existing interfaces remain the same
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

// Update your service functions to use the new `api` instance
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