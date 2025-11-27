// src/services/absenceService.ts
import {
  AbsenceResponseDTO,
  CreateAbsenceDTO,
  PatientWithAbsences,
} from '@/types/absence';
import { Page } from '@/types/pagination';

type UUID = string;

interface AbsenceStatistics {
  totalPatients: number;
  totalAppointments: number;
  patientsWithMinAbsences: number;
}

// ========== MOCK DATA (mantido apenas para testes locais) ==========
const mockAbsences: AbsenceResponseDTO[] = [
  {
    id: '1',
    generatedAppointmentId: 'gen-1',
    patientId: 'patient-1',
    professionalId: 'prof-1',
    absenceDate: '2024-01-15',
    justification: 'Paciente não compareceu',
    notified: true,
  },
];

const mockPatients = [
  {
    id: 'patient-1',
    name: 'João Silva',
    contact: '(11) 99999-9999',
    birthDate: '1990-01-01',
  },
];

export class AbsenceService {
  private static readonly API_BASE_URL =
    process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8090';
  private static readonly API_PATH = '/absences';
  private static useMock = false;

  private static getAuthHeaders(): HeadersInit {
    const token = localStorage.getItem('token');
    return {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    };
  }

  private static async handleResponse<T>(response: Response): Promise<T> {
    if (!response.ok) {
      const error = await response.text();
      throw new Error(`HTTP ${response.status}: ${error || response.statusText}`);
    }
    return response.json();
  }

  // POST /absences → registrar falta
  static async registerAbsence(dto: CreateAbsenceDTO): Promise<AbsenceResponseDTO> {
    if (this.useMock) {
      await new Promise((resolve) => setTimeout(resolve, 500));
      const exists = mockAbsences.some(
        (a) => a.generatedAppointmentId === dto.generatedAppointmentId
      );
      if (exists) {
        throw new Error('Já existe uma falta registrada para esse agendamento.');
      }
      const newAbsence: AbsenceResponseDTO = {
        ...dto,
        id: Math.random().toString(36).substr(2, 9),
        patientId: 'patient-mock',
        professionalId: 'prof-mock',
        notified: false,
      };
      mockAbsences.push(newAbsence);
      return newAbsence;
    }

    const response = await fetch(`${this.API_BASE_URL}${this.API_PATH}`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(dto),
    });

    return this.handleResponse<AbsenceResponseDTO>(response);
  }

  // GET /absences com filtros e paginação → 100% compatível com Spring Pageable
  static async findAllByFilters(
    filters: {
      generatedId?: UUID;
      patientId?: UUID;
      professionalId?: UUID;
      page?: number;
      size?: number;
    } = {}
  ): Promise<Page<AbsenceResponseDTO>> {
    const {
      generatedId,
      patientId,
      professionalId,
      page = 0,
      size = 10,
    } = filters;

    if (this.useMock) {
      await new Promise((resolve) => setTimeout(resolve, 400));
      let filtered = [...mockAbsences];

      if (generatedId)
        filtered = filtered.filter((a) => a.generatedAppointmentId === generatedId);
      if (patientId) filtered = filtered.filter((a) => a.patientId === patientId);
      if (professionalId)
        filtered = filtered.filter((a) => a.professionalId === professionalId);

      const start = page * size;
      const end = start + size;
      const content = filtered.slice(start, end);

      return {
        content,
        totalElements: filtered.length,
        totalPages: Math.ceil(filtered.length / size),
        size,
        number: page,
        first: page === 0,
        last: end >= filtered.length,
        empty: content.length === 0,
        length: 0
      };
    }

    // Query string exatamente como o Spring espera
    const params = new URLSearchParams();
    if (generatedId) params.append('generatedId', generatedId);
    if (patientId) params.append('patientId', patientId);
    if (professionalId) params.append('professionalId', professionalId);
    params.append('page', page.toString());
    params.append('size', size.toString());

    const url = `${this.API_BASE_URL}${this.API_PATH}?${params.toString()}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: this.getAuthHeaders(),
    });

    return this.handleResponse<Page<AbsenceResponseDTO>>(response);
  }

  // Função auxiliar: buscar todas as ausências (para estatísticas)
  private static async getAllAbsences(): Promise<AbsenceResponseDTO[]> {
    if (this.useMock) return mockAbsences;

    const all: AbsenceResponseDTO[] = [];
    let page = 0;
    const size = 50;

    while (true) {
      const result = await this.findAllByFilters({ page, size });
      all.push(...result.content);
      if (result.last) break;
      page++;
    }

    return all;
  }

// src/services/absenceService.ts (apenas a parte alterada)

static async getPatientsWithAbsences(minAbsences: number = 3): Promise<PatientWithAbsences[]> {
  const allAbsences = await this.getAllAbsences();

  // Agrupa ausências por patientId
  const absencesByPatient = new Map<UUID, AbsenceResponseDTO[]>();
  allAbsences.forEach((absence) => {
    const list = absencesByPatient.get(absence.patientId) || [];
    list.push(absence);
    absencesByPatient.set(absence.patientId, list);
  });

  const result: PatientWithAbsences[] = [];

  // Para cada patientId com 3+ faltas, busca os dados do paciente
  for (const [patientId, absences] of absencesByPatient) {
    if (absences.length < minAbsences) continue;

    try {
      // Chama o endpoint real: GET /patients/{id}
      const patientResponse = await fetch(
        `${this.API_BASE_URL}/patients/${patientId}`,
        {
          method: 'GET',
          headers: this.getAuthHeaders(),
        }
      );

      if (!patientResponse.ok) {
        console.warn(`Paciente ${patientId} não encontrado (404 ou erro). Pulando...`);
        continue; // pula se o paciente foi excluído ou não existe mais
      }

      const patientData = await patientResponse.json();

      // Assumindo que PatientResponseDTO tem: id, name (ou fullName?), contact, birthDate
      const patient = {
        id: patientData.id,
        name: patientData.name || patientData.fullName || 'Nome não informado',
        contact: patientData.contact || patientData.phone || 'Não informado',
        birthDate: patientData.birthDate || '',
      };

      // Ordena as faltas por data (mais recente primeiro)
      const sortedAbsences = [...absences].sort(
        (a, b) => new Date(b.absenceDate).getTime() - new Date(a.absenceDate).getTime()
      );

      result.push({
        patient,
        absenceCount: absences.length,
        lastAbsenceDate: sortedAbsences[0]?.absenceDate || absences[0].absenceDate,
        absences: sortedAbsences,
      });
    } catch (error) {
      console.error(`Erro ao buscar paciente ${patientId}:`, error);
      // Opcional: adicionar com dados parciais ou pular
      continue;
    }
  }

  // Ordena por número de faltas (maior primeiro)
  return result.sort((a, b) => b.absenceCount - a.absenceCount);
}

  // Estatísticas gerais
  static async getAbsenceStatistics(): Promise<AbsenceStatistics> {
    const patientsWithAbsences = await this.getPatientsWithAbsences(3);

    return {
      totalPatients: mockPatients.length,
      totalAppointments: 50, // substituir por endpoint real no futuro
      patientsWithMinAbsences: patientsWithAbsences.length,
    };
  }
}

export default AbsenceService;