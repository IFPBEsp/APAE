export interface AgendamentoResponseDTO {
  id: string;
  idPaciente: string;
  idProfissional: string;
  frequenciaDias?: number;
  proximaConsulta?: string; // formato ISO date, ex: "2025-08-12"
  horaProximaConsulta?: string; // formato ISO time, ex: "14:30:00"
  confirmado?: boolean;
  dataCriacao?: string; // formato ISO datetime

  profissionalDaSaude?: ProfissionalSaudeDTO;
  dadosPaciente?: DadosPacienteDTO;
  dadosResidenciais?: DadosResidenciaisDTO;
  informacoesSaudePaciente?: InformacoesSaudePacienteDTO;
}

export interface ProfissionalSaudeDTO {
  id: string;
  areaDaSaude?: string;
  telefone?: string;
  docProfissional?: string;
  email?: string;
  nome?: string;
}

export interface DadosPacienteDTO {
  contato?: string;
  dataDeNascimento?: string;
  cpf?: string;
  rg?: string;
}

export interface DadosResidenciaisDTO {
  endereco?: string;
  bairro?: string;
  cidade?: string;
  estado?: string;
  cep?: string;
}

export interface InformacoesSaudePacienteDTO {
  vacinas?: string[];
  doencas?: string[];
  alergias?: string[];
  medicacoes?: string[];
  deficiencia?: string;
  atendimento?: string;
}
