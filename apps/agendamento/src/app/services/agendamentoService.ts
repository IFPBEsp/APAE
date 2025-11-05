const API_BASE_URL = process.env.API_BASE_URL || "http://localhost:8093";

export interface Appointment {
  id: string;
  patient: Patient;
  professional: HealthProfessional;
  frequencyDays: number;
  nextAppointment: string;
  nextAppointmentTime: string;
  confirmed: boolean;
  description: string;
  justification: string;
  creationDate: string;
}

export interface Abscense {
  id: string;
  generatedAppointmentId: string;
  abscenseDate: Date;
  justification: string;
  notified: boolean;
}

export interface HistoryEntry {
  id: string;
  consultationDate: string;
  consultationTime: string;
  wasPerformed: boolean;
  appointmentId: string;
  justification?: string;
}

export interface PatientWithAbsences {
  patient: Patient;
  absenceCount: number;
  lastAbsenceDate: string;
  absences: HistoryEntry[];
}

export interface AppointmentCreateDTO {
  patientId: string;
  professionalId: string;
  frequencyDays: number;
  nextAppointment: string;
  confirmed: boolean;
  nextAppointmentTime: string;
  description: string;
  justification?: string;
}

export interface ConsultationHistoryCreateDTO {
  appointmentId: string;
  consultationDate: string;
  consultationTime: string;
  wasPerformed: boolean;
  justification?: string;
}

export interface ConsultationHistoryResponseDTO {
  id: string;
  appointmentId: string;
  consultationDate: string;
  consultationTime: string;
  wasPerformed: boolean;
  justification?: string;
  creationDate: string;
}

export interface Patient {
  id?: string;
  name: string;
  email: string;
  phone: string;
  birthDate: string;
  cpf: string;
  rg: string;
  address: string;
  neighborhood: string;
  city: string;
  state: string;
  zipCode: string;
}

export interface Address {
  state: string;
  city: string;
  neighborhood: string;
  street: string;
  number: string;
  zipCode: string;
  complement?: string;
}

export interface HealthProfessional {
  id?: string;
  healthArea: string;
  phone: string;
  professionalDocument: string;
  email: string;
  name: string;
  rg: string;
  address: Address;
}

const patients: Patient[] = [
  {
    name: "João Silva",
    email: "joao.silva@email.com",
    phone: "(11) 91234-5678",
    birthDate: "1990-05-10",
    cpf: "12345678900",
    rg: "12.345.678-9",
    address: "Rua A, 123",
    neighborhood: "Centro",
    city: "São Paulo",
    state: "SP",
    zipCode: "01000000",
  },
  {
    name: "Maria Oliveira",
    email: "maria.oliveira@email.com",
    phone: "(21) 98765-4321",
    birthDate: "1985-07-22",
    cpf: "98765432100",
    rg: "98.765.432-1",
    address: "Rua B, 456",
    neighborhood: "Jardim Botânico",
    city: "Rio de Janeiro",
    state: "RJ",
    zipCode: "20000000",
  },
  {
    name: "Carlos Santos",
    email: "carlos.santos@email.com",
    phone: "(31) 93456-7890",
    birthDate: "1992-11-15",
    cpf: "23456789012",
    rg: "23.456.789-0",
    address: "Avenida C, 789",
    neighborhood: "Funcionários",
    city: "Belo Horizonte",
    state: "MG",
    zipCode: "30123456",
  },
  {
    name: "Ana Souza",
    email: "ana.souza@email.com",
    phone: "(41) 98123-4567",
    birthDate: "1988-12-02",
    cpf: "34567890123",
    rg: "34.567.890-1",
    address: "Rua D, 101",
    neighborhood: "Bigorrilho",
    city: "Curitiba",
    state: "PR",
    zipCode: "80210100",
  },
  {
    name: "Felipe Pereira",
    email: "felipe.pereira@email.com",
    phone: "(51) 93567-8912",
    birthDate: "1993-03-30",
    cpf: "45678901234",
    rg: "45.678.901-2",
    address: "Rua E, 202",
    neighborhood: "Moinhos de Vento",
    city: "Porto Alegre",
    state: "RS",
    zipCode: "90035200",
  },
  {
    name: "Patrícia Costa",
    email: "patricia.costa@email.com",
    phone: "(61) 98123-0987",
    birthDate: "1995-09-10",
    cpf: "56789012345",
    rg: "56.789.012-3",
    address: "Avenida F, 303",
    neighborhood: "Asa Sul",
    city: "Brasília",
    state: "DF",
    zipCode: "70070100",
  },
  {
    name: "Lucas Almeida",
    email: "lucas.almeida@email.com",
    phone: "(85) 99876-5432",
    birthDate: "1994-06-18",
    cpf: "67890123456",
    rg: "67.890.123-4",
    address: "Rua G, 404",
    neighborhood: "Aldeota",
    city: "Fortaleza",
    state: "CE",
    zipCode: "60160050",
  },
  {
    name: "Fernanda Rodrigues",
    email: "fernanda.rodrigues@email.com",
    phone: "(11) 94321-8765",
    birthDate: "1991-02-25",
    cpf: "78901234567",
    rg: "78.901.234-5",
    address: "Rua H, 505",
    neighborhood: "Vila Progredior",
    city: "São Paulo",
    state: "SP",
    zipCode: "02012030",
  },
  {
    name: "Ricardo Martins",
    email: "ricardo.martins@email.com",
    phone: "(41) 98765-4321",
    birthDate: "1987-10-05",
    cpf: "89012345678",
    rg: "89.012.345-6",
    address: "Avenida I, 606",
    neighborhood: "Água Verde",
    city: "Curitiba",
    state: "PR",
    zipCode: "81050210",
  },
  {
    name: "Juliana Rocha",
    email: "juliana.rocha@email.com",
    phone: "(21) 96321-0987",
    birthDate: "1996-04-13",
    cpf: "90123456789",
    rg: "90.123.456-7",
    address: "Rua J, 707",
    neighborhood: "Copacabana",
    city: "Rio de Janeiro",
    state: "RJ",
    zipCode: "22010010",
  },
];

const professionals: HealthProfessional[] = [
  {
    healthArea: "Speech Therapy",
    phone: "(11) 98765-4321",
    professionalDocument: "CRFa 12345",
    email: "ana.fonou@email.com",
    name: "Ana Fonseca",
    rg: "12.345.678-9",
    address: {
      state: "SP",
      city: "São Paulo",
      neighborhood: "Centro",
      street: "Rua A",
      number: "123",
      zipCode: "01000-000",
      complement: "Apto 101",
    },
  },
  {
    healthArea: "Physiotherapy",
    phone: "(21) 91234-5678",
    professionalDocument: "CREFITO 54321",
    email: "marcos.fisio@email.com",
    name: "Marcos Pereira",
    rg: "98.765.432-1",
    address: {
      state: "RJ",
      city: "Rio de Janeiro",
      neighborhood: "Jardim Botânico",
      street: "Rua B",
      number: "456",
      zipCode: "20000-000",
      complement: "",
    },
  },
  {
    healthArea: "Psychology",
    phone: "(31) 93456-7890",
    professionalDocument: "CRP 67890",
    email: "juliana.psico@email.com",
    name: "Juliana Souza",
    rg: "23.456.789-0",
    address: {
      state: "MG",
      city: "Belo Horizonte",
      neighborhood: "Funcionários",
      street: "Avenida C",
      number: "789",
      zipCode: "30123-456",
      complement: "Sala 202",
    },
  },
  {
    healthArea: "Occupational Therapy",
    phone: "(41) 99876-1234",
    professionalDocument: "CREFITO 13579",
    email: "roberto.to@email.com",
    name: "Roberto Lima",
    rg: "34.567.890-1",
    address: {
      state: "PR",
      city: "Curitiba",
      neighborhood: "Bigorrilho",
      street: "Rua D",
      number: "101",
      zipCode: "80210-100",
      complement: "",
    },
  },
  {
    healthArea: "Psychopedagogy",
    phone: "(61) 98123-4567",
    professionalDocument: "CRP 24680",
    email: "marcia.psi@email.com",
    name: "Márcia Andrade",
    rg: "56.789.012-3",
    address: {
      state: "DF",
      city: "Brasília",
      neighborhood: "Asa Sul",
      street: "Avenida F",
      number: "303",
      zipCode: "70070-100",
      complement: "Bloco B",
    },
  },
  {
    healthArea: "Neurology",
    phone: "(85) 98765-4321",
    professionalDocument: "CRM 112233",
    email: "carlos.neuro@email.com",
    name: "Carlos Moreira",
    rg: "67.890.123-4",
    address: {
      state: "CE",
      city: "Fortaleza",
      neighborhood: "Aldeota",
      street: "Rua G",
      number: "404",
      zipCode: "60160-050",
      complement: "",
    },
  },
  {
    healthArea: "Psychiatry",
    phone: "(11) 97654-3210",
    professionalDocument: "CRM 445566",
    email: "luana.psi@email.com",
    name: "Luana Rocha",
    rg: "78.901.234-5",
    address: {
      state: "SP",
      city: "São Paulo",
      neighborhood: "Vila Progredior",
      street: "Rua H",
      number: "505",
      zipCode: "02012-030",
      complement: "Apto 303",
    },
  },
  {
    healthArea: "Nursing",
    phone: "(71) 92345-6789",
    professionalDocument: "COREN 998877",
    email: "fernando.enf@email.com",
    name: "Fernando Nogueira",
    rg: "89.012.345-6",
    address: {
      state: "BA",
      city: "Salvador",
      neighborhood: "Pituba",
      street: "Avenida I",
      number: "606",
      zipCode: "41810-010",
      complement: "",
    },
  },
  {
    healthArea: "Pediatrics",
    phone: "(51) 93456-1234",
    professionalDocument: "CRM 556677",
    email: "claudia.ped@email.com",
    name: "Cláudia Martins",
    rg: "90.123.456-7",
    address: {
      state: "RS",
      city: "Porto Alegre",
      neighborhood: "Moinhos de Vento",
      street: "Rua E",
      number: "202",
      zipCode: "90035-200",
      complement: "Sala 101",
    },
  },
];

export async function saveAppointment(
  newAppointment: AppointmentCreateDTO,
  id?: string
): Promise<Appointment> {
  try {
    if (!id) {
      delete newAppointment.justification;
    }

    const res = await fetch(
      `${API_BASE_URL}/agendamentos${id ? `/${id}` : ""}`,
      {
        method: id ? "PUT" : "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newAppointment),
      }
    );
    const data = await res.json();
    return data as Appointment;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function saveCompletedAppointment(
  appointment: Appointment
): Promise<ConsultationHistoryResponseDTO> {
  try {
    const dto: ConsultationHistoryCreateDTO = {
      appointmentId: appointment.id,
      consultationDate: appointment.nextAppointment,
      consultationTime: appointment.nextAppointmentTime,
      wasPerformed: true,
      justification: appointment.justification,
    };
    const res = await fetch(`${API_BASE_URL}/historico-consultas`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(dto),
    });
    console.log(res);
    const data = await res.json();

    const [ano, mes, dia] = appointment.nextAppointment.split("-");
    const emDate = new Date(parseInt(ano), parseInt(mes), parseInt(dia));
    emDate.setDate(emDate.getDate() + appointment.frequencyDays);
    const nextAppointment = `${String(emDate.getFullYear()).padStart(
      2,
      "0"
    )}-${String(emDate.getMonth()).padStart(2, "0")}-${String(
      emDate.getDate()
    ).padStart(2, "0")}`;

    appointment.nextAppointment = nextAppointment;
    if (appointment.patient?.id && appointment.professional?.id) {
      const { patient, professional, id, ...appointmentDTO } = {
        ...appointment,
        patientId: appointment.patient.id,
        professionalId: appointment.professional.id,
      };
      appointmentDTO.confirmed = false;
      await saveAppointment(appointmentDTO, appointment.id);
    }

    return data as ConsultationHistoryResponseDTO;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function getAppointments(
  data?: string,
  hora?: string
): Promise<Appointment[]> {
  try {
    if (data) {
      let [ano, mes, dia] = data?.split("-").map(Number);
      data = `${ano}-${(mes - 1).toString().padStart(2, "0")}-${dia
        .toString()
        .padStart(2, "0")}`;
    }

    const response = await fetch(
      `${API_BASE_URL}/agendamentos${data ? `?data=${data}` : ""}${
        hora ? `&hora=${hora}` : ""
      }`
    ).then((res) => res.json());

    console.log(response);

    const appointments: Appointment[] = [];
    for (let appointment of response.content) {
      const patient = await fetch(
        `${API_BASE_URL}/pacientes/${appointment.patientId}`
      ).then((res) => res.json());
      const professional = await fetch(
        `${API_BASE_URL}/profissionais/${appointment.professionalId}`
      ).then((res) => res.json());

      appointments.push({ ...appointment, patient, professional });
    }

    return appointments;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function getAppointmentById(id: string): Promise<Appointment> {
  try {
    const appointment = await fetch(`${API_BASE_URL}/agendamentos/${id}`).then(
      (res) => res.json()
    );
    const patient = await fetch(
      `${API_BASE_URL}/pacientes/${appointment.patientId}`
    ).then((res) => res.json());
    const professional = await fetch(
      `${API_BASE_URL}/profissionais/${appointment.professionalId}`
    ).then((res) => res.json());

    return { ...appointment, patient, professional };
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function getCompletedAppointmentById(
  id: string
): Promise<Appointment> {
  try {
    const completedAppointment: ConsultationHistoryResponseDTO = await fetch(
      `${API_BASE_URL}/historico-consultas/${id}`
    ).then((res) => res.json());
    const appointment: Appointment = await getAppointmentById(
      completedAppointment.appointmentId
    );
    appointment.nextAppointment = completedAppointment.consultationDate;
    appointment.nextAppointmentTime = completedAppointment.consultationTime;
    return appointment;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function deleteAppointment(
  id: string,
  completed: boolean
): Promise<void> {
  try {
    await fetch(
      `${API_BASE_URL}/${
        completed ? "historico-consultas" : "agendamentos"
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

export async function getAppointmentHistory(
  appointmentId: string,
  page: number = 0,
  size: number = 100
): Promise<{ content: HistoryEntry[] }> {
  try {
    const response = await fetch(
      `${API_BASE_URL}/historico-consultas?appointmentId=${appointmentId}&page=${page}&size=${size}`
    );

    if (!response.ok) {
      throw new Error(`Erro ao buscar histórico: ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching appointment history:", error);
    throw error;
  }
}

export async function getPatientsWithAbsences(
  minAbsences: number = 3
): Promise<PatientWithAbsences[]> {
  try {
    const appointments = await getAppointments();

    const patientAbsenceMap = new Map<
      string,
      {
        patient: Patient;
        count: number;
        lastDate: string;
        absences: HistoryEntry[];
      }
    >();

    for (const appointment of appointments) {
      try {
        const patientId = appointment.patient.id;
        if (!patientId) continue;

        const historyResponse = await getAppointmentHistory(appointment.id);
        const absences = historyResponse.content.filter(
          (history) => !history.wasPerformed
        );

        if (absences.length > 0) {
          const currentData = patientAbsenceMap.get(patientId) || {
            patient: appointment.patient,
            count: 0,
            lastDate: "",
            absences: [],
          };

          currentData.count += absences.length;
          currentData.absences.push(...absences);

          const latestAbsence = absences.reduce((latest, current) =>
            new Date(current.consultationDate) >
            new Date(latest.consultationDate)
              ? current
              : latest
          );

          if (
            !currentData.lastDate ||
            new Date(latestAbsence.consultationDate) >
              new Date(currentData.lastDate)
          ) {
            currentData.lastDate = latestAbsence.consultationDate;
          }

          patientAbsenceMap.set(patientId, currentData);
        }
      } catch (error) {
        console.error(
          `Error processing appointment for patient ${appointment.patient.name}:`,
          error
        );
      }
    }

    return Array.from(patientAbsenceMap.values())
      .filter((item) => item.count >= minAbsences)
      .map((item) => ({
        patient: item.patient,
        absenceCount: item.count,
        lastAbsenceDate: item.lastDate,
        absences: item.absences.sort(
          (a, b) =>
            new Date(b.consultationDate).getTime() -
            new Date(a.consultationDate).getTime()
        ),
      }))
      .sort((a, b) => b.absenceCount - a.absenceCount);
  } catch (error) {
    console.error("Error fetching patients with absences:", error);
    throw error;
  }
}

export async function getAbsenceStatistics(): Promise<{
  totalPatients: number;
  totalAppointments: number;
  patientsWithMinAbsences: number;
}> {
  try {
    const [patients, appointments, patientsWithAbsences] = await Promise.all([
      getPatients(),
      getAppointments(),
      getPatientsWithAbsences(3),
    ]);

    return {
      totalPatients: patients.length,
      totalAppointments: appointments.length,
      patientsWithMinAbsences: patientsWithAbsences.length,
    };
  } catch (error) {
    console.error("Error fetching absence statistics:", error);
    throw error;
  }
}

export async function getPatients(): Promise<Patient[]> {
  try {
    const response = await fetch(
      `${API_BASE_URL}/pacientes?page=0&size=100`
    ).then((res) => res.json());
    let returndPatients: Patient[] = response.content;
    const exists = new Set(returndPatients.map((p: Patient) => p.cpf));

    if (!returndPatients.length || !patients.some((p) => !exists.has(p.cpf))) {
      for (const patient of patients) {
        if (!exists.has(patient.cpf)) {
          const createdPatient = await fetch(
            `${API_BASE_URL}/pacientes/create`,
            {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(patient),
            }
          ).then((res) => res.json());

          returndPatients.push(createdPatient);
        }
      }
    }

    return returndPatients.filter((patient, index, self) => {
      return self.findIndex((p) => p.cpf === patient.cpf) === index;
    });
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function getHealthProfessionals(): Promise<HealthProfessional[]> {
  try {
    const response = await fetch(
      `${API_BASE_URL}/profissionais?page=0&size=100`
    ).then((res) => res.json());
    let returnedProfessionals: HealthProfessional[] = response.content || [];
    const exists = new Set(
      returnedProfessionals.map(
        (p: HealthProfessional) => p.professionalDocument
      )
    );

    for (const professional of professionals) {
      if (!exists.has(professional.professionalDocument)) {
        const res = await fetch(`${API_BASE_URL}/profissionais`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(professional),
        });

        if (!res.ok) {
          continue;
        }

        const createdProfessional = await res.json();
        returnedProfessionals.push(createdProfessional);
      }
    }

    return returnedProfessionals.filter((professional, index, self) => {
      return (
        self.findIndex(
          (p) => p.professionalDocument === professional.professionalDocument
        ) === index
      );
    });
  } catch (error) {
    console.error("Error fetching professionals:", error);
    throw error;
  }
}

export async function getHealthProfessional(
  id: string
): Promise<HealthProfessional> {
  try {
    const professional = await fetch(
      `${API_BASE_URL}/profissionais/${id}`
    ).then((res) => res.json());

    return professional;
  } catch (error) {
    console.error(`Error fetching professional with ID ${id}:`, error);
    throw error;
  }
}

export async function getHealthAreas(): Promise<string[]> {
  try {
    const professionals = await getHealthProfessionals();
    const areas = professionals.map((p) => p.healthArea);
    const setList: string[] = [];
    new Set(areas).forEach((e) => setList.push(e as string));

    return setList;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export const toggleConfirmation = async (id: string) => {
  const appointment = await getAppointmentById(id);

  if (!appointment.patient.id || !appointment.professional.id) return;

  const { patient, professional, ...dto } = {
    ...appointment,
    patientId: appointment.patient.id,
    professionalId: appointment.professional.id,
  };

  dto.confirmed = !dto.confirmed;

  await saveAppointment(dto, id);
};
