import {
  CreateAbsenceDTO,
  AbsenceResponseDTO,
  PatientWithAbsences,
  Patient,
} from '@/types/absence';
import { UUID } from './appointmentService';
import { Page } from '@/types/pagination';

interface AbsenceStatistics {
  totalPatients: number;
  totalAppointments: number;
  patientsWithMinAbsences: number;
}
// ========== MOCK DATA ==========

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
  {
    id: '2',
    generatedAppointmentId: 'gen-2',
    patientId: 'patient-1',
    professionalId: 'prof-1',
    absenceDate: '2024-02-10',
    justification: 'Problemas de transporte',
    notified: false,
  },
  {
    id: '3',
    generatedAppointmentId: 'gen-3',
    patientId: 'patient-1',
    professionalId: 'prof-2',
    absenceDate: '2024-03-05',
    justification: 'Motivo de saúde',
    notified: true,
  },

  {
    id: '4',
    generatedAppointmentId: 'gen-4',
    patientId: 'patient-2',
    professionalId: 'prof-2',
    absenceDate: '2024-01-18',
    justification: 'Não compareceu',
    notified: true,
  },
  {
    id: '5',
    generatedAppointmentId: 'gen-5',
    patientId: 'patient-2',
    professionalId: 'prof-2',
    absenceDate: '2024-02-20',
    justification: 'Problemas familiares',
    notified: false,
  },
  {
    id: '6',
    generatedAppointmentId: 'gen-6',
    patientId: 'patient-2',
    professionalId: 'prof-1',
    absenceDate: '2024-04-01',
    justification: 'Adiado por choque de agenda',
    notified: true,
  },

  {
    id: '7',
    generatedAppointmentId: 'gen-7',
    patientId: 'patient-3',
    professionalId: 'prof-1',
    absenceDate: '2024-02-02',
    justification: 'Doença',
    notified: true,
  },
  {
    id: '8',
    generatedAppointmentId: 'gen-8',
    patientId: 'patient-3',
    professionalId: 'prof-3',
    absenceDate: '2024-03-09',
    justification: 'Transporte',
    notified: false,
  },
  {
    id: '9',
    generatedAppointmentId: 'gen-9',
    patientId: 'patient-3',
    professionalId: 'prof-2',
    absenceDate: '2024-05-12',
    justification: 'Compromisso familiar',
    notified: true,
  },

  {
    id: '10',
    generatedAppointmentId: 'gen-10',
    patientId: 'patient-4',
    professionalId: 'prof-4',
    absenceDate: '2024-02-14',
    justification: 'Sem transporte',
    notified: false,
  },
  {
    id: '11',
    generatedAppointmentId: 'gen-11',
    patientId: 'patient-4',
    professionalId: 'prof-2',
    absenceDate: '2024-05-21',
    justification: 'Não compareceu',
    notified: false,
  },
  {
    id: '12',
    generatedAppointmentId: 'gen-12',
    patientId: 'patient-4',
    professionalId: 'prof-4',
    absenceDate: '2024-06-21',
    justification: 'Motivo pessoal',
    notified: true,
  },

  {
    id: '13',
    generatedAppointmentId: 'gen-13',
    patientId: 'patient-5',
    professionalId: 'prof-2',
    absenceDate: '2024-02-18',
    justification: 'Motivo pessoal',
    notified: true,
  },
  {
    id: '14',
    generatedAppointmentId: 'gen-14',
    patientId: 'patient-5',
    professionalId: 'prof-5',
    absenceDate: '2024-07-01',
    justification: 'Não compareceu',
    notified: false,
  },
  {
    id: '15',
    generatedAppointmentId: 'gen-15',
    patientId: 'patient-5',
    professionalId: 'prof-2',
    absenceDate: '2024-09-15',
    justification: 'Agendamento em conflito',
    notified: false,
  },

  {
    id: '16',
    generatedAppointmentId: 'gen-16',
    patientId: 'patient-6',
    professionalId: 'prof-3',
    absenceDate: '2024-02-20',
    justification: 'Fever',
    notified: false,
  },
  {
    id: '17',
    generatedAppointmentId: 'gen-17',
    patientId: 'patient-6',
    professionalId: 'prof-3',
    absenceDate: '2024-07-02',
    justification: 'Doença',
    notified: true,
  },
  {
    id: '18',
    generatedAppointmentId: 'gen-18',
    patientId: 'patient-6',
    professionalId: 'prof-3',
    absenceDate: '2024-11-05',
    justification: 'Não compareceu',
    notified: true,
  },

  {
    id: '19',
    generatedAppointmentId: 'gen-19',
    patientId: 'patient-7',
    professionalId: 'prof-4',
    absenceDate: '2024-03-01',
    justification: 'Não compareceu',
    notified: true,
  },
  {
    id: '20',
    generatedAppointmentId: 'gen-20',
    patientId: 'patient-7',
    professionalId: 'prof-4',
    absenceDate: '2024-04-10',
    justification: 'Falta justificável',
    notified: false,
  },
  {
    id: '21',
    generatedAppointmentId: 'gen-21',
    patientId: 'patient-7',
    professionalId: 'prof-6',
    absenceDate: '2024-06-18',
    justification: 'Problema de saúde',
    notified: true,
  },

  {
    id: '22',
    generatedAppointmentId: 'gen-22',
    patientId: 'patient-8',
    professionalId: 'prof-5',
    absenceDate: '2024-03-02',
    justification: 'Sem transporte',
    notified: true,
  },
  {
    id: '23',
    generatedAppointmentId: 'gen-23',
    patientId: 'patient-8',
    professionalId: 'prof-5',
    absenceDate: '2024-05-05',
    justification: 'Problemas logísticos',
    notified: false,
  },
  {
    id: '24',
    generatedAppointmentId: 'gen-24',
    patientId: 'patient-8',
    professionalId: 'prof-5',
    absenceDate: '2024-09-01',
    justification: 'Motivo pessoal',
    notified: true,
  },
];

const mockPatients: Patient[] = [
  {
    id: 'patient-1',
    name: 'João Silva',
    contact: '(11) 99999-9999',
    birthDate: '1990-01-01',
  },
  {
    id: 'patient-2',
    name: 'Maria Santos',
    contact: '(11) 88888-8888',
    birthDate: '1985-05-15',
  },
  {
    id: 'patient-3',
    name: 'Pedro Oliveira',
    contact: '(11) 77777-7777',
    birthDate: '1992-08-20',
  },
  {
    id: 'patient-4',
    name: 'Ana Costa',
    contact: '(11) 66666-6666',
    birthDate: '2001-04-10',
  },
  {
    id: 'patient-5',
    name: 'Lucas Almeida',
    contact: '(11) 55555-5555',
    birthDate: '1988-03-03',
  },
  {
    id: 'patient-6',
    name: 'Helena Rocha',
    contact: '(11) 44444-4444',
    birthDate: '1978-12-12',
  },
  {
    id: 'patient-7',
    name: 'Gustavo Lima',
    contact: '(11) 33333-3333',
    birthDate: '2005-09-09',
  },
  {
    id: 'patient-8',
    name: 'Mariana Oliveira',
    contact: '(11) 22222-2222',
    birthDate: '1998-06-21',
  },
];

export class AbsenceService {
  private static readonly API_BASE_URL =
    process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8093';
  private static readonly API_PATH = '/absences';
  private static useMock = process.env.NEXT_PUBLIC_USE_MOCK_DATA === 'false';

  private static getAuthHeaders(): HeadersInit {
    return {
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    };
  }

  private static async handleResponse<T>(response: Response): Promise<T> {
    if (!response.ok) {
      const error = await response.text();
      throw new Error(`HTTP ${response.status}: ${error}`);
    }
    return response.json();
  }

  /**
   * Registra uma nova falta - POST /api/absences
   */
  static async registerAbsence(
    dto: CreateAbsenceDTO
  ): Promise<AbsenceResponseDTO> {
    if (this.useMock) {
      await new Promise(resolve => setTimeout(resolve, 500));

      const existingAbsence = mockAbsences.find(
        absence => absence.generatedAppointmentId === dto.generatedAppointmentId
      );

      if (existingAbsence) {
        throw new Error(
          `Já existe uma falta registrada para o agendamento gerado de ID: ${dto.generatedAppointmentId}`
        );
      }

      const newAbsence: AbsenceResponseDTO = {
        ...dto,
        id: Math.random().toString(36).substr(2, 9),
        patientId: 'patient-mock',
        professionalId: 'professional-mock',
        notified: false,
      };

      mockAbsences.push(newAbsence);
      return newAbsence;
    }

    // Implementação real
    const response = await fetch(`${this.API_BASE_URL}${this.API_PATH}`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(dto),
    });
    return this.handleResponse<AbsenceResponseDTO>(response);
  }

  static async findAllByFilters(
    generatedId?: UUID,
    patientId?: UUID,
    professionalId?: UUID,
    page: number = 0,
    size: number = 10
  ): Promise<Page<AbsenceResponseDTO>> {
    if (this.useMock) {
      await new Promise(resolve => setTimeout(resolve, 400));

      let filteredAbsences = [...mockAbsences];

      if (generatedId) {
        filteredAbsences = filteredAbsences.filter(
          absence => absence.generatedAppointmentId === generatedId
        );
      }

      if (patientId) {
        filteredAbsences = filteredAbsences.filter(
          absence => absence.patientId === patientId
        );
      }

      if (professionalId) {
        filteredAbsences = filteredAbsences.filter(
          absence => absence.professionalId === professionalId
        );
      }

      const startIndex = page * size;
      const endIndex = startIndex + size;
      const paginatedAbsences = filteredAbsences.slice(startIndex, endIndex);

      return {
        content: paginatedAbsences,
        totalElements: filteredAbsences.length,
        totalPages: Math.ceil(filteredAbsences.length / size),
        size: size,
        number: page,
        first: page === 0,
        last: endIndex >= filteredAbsences.length,
        empty: paginatedAbsences.length === 0,
        length: paginatedAbsences.length,
      };
    }

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

  private static async getAllAbsences(): Promise<AbsenceResponseDTO[]> {
    if (this.useMock) {
      // Retorna todas as faltas diretamente do mock
      return mockAbsences;
    }

    // Em produção, faria várias chamadas paginadas até pegar tudo
    const allAbsences: AbsenceResponseDTO[] = [];
    let page = 0;
    const size = 10;

    while (true) {
      const absencesPage = await this.findAllByFilters(
        undefined,
        undefined,
        undefined,
        page,
        size
      );
      allAbsences.push(...absencesPage.content);

      if (absencesPage.last) break;
      page++;
    }

    return allAbsences;
  }

  /**
   * Busca pacientes com número mínimo de faltas (para a tela específica)
   */
  static async getPatientsWithAbsences(
    minAbsences: number = 3
  ): Promise<PatientWithAbsences[]> {
    const allAbsences = await this.getAllAbsences();

    const absencesByPatient = new Map<UUID, AbsenceResponseDTO[]>();

    allAbsences.forEach(absence => {
      const patientAbsences = absencesByPatient.get(absence.patientId) || [];
      patientAbsences.push(absence);
      absencesByPatient.set(absence.patientId, patientAbsences);
    });

    const patientsWithAbsences: PatientWithAbsences[] = [];

    absencesByPatient.forEach((absences, patientId) => {
      const patient = mockPatients.find(p => p.id === patientId);

      if (patient && absences.length >= minAbsences) {
        const latestAbsence = absences.reduce((latest, current) =>
          new Date(current.absenceDate) > new Date(latest.absenceDate)
            ? current
            : latest
        );

        patientsWithAbsences.push({
          patient,
          absenceCount: absences.length,
          lastAbsenceDate: latestAbsence.absenceDate,
          absences: absences.sort(
            (a, b) =>
              new Date(b.absenceDate).getTime() -
              new Date(a.absenceDate).getTime()
          ),
        });
      }
    });

    return patientsWithAbsences.sort((a, b) => b.absenceCount - a.absenceCount);
  }

  /**
   * Busca estatísticas de faltas (para os cards da tela)
   */
  static async getAbsenceStatistics(): Promise<AbsenceStatistics> {
    const patientsWithAbsences = await this.getPatientsWithAbsences(3);

    return {
      totalPatients: mockPatients.length,
      totalAppointments: 50,
      patientsWithMinAbsences: patientsWithAbsences.length,
    };
  }
}

export default AbsenceService;
