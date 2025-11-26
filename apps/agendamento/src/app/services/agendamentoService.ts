const API_BASE_URL = process.env.API_BASE_URL || "http://localhost:8090/api";

export interface Agendamento {
  id: string;
  paciente: Paciente;
  profissional: ProfissionalSaude;
  frequenciaDias: number;
  proximaConsulta: string;
  horaProximaConsulta: string;
  confirmado: boolean;
  descricao: string;
  justificativa: string;
  dataCriacao: string;
}

export interface AgendamentoCreateDTO {
  idPaciente: string;
  idProfissional: string;
  frequenciaDias: number;
  proximaConsulta: string;
  confirmado: boolean;
  horaProximaConsulta: string;
  descricao: string;
  justificativa?: string;
}

export interface HistoricoConsultaCreateDTO {
  idAgendamento: string;
  dataConsulta: string;
  horaConsulta: string;
  foiRealizada: boolean;
  justificativa?: string;
}

export interface HistoricoConsultaResponseDTO {
  id: string;
  idAgendamento: string;
  dataConsulta: string;
  horaConsulta: string;
  foiRealizada: boolean;
  justificativa?: string;
  dataCriacao: string;
}

export interface Paciente {
  id?: string;
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

export interface Endereco {
  estado: string;
  cidade: string;
  bairro: string;
  rua: string;
  numero: string;
  cep: string;
  complemento?: string;
}

export interface ProfissionalSaude {
  id?: string;
  areaDaSaude: string;
  telefone: string;
  docProfissional: string;
  email: string;
  nome: string;
  rg: string;
  endereco: Endereco;
}

const pacientes: Paciente[] = [
  {
    nome: "João Silva",
    email: "joao.silva@email.com",
    telefone: "(11) 91234-5678",
    dateNascimento: "1990-05-10",
    cpf: "12345678900",
    rg: "12.345.678-9",
    endereco: "Rua A, 123",
    bairro: "Centro",
    cidade: "São Paulo",
    estado: "SP",
    cep: "01000000",
  },
  {
    nome: "Maria Oliveira",
    email: "maria.oliveira@email.com",
    telefone: "(21) 98765-4321",
    dateNascimento: "1985-07-22",
    cpf: "98765432100",
    rg: "98.765.432-1",
    endereco: "Rua B, 456",
    bairro: "Jardim Botânico",
    cidade: "Rio de Janeiro",
    estado: "RJ",
    cep: "20000000",
  },
  {
    nome: "Carlos Santos",
    email: "carlos.santos@email.com",
    telefone: "(31) 93456-7890",
    dateNascimento: "1992-11-15",
    cpf: "23456789012",
    rg: "23.456.789-0",
    endereco: "Avenida C, 789",
    bairro: "Funcionários",
    cidade: "Belo Horizonte",
    estado: "MG",
    cep: "30123456",
  },
  {
    nome: "Ana Souza",
    email: "ana.souza@email.com",
    telefone: "(41) 98123-4567",
    dateNascimento: "1988-12-02",
    cpf: "34567890123",
    rg: "34.567.890-1",
    endereco: "Rua D, 101",
    bairro: "Bigorrilho",
    cidade: "Curitiba",
    estado: "PR",
    cep: "80210100",
  },
  {
    nome: "Felipe Pereira",
    email: "felipe.pereira@email.com",
    telefone: "(51) 93567-8912",
    dateNascimento: "1993-03-30",
    cpf: "45678901234",
    rg: "45.678.901-2",
    endereco: "Rua E, 202",
    bairro: "Moinhos de Vento",
    cidade: "Porto Alegre",
    estado: "RS",
    cep: "90035200",
  },
  {
    nome: "Patrícia Costa",
    email: "patricia.costa@email.com",
    telefone: "(61) 98123-0987",
    dateNascimento: "1995-09-10",
    cpf: "56789012345",
    rg: "56.789.012-3",
    endereco: "Avenida F, 303",
    bairro: "Asa Sul",
    cidade: "Brasília",
    estado: "DF",
    cep: "70070100",
  },
  {
    nome: "Lucas Almeida",
    email: "lucas.almeida@email.com",
    telefone: "(85) 99876-5432",
    dateNascimento: "1994-06-18",
    cpf: "67890123456",
    rg: "67.890.123-4",
    endereco: "Rua G, 404",
    bairro: "Aldeota",
    cidade: "Fortaleza",
    estado: "CE",
    cep: "60160050",
  },
  {
    nome: "Fernanda Rodrigues",
    email: "fernanda.rodrigues@email.com",
    telefone: "(11) 94321-8765",
    dateNascimento: "1991-02-25",
    cpf: "78901234567",
    rg: "78.901.234-5",
    endereco: "Rua H, 505",
    bairro: "Vila Progredior",
    cidade: "São Paulo",
    estado: "SP",
    cep: "02012030",
  },
  {
    nome: "Ricardo Martins",
    email: "ricardo.martins@email.com",
    telefone: "(41) 98765-4321",
    dateNascimento: "1987-10-05",
    cpf: "89012345678",
    rg: "89.012.345-6",
    endereco: "Avenida I, 606",
    bairro: "Água Verde",
    cidade: "Curitiba",
    estado: "PR",
    cep: "81050210",
  },
  {
    nome: "Juliana Rocha",
    email: "juliana.rocha@email.com",
    telefone: "(21) 96321-0987",
    dateNascimento: "1996-04-13",
    cpf: "90123456789",
    rg: "90.123.456-7",
    endereco: "Rua J, 707",
    bairro: "Copacabana",
    cidade: "Rio de Janeiro",
    estado: "RJ",
    cep: "22010010",
  },
];

const profissionais: ProfissionalSaude[] = [
  {
    areaDaSaude: "Fonoaudiologia",
    telefone: "(11) 98765-4321",
    docProfissional: "CRFa 12345",
    email: "ana.fonou@email.com",
    nome: "Ana Fonseca",
    rg: "12.345.678-9",
    endereco: {
      estado: "SP",
      cidade: "São Paulo",
      bairro: "Centro",
      rua: "Rua A",
      numero: "123",
      cep: "01000-000",
      complemento: "Apto 101",
    },
  },
  {
    areaDaSaude: "Fisioterapia",
    telefone: "(21) 91234-5678",
    docProfissional: "CREFITO 54321",
    email: "marcos.fisio@email.com",
    nome: "Marcos Pereira",
    rg: "98.765.432-1",
    endereco: {
      estado: "RJ",
      cidade: "Rio de Janeiro",
      bairro: "Jardim Botânico",
      rua: "Rua B",
      numero: "456",
      cep: "20000-000",
      complemento: "",
    },
  },
  {
    areaDaSaude: "Psicologia",
    telefone: "(31) 93456-7890",
    docProfissional: "CRP 67890",
    email: "juliana.psico@email.com",
    nome: "Juliana Souza",
    rg: "23.456.789-0",
    endereco: {
      estado: "MG",
      cidade: "Belo Horizonte",
      bairro: "Funcionários",
      rua: "Avenida C",
      numero: "789",
      cep: "30123-456",
      complemento: "Sala 202",
    },
  },
  {
    areaDaSaude: "Terapia Ocupacional",
    telefone: "(41) 99876-1234",
    docProfissional: "CREFITO 13579",
    email: "roberto.to@email.com",
    nome: "Roberto Lima",
    rg: "34.567.890-1",
    endereco: {
      estado: "PR",
      cidade: "Curitiba",
      bairro: "Bigorrilho",
      rua: "Rua D",
      numero: "101",
      cep: "80210-100",
      complemento: "",
    },
  },
  {
    areaDaSaude: "Psicopedagogia",
    telefone: "(61) 98123-4567",
    docProfissional: "CRP 24680",
    email: "marcia.psi@email.com",
    nome: "Márcia Andrade",
    rg: "56.789.012-3",
    endereco: {
      estado: "DF",
      cidade: "Brasília",
      bairro: "Asa Sul",
      rua: "Avenida F",
      numero: "303",
      cep: "70070-100",
      complemento: "Bloco B",
    },
  },
  {
    areaDaSaude: "Neurologia",
    telefone: "(85) 98765-4321",
    docProfissional: "CRM 112233",
    email: "carlos.neuro@email.com",
    nome: "Carlos Moreira",
    rg: "67.890.123-4",
    endereco: {
      estado: "CE",
      cidade: "Fortaleza",
      bairro: "Aldeota",
      rua: "Rua G",
      numero: "404",
      cep: "60160-050",
      complemento: "",
    },
  },
  {
    areaDaSaude: "Psiquiatria",
    telefone: "(11) 97654-3210",
    docProfissional: "CRM 445566",
    email: "luana.psi@email.com",
    nome: "Luana Rocha",
    rg: "78.901.234-5",
    endereco: {
      estado: "SP",
      cidade: "São Paulo",
      bairro: "Vila Progredior",
      rua: "Rua H",
      numero: "505",
      cep: "02012-030",
      complemento: "Apto 303",
    },
  },
  {
    areaDaSaude: "Enfermagem",
    telefone: "(71) 92345-6789",
    docProfissional: "COREN 998877",
    email: "fernando.enf@email.com",
    nome: "Fernando Nogueira",
    rg: "89.012.345-6",
    endereco: {
      estado: "BA",
      cidade: "Salvador",
      bairro: "Pituba",
      rua: "Avenida I",
      numero: "606",
      cep: "41810-010",
      complemento: "",
    },
  },
  {
    areaDaSaude: "Pediatria",
    telefone: "(51) 93456-1234",
    docProfissional: "CRM 556677",
    email: "claudia.ped@email.com",
    nome: "Cláudia Martins",
    rg: "90.123.456-7",
    endereco: {
      estado: "RS",
      cidade: "Porto Alegre",
      bairro: "Moinhos de Vento",
      rua: "Rua E",
      numero: "202",
      cep: "90035-200",
      complemento: "Sala 101",
    },
  },
];

export async function saveAgendamento(
  novoAgendamento: AgendamentoCreateDTO,
  id?: string
): Promise<Agendamento> {
  try {
    if (!id) {
      delete novoAgendamento.justificativa;
    }

    const res = await fetch(
      `${API_BASE_URL}/appointments${id ? `/${id}` : ""}`,
      {
        method: id ? "PUT" : "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(novoAgendamento),
      }
    );
    const data = await res.json();
    return data as Agendamento;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function saveAgendamentoRealizado(
  agendamento: Agendamento
): Promise<HistoricoConsultaResponseDTO> {
  try {
    const dto: HistoricoConsultaCreateDTO = {
      idAgendamento: agendamento.id,
      dataConsulta: agendamento.proximaConsulta,
      horaConsulta: agendamento.horaProximaConsulta,
      foiRealizada: true,
      justificativa: agendamento.justificativa,
    };
    const res = await fetch(`${API_BASE_URL}/consultation-histories`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(dto),
    });
    console.log(res);
    const data = await res.json();

    const [ano, mes, dia] = agendamento.proximaConsulta.split("-");
    const emDate = new Date(parseInt(ano), parseInt(mes), parseInt(dia));
    emDate.setDate(emDate.getDate() + 15);
    const proximaConsulta = `${String(emDate.getFullYear()).padStart(
      2,
      "0"
    )}-${String(emDate.getMonth()).padStart(2, "0")}-${String(
      emDate.getDate()
    ).padStart(2, "0")}`;

    agendamento.proximaConsulta = proximaConsulta;
    if (agendamento.paciente?.id && agendamento.profissional?.id) {
      const { paciente, profissional, id, ...agendamentoDTO } = {
        ...agendamento,
        idPaciente: agendamento.paciente.id,
        idProfissional: agendamento.profissional.id,
      };
      agendamentoDTO.confirmado = false;
      await saveAgendamento(agendamentoDTO, agendamento.id);
    }

    return data as HistoricoConsultaResponseDTO;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function getAgendamentos(
  data?: string,
  hora?: string
): Promise<Agendamento[]> {
  try {
    if (data) {
      let [ano, mes, dia] = data?.split("-").map(Number);
      data = `${ano}-${(mes-1).toString().padStart(2, "0")}-${dia.toString().padStart(2, "0")}`;
    }

    const response = await fetch(
      `${API_BASE_URL}/appointments${data ? `?date=${data}` : ""}${
        hora ? `&hora=${hora}` : ""
      }`
    ).then((res) => res.json());

    console.log(response);

    const agendamentos: Agendamento[] = [];
    for (let agendamento of response.content) {
      const paciente = await fetch(
        `${API_BASE_URL}/patients/${agendamento.idPaciente}`
      ).then((res) => res.json());
      const profissional = await fetch(
        `${API_BASE_URL}/professionals/${agendamento.idProfissional}`
      ).then((res) => res.json());

      agendamentos.push({ ...agendamento, paciente, profissional });
    }

    return agendamentos;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function getAgendamentoById(id: string): Promise<Agendamento> {
  try {
    const agendamento = await fetch(`${API_BASE_URL}/appointments/${id}`).then(
      (res) => res.json()
    );
    const paciente = await fetch(
      `${API_BASE_URL}/patients/${agendamento.idPaciente}`
    ).then((res) => res.json());
    const profissional = await fetch(
      `${API_BASE_URL}/professionals/${agendamento.idProfissional}`
    ).then((res) => res.json());

    return { ...agendamento, paciente, profissional };
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function getAgendamentoRealizadoById(
  id: string
): Promise<Agendamento> {
  try {
    const agendamentoRealizado: HistoricoConsultaResponseDTO = await fetch(
      `${API_BASE_URL}/consultation-histories/${id}`
    ).then((res) => res.json());
    const agendamento: Agendamento = await getAgendamentoById(
      agendamentoRealizado.idAgendamento
    );
    agendamento.proximaConsulta = agendamentoRealizado.dataConsulta;
    agendamento.horaProximaConsulta = agendamentoRealizado.horaConsulta;
    return agendamento;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function deleteAgendamento(
  id: string,
  realizado: boolean
): Promise<void> {
  try {
    await fetch(
      `${API_BASE_URL}/${
        realizado ? "consultation-histories" : "appointments"
      }/${id}`,
      {
        method: "DELETE",
      }
    );
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function getPacientes(): Promise<Paciente[]> {
  try {
    const response = await fetch(
      `${API_BASE_URL}/patients?page=0&size=100`
    ).then((res) => res.json());
    let pacientesRetornados: Paciente[] = response.content;
    const existentes = new Set(pacientesRetornados.map((p: Paciente) => p.cpf));

    if (
      !pacientesRetornados.length ||
      !pacientes.some((p) => !existentes.has(p.cpf))
    ) {
      for (const paciente of pacientes) {
        if (!existentes.has(paciente.cpf)) {
          const pacienteCriado = await fetch(
            `${API_BASE_URL}/patients/create`,
            {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(paciente),
            }
          ).then((res) => res.json());

          pacientesRetornados.push(pacienteCriado);
        }
      }
    }

    return pacientesRetornados.filter((paciente, index, self) => {
      return self.findIndex((p) => p.cpf === paciente.cpf) === index;
    });
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function getProfissionaisDaSaude(): Promise<ProfissionalSaude[]> {
  try {
    const response = await fetch(
      `${API_BASE_URL}/professionals?page=0&size=100`
    ).then((res) => res.json());
    let profissionaisRetornados: ProfissionalSaude[] = response.content || [];
    const existentes = new Set(
      profissionaisRetornados.map((p: ProfissionalSaude) => p.docProfissional)
    );

    for (const profissional of profissionais) {
      if (!existentes.has(profissional.docProfissional)) {
        const res = await fetch(`${API_BASE_URL}/professionals`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(profissional),
        });

        if (!res.ok) {
          continue;
        }

        const createdProfissional = await res.json();
        profissionaisRetornados.push(createdProfissional);
      }
    }

    return profissionaisRetornados.filter((profissional, index, self) => {
      return (
        self.findIndex(
          (p) => p.docProfissional === profissional.docProfissional
        ) === index
      );
    });
  } catch (error) {
    console.error("Error fetching professionals:", error);
    throw error;
  }
}

export async function getProfissionalDaSaude(
  id: string
): Promise<ProfissionalSaude> {
  try {
    const profissional = await fetch(
      `${API_BASE_URL}/professionals/${id}`
    ).then((res) => res.json());

    return profissional;
  } catch (error) {
    console.error(`Error fetching professional with ID ${id}:`, error);
    throw error;
  }
}

export async function getAreasDaSaude(): Promise<string[]> {
  try {
    const profissionais = await getProfissionaisDaSaude();
    const areas = profissionais.map((p) => p.areaDaSaude);
    const setList: string[] = [];
    new Set(areas).forEach((e) => setList.push(e as string));

    return setList;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export const toggleConfirmacao = async (id: string) => {
  const agendamento = await getAgendamentoById(id);

  if (!agendamento.paciente.id || !agendamento.profissional.id) return;

  const { paciente, profissional, ...dto } = {
    ...agendamento,
    idPaciente: agendamento.paciente.id,
    idProfissional: agendamento.profissional.id,
  };

  dto.confirmado = !dto.confirmado;

  await saveAgendamento(dto, id);
};
