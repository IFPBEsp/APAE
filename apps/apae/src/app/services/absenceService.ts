import { AbsenceResponseDTO, CreateAbsenceDTO } from '@/types/absence';

export class AbsenceService {
  private static readonly API_PATH = '/apae-geral/api/absences'; 


  private static async handleResponse<T>(response: Response): Promise<T> {
    if (!response.ok) {
      const error = await response.text();
      throw new Error(`HTTP ${response.status}: ${error || response.statusText}`);
    }
    return response.json();
  }

  static async registerAbsence(dto: CreateAbsenceDTO): Promise<AbsenceResponseDTO> {
    const response = await fetch(`${this.API_PATH}`, {
      method: 'POST',
      headers: {"Content-Type": "application/json",},
      body: JSON.stringify(dto),
    });


    return this.handleResponse<AbsenceResponseDTO>(response);
  }

  static async justifyAbsence(
    absenceId: string, 
    justification: string, 
    justificationDocumentId?: string | null
  ): Promise<AbsenceResponseDTO> {
    
    const response = await fetch(`${this.API_PATH}/${absenceId}/justify`, {
      method: 'PATCH',
      headers: {"Content-Type": "application/json",},
      body: JSON.stringify({ 
        justification,
        justificationDocumentId
      }),
    });
  
    return this.handleResponse<AbsenceResponseDTO>(response);
  }
}

export default AbsenceService;