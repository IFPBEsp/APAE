import type { CreateAbsenceDTO, AbsenceResponseDTO } from './types/absences.types';

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