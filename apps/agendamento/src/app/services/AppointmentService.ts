import { Page } from "@/types/pagination";

type UUID = string;

const API_BASE_URL = process.env.API_BASE_URL || "http://localhost:8093";

export interface AnnualRegistry{
  id: UUID;
  bpc: string;
  diseases: string;
  familyIncome: number;
  year: string;
  patient: Patient;
  disorders: Disorder;
  endDate: string;
  professional: Professional;

}

export interface Appointment {
  id: UUID;
  professionalId: UUID;
  serviceId: UUID;
  annualRegistration: AnnualRegistry;
  frequencyDays: number;
  hour: string;
  initialDate: string;
  endDate: string;
  isActive: boolean;
  creationDate: string;

}

export interface CreateAppointmentDTO{
  professionalId: UUID;
  serviceId: UUID;
  annualRegistrationId: UUID;
  frequencyDays: number;
  initialDate: string;
  hour: string;
}

export interface AppointmentResponseDTO {
  id: UUID;
  professionalId: UUID;
  serviceId: UUID;
  annualRegistration?: AnnualRegistry;
  frequencyDays: number;
  hour: string;
  initialDate: string;
  endDate: string;
  isActive: boolean;
  creationDate: string;
}

export interface GeneratedAppointment{
  id: UUID;
  appointment?: Appointment;
  scheduledDateTime: string;
  overriddenDateTime: string;
  performed: boolean;
  cancelled: boolean;
  cancellationReason: string;
  patientId: UUID;
}

export interface GeneratedAppointmentResponseDTO{
  id: UUID;
  appointmentId: UUID;
  scheduledDateTime: string;
  overriddenDateTime: string;
  performed: boolean;
  cancelled: boolean;
  cancellationReason: string;
  patientId: UUID;
  effectiveDateTime: string;
}



export interface Absence {
  id: UUID;
  generatedAppointment?: GeneratedAppointment;
  absenceDate: string;
  justification: string;
  notified: boolean;

}

export interface Patient {
  id?: string;
  name: string;
  fullName: string;
  birthplace: string;
  birthDate: string;
  contact: string;
  birthCertificateNumber: string;
  registryOffice: string;
  fls: string;
  book: string;
  rg: string;
  issueDate: string;
  issuingAgency: string;
  cpf: string;
  cns: string;
  nis: string;
  registrationDate: string;
  allergies: string;
  isStudent: boolean;
  address?: Address; 
  guardian?: Guardian; 
  parents?: Parent[];
  vaccines?: Vaccine[];
}

export interface Address {
  id: UUID;
  city: string;
  cep: string;
  state: string;
  neighborhood: string;
  street: string;
  number: string;
  complement: string;
}


export interface Guardian{
  id: UUID;
  name: string;
  contact: string;
  kinship: string;
  address?: Address;

}

export interface Parent{
  id: UUID;
  name: string;
  rg: string;
  cpf: string;
  isAlive: boolean;
  profession: string;
  kinship: string;
  patient?: Patient;
}

export interface Vaccine{
  id: UUID;
  name: string;
}

export interface UpdateAppointmentRuleDTO {
  newFrequency: number;
  newTime: string;
}

export interface RescheduleGeneratedAppointmentDTO {
  newDateTime: string;
}

export interface CancelGeneratedAppointmentDTO {
  reason: string;
}

export interface Professional {
  id?: string;
  healthArea: string;
  phone: string; 
  professionalDoc: string; 
  email: string;
  name: string; 
  rg: string;
  address: Address; 
}

export interface Disorder{
  id: UUID;
  name: string;
}

const patients: Patient[] = [
  {
    name: "João",
    fullName: "Silva",
    birthplace: "Brasileiro",
    birthDate: "1990-05-10",
    contact: "(11) 91234-5678",
    birthCertificateNumber: "112233858000099999",
    registryOffice: "cartorio silva",
    fls: "4",
    book: "1",
    rg: "12.345.678-9",
    issueDate: "1990-05-10",
    issuingAgency: "Cartorio 123",
    cpf: "12345678900",
    cns: "1990-05-10",
    nis: "1990-05-10",
    registrationDate:"2010-05-10",
    allergies: "Dipirona",
    isStudent: true,
    address: {
      id: "123id456iidd",
      city: "string",
      cep: "58135000",
      state: "Paraíba",
      neighborhood: "centro",
      street: "Rua das flores",
      number: "120",
      complement: "casa rosa",
    }, 
    guardian: {
      id: "guardian-id", // Adicionado ID faltante
      name: "Guardian Name",
      contact: "guardian contact",
      kinship: "parent",
    },
    parents: [
      {
        id: "parent-id",
        name: "Parent Name",
        rg: "parent rg",
        cpf: "parent cpf",
        isAlive: true,
        profession: "profession",
        kinship: "father",
      },
    ],
    vaccines: [
      {
        id: "vaccine-id",
        name: "Vaccine Name",
      },
    ],
  }, 
  {
    name: "Maria",
    fullName: "Oliveira",
    birthplace: "Brasileira",
    birthDate: "1990-05-10",
    contact: "(11) 91234-5698",
    birthCertificateNumber: "112233858000099999",
    registryOffice: "cartorio silva",
    fls: "4",
    book: "1",
    rg: "12.345.678-9",
    issueDate: "1990-05-10",
    issuingAgency: "Cartorio 123",
    cpf: "12345678900",
    cns: "1990-05-10",
    nis: "1990-05-10",
    registrationDate:"2010-05-10",
    allergies: "Dipirona",
    isStudent: true,
    address: {
      id: "123id456iidd",
      city: "Cidade1",
      cep: "58135000",
      state: "Paraíba",
      neighborhood: "centro",
      street: "Rua das pedras",
      number: "23",
      complement: "casa azul",
    }, 
    guardian: {
      id: "guardian-id2", 
      name: "Ana",
      contact: "(86) 90999-8899",
      kinship: "pai",
    },
    parents: [
      {
        id: "parent-id2",
        name: "Parent Name",
        rg: "parent rg",
        cpf: "parent cpf",
        isAlive: true,
        profession: "profession",
        kinship: "father",
      },
    ],
    vaccines: [
      {
        id: "vaccine-id",
        name: "Vaccine Name",
      },
    ],
  }, 
];

const professionals: Professional[] = [
 {
    healthArea: "Medicina", 
    phone: "(11) 98765-4321",
    professionalDoc: "CRFa 12345",
    email: "ana.fonou@email.com",
    name: "Ana Fonseca",
    rg: "12.345.678-9",
    address: {
      id: "address-id-prof-1",
      city: "São Paulo",
      cep: "01000-000",
      state: "SP",
      neighborhood: "Centro",
      street: "Rua A",
      number: "123",
      complement: "Apto 101",
    },
  },
];

// creat() do back
export async function saveAppointment(
  dto: CreateAppointmentDTO): Promise<void> {
  try {
    // Precisa mandar todos os atributos ao criar/reagendar?

    // if (!id) {
    //   delete newAppointment.justificativa;
    // }

    const res = await fetch(`${API_BASE_URL}/appointments`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(dto),
      }
    );

    if (!res.ok) {
      throw new Error("Error creating appointment");
    }

  } catch (error) {
    console.log(error);
    throw error;
  }
}

// export async function saveAppointmentMade(
//   appointment: Appointment
// ){
//   try {
//     const dto: HistoricoConsultaCreateDTO = {
//       idAgendamento: appointment.id,
//       dataConsulta: appointment.proximaConsulta,
//       horaConsulta: appointment.horaProximaConsulta,
//       foiRealizada: true,
//       justificativa: appointment.justificativa,
//     };
//     const res = await fetch(`${API_BASE_URL}/historico-consultas`, {
//       method: "POST",
//       headers: {
//         "Content-Type": "application/json",
//       },
//       body: JSON.stringify(dto),
//     });
//     console.log(res);
//     const data = await res.json();

//     const [ano, mes, dia] = appointment.proximaConsulta.split("-");
//     const emDate = new Date(parseInt(ano), parseInt(mes), parseInt(dia));
//     emDate.setDate(emDate.getDate() + 15);
//     const proximaConsulta = `${String(emDate.getFullYear()).padStart(
//       2,
//       "0"
//     )}-${String(emDate.getMonth()).padStart(2, "0")}-${String(
//       emDate.getDate()
//     ).padStart(2, "0")}`;

//     appointment.proximaConsulta = proximaConsulta;
//     if (appointment.paciente?.id && appointment.profissional?.id) {
//       const { paciente, profissional, id, ...appointmentDTO } = {
//         ...appointment,
//         idPaciente: appointment.paciente.id,
//         idProfissional: appointment.profissional.id,
//       };
//       appointmentDTO.confirmado = false;
//       await saveAppointment(appointmentDTO, appointment.id);
//     }

//     return data as HistoricoConsultaResponseDTO;
//   } catch (error) {
//     console.log(error);
//     throw error;
//   }
// }


// getAll() do back


export async function getAppointments(date?: string, time?: string,
   page: number = 0, size: number = 20
): Promise<Page<AppointmentResponseDTO>> {
  try {
    const query = new URLSearchParams({ page: `${page}`, size: `${size}` });
    if (date) {
      query.append("date", date);
      // let [year, month, day] = date?.split("-").map(Number);
      // date = `${year}-${(month-1).toString().padStart(2, "0")}-${day.toString().padStart(2, "0")}`;
    }
    if (time) {
      query.append("time", time);
    }

    const response = await fetch(`${API_BASE_URL}/appointments?${query}`);
    if (!response.ok) {
      throw new Error("Error searching for appointments");
    }
    return await response.json();

    // console.log(response);

    // const appointments: Appointment[] = [];
    // for (let appointment of response.content) {
    //   const paciente = await fetch(
    //     `${API_BASE_URL}/pacientes/${appointment.idPaciente}`
    //   ).then((res) => res.json());
    //   const profissional = await fetch(
    //     `${API_BASE_URL}/profissionais/${appointment.idProfissional}`
    //   ).then((res) => res.json());

    //   appointments.push({ ...appointment, paciente, profissional });
    // }

    // return appointments;
  } catch (error) {
    console.log(error);
    throw error;
  }
}


// get() no back
export async function getAppointmentById(id: UUID): Promise<AppointmentResponseDTO> {
  try {
    const appointment = await fetch(`${API_BASE_URL}/appointments/${id}`);
    if (!appointment.ok) {
      throw new Error("Error fetching appointment");
    }

    return await appointment.json();

    // const paciente = await fetch(
    //   `${API_BASE_URL}/patients/${appointment.}`
    // ).then((res) => res.json());
    // const profissional = await fetch(
    //   `${API_BASE_URL}/profissionais/${appointment.idProfissional}`
    // ).then((res) => res.json());

    // return { ...appointment, paciente, profissional };

  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function updateAppointmentRule(id: UUID, dto: UpdateAppointmentRuleDTO): Promise<AppointmentResponseDTO> {
  try {
    const response = await fetch(`${API_BASE_URL}/appointments/${id}/rule`, {
      method: "PATCH",
      headers: { "Content-Type": "application/json" 
      },
      body: JSON.stringify(dto),
    });

    if (!response.ok) {
      throw new Error("Error updating appointment rule");
    }

    return await response.json();

  } catch(error) {
    console.log(error);
    throw error;
  }
}

// export async function getAppointmentRealizadoById(
//   id: string
// ) {
//   // : Promise<Appointment>
//   try {
//     // const agendamentoRealizado: HistoricoConsultaResponseDTO = await fetch(
//     //   `${API_BASE_URL}/historico-consultas/${id}`
//     // ).then((res) => res.json());
//     // const appointment: Appointment = await getAppointmentById(
//     //   agendamentoRealizado.idAgendamento
//     // );
//     // appointment.proximaConsulta = agendamentoRealizado.dataConsulta;
//     // appointment.horaProximaConsulta = agendamentoRealizado.horaConsulta;
//     // return appointment;
//   } catch (error) {
//     console.log(error);
//     throw error;
//   }
// }

// delete() no back


export async function deleteAppointment(id: UUID): Promise<void> {
  try {
    const response = await fetch(`${API_BASE_URL}/appointments/${id}`,{
        method: "DELETE",
      }
    );

    if (!response.ok) {
      throw new Error("Error deleting appointment")
    }

  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function rescheduleGeneratedAppointment(id: UUID, dto: RescheduleGeneratedAppointmentDTO): Promise<GeneratedAppointmentResponseDTO>{
  try {
    const response = await fetch(`${API_BASE_URL}/generated/${id}/reschedule`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dto),
    });
    
    if (!response.ok) {
      throw new Error("Error rescheduling appointment")
    }

    return await response.json();

  } catch(error){
    console.log(error);
    throw error;
  }

}

export async function markAsPerformed(id: UUID): 
  Promise<GeneratedAppointmentResponseDTO>{

    try {
      const response = await fetch(`${API_BASE_URL}/generated/${id}/performed`, {
      method: "PUT",
    });

    if (!response.ok){
      throw new Error(`Error marking appointment as performed`);
    }
    
    return await response.json();
    } catch (error) {
      console.error(error);
      throw error;
    }

}

export async function cancelGeneratedAppointment(id: UUID, dto: CancelGeneratedAppointmentDTO): Promise<GeneratedAppointmentResponseDTO>{

   try {
    const response = await fetch(`${API_BASE_URL}/generated/${id}/cancel`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dto),
    });

    if (!response.ok) throw new Error(`Error cancelling appointment`);
    return await response.json();
  } catch (error) {
    console.error(error);
    throw error;
  }

}


export async function listByPatient(patientId: UUID, start: string, 
  end: string, page: 0, size: 20): 
  Promise<Page<GeneratedAppointmentResponseDTO>>{

    try {
    const query = new URLSearchParams({ page: `${page}`, size: `${size}` });
    if (start) {
      query.append("start", start);
    }
    if (end) {
      query.append("end", end);
    }

    const response = await fetch(`${API_BASE_URL}/patient?${patientId}`);
    if (!response.ok) {
      throw new Error("Error searching for appointments");
    }
    return await response.json();

  } catch (error) {
    console.log(error);
    throw error;
  }
  }


export async function registerAbsence(
  generatedAppointmentId: UUID,
  justification: string
): Promise<Absence> {
  try {
    const body = {
      generatedAppointmentId,
      justification,
      date: new Date().toISOString(),
      notified: false,
    };

    const res = await fetch(`${API_BASE_URL}/absences`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });

    if (!res.ok) throw new Error(`Error registering absence`);

    return await res.json();
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// export async function getPacientes(): Promise<Patient[]> {
//   try {
//     const response = await fetch(
//       `${API_BASE_URL}/pacientes?page=0&size=100`
//     ).then((res) => res.json());
//     let pacientesRetornados: Patient[] = response.content;
//     const existentes = new Set(pacientesRetornados.map((p: Patient) => p.cpf));

//     if (
//       !pacientesRetornados.length ||
//       !patients.some((p) => !existentes.has(p.cpf))
//     ) {
//       for (const paciente of patients) {
//         if (!existentes.has(paciente.cpf)) {
//           const pacienteCriado = await fetch(
//             `${API_BASE_URL}/pacientes/create`,
//             {
//               method: "POST",
//               headers: { "Content-Type": "application/json" },
//               body: JSON.stringify(paciente),
//             }
//           ).then((res) => res.json());

//           pacientesRetornados.push(pacienteCriado);
//         }
//       }
//     }

//     return pacientesRetornados.filter((paciente, index, self) => {
//       return self.findIndex((p) => p.cpf === paciente.cpf) === index;
//     });
//   } catch (error) {
//     console.log(error);
//     throw error;
//   }
// }

export async function getProfissionaisDaSaude(): Promise<Professional[]> {
  try {
    const response = await fetch(
      `${API_BASE_URL}/profissionais?page=0&size=100`
    ).then((res) => res.json());
    let profissionaisRetornados: Professional[] = response.content || [];
    const existentes = new Set(
      profissionaisRetornados.map((p: Professional) => p.professionalDoc)
    );

    for (const profissional of professionals) {
      if (!existentes.has(profissional.professionalDoc)) {
        const res = await fetch(`${API_BASE_URL}/profissionais`, {
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
          (p) => p.professionalDoc === profissional.professionalDoc
        ) === index
      );
    });
  } catch (error) {
    console.error("Error fetching professionals:", error);
    throw error;
  }
}

// export async function getProfissionalDaSaude(
//   id: string
// ): Promise<Professional> {
//   try {
//     const profissional = await fetch(
//       `${API_BASE_URL}/profissionais/${id}`
//     ).then((res) => res.json());

//     return profissional;
//   } catch (error) {
//     console.error(`Error fetching professional with ID ${id}:`, error);
//     throw error;
//   }
// }

export async function getAreasDaSaude(): Promise<string[]> {
  try {
    const profissionais = await getProfissionaisDaSaude();
    const areas = profissionais.map((p) => p.healthArea);
    const setList: string[] = [];
    new Set(areas).forEach((e) => setList.push(e as string));

    return setList;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export const toggleConfirmacao = async (id: UUID) => {
  
  try {
    const appointment = await getAppointmentById(id);

    if (!appointment.professionalId) return;
    
    // Prepara o DTO para envio
    const dto: CreateAppointmentDTO = {
      professionalId: appointment.professionalId,
      serviceId: appointment.serviceId,
      annualRegistrationId: appointment.annualRegistration?.id || "",
      frequencyDays: appointment.frequencyDays,
      initialDate: appointment.initialDate,
      hour: appointment.hour,
    };
  

    await saveAppointment(dto);
    console.log("Query updated successfully:", dto);

  } catch (error) {
    console.error(error);
    throw error;
  }
};
