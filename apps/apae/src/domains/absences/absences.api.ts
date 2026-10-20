import type { CreateAbsenceDTO, AbsenceResponseDTO } from './types/absences.types';
import type { DocumentWithOutUrl } from '@/types/document';
import { ABSENCE_DOCUMENT_CATEGORY, ABSENCE_DOCUMENT_TYPE } from './absences.constants';
import { bffFetch, bffFetchJson } from '@/lib/bff-client';

const API_PATH = '/apae-geral/api/absences';

export async function registerAbsence(dto: CreateAbsenceDTO): Promise<AbsenceResponseDTO> {
  return bffFetchJson<AbsenceResponseDTO>(API_PATH, {
    method: 'POST',
    body: JSON.stringify(dto),
  });
}

export async function justifyAbsence(
  absenceId: string,
  justification: string,
  justificationDocumentId?: string | null,
): Promise<AbsenceResponseDTO> {
  return bffFetchJson<AbsenceResponseDTO>(`${API_PATH}/${absenceId}/justify`, {
    method: 'PATCH',
    body: JSON.stringify({ justification, justificationDocumentId }),
  });
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

  const response = await bffFetch(`/apae-geral/api/patients/${patientId}/documents`, {
    method: 'POST',
    body: formData,
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.message || 'Erro ao fazer upload do documento.');
  }

  const uploadedDocument = (await response.json()) as DocumentWithOutUrl;
  return uploadedDocument.name;
}
