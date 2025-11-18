import { Page } from '@/types/pagination';

type UUID = string;

const API_BASE_URL = process.env.API_BASE_URL || 'http://localhost:8090';
const USE_MOCK_DATA = process.env.NEXT_PUBLIC_USE_MOCK_DATA === 'false' || false; // Forçar true para desenvolvimento
const MOCK_DELAY = 500; // delay simulado em ms

// ========== DADOS MOCKADOS ==========

const mockPatients: Patient[] = [
  {
    id: 'patient-1',
    name: 'João',
    fullName: 'João Silva Santos',
    birthplace: 'São Paulo',
    birthDate: '1990-05-10',
    contact: '(11) 91234-5678',
    birthCertificateNumber: '112233858000099999',
    registryOffice: 'Cartório Silva',
    fls: '4',
    book: '1',
    rg: '12.345.678-9',
    issueDate: '2010-05-10',
    issuingAgency: 'SSP-SP',
    cpf: '123.456.789-00',
    cns: '123456789012345',
    nis: '12345678901',
    registrationDate: '2020-05-10',
    allergies: 'Dipirona, Penicilina',
    isStudent: false,
    address: {
      id: 'address-1',
      city: 'São Paulo',
      cep: '01234-567',
      state: 'SP',
      neighborhood: 'Centro',
      street: 'Rua das Flores',
      number: '123',
      complement: 'Apto 45',
    },
  },
  {
    id: 'patient-2',
    name: 'Maria',
    fullName: 'Maria Oliveira Costa',
    birthplace: 'Rio de Janeiro',
    birthDate: '1985-08-15',
    contact: '(21) 99876-5432',
    birthCertificateNumber: '998877665500011111',
    registryOffice: 'Cartório Central',
    fls: '2',
    book: '3',
    rg: '98.765.432-1',
    issueDate: '2005-08-15',
    issuingAgency: 'SSP-RJ',
    cpf: '987.654.321-00',
    cns: '987654321098765',
    nis: '98765432109',
    registrationDate: '2019-03-20',
    allergies: 'Nenhuma',
    isStudent: true,
    address: {
      id: 'address-2',
      city: 'Rio de Janeiro',
      cep: '22345-678',
      state: 'RJ',
      neighborhood: 'Copacabana',
      street: 'Avenida Atlântica',
      number: '456',
      complement: 'Bloco B',
    },
  },
];

const mockProfessionals: Professional[] = [
  {
    id: 'professional-1',
    healthArea: 'Fisioterapia',
    phone: '(11) 3456-7890',
    professionalDoc: 'CREFITO 12345/SP',
    email: 'ana.fisio@email.com',
    name: 'Ana Fonseca',
    rg: '11.223.344-5',
    address: {
      id: 'address-prof-1',
      city: 'São Paulo',
      cep: '01234-000',
      state: 'SP',
      neighborhood: 'Centro',
      street: 'Rua Augusta',
      number: '789',
      complement: 'Sala 501',
    },
  },
  {
    id: 'professional-2',
    healthArea: 'Psicologia',
    phone: '(11) 4567-8901',
    professionalDoc: 'CRP 06/123456',
    email: 'carlos.psi@email.com',
    name: 'Carlos Mendes',
    rg: '22.334.455-6',
    address: {
      id: 'address-prof-2',
      city: 'São Paulo',
      cep: '01345-000',
      state: 'SP',
      neighborhood: 'Jardins',
      street: 'Alameda Santos',
      number: '1001',
      complement: 'Conjunto 302',
    },
  },
  {
    id: 'professional-3',
    healthArea: 'Terapia Ocupacional',
    phone: '(11) 5678-9012',
    professionalDoc: 'CREFITO 54321/SP',
    email: 'beatriz.to@email.com',
    name: 'Beatriz Lima',
    rg: '33.445.566-7',
    address: {
      id: 'address-prof-3',
      city: 'São Paulo',
      cep: '01456-000',
      state: 'SP',
      neighborhood: 'Moema',
      street: 'Rua Groenlândia',
      number: '200',
      complement: 'Sala 101',
    },
  },
];

const mockAnnualRegistries: AnnualRegistry[] = [
  {
    id: 'annual-1',
    bpc: 'Sim',
    diseases: 'Hipertensão, Diabetes',
    familyIncome: 2500.0,
    year: '2024',
    patient: mockPatients[0],
    disorders: { id: 'disorder-1', name: 'TEA' },
    endDate: '2024-12-31',
  },
  {
    id: 'annual-2',
    bpc: 'Não',
    diseases: 'Ansiedade',
    familyIncome: 1800.0,
    year: '2024',
    patient: mockPatients[1],
    disorders: { id: 'disorder-2', name: 'TDAH' },
    endDate: '2024-12-31',
  },
];

const mockAppointments: AppointmentResponseDTO[] = [
  {
    id: 'appointment-1',
    professional: mockProfessionals[2],
    serviceId: 'service-1',
    annualRegistration: mockAnnualRegistries[0],
    frequencyDays: 15,
    hour: '14:30:00',
    initialDate: '2024-01-15',
    endDate: '2024-12-31',
    isActive: true,
    creationDate: '2024-01-01T10:00:00',
  },
  {
    id: 'appointment-2',
    professional: mockProfessionals[0],
    serviceId: 'service-2',
    annualRegistration: mockAnnualRegistries[1],
    frequencyDays: 30,
    hour: '09:00:00',
    initialDate: '2024-01-20',
    endDate: '2024-12-31',
    isActive: false,
    creationDate: '2024-01-02T14:30:00',
  },
  {
    id: 'appointment-3',
    professional: mockProfessionals[1],
    serviceId: 'service-3',
    annualRegistration: mockAnnualRegistries[0],
    frequencyDays: 7,
    hour: '16:00:00',
    initialDate: '2024-02-01',
    endDate: '2024-06-30',
    isActive: true,
    creationDate: '2024-01-15T09:00:00',
  },
];

const mockGeneratedAppointments: GeneratedAppointmentResponseDTO[] = [
  {
    id: 'generated-1',
    appointmentId: 'appointment-1',
    scheduledDateTime: '2024-01-15T14:30:00',
    overriddenDateTime: '2024-01-15T15:00:00',
    performed: true,
    cancelled: false,
    cancellationReason: '',
    patientId: 'patient-1',
    effectiveDateTime: '2024-01-15T15:00:00',
  },
  {
    id: 'generated-2',
    appointmentId: 'appointment-2',
    scheduledDateTime: '2024-01-20T09:00:00',
    overriddenDateTime: '2024-01-20T09:00:00',
    performed: false,
    cancelled: false,
    cancellationReason: '',
    patientId: 'patient-2',
    effectiveDateTime: '2024-01-20T09:00:00',
  },
];

// ========== INTERFACES ==========

export interface AnnualRegistry {
  id: UUID;
  bpc: string;
  diseases: string;
  familyIncome: number;
  year: string;
  patient: Patient;
  disorders: Disorder;
  endDate: string;
}

export interface Appointment {
  id: UUID;
  professional: Professional;
  serviceId: UUID;
  annualRegistration: AnnualRegistry;
  frequencyDays: number;
  hour: string;
  initialDate: string;
  endDate: string;
  isActive: boolean;
  creationDate: string;
}

export interface CreateAppointmentDTO {
  professionalId: UUID;
  serviceId: UUID;
  annualRegistrationId: UUID;
  frequencyDays: number;
  initialDate: string;
  hour: string;
}

export interface AppointmentResponseDTO {
  id: UUID;
  professional: Professional;
  serviceId: UUID;
  annualRegistration: AnnualRegistry;
  frequencyDays: number;
  hour: string;
  initialDate: string;
  endDate: string;
  isActive: boolean;
  creationDate: string;
}

export interface GeneratedAppointment {
  id: UUID;
  appointment?: Appointment;
  scheduledDateTime: string;
  overriddenDateTime: string;
  performed: boolean;
  cancelled: boolean;
  cancellationReason: string;
  patientId: UUID;
}

export interface GeneratedAppointmentResponseDTO {
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

export interface Guardian {
  id: UUID;
  name: string;
  contact: string;
  kinship: string;
  address?: Address;
}

export interface Parent {
  id: UUID;
  name: string;
  rg: string;
  cpf: string;
  isAlive: boolean;
  profession: string;
  kinship: string;
  patient?: Patient;
}

export interface Vaccine {
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
  id: string;
  healthSector: string;
  phoneNumber: string;
  professionalDocument: string;
  email: string;
  name: string;
  identityDocument: string;
  address: Address;
}

export interface Disorder {
  id: UUID;
  name: string;
}

// ========== FUNÇÕES AUXILIARES MOCK ==========

const mockFetch = <T>(data: T, success: boolean = true): Promise<T> => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (success) {
        console.log('📦 [MOCK] Retornando dados mockados');
        resolve(data);
      } else {
        reject(new Error('Erro simulado do mock'));
      }
    }, MOCK_DELAY);
  });
};

const mockPage = <T>(content: T[]): Page<T> => ({
  content,
  totalElements: content.length,
  totalPages: 1,
  size: content.length,
  number: 0,
  first: true,
  last: true,
  empty: content.length === 0,
});

// ========== FUNÇÕES PRINCIPAIS COM MOCK ==========

// Criar agendamento
export async function saveAppointment(
  dto: CreateAppointmentDTO
): Promise<void> {
  if (USE_MOCK_DATA) {
    console.log('📦 [MOCK] Criando agendamento:', dto);
    return mockFetch(undefined);
  }

  try {
    const backendDto = {
      professionalId: dto.professionalId,
      serviceId: dto.serviceId,
      annualRegistration: dto.annualRegistrationId,
      frequencyDays: dto.frequencyDays,
      initialDate: dto.initialDate,
      hour: `${dto.hour}:00`,
    };

    const res = await fetch(`${API_BASE_URL}/appointments`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(backendDto),
    });

    if (!res.ok) {
      const errorText = await res.text();
      throw new Error(
        `Error creating appointment: ${res.status} - ${errorText}`
      );
    }
  } catch (error) {
    console.error('Error in saveAppointment, falling back to mock:', error);
    return mockFetch(undefined);
  }
}

// Buscar todos os agendamentos
export async function getAppointments(
  date?: string,
  time?: string,
  page: number = 0,
  size: number = 20
): Promise<Page<AppointmentResponseDTO>> {
  if (USE_MOCK_DATA) {
    let filteredAppointments = [...mockAppointments];

    // Filtro por data
    if (date) {
      filteredAppointments = filteredAppointments.filter(
        appointment => appointment.initialDate === date
      );
    }

    // Filtro por hora
    if (time) {
      filteredAppointments = filteredAppointments.filter(appointment =>
        appointment.hour.startsWith(time)
      );
    }

    return mockFetch(mockPage(filteredAppointments));
  }

  try {
    const query = new URLSearchParams({
      page: `${page}`,
      size: `${size}`,
    });

    if (date) {
      query.append('date', date);
    }
    if (time) {
      query.append('time', time);
    }

    const response = await fetch(`${API_BASE_URL}/appointments?${query}`);
    if (!response.ok) {
      throw new Error('Error searching for appointments');
    }
    const res = await response.json()
    console.log(res)
    return res;
  } catch (error) {
    console.error('Error in getAppointments, falling back to mock:', error);
    return mockFetch(mockPage(mockAppointments));
  }
}

// Buscar agendamento por ID
export async function getAppointmentById(
  id: UUID
): Promise<AppointmentResponseDTO> {
  if (USE_MOCK_DATA) {
    const appointment =
      mockAppointments.find(a => a.id === id) || mockAppointments[0];
    return mockFetch(appointment);
  }

  try {
    const response = await fetch(`${API_BASE_URL}/appointments/${id}`);
    if (!response.ok) {
      throw new Error('Error fetching appointment');
    }
    return await response.json();
  } catch (error) {
    console.error('Error in getAppointmentById, falling back to mock:', error);
    const appointment =
      mockAppointments.find(a => a.id === id) || mockAppointments[0];
    return mockFetch(appointment);
  }
}

// Atualizar regra do agendamento
export async function updateAppointmentRule(
  id: UUID,
  dto: UpdateAppointmentRuleDTO
): Promise<AppointmentResponseDTO> {
  if (USE_MOCK_DATA) {
    console.log('📦 [MOCK] Atualizando regra do agendamento:', id, dto);
    const appointment =
      mockAppointments.find(a => a.id === id) || mockAppointments[0];
    const updatedAppointment = {
      ...appointment,
      frequencyDays: dto.newFrequency,
      hour: `${dto.newTime}:00`,
    };
    return mockFetch(updatedAppointment);
  }

  try {
    const backendDto = {
      newFrequency: dto.newFrequency,
      newTime: `${dto.newTime}:00`,
    };

    const response = await fetch(`${API_BASE_URL}/appointments/${id}/rule`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(backendDto),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(
        `Error updating appointment rule: ${response.status} - ${errorText}`
      );
    }

    return await response.json();
  } catch (error) {
    console.error(
      'Error in updateAppointmentRule, falling back to mock:',
      error
    );
    const appointment =
      mockAppointments.find(a => a.id === id) || mockAppointments[0];
    return mockFetch(appointment);
  }
}

// Deletar agendamento
export async function deleteAppointment(id: UUID): Promise<void> {
  if (USE_MOCK_DATA) {
    console.log('📦 [MOCK] Deletando agendamento:', id);
    return mockFetch(undefined);
  }

  try {
    const response = await fetch(`${API_BASE_URL}/appointments/${id}`, {
      method: 'DELETE',
    });

    if (!response.ok) {
      throw new Error('Error deleting appointment');
    }
  } catch (error) {
    console.error('Error in deleteAppointment, falling back to mock:', error);
    return mockFetch(undefined);
  }
}

// Reagendar agendamento gerado
export async function rescheduleGeneratedAppointment(
  id: UUID,
  dto: RescheduleGeneratedAppointmentDTO
): Promise<GeneratedAppointmentResponseDTO> {
  if (USE_MOCK_DATA) {
    console.log('📦 [MOCK] Reagendando agendamento:', id, dto);
    const generatedAppointment =
      mockGeneratedAppointments.find(g => g.id === id) ||
      mockGeneratedAppointments[0];
    const updatedAppointment = {
      ...generatedAppointment,
      scheduledDateTime: dto.newDateTime,
      overriddenDateTime: dto.newDateTime,
    };
    return mockFetch(updatedAppointment);
  }

  try {
    const backendDto = {
      newDateTime: new Date(dto.newDateTime).toISOString(),
    };

    const response = await fetch(`${API_BASE_URL}/generated/${id}/reschedule`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(backendDto),
    });

    if (!response.ok) {
      throw new Error('Error rescheduling appointment');
    }

    return await response.json();
  } catch (error) {
    console.error(
      'Error in rescheduleGeneratedAppointment, falling back to mock:',
      error
    );
    const generatedAppointment =
      mockGeneratedAppointments.find(g => g.id === id) ||
      mockGeneratedAppointments[0];
    return mockFetch(generatedAppointment);
  }
}

// Marcar como realizado
export async function markAsPerformed(
  id: UUID
): Promise<GeneratedAppointmentResponseDTO> {
  if (USE_MOCK_DATA) {
    console.log('📦 [MOCK] Marcando como realizado:', id);
    const generatedAppointment =
      mockGeneratedAppointments.find(g => g.id === id) ||
      mockGeneratedAppointments[0];
    const updatedAppointment = {
      ...generatedAppointment,
      performed: true,
      effectiveDateTime: new Date().toISOString(),
    };
    return mockFetch(updatedAppointment);
  }

  try {
    const response = await fetch(`${API_BASE_URL}/generated/${id}/performed`, {
      method: 'PUT',
    });

    if (!response.ok) {
      throw new Error(`Error marking appointment as performed`);
    }

    return await response.json();
  } catch (error) {
    console.error('Error in markAsPerformed, falling back to mock:', error);
    const generatedAppointment =
      mockGeneratedAppointments.find(g => g.id === id) ||
      mockGeneratedAppointments[0];
    return mockFetch({ ...generatedAppointment, performed: true });
  }
}

// Cancelar agendamento gerado
export async function cancelGeneratedAppointment(
  id: UUID,
  dto: CancelGeneratedAppointmentDTO
): Promise<GeneratedAppointmentResponseDTO> {
  if (USE_MOCK_DATA) {
    console.log('📦 [MOCK] Cancelando agendamento:', id, dto);
    const generatedAppointment =
      mockGeneratedAppointments.find(g => g.id === id) ||
      mockGeneratedAppointments[0];
    const updatedAppointment = {
      ...generatedAppointment,
      cancelled: true,
      cancellationReason: dto.reason,
    };
    return mockFetch(updatedAppointment);
  }

  try {
    const response = await fetch(`${API_BASE_URL}/generated/${id}/cancel`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(dto),
    });

    if (!response.ok) throw new Error(`Error cancelling appointment`);
    return await response.json();
  } catch (error) {
    console.error(
      'Error in cancelGeneratedAppointment, falling back to mock:',
      error
    );
    const generatedAppointment =
      mockGeneratedAppointments.find(g => g.id === id) ||
      mockGeneratedAppointments[0];
    return mockFetch({
      ...generatedAppointment,
      cancelled: true,
      cancellationReason: dto.reason,
    });
  }
}

// Listar por paciente
export async function listByPatient(
  patientId: UUID,
  start: string,
  end: string,
  page: number = 0,
  size: number = 20
): Promise<Page<GeneratedAppointmentResponseDTO>> {
  if (USE_MOCK_DATA) {
    const patientAppointments = mockGeneratedAppointments.filter(
      g => g.patientId === patientId
    );
    return mockFetch(mockPage(patientAppointments));
  }

  try {
    const query = new URLSearchParams({
      page: `${page}`,
      size: `${size}`,
    });

    if (start) {
      query.append('start', start);
    }
    if (end) {
      query.append('end', end);
    }

    const response = await fetch(
      `${API_BASE_URL}/appointments/patient/${patientId}?${query}`
    );
    if (!response.ok) {
      throw new Error('Error searching for patient appointments');
    }
    return await response.json();
  } catch (error) {
    console.error('Error in listByPatient, falling back to mock:', error);
    const patientAppointments = mockGeneratedAppointments.filter(
      g => g.patientId === patientId
    );
    return mockFetch(mockPage(patientAppointments));
  }
}

// Registrar ausência
export async function registerAbsence(
  generatedAppointmentId: UUID,
  justification: string
): Promise<Absence> {
  if (USE_MOCK_DATA) {
    console.log(
      '📦 [MOCK] Registrando ausência:',
      generatedAppointmentId,
      justification
    );
    const mockAbsence: Absence = {
      id: `absence-${Date.now()}`,
      generatedAppointment: mockGeneratedAppointments.find(
        g => g.id === generatedAppointmentId
      ),
      absenceDate: new Date().toISOString(),
      justification,
      notified: false,
    };
    return mockFetch(mockAbsence);
  }

  try {
    const body = {
      generatedAppointmentId,
      justification,
      date: new Date().toISOString(),
      notified: false,
    };

    const res = await fetch(`${API_BASE_URL}/absences`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    });

    if (!res.ok) throw new Error(`Error registering absence`);
    return await res.json();
  } catch (error) {
    console.error('Error in registerAbsence, falling back to mock:', error);
    const mockAbsence: Absence = {
      id: `absence-${Date.now()}`,
      absenceDate: new Date().toISOString(),
      justification,
      notified: false,
    };
    return mockFetch(mockAbsence);
  }
}

// ========== FUNÇÕES AUXILIARES ==========

export async function getPacientes(): Promise<Patient[]> {
  if (USE_MOCK_DATA) {
    return mockFetch(mockPatients);
  }

  try {
    const response = await fetch(`${API_BASE_URL}/patients?page=0&size=100`);
    const data = await response.json();
    console.log(data)
    return data.content || [];
  } catch (error) {
    console.error('Error in getPacientes, falling back to mock:', error);
    return mockFetch(mockPatients);
  }
}

export async function getProfissionaisDaSaude(): Promise<Professional[]> {
  if (USE_MOCK_DATA) {
    return mockFetch(mockProfessionals);
  }

  try {
    const response = await fetch(
      `${API_BASE_URL}/professionals?page=0&size=100`
    );
    const data = await response.json();
    return data.content || [];
  } catch (error) {
    console.error('Error fetching professionals:', error);
    return mockFetch(mockProfessionals);
  }
}

export async function getProfissionalDaSaude(
  id: string
): Promise<Professional> {
  if (USE_MOCK_DATA) {
    const professional =
      mockProfessionals.find(p => p.id === id) || mockProfessionals[0];
    return mockFetch(professional);
  }

  try {
    const response = await fetch(`${API_BASE_URL}/professionals/${id}`);
    if (!response.ok) {
      throw new Error(`Professional with ID ${id} not found`);
    }
    return await response.json();
  } catch (error) {
    console.error(`Error fetching professional with ID ${id}:`, error);
    const professional =
      mockProfessionals.find(p => p.id === id) || mockProfessionals[0];
    return mockFetch(professional);
  }
}

export async function getAreasDaSaude(): Promise<string[]> {
  if (USE_MOCK_DATA) {
    const areas = mockProfessionals.map(p => p.healthArea);
    return mockFetch([...new Set(areas)]);
  }

  try {
    const profissionais = await getProfissionaisDaSaude();
    const areas = profissionais.map(p => p.healthArea);
    return [...new Set(areas)].filter(Boolean) as string[];
  } catch (error) {
    console.error('Error in getAreasDaSaude, falling back to mock:', error);
    const areas = mockProfessionals.map(p => p.healthArea);
    return mockFetch([...new Set(areas)]);
  }
}

// Toggle confirmação (reativar agendamento)
export const toggleConfirmacao = async (id: UUID) => {
  if (USE_MOCK_DATA) {
    console.log('📦 [MOCK] Confirmando agendamento:', id);
    return mockFetch(undefined);
  }

  try {
    const appointment = await getAppointmentById(id);

    if (!appointment.professional || !appointment.annualRegistration?.id) {
      throw new Error('Appointment data is incomplete');
    }

    const dto: CreateAppointmentDTO = {
      professionalId: appointment.professional.id,
      serviceId: appointment.serviceId,
      annualRegistrationId: appointment.annualRegistration.id,
      frequencyDays: appointment.frequencyDays,
      initialDate: appointment.initialDate,
      hour: appointment.hour.replace(':00', ''),
    };

    await saveAppointment(dto);
    console.log('Appointment confirmed successfully');
  } catch (error) {
    console.error('Error in toggleConfirmacao, falling back to mock:', error);
    return mockFetch(undefined);
  }
};

// Função auxiliar para formatar hora
export const formatTimeForBackend = (timeString: string): string => {
  if (timeString.length === 5) {
    return `${timeString}:00`;
  }
  return timeString;
};

// Função auxiliar para parse de hora do backend
export const parseTimeFromBackend = (timeString: string): string => {
  return timeString.substring(0, 5);
};

// Função para verificar se está usando mock (útil para debug)
export const isUsingMockData = (): boolean => {
  return USE_MOCK_DATA;
};
