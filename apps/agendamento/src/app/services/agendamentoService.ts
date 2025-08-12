export interface AgendamentoResponseDTO {
  id: string;
  nome: string;
  consulta: boolean;
  agendamentoMarcado?: {
    dataHora?: string;
    periodo?: string;
    areaDeAtendimento?: string;
    confirmado?: boolean;
    descricao?: string;
    justificativa?: string;
  };
  profissionalDaSaude?: {
    nome?: string;
    email?: string;
    telefone?: string;
  };
  dadosPaciente?: {
    contato?: string;
    dataDeNascimento?: string;
    cpf?: string;
    rg?: string;
  };
  dadosResidenciais?: {
    endereco?: string;
    bairro?: string;
    cidade?: string;
    estado?: string;
    cep?: string;
  };
  informacoesSaudePaciente?: {
    vacinas?: string[];
    doencas?: string[];
    alergias?: string[];
    medicacoes?: string[];
    deficiencia?: string;
    atendimento?: string;
  };
}

const API_BASE_URL = process.env.API_BASE_URL || "http://localhost:8080";

export async function getAgendamentoById(id: string): Promise<AgendamentoResponseDTO> {
  const res = await fetch(`${API_BASE_URL}/api/agendamentos/${id}`, {
    cache: "no-store",
  });
  if (!res.ok) throw new Error(`Erro ao buscar agendamento id: ${id}`);
  return res.json();
}
