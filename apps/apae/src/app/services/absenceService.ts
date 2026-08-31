import { AbsenceResponseDTO, CreateAbsenceDTO } from "@/types/absence";

export class AbsenceService {
  private static readonly API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "/apae-geral/api";
  private static readonly API_PATH = "/absences";

  private static getAuthHeaders(): HeadersInit {
    const token = localStorage.getItem("token");
    return {
      "Content-Type": "application/json",
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

  static async registerAbsence(dto: CreateAbsenceDTO): Promise<AbsenceResponseDTO> {
    const response = await fetch(`${this.API_BASE_URL}${this.API_PATH}`, {
      method: "POST",
      headers: this.getAuthHeaders(),
      body: JSON.stringify(dto),
    });

    return this.handleResponse<AbsenceResponseDTO>(response);
  }

  static async justifyAbsence(
    absenceId: string,
    justification: string,
    justificationDocumentId?: string | null,
  ): Promise<AbsenceResponseDTO> {
    const response = await fetch(`/apae-geral/api/absences/${absenceId}/justify`, {
      method: "PATCH",
      headers: this.getAuthHeaders(),
      body: JSON.stringify({
        justification,
        justificationDocumentId,
      }),
    });

    return this.handleResponse<AbsenceResponseDTO>(response);
  }
}

export default AbsenceService;
