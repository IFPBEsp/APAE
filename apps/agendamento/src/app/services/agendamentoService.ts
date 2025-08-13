export interface AgendamentoResponseDTO {
    id: string;
    idPaciente: string;
    idProfissional: string;
    frequenciaDias: number;
    proximaConsulta: string;
    horaProximaConsulta: string;
    confirmado: boolean;
    dataCriacao: string;
}

interface AgendamentoCreateDTO {
    idPaciente: string;
    idProfissional: string;
    frequenciaDias: number;
    proximaConsulta: string;
    confirmado: boolean;
    horaProximaConsulta: string;
}

const API_BASE_URL = process.env.API_BASE_URL || "http://localhost:8080";

export async function saveAgendamento(novoAgendamento: AgendamentoCreateDTO): Promise<AgendamentoResponseDTO> {
  try {
    const res = await fetch(`${API_BASE_URL}/agendamentos`, {
      method: "POST",
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(novoAgendamento)
    });
    const data = await res.json();
    return data as AgendamentoResponseDTO;
  } catch(error) {
    console.log(error);
    throw error;
  }
}

export async function getAgendamentos(): Promise<AgendamentoResponseDTO[]> {
  try {
    const response = await fetch(`${API_BASE_URL}/agendamentos`).then(res => res.json());
    console.log(response.content)
    return response.content;
  } catch(error) {
    console.log(error);
    throw error;
  }
}

export async function getAgendamentoById(id: string): Promise<AgendamentoResponseDTO> {
  try {
    const response = await fetch(`${API_BASE_URL}/agendamentos/${id}`).then(res => res.json());
    return response as AgendamentoResponseDTO;
  } catch(error) {
    console.log(error);
    throw error;
  }
}
