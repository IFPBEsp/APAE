"use client";

import { AbsenceResponseDTO, CreateAbsenceDTO } from '@/types/absence';
import { DocumentWithOutUrl } from '@/types/document';
import { bffFetch, bffFetchJson } from '@/lib/bff-client';
import { ABSENCE_DOCUMENT_CATEGORY, ABSENCE_DOCUMENT_TYPE } from './absence.constants';

export class AbsenceService {
  private static readonly API_PATH = '/apae-geral/api/absences'; 


  static async registerAbsence(dto: CreateAbsenceDTO): Promise<AbsenceResponseDTO> {
    return bffFetchJson<AbsenceResponseDTO>(`${this.API_PATH}`, {
      method: 'POST',
      body: JSON.stringify(dto),
    });
  }

  static async justifyAbsence(
    absenceId: string, 
    justification: string, 
    justificationDocumentId?: string | null
  ): Promise<AbsenceResponseDTO> {
    
    return bffFetchJson<AbsenceResponseDTO>(`${this.API_PATH}/${absenceId}/justify`, {
      method: 'PATCH',
      body: JSON.stringify({ 
        justification,
        justificationDocumentId
      }),
    });
  }

  static async uploadJustificationDocument(patientId: string, file: File): Promise<string> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('category', ABSENCE_DOCUMENT_CATEGORY);
    formData.append('type', ABSENCE_DOCUMENT_TYPE);
    formData.append('year', String(new Date().getFullYear()));

    const response = await bffFetch(`/apae-geral/api/patients/${patientId}/documents`, {
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