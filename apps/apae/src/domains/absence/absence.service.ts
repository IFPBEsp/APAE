import { AbsenceResponseDTO, CreateAbsenceDTO } from '@/types/absence';
import { DocumentWithOutUrl } from '@/types/document';
import { ABSENCE_DOCUMENT_CATEGORY, ABSENCE_DOCUMENT_TYPE } from './absence.constants';

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

  static async uploadJustificationDocument(patientId: string, file: File): Promise<string> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('category', ABSENCE_DOCUMENT_CATEGORY);
    formData.append('type', ABSENCE_DOCUMENT_TYPE);
    formData.append('year', String(new Date().getFullYear()));

    const response = await fetch(`/apae-geral/api/patients/${patientId}/documents`, {
      method: 'POST',
      body: formData,
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Erro ao fazer upload do documento.');
    }

    const uploadedDocument = (await response.json()) as DocumentWithOutUrl;
    return uploadedDocument.name;
  }
}

export default AbsenceService;