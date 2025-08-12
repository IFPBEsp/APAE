import axios from "axios";

const API_BASE_URL = "http://localhost:8086/";

const TOKEN = process.env.NEXT_PUBLIC_JWT_TOKEN;

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
  numeroTelefone: string;
  contatoRequest: ContatoRequest;
  vacinacoesRequests: VacinaRequest[];
  deficienciasRequests: TipoDeficienciaRequest[];
  atendimentosRequests: TipoAtendimentoRequest[];
  responsaveisRequests: PessoaResponsavelRequest[];
  cadastrosAnuaisRequests: CadastroAnualRequest[];
  funcao?: 'Aluno' | 'Paciente' | 'Ambos' | '';
}

export interface PessoaResponse extends PessoaRequest {
  id: string;
}

export async function criarPessoa(data: PessoaRequest) {
  const response = await api.post<PessoaResponse>("/pessoas", data);
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

// Contato
export async function criarContato(data: ContatoRequest) {
  const response = await api.post("/contatos", data);
  return response.data;
}

// Vacina
export async function criarVacina(data: VacinaRequest) {
  const response = await api.post("/vacinas", data);
  return response.data;
}

// Tipo Deficiencia
export async function criarTipoDeficiencia(data: TipoDeficienciaRequest) {
  const response = await api.post("/tipo_deficiencia", data);
  return response.data;
}

// Tipo Atendimento
export async function criarTipoAtendimento(data: TipoAtendimentoRequest) {
  const response = await api.post("/tipo_atendimento", data);
  return response.data;
}

// Pessoa Responsavel
export async function criarPessoaResponsavel(data: PessoaResponsavelRequest) {
  const response = await api.post("/pessoa_responsavel", data);
  return response.data;
}

// Cadastro Anual
export async function criarCadastroAnual(data: CadastroAnualRequest) {
  const response = await api.post("/api/cadastros-anual", data);
  return response.data;
}