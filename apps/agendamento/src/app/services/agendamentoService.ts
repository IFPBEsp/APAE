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

const API_BASE_URL = process.env.API_BASE_URL || "http://localhost:8081";

export async function saveAgendamento(
  novoAgendamento: AgendamentoCreateDTO
): Promise<AgendamentoResponseDTO> {
  try {
    const res = await fetch(`${API_BASE_URL}/agendamentos`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(novoAgendamento),
    });
    const data = await res.json();
    return data as AgendamentoResponseDTO;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function getAgendamentos(): Promise<AgendamentoResponseDTO[]> {
  try {
    const response = await fetch(`${API_BASE_URL}/agendamentos`).then((res) =>
      res.json()
    );
    console.log(response.content);
    return response.content;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function getAgendamentoById(
  id: string
): Promise<AgendamentoResponseDTO> {
  try {
    const response = await fetch(`${API_BASE_URL}/agendamentos/${id}`).then(
      (res) => res.json()
    );
    return response as AgendamentoResponseDTO;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

/* MOCK PACIENTES */

export interface Paciente {
  nome: string;
  email: string;
  telefone: string;
  dateNascimento: string;
  cpf: string;
  rg: string;
  endereco: string;
  bairro: string;
  cidade: string;
  estado: string;
  cep: string;
}

const pacientes = [
  {
    nome: "João Silva",
    email: "joao.silva@email.com",
    telefone: "(11) 91234-5678",
    dateNascimento: "1990-05-10",
    cpf: "123.456.789-00",
    rg: "12.345.678-9",
    endereco: "Rua A, 123",
    bairro: "Centro",
    cidade: "São Paulo",
    estado: "SP",
    cep: "01000-000",
  },
  {
    nome: "Maria Oliveira",
    email: "maria.oliveira@email.com",
    telefone: "(21) 98765-4321",
    dateNascimento: "1985-07-22",
    cpf: "987.654.321-00",
    rg: "98.765.432-1",
    endereco: "Rua B, 456",
    bairro: "Jardim Botânico",
    cidade: "Rio de Janeiro",
    estado: "RJ",
    cep: "20000-000",
  },
  {
    nome: "Carlos Santos",
    email: "carlos.santos@email.com",
    telefone: "(31) 93456-7890",
    dateNascimento: "1992-11-15",
    cpf: "234.567.890-12",
    rg: "23.456.789-0",
    endereco: "Avenida C, 789",
    bairro: "Funcionários",
    cidade: "Belo Horizonte",
    estado: "MG",
    cep: "30123-456",
  },
  {
    nome: "Ana Souza",
    email: "ana.souza@email.com",
    telefone: "(41) 98123-4567",
    dateNascimento: "1988-12-02",
    cpf: "345.678.901-23",
    rg: "34.567.890-1",
    endereco: "Rua D, 101",
    bairro: "Bigorrilho",
    cidade: "Curitiba",
    estado: "PR",
    cep: "80210-100",
  },
  {
    nome: "Felipe Pereira",
    email: "felipe.pereira@email.com",
    telefone: "(51) 93567-8912",
    dateNascimento: "1993-03-30",
    cpf: "456.789.012-34",
    rg: "45.678.901-2",
    endereco: "Rua E, 202",
    bairro: "Moinhos de Vento",
    cidade: "Porto Alegre",
    estado: "RS",
    cep: "90035-200",
  },
  {
    nome: "Patrícia Costa",
    email: "patricia.costa@email.com",
    telefone: "(61) 98123-0987",
    dateNascimento: "1995-09-10",
    cpf: "567.890.123-45",
    rg: "56.789.012-3",
    endereco: "Avenida F, 303",
    bairro: "Asa Sul",
    cidade: "Brasília",
    estado: "DF",
    cep: "70070-100",
  },
  {
    nome: "Lucas Almeida",
    email: "lucas.almeida@email.com",
    telefone: "(85) 99876-5432",
    dateNascimento: "1994-06-18",
    cpf: "678.901.234-56",
    rg: "67.890.123-4",
    endereco: "Rua G, 404",
    bairro: "Aldeota",
    cidade: "Fortaleza",
    estado: "CE",
    cep: "60160-050",
  },
  {
    nome: "Fernanda Rodrigues",
    email: "fernanda.rodrigues@email.com",
    telefone: "(11) 94321-8765",
    dateNascimento: "1991-02-25",
    cpf: "789.012.345-67",
    rg: "78.901.234-5",
    endereco: "Rua H, 505",
    bairro: "Vila Progredior",
    cidade: "São Paulo",
    estado: "SP",
    cep: "02012-030",
  },
  {
    nome: "Ricardo Martins",
    email: "ricardo.martins@email.com",
    telefone: "(41) 98765-4321",
    dateNascimento: "1987-10-05",
    cpf: "890.123.456-78",
    rg: "89.012.345-6",
    endereco: "Avenida I, 606",
    bairro: "Água Verde",
    cidade: "Curitiba",
    estado: "PR",
    cep: "81050-210",
  },
  {
    nome: "Juliana Rocha",
    email: "juliana.rocha@email.com",
    telefone: "(21) 96321-0987",
    dateNascimento: "1996-04-13",
    cpf: "901.234.567-89",
    rg: "90.123.456-7",
    endereco: "Rua J, 707",
    bairro: "Copacabana",
    cidade: "Rio de Janeiro",
    estado: "RJ",
    cep: "22010-010",
  },
];

export async function mockPacientes(): Promise<Paciente[]> {
  try {
    const response = await fetch(`${API_BASE_URL}/pacientes?page=0&size=10`).then(res => res.json());

    if(response.content.length) {
      return response.content;
    }

    try {
      const novosPacientes: Paciente[] = [];
      for (const paciente of pacientes) {
        const response = await fetch(`${API_BASE_URL}/pacientes/create`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(paciente),
        }).then((res) => res.json());

        novosPacientes.push(response as Paciente);
      }

      return novosPacientes;
    } catch (error) {
      console.log(error);
      throw error;
    }
  } catch (error) {
    console.log(error);
    throw error;
  }
}
