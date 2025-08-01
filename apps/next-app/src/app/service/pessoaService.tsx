import axios from "axios";

const API_BASE_URL = "http://localhost:8086/pessoas";

export interface PessoaRequest {
  nome: string;
  cpf: string;
  rg: string;
  contato: string;
  registroNascimento: string;
  orgaoEmissor: string;
  dataNascimento: string;
  dataEmissao: string;
  rua: string;
  bairro: string;
  cep: string;
  estado: string;
}

export interface PessoaResponse {
  id: string;
  nome: string;
  cpf: string;
  rg: string;
  contato: string;
  registroNascimento: string;
  orgaoEmissor: string;
  dataNascimento: string;
  dataEmissao: string;
  rua: string;
  bairro: string;
  cep: string;
  estado: string;
}

// Criar pessoa
export async function criarPessoa(data: PessoaRequest) {
  const response = await axios.post<PessoaResponse>(API_BASE_URL, data);
  return response.data;
}

// Buscar pessoa por ID
export async function getPessoa(id: string) {
  const response = await axios.get<PessoaResponse>(`${API_BASE_URL}/${id}`);
  return response.data;
}

// Listar todas (com paginação)
export async function getTodasPessoas(page = 0, size = 10) {
  const response = await axios.get(`${API_BASE_URL}?page=${page}&size=${size}`);
  return response.data;
}

// Atualizar pessoa
export async function atualizarPessoa(id: string, data: PessoaRequest) {
  const response = await axios.put<PessoaResponse>(`${API_BASE_URL}/${id}`, data);
  return response.data;
}

// Deletar pessoa
export async function deletarPessoa(id: string) {
  await axios.delete(`${API_BASE_URL}/${id}`);
}
