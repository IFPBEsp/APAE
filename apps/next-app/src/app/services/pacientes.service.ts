import { Patient, Page } from "@/lib/types";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8086';

export async function fetchPatients(): Promise<Patient[]> {
  try {
    const response = await fetch(`${API_BASE_URL}/pessoas`);

    if (!response.ok) {
      throw new Error('Falha ao buscar os dados dos pacientes.');
    }

    const data: Page<Patient> = await response.json();
    
    return data.content;

  } catch (error) {
    console.error("Erro no serviço da API:", error);
    throw error;
  }
}