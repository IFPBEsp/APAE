export interface DocumentDTO {
  id: string;
  name: string;
  category: string;
  type: string;
  url: string;
}

export interface DisorderItem {
  id?: string | number;
  name?: string;
  label?: string;
  value?: string;
}

export interface ServiceAreaItem {
  id?: string | number;
  area?: string;
  name?: string;
  label?: string;
  value?: string;
}

export interface AnnualRegistry {
  id?: string;
  bpc: boolean | string;
  familyIncome: number | string;
  diseases: string;
  continuousMedication: string;
  medications?: string;
  medicamentos?: string;
  medication?: string;
  disorders?: DisorderItem[];
  serviceAreas?: ServiceAreaItem[];
  serviceArea?: ServiceAreaItem[];
  serviceTypes?: ServiceAreaItem[];
}

export interface FullPatientData {
  vaccineNames?: { name: string }[] | string[];
  allergies?: string;
  additionals?: {
    medications?: string;
    diseases?: string;
    [key: string]: unknown;
  };
  address?: Record<string, string | null | undefined>;
  guardian?: Record<string, string | Record<string, string> | null | undefined>;
  parents?: Array<Record<string, string | boolean>>;
  nationality?: string;
  birthplace?: string;
  [key: string]: unknown;
}

export const MEDICAL_DOC_TYPES = [
  { value: "MEDICAL_REPORT", label: "Laudo Médico" },
  { value: "EXAMINATION", label: "Exame" },
  { value: "REFERRAL", label: "Encaminhamento" },
  { value: "OTHER", label: "Outro" },
] as const;

export const DOC_TYPE_TRANSLATIONS: Record<string, string> = {
  MEDICAL_REPORT: "Laudo Médico",
  EXAMINATION: "Exame",
  REFERRAL: "Encaminhamento",
  OTHER: "Outro",
  VACCINE_CARD: "Cartão de Vacina",
};
