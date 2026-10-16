import type { CreateAbsenceDTO, AbsenceResponseDTO } from './types/absences.types';
import type { DocumentWithOutUrl } from '@/types/document';
import { ABSENCE_DOCUMENT_CATEGORY, ABSENCE_DOCUMENT_TYPE } from './absences.constants';

const API_PATH = '/apae-geral/api/absences';

export async function registerAbsence(dto: CreateAbsenceDTO): Promise<AbsenceResponseDTO> {
  const response = await fetch(API_PATH, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dto),
  });

  if (!response.ok) {
    const error = await response.text();
    throw new Error(`HTTP ${response.status}: ${error || response.statusText}`);
  }

  return response.json();
}

export async function justifyAbsence(
  absenceId: string,
  justification: string,
  justificationDocumentId?: string | null,
): Promise<AbsenceResponseDTO> {
  const response = await fetch(`${API_PATH}/${absenceId}/justify`, {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ justification, justificationDocumentId }),
  });

  if (!response.ok) {
    const error = await response.text();
    throw new Error(`HTTP ${response.status}: ${error || response.statusText}`);
  }

  return response.json();
}

export async function uploadJustificationDocument(
  patientId: string,
  file: File,
): Promise<string> {
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